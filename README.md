# Sistema de adopción de mascotas

API backend para adopción de mascotas y registro veterinario básico de animales rescatados. Construida con Spring Boot, autenticación JWT sin estado, control de acceso por roles y persistencia en MySQL.

## Stack

- Java 21
- Spring Boot 4
- Spring Web MVC, Spring Security, Spring Data JPA
- MySQL (`mysql-connector-j`)
- JWT (biblioteca JJWT) para autenticación de la API
- Bean Validation (`@Valid`)

## Roles

| Rol | Descripción |
|-----|-------------|
| **ADMIN** | Acceso completo: gestionar mascotas y pacientes veterinarios. |
| **STAFF** | En esta API, equivalente a ADMIN: gestionar mascotas y pacientes. |
| **USER** | Solo lectura en zonas protegidas: puede usar los endpoints públicos del catálogo de mascotas. No puede crear, actualizar ni eliminar mascotas ni acceder a pacientes. |

Los claims del JWT incluyen los nombres de rol; Spring Security los mapea a autoridades `ROLE_ADMIN`, `ROLE_STAFF`, `ROLE_USER`.

## Autenticación

1. Llamar `POST /api/auth/login` con cuerpo JSON `{"username":"...","password":"..."}`.
2. Respuesta: `{"accessToken":"...","tokenType":"Bearer"}` (nunca se devuelven contraseñas).
3. En rutas protegidas, enviar cabecera: `Authorization: Bearer <accessToken>`.

Las peticiones sin autenticar a rutas protegidas reciben **401** en JSON. Los usuarios autenticados sin rol suficiente reciben **403** en JSON.

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

## Ejecución local

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
