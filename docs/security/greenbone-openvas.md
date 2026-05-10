# Greenbone Community Edition (OpenVAS / GSA) en Docker

Documentación del perfil Compose **`greenbone`** del proyecto. Referencia oficial: [Greenbone Community Containers](https://greenbone.github.io/docs/latest/22.4/container/index.html).

## Arranque

**Solo aplicación y MySQL** (y el resto de servicios sin perfil `greenbone`):

```bash
docker compose --env-file .env.docker up --build
```

**Incluir el stack Greenbone** (muchas imágenes y consumo de RAM/disco):

```bash
docker compose --env-file .env.docker --profile greenbone pull
docker compose --env-file .env.docker --profile greenbone up -d
```

Si el registro `registry.community.greenbone.net` no es alcanzable pero **ya tienes** las imágenes en local (mismo nombre `registry.community.greenbone.net/community/...`), **no ejecutes `pull`**. Usa:

```bash
docker compose --env-file .env.docker --profile greenbone up -d --pull never
```

El `docker-compose.yml` usa `pull_policy: missing` en esos servicios para favorecer la caché local; `--pull never` evita contactar el registro al crear contenedores.

## Variables de entorno

Ver comentarios en [`.env.docker.example`](../../.env.docker.example): `GREENBONE_IMAGE_PREFIX`, `GREENBONE_FEED_RELEASE`, `GREENBONE_KEEP_ALIVE`, `GREENBONE_NGINX_HTTPS_PORT`, `GREENBONE_NGINX_GSA_PORT`.

## Interfaz web (GSA)

- URL típica (TLS autofirmado): **`https://127.0.0.1:8443`** con el valor por defecto de `GREENBONE_NGINX_HTTPS_PORT` (mapea el **443** interno de nginx; no confundir con el puerto **9392** del segundo mapeo).
- Los puertos están publicados en **127.0.0.1**; si `localhost` resuelve a IPv6, usa **`127.0.0.1`** explícito.
- Conflicto con el proxy Apache del perfil **`https`**: ambos pueden querer el **443** del host; ajusta `HTTPS_HOST_PORT` o los puertos Greenbone.

Credenciales iniciales y cambio de contraseña del usuario `admin`: documentación oficial, sección *Setting up an Admin User*.

## Registro de imágenes inalcanzable

Si `docker compose ... pull` falla con `connection refused` o timeout hacia `registry.community.greenbone.net:443`, el problema es de **red** hasta el registro, no del nombre de imagen concreto.

- Comprueba: `curl -vI https://registry.community.greenbone.net/v2/`
- Prueba otra red, VPN, firewall o proxy en Docker Desktop (*Settings → Resources → Proxies*).
- Con un mirror interno, publica las mismas imágenes y define **`GREENBONE_IMAGE_PREFIX`** en `.env.docker` (prefijo **sin** barra final).

## Sincronización de feeds

La primera carga puede tardar **de minutos a horas** (import SCAP/CVE en `gvmd`, VTs en el escáner). Mientras el dashboard indique que el feed sincroniza, los escaneos con configuración por defecto pueden fallar.

Guía: [Workflows / loading feeds](https://greenbone.github.io/docs/latest/22.4/container/workflows.html). Señal de SCAP terminado en logs de `gvmd`: `update_scap_end: Updating SCAP info succeeded`.

## Contenedores en estado `Exited`

Servicios como `pg-gvm-migrator`, `gpg-data`, `configure-openvas` y `gvm-config` son **tareas one-shot**: deben terminar con código 0, no permanecer en ejecución.

## Escanear la aplicación del mismo `docker-compose`

El tráfico de escaneo sale de **`ospd-openvas`**. Ese servicio está en **`greenbone_net`** y en la red **`default`** (la de `mysql` y `app`) para poder alcanzar IPs como `172.x.x.x` del stack principal. Si añades servicios solo en **`app_net`**, conecta también `ospd-openvas` a esa red o usa un objetivo alcanzable desde `default`.

## Mitigar hallazgos bajos en informes OpenVAS

### TCP timestamps (p. ej. NVT "TCP Timestamps Information Disclosure")

En el servicio **`app`** del compose se define:

```yaml
sysctls:
  - net.ipv4.tcp_timestamps=0
```

Eso desactiva timestamps TCP en el **network namespace** del contenedor cuando el runtime lo permite. **No garantiza** desaparición del hallazgo en todos los entornos: Docker Desktop a veces no aplica `net.ipv4.*` en el contenedor; en Linux nativo suele funcionar. Tras cambiar el compose, recrea el contenedor: `docker compose ... up -d --force-recreate app`.

### Peticiones ICMP timestamp (p. ej. CVE-1999-0524)

No se mitiga con código Java. Opciones: filtrar ICMP timestamp en firewall del host o aceptar el riesgo en desarrollo. Dentro de un contenedor aislado suele ser incómodo configurar sin `NET_ADMIN` e iptables.

## Referencias

- [Greenbone Community Containers (índice)](https://greenbone.github.io/docs/latest/22.4/container/index.html)
- [OWASP Dependency-Check en este repo](owasp-dependency-check.md)
