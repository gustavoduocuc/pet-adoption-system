# Sistema de adopción de mascotas

Aplicación para adopción de mascotas y registro veterinario básico de animales rescatados. Incluye **API REST** (`/api/**`) con **JWT sin estado** y **interfaz web** con **Thymeleaf**, **login por formulario** y **sesión HTTP** (cookie `JSESSIONID`). Persistencia en MySQL.

## Stack

- Java 21
- Spring Boot 4
- Spring Web MVC, Spring Security, Spring Data JPA
- MySQL (`mysql-connector-j`)
- JWT (biblioteca JJWT) para autenticación de la API
- Thymeleaf + Spring Security (form login y CSRF en la parte web)
- Bean Validation (`@Valid`)

## Roles

| Rol | Descripción |
|-----|-------------|
| **ADMIN** | Acceso completo: gestionar mascotas y pacientes veterinarios. |
| **STAFF** | En esta API, equivalente a ADMIN: gestionar mascotas y pacientes. |
| **USER** | En la **API**: solo lectura pública del catálogo (`GET /api/pets/**`). No puede crear, actualizar ni eliminar mascotas ni acceder a pacientes. En la **web**: puede iniciar sesión y usar el catálogo HTML (`/catalog`); **no** puede entrar a `/app/**` (gestión). |
| **ADMIN** / **STAFF** | API y web: gestión de mascotas y pacientes según los endpoints y rutas descritos abajo. |

Los claims del JWT incluyen los nombres de rol; Spring Security los mapea a autoridades `ROLE_ADMIN`, `ROLE_STAFF`, `ROLE_USER`.

## Autenticación: API (JWT) frente a navegador (sesión)

Hay **dos cadenas de seguridad**: `/api/**` usa **JWT y sesión desactivada**; el resto del sitio usa **formulario de login, sesión y CSRF** en formularios POST. No mezcles JWT en `localStorage` para las páginas HTML: el navegador usa solo la cookie de sesión para Thymeleaf.

### Clientes API (Postman, integraciones, SPA futura)

1. Llamar `POST /api/auth/login` con cuerpo JSON `{"username":"...","password":"..."}`.
2. Respuesta: `{"accessToken":"...","tokenType":"Bearer"}` (nunca se devuelven contraseñas).
3. En rutas protegidas de `/api/**`, enviar cabecera: `Authorization: Bearer <accessToken>`.

Las peticiones sin autenticar a rutas protegidas de la API reciben **401** en JSON. Los usuarios autenticados sin rol suficiente reciben **403** en JSON.

### Navegador (Thymeleaf)

1. Abrir `/login`, enviar usuario y contraseña con el formulario (incluye token CSRF).
2. Tras un login correcto, **ADMIN** y **STAFF** se redirigen a `/app/pets`; **USER** se redirige a `/catalog`.
3. Las rutas bajo `/app/**` exigen rol **ADMIN** o **STAFF**. **USER** verá la página de acceso denegado si intenta entrar ahí.
4. Cerrar sesión: formulario POST a `/logout` (por ejemplo desde la barra de navegación de la plantilla).

Rutas web útiles: `/` (redirige al catálogo), `/catalog`, `/catalog/{id}`, `/app/pets`, `/app/patients` (listado, alta y detalle; **no** hay eliminación de pacientes en la UI hasta exista `DELETE` en la API).

## Endpoints de la API

Ruta base de la API REST: **`/api`**.

### Autenticación

| Método | Ruta | Autenticación | Roles |
|--------|------|---------------|-------|
| POST | `/api/auth/login` | Ninguna | — |

### Mascotas (catálogo de adopción)

| Método | Ruta | Autenticación | Roles |
|--------|------|---------------|-------|
| GET | `/api/pets` | Ninguna | Público |
| GET | `/api/pets/available` | Ninguna | Público |
| GET | `/api/pets/search` | Ninguna | Público (parámetros de consulta `species`, `age`, `location`, `gender`, todos opcionales) |
| GET | `/api/pets/{id}` | Ninguna | Público |
| POST | `/api/pets` | JWT obligatorio | `ADMIN`, `STAFF` |
| PUT | `/api/pets/{id}` | JWT obligatorio | `ADMIN`, `STAFF` |
| DELETE | `/api/pets/{id}` | JWT obligatorio | `ADMIN`, `STAFF` |

### Pacientes (veterinaria)

| Método | Ruta | Autenticación | Roles |
|--------|------|---------------|-------|
| GET | `/api/patients` | JWT obligatorio | `ADMIN`, `STAFF` |
| GET | `/api/patients/{id}` | JWT obligatorio | `ADMIN`, `STAFF` |
| POST | `/api/patients` | JWT obligatorio | `ADMIN`, `STAFF` |

### Operaciones (salud)

| Método | Ruta | Autenticación | Roles |
|--------|------|---------------|-------|
| GET | `/actuator/health` | Ninguna | Público |

## Configuración

Se define en `application.properties` o variables de entorno:

| Propiedad / variable | Uso |
|---------------------|-----|
| `DATABASE_URL` / `spring.datasource.url` | URL JDBC (por defecto MySQL local `pet_adoption`) |
| `DATABASE_USERNAME` / `spring.datasource.username` | Usuario de la base de datos |
| `DATABASE_PASSWORD` / `spring.datasource.password` | Contraseña de la base de datos |
| `JWT_SECRET` / `app.security.jwt.secret` | Secreto para firmar JWT (usar un valor fuerte en producción) |
| `JWT_EXPIRATION_MINUTES` / `app.security.jwt.expiration-minutes` | Duración del token |
| `CORS_ORIGIN_1`, `CORS_ORIGIN_2` / `app.cors.allowed-origins[n]` | Orígenes permitidos del navegador para `/api/**` |

### Archivo `.env` (desarrollo local)

Para no subir secretos al repositorio:

1. Copia la plantilla: `cp .env.example .env`
2. Edita `.env` con tus valores reales (contraseña de MySQL, `JWT_SECRET`, etc.).
3. El archivo `.env` está en `.gitignore` y **no se debe commitear**.
4. Al arrancar con `./mvnw spring-boot-run` (o ejecutando `PetAdoptionSystemApplication`), se carga `.env` en **propiedades del sistema** antes de Spring Boot. Si una variable ya existe en el entorno del SO, **gana la del SO** (útil en CI/producción).

Los nombres en `.env` deben coincidir con las variables de la tabla anterior (`DATABASE_PASSWORD`, `JWT_SECRET`, …).

**Nota:** `./mvnw test` no pasa por `main()`; las pruebas siguen usando `application-test.properties` y no dependen de `.env`.

## Docker (MySQL + aplicación)

Requisitos: [Docker](https://docs.docker.com/get-docker/) y Docker Compose v2.

MySQL se crea automáticamente con la base `pet_adoption` y un usuario de aplicación; la app espera a que MySQL pase el healthcheck antes de arrancar.

### `.env` local frente a `.env.docker` (Compose)

| Archivo | Uso |
|---------|-----|
| **`.env`** | Desarrollo en el host: `./mvnw spring-boot-run`, tests, IDE. JDBC con `localhost`, tus credenciales. **No lo pises** al usar Docker. |
| **`.env.docker`** | Solo para `docker compose` (archivo en `.gitignore`, crearlo manualmente). Copiar desde [`.env.docker.example`](.env.docker.example). |

Compose, por defecto, también lee `.env` del directorio del proyecto para sustituir variables en `docker-compose.yml`. Para **no mezclar** configuración local con la del stack en contenedores, usar siempre:

```bash
docker compose --env-file .env.docker up --build
```

La URL JDBC del contenedor `app` se resuelve con **`COMPOSE_DATABASE_URL`** en la interpolación del compose (por defecto `jdbc:mysql://mysql:3306/pet_adoption`). Así un `DATABASE_URL=jdbc:mysql://localhost:...` en tu **`.env` local** (para `./mvnw`) **no** rompe el stack Docker: esa variable ya no alimenta el servicio `app` en `docker-compose.yml`. Si necesitas parámetros extra en la URL dentro de Docker, define `COMPOSE_DATABASE_URL` en `.env.docker` (sigue siendo host `mysql`, puerto `3306`, nunca `localhost` ni el puerto publicado en el host).

### Si algo falla al levantar el stack

- **Contraseña de MySQL rechazada** tras cambiar `MYSQL_*` en `.env.docker`: el volumen `mysql_data` puede haberse inicializado con credenciales anteriores. Prueba `docker compose --env-file .env.docker down -v` (borra datos locales del contenedor MySQL) y vuelve a subir.
- **Puerto del host**: `MYSQL_HOST_PORT` solo cambia el mapeo en tu máquina; el contenedor `app` sigue usando `mysql:3306` por dentro.

### Arranque desde cero

1. Crea el archivo de variables de Docker (una vez):
   ```bash
   cp .env.docker.example .env.docker
   ```
   Edita `.env.docker`: `MYSQL_ROOT_PASSWORD`, `MYSQL_APP_PASSWORD` (y opcionalmente `MYSQL_APP_USER`), `JWT_SECRET`, y si hace falta `MYSQL_HOST_PORT` (ver siguiente apartado).

2. Construye y levanta los servicios:
   ```bash
   docker compose --env-file .env.docker up --build
   ```

   Prueba rápida con la plantilla sin copiar (valores de ejemplo, no producción):
   ```bash
   docker compose --env-file .env.docker.example up --build
   ```

3. Abre la aplicación en `http://localhost:8080` (catálogo en `/catalog`). Salud: `GET http://localhost:8080/actuator/health`.

### Puerto 3306 ya en uso

Si en tu máquina ya corre otro MySQL en el puerto **3306**, el servicio `mysql` del compose no podrá mapear `3306:3306` en el host. Opciones:

- En **`.env.docker`** define `MYSQL_HOST_PORT=3307` (u otro libre). Dentro de la red Docker la app sigue usando el puerto **3306** del contenedor `mysql`; solo cambia el puerto en **tu máquina** para conectarte con un cliente externo.
- O usa un `docker-compose.override.yml` local (no versionado) para ajustar `ports`.

Quien clone el repo y no tenga nada escuchando en 3306 puede usar el valor por defecto sin cambios.

### Solo MySQL en Docker, app en el host

1. Levanta únicamente la base (usa el mismo archivo que para el stack completo):
   ```bash
   docker compose --env-file .env.docker up mysql
   ```
2. En **`.env` local** (no en `.env.docker`) apunta a `localhost` y usa el **mismo usuario y contraseña de aplicación** que en `.env.docker` (`MYSQL_APP_USER` / `MYSQL_APP_PASSWORD`, por defecto `petapp`). Si cambiaste el puerto del host (`MYSQL_HOST_PORT`), ajústalo en la URL:
   ```bash
   DATABASE_URL=jdbc:mysql://localhost:3306/pet_adoption
   DATABASE_USERNAME=petapp
   DATABASE_PASSWORD=<igual que MYSQL_APP_PASSWORD en .env.docker>
   ```
3. Ejecuta `./mvnw spring-boot-run` como siempre.

### App en Docker: JDBC y usuario de BD

Host JDBC: servicio **`mysql`**, puerto **3306** en la red interna (variable opcional **`COMPOSE_DATABASE_URL`** en `.env.docker` o el default del `docker-compose.yml`). La app se conecta con **`MYSQL_APP_USER`** / **`MYSQL_APP_PASSWORD`** (usuario limitado a la base `pet_adoption`; `MYSQL_ROOT_PASSWORD` es solo para admin del motor). En local con `.env` y `./mvnw` sigues usando **`DATABASE_URL`** y credenciales que quieras (`localhost`, `root`, etc.); no hace falta duplicar eso en `.env.docker` para el contenedor `app`.

### Seguridad (recordatorio)

- No subir `.env` / `.env.docker` con secretos reales; `JWT_SECRET` y contraseñas fuertes en prod.
- MySQL publicado en un puerto del host: asumir solo desarrollo; en red expuesta, restringe acceso.
- Proceso Java en imagen sin root; JDBC en compose sin usuario `root` de MySQL (menor privilegio).

## Ejecución local (sin Docker)

Requisitos: Java 21, Maven, MySQL con la base de datos creada (por ejemplo `pet_adoption`).

```bash
./mvnw spring-boot-run
```

En el primer arranque, si no existen usuarios, se crean cuentas de ejemplo (solo para desarrollo):

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| `admin` | `admin123` | ADMIN |
| `staff` | `staff123` | STAFF |
| `user` | `user123` | USER |

En producción, cambiar contraseñas y desactivar o sustituir esta carga inicial de datos.

## Pruebas

```bash
./mvnw test
```

Las pruebas usan el perfil `test` con base de datos **H2** en memoria (el driver MySQL sigue siendo la dependencia de ejecución para despliegues reales).
