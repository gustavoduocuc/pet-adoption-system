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

### Arranque desde cero

1. Copia la plantilla de variables (o úsala al vuelo):
   ```bash
   cp .env.docker.example .env
   ```
   Edita `.env` y cambia al menos `MYSQL_ROOT_PASSWORD`, `DATABASE_PASSWORD` y `JWT_SECRET` (valores de ejemplo **no** son adecuados para producción).

2. Construye y levanta los servicios:
   ```bash
   docker compose up --build
   ```

   Equivalente sin copiar archivo:
   ```bash
   docker compose --env-file .env.docker.example up --build
   ```

3. Abre la aplicación en `http://localhost:8080` (catálogo en `/catalog`). Salud: `GET http://localhost:8080/actuator/health`.

### Puerto 3306 ya en uso

Si en tu máquina ya corre otro MySQL (u otro servicio) en el puerto **3306**, el mapeo del contenedor fallará. Opciones:

- Define en tu `.env` otro puerto host, por ejemplo `MYSQL_HOST_PORT=3307`, y conecta desde el host con `jdbc:mysql://localhost:3307/pet_adoption`.
- O usa un `docker-compose.override.yml` local (no versionado) solo en tu equipo para ajustar `ports`.

Quien clone el repo y no tenga nada escuchando en 3306 puede usar el valor por defecto sin cambios.

### Solo MySQL en Docker, app en el host

1. Levanta únicamente la base:
   ```bash
   docker compose up mysql
   ```
2. En tu `.env` (o variables de entorno) usa **`localhost`** en la URL JDBC, y el mismo `DATABASE_USERNAME` / `DATABASE_PASSWORD` que definiste para el servicio MySQL (por defecto `petuser` / `petpassword` si no personalizaste):
   ```bash
   DATABASE_URL=jdbc:mysql://localhost:3306/pet_adoption
   ```
3. Ejecuta `./mvnw spring-boot-run` como siempre.

### App en Docker: host JDBC

Dentro de Compose el host del servidor MySQL es el nombre del servicio: **`mysql`** (ya reflejado en `.env.docker.example`). No uses `localhost` en `DATABASE_URL` del contenedor `app`: apuntaría al propio contenedor de la aplicación.

### Seguridad (recordatorio)

- No subas `.env` con secretos reales; rota contraseñas y `JWT_SECRET` fuera de los ejemplos del repositorio.
- El mapeo `3306:3306` es cómodo para **desarrollo**; en entornos expuestos a red, evita publicar MySQL al host o restringe firewall y credenciales.
- La aplicación corre en el contenedor como usuario no root.

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
