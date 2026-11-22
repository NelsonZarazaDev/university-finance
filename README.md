<div align="center">

# University Finance

**Gestión académica y financiera universitaria · Proyecto práctico Full Stack**

[![JHipster](https://img.shields.io/badge/JHipster-9.4.0-1b5e20?style=for-the-badge)](https://www.jhipster.tech/)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React-19-149ECA?style=for-the-badge&logo=react&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-Database-F80000?style=for-the-badge&logo=oracle&logoColor=white)

**[JHipster](https://www.jhipster.tech/)** es la tecnología principal utilizada para generar y estructurar la aplicación: un monolito con API REST en Spring Boot y cliente React.

</div>

---

## ¿Qué es University Finance?

University Finance es una **aplicación web de práctica** que representa una parte del sistema administrativo-financiero de una universidad. Permite administrar estudiantes y sus obligaciones financieras, manteniendo una relación consistente entre ambas entidades.

El objetivo es ejercitar el desarrollo **end-to-end** con JHipster, la arquitectura de Spring Boot, el consumo de APIs REST desde React, la persistencia relacional en Oracle y el uso de DTO, validaciones y migraciones de base de datos. **No es un sistema financiero institucional en producción**.

## Alcance y funcionalidades implementadas

| Módulo | Funcionalidades existentes |
| --- | --- |
| **Estudiantes** | Crear, listar, consultar, editar y eliminar estudiantes; código único, nombre, apellido, correo, fecha de matrícula y estado (`ACTIVE`, `INACTIVE`, `GRADUATED`). |
| **Obligaciones financieras** | Operaciones CRUD para obligaciones con valor, fecha de vencimiento y estado (`OUTSTANDING`, `PARTIALLY_PAID`, `PAID`); asociación obligatoria a un estudiante. |
| **Relación entre entidades** | Un estudiante puede tener múltiples obligaciones; la API usa DTO y MapStruct para exponer datos del estudiante asociado. |
| **Interfaz web** | Formularios y tablas en React, paginación, ordenación, vistas de detalle, navegación y traducciones en español e inglés. |
| **Acceso y administración** | Inicio de sesión con JWT, roles `ROLE_ADMIN` y `ROLE_USER`, administración de usuarios y vistas de configuración, salud, registros y métricas de JHipster. |
| **Datos y API** | REST con Spring Data JPA, validaciones, changelogs Liquibase y datos de ejemplo para el perfil `dev`. Documentación de API mediante Swagger UI. |

**Límites actuales:** el repositorio **no implementa** pasarela de pagos, recaudo real, conciliación bancaria, facturación ni reportes financieros avanzados. Los estados de las obligaciones se administran como datos; no representan transacciones monetarias procesadas.

## Tecnologías

| Capa | Tecnologías |
| --- | --- |
| Generación y estructura | **JHipster 9.4.0** (monolito) |
| Backend | Java 21, Spring Boot 4.1.1, Spring MVC, Spring Data JPA, Spring Security, JWT |
| Frontend | React 19, TypeScript, Vite 8, React Bootstrap, Redux Toolkit |
| Persistencia | Oracle Database, Liquibase, Hibernate/JPA |
| Mapeo y calidad | MapStruct, Bean Validation, Gradle 9.7.1 |
| Pruebas | JUnit, Vitest, Playwright; integración configurada con Testcontainers |
| Documentación / observabilidad | OpenAPI/Swagger UI, Spring Boot Actuator |

### Estructura principal

```text
src/
├── main/
│   ├── java/com/universityfinance/
│   │   ├── domain/           # Entidades y enumeraciones
│   │   ├── repository/       # Persistencia JPA
│   │   ├── service/          # Servicios, DTO y mappers MapStruct
│   │   ├── web/rest/         # Endpoints REST
│   │   ├── security/         # Autenticación y autorización
│   │   └── config/           # Configuración de Spring/JHipster
│   ├── resources/config/
│   │   ├── application*.yml  # Perfiles y conexión a Oracle
│   │   └── liquibase/        # Migraciones y datos de prueba
│   └── webapp/app/
│       ├── entities/         # Vistas React: Student / FinancialObligation
│       ├── modules/          # Acceso, cuenta, administración
│       └── shared/           # Componentes y utilidades comunes
├── test/
.jhipster/                    # Definición de entidades del generador
build.gradle                  # Build backend
package.json                  # Scripts y dependencias frontend
```

## Requisitos previos

- **JDK 21** (el proyecto compila con compatibilidad Java 21).
- **Node.js 24.21.0 o superior** y npm.
- **Oracle Database** accesible localmente o por red; el perfil de desarrollo apunta actualmente a `localhost:1521/FREEPDB1`.
- Git. **No necesitas instalar Gradle por separado**, porque se incluye su wrapper.

> La base de datos debe estar iniciada y el usuario configurado debe tener permisos suficientes para las migraciones de Liquibase. El repositorio no crea automáticamente una instancia de Oracle.

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/NelsonZarazaDev/university-finance.git
cd university-finance
npm ci
```

### 2. Configurar Oracle

El perfil `dev` utiliza `src/main/resources/config/application-dev.yml`. Por defecto contiene una URL JDBC con `localhost`, puerto `1521` y servicio `FREEPDB1`, además de un usuario y una contraseña de ejemplo escritos en la configuración.

Inicia tu instancia de Oracle y verifica que la URL, el usuario, la contraseña y el servicio correspondan a tu entorno. Puedes modificar la configuración local o, preferentemente, **sobrescribirla con variables de entorno** (ver la sección de credenciales).

Cuando arranca la aplicación, **Liquibase aplica las migraciones**. El perfil `dev` incluye el contexto `faker` para insertar datos de demostración.

### 3. Iniciar el backend

**Windows (PowerShell):**

```powershell
.\gradlew.bat
```

**Linux / macOS:**

```bash
./gradlew
```

El task predeterminado de Gradle es `bootRun` y utiliza el perfil de desarrollo. La API se expone en **http://localhost:8080**.

### 4. Iniciar el frontend

En **otra terminal**, desde la raíz del repositorio:

```bash
npm run start
```

Abre **http://localhost:9000**. Vite sirve la interfaz React y redirige las solicitudes de API al backend en el puerto `8080`.

## Cómo usar la aplicación

1. Accede a `http://localhost:9000` e inicia sesión con una cuenta de demostración.
2. En **Entities → Student**, consulta los estudiantes de ejemplo o registra uno nuevo.
3. En **Entities → Financial Obligation**, crea una obligación indicando monto, vencimiento, estado y estudiante asociado.
4. Usa las pantallas de detalle y edición, así como las tablas paginadas, para consultar y administrar la información.
5. Con el perfil adecuado y permisos de administrador, explora las opciones de administración y la documentación **Swagger UI**.

### Cuentas de demostración

| Rol | Usuario | Contraseña |
| --- | --- | --- |
| Administrador | `admin` | `admin` |
| Usuario estándar | `user` | `user` |

Son las cuentas de ejemplo de JHipster, incluidas en los datos iniciales y mencionadas en la pantalla de inicio. **Úsalas exclusivamente en un entorno de práctica.**

### API REST

| Recurso | Endpoint base | Métodos |
| --- | --- | --- |
| Estudiantes | `/api/students` | `GET`, `POST`, `PUT`, `PATCH`, `DELETE` |
| Obligaciones | `/api/financial-obligations` | `GET`, `POST`, `PUT`, `PATCH`, `DELETE` |

Las operaciones por elemento usan `/{id}`. Los listados admiten paginación y ordenación. Los endpoints protegidos requieren la autenticación y autorización correspondientes.

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) (perfil `dev` / `api-docs`).
- **API OpenAPI:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).

## Credenciales de práctica y configuración recomendada

> **Aviso importante:** este repositorio fue creado para **aprendizaje y demostración**. Actualmente contiene **credenciales de Oracle y una clave JWT de ejemplo directamente en archivos de configuración** (`application-dev.yml`, `application-prod.yml`, `application-secret-samples.yml` y configuración del generador), además de las cuentas de prueba `admin/admin` y `user/user`. Esto es intencional para el contexto de práctica; **no es una práctica segura para producción ni para bases de datos reales**.

La alternativa recomendada es extraer los secretos del código fuente y configurar **variables de entorno**. Por ejemplo, un archivo **`.env` local y excluido de Git** podría contener:

```dotenv
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@//localhost:1521/FREEPDB1
SPRING_DATASOURCE_USERNAME=oracle
SPRING_DATASOURCE_PASSWORD=REPLACE_WITH_LOCAL_PASSWORD
JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET=REPLACE_WITH_VALID_BASE64_SECRET
```

**Importante:** Spring Boot **no carga automáticamente** un archivo `.env`. Debes exportar esas variables al entorno del proceso, utilizar una herramienta que cargue el archivo o configurar explícitamente su importación. Un `.env` real no debe subirse al repositorio; antes de utilizarlo, añade `.env` al `.gitignore`.

Ejemplo de variables para una sesión **PowerShell**, antes de ejecutar el backend:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1"
$env:SPRING_DATASOURCE_USERNAME = "oracle"
$env:SPRING_DATASOURCE_PASSWORD = "REPLACE_WITH_LOCAL_PASSWORD"
.\gradlew.bat
```

> Si estas credenciales se reutilizan fuera del laboratorio, deben considerarse expuestas y reemplazarse. El hecho de que sean visibles en GitHub **no las hace seguras para otros entornos**.

## Pruebas y calidad

```bash
# Pruebas backend (Linux/macOS)
./gradlew test

# Pruebas unitarias frontend (Vitest)
npm test

# Comprobación de estilo
npm run lint

# Pruebas end-to-end (requieren la aplicación en ejecución)
npm run e2e
```

En Windows, utiliza `.\gradlew.bat test` para las pruebas backend. Las pruebas de integración y algunas pruebas automatizadas pueden necesitar servicios o infraestructura adicional; su disponibilidad depende del entorno local.

## Próximos pasos posibles

- Reglas de negocio para vencimientos y saldos.
- Consultas y filtros más específicos por estudiante.
- Indicadores y reportes financieros.
- Configuración de secretos externa y perfiles de despliegue.
- Mayor cobertura de pruebas de reglas de negocio.

Estas ideas son **mejoras propuestas, no funcionalidades existentes**.

---

**Propósito:** aprendizaje y portafolio técnico. Generado y desarrollado sobre [JHipster 9.4.0](https://www.jhipster.tech/documentation-archive/v9.4.0/), con funcionalidades y modelos propios para el dominio universitario.
