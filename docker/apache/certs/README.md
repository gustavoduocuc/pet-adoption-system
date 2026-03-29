# Certificados TLS para Apache (proxy HTTPS)

Coloca aquí **`server.crt`** y **`server.key`** (PEM) antes de levantar el servicio `proxy` con perfil `https`.

No subas claves privadas al repositorio (están en `.gitignore`).

## Certificado de desarrollo (autofirmado)

Desde el directorio `docker/apache/certs/`:

```bash
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout server.key \
  -out server.crt \
  -subj "/CN=localhost"
```

Para otro nombre (p. ej. `miapp.local`), ajusta `-subj` y usa el mismo nombre en el navegador o añade la CA al almacén de confianza.
