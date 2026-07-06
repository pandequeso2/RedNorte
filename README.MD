# 🏥 RedNorte — Sistema de Gestión de Listas de Espera Hospitalarias

Plataforma web para la gestión inteligente de listas de espera médicas, citas y reasignaciones automáticas en el Servicio Público de Salud RedNorte.

---

## 📋 Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Arquitectura del Sistema](#arquitectura-del-sistema)
- [Stack Tecnológico](#stack-tecnológico)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Módulos del Backend](#módulos-del-backend)
- [Frontend](#frontend)
- [Base de Datos](#base-de-datos)
- [Infraestructura (AWS)](#infraestructura-aws)
- [CI/CD](#cicd)
- [Instalación y Ejecución Local](#instalación-y-ejecución-local)
- [Ejecución con Docker Compose](#ejecución-con-docker-compose)
- [API Endpoints](#api-endpoints)
- [Credenciales de Prueba](#credenciales-de-prueba)

---

## Descripción General

RedNorte resuelve uno de los principales problemas del sistema de salud pública: la gestión ineficiente de listas de espera y la pérdida de horas médicas por cancelaciones de último momento.

El sistema permite:

- Registrar y priorizar pacientes en listas de espera por especialidad
- Cancelar citas médicas y reasignar automáticamente la hora al paciente con mayor prioridad
- Notificar a los pacientes sobre cambios en su estado de espera
- Consultar el estado de solicitudes desde un portal de paciente
- Gestionar el sistema completo desde un portal médico

---

## Arquitectura del Sistema

El proyecto sigue una arquitectura de **múltiples bases de datos** con un backend monolítico que gestiona cuatro datasources independientes, un BFF (Backend for Frontend) con circuit breaker, y microservicios especializados.

```
┌─────────────────────────────────────────────┐
│              FRONTEND (React + Vite)         │
│         Portal Médico | Portal Paciente      │
└────────────────────┬────────────────────────┘
                     │ HTTP (port 8080)
┌────────────────────▼────────────────────────┐
│           BACKEND (Spring Boot 4)            │
│                                              │
│  ┌──────────┐ ┌──────────┐ ┌─────────────┐  │
│  │ Pacientes│ │  Espera  │ │Reasignación │  │
│  │  /Citas  │ │  /Notas  │ │  /Eventos   │  │
│  └────┬─────┘ └────┬─────┘ └──────┬──────┘  │
└───────┼─────────── ┼──────────────┼──────────┘
        │            │              │
┌───────▼─┐  ┌───────▼──┐  ┌───────▼──────────┐
│rednortedb│  │rednorte_ │  │rednorte_         │
│(principal│  │espera /  │  │reasignaciones /  │
│ pacientes│  │noti      │  │notificaciones    │
│ , citas) │  │ficaciones│  └──────────────────┘
└──────────┘  └──────────┘
```

### Patrón de eventos interno

Cuando se cancela una cita, el `CitaMedicaService` publica un `CitaCanceladaEvent` usando el `ApplicationEventPublisher` de Spring. El `ReasignacionService` escucha este evento con `@EventListener` y ejecuta la lógica de reasignación de forma desacoplada.

---

## Stack Tecnológico

### Backend
| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 (CI) / 25 (Dockerfiles) | Lenguaje principal |
| Spring Boot | 4.0.4 | Framework principal |
| Spring Data JPA | — | Acceso a datos |
| Resilience4j | — | Circuit breaker (BFF) |
| Lombok | — | Reducción de boilerplate |
| SpringDoc OpenAPI | 3.0.2 | Documentación Swagger |
| MySQL Connector | — | Driver de base de datos |
| Maven | 3.9.12 | Build y dependencias |

### Frontend
| Tecnología | Versión | Uso |
|---|---|---|
| React | 19.2.5 | Framework UI |
| Vite | 8.0.10 | Bundler y dev server |
| React Router DOM | 7.14.2 | Enrutamiento |
| Axios | 1.15.2 | Cliente HTTP |
| Tailwind CSS | 4.2.4 | Estilos utilitarios |

### Infraestructura
| Tecnología | Uso |
|---|---|
| Docker / Docker Compose | Contenedores locales |
| Nginx | Servidor web (Frontend) |
| Terraform | Infraestructura como código (AWS) |
| GitHub Actions | CI/CD |
| AWS EC2 + VPC | Despliegue en nube |

---

## Estructura del Proyecto

```
RedNorte/
├── .github/
│   └── workflows/
│       └── ci.yml                  # Pipeline CI/CD
├── Backend/
│   ├── src/
│   │   ├── main/java/cl/RedNorte/Backend/
│   │   │   ├── config/             # Configuración multi-datasource
│   │   │   ├── controller/         # Controladores REST
│   │   │   ├── events/             # Eventos internos (CitaCanceladaEvent)
│   │   │   ├── model/
│   │   │   │   ├── primary/        # Entidades: Paciente, CitaMedica, Especialidad
│   │   │   │   ├── espera/         # Entidades: SolicitudEspera
│   │   │   │   ├── notificaciones/ # Entidades: Notificacion
│   │   │   │   └── reasignaciones/ # Entidades: ReasignacionCita
│   │   │   ├── repository/         # Repositorios JPA (por datasource)
│   │   │   └── service/            # Lógica de negocio
│   │   └── test/                   # Tests unitarios con Mockito
│   ├── BFF/                        # Backend for Frontend con Circuit Breaker
│   ├── ms-listas-espera/           # Microservicio de listas de espera
│   ├── ms-reasignacion/            # Microservicio de reasignación
│   ├── Dockerfile
│   └── pom.xml
├── Frontend/
│   ├── src/
│   │   ├── api/                    # Configuración de Axios
│   │   ├── components/             # NavBar, Layout, ProtectedRoute
│   │   ├── context/                # AuthContext (autenticación simulada)
│   │   └── pages/                  # Vistas de la aplicación
│   ├── Dockerfile
│   └── nginx.conf
├── Infra/
│   ├── main.tf                     # EC2, Security Groups
│   ├── vpc.tf                      # VPC, subredes, NAT Gateway
│   └── variables.tf
├── docker-compose.yml
├── init dbs.sql
└── README.md
```

---

## Módulos del Backend

### Configuración Multi-Datasource

El backend gestiona **4 bases de datos independientes**, cada una con su propio `DataSource`, `EntityManagerFactory` y `TransactionManager`:

| Config Class | Prefix | Paquete Entidades | Paquete Repositorios |
|---|---|---|---|
| `PrimaryDatabaseConfig` | `spring.datasource.primary` | `model.primary` | `repository.primary` |
| `EsperaDatabaseConfig` | `spring.datasource.espera` | `model.espera` | `repository.espera` |
| `NotificacionesDatabaseConfig` | `spring.datasource.notificaciones` | `model.notificaciones` | `repository.notificacion` |
| `ReasignacionesDatabaseConfig` | `spring.datasource.reasignaciones` | `model.reasignaciones` | `repository.reasignacion` |

### Flujo de Cancelación y Reasignación

```
POST /api/citas/{id}/cancelar
        │
        ▼
CitaMedicaService.cancelarCita()
  ├── Cambia estado cita → CANCELADA
  ├── Guarda en BD
  └── Publica CitaCanceladaEvent
              │
              ▼ (ApplicationEventPublisher)
ReasignacionService.manejarCitaCancelada()
  ├── Busca candidatos en lista de espera (mayor prioridad)
  ├── Actualiza SolicitudEspera → ASIGNADO
  └── Registra ReasignacionCita en BD
```

### Patrones de Diseño Implementados

- **Strategy Pattern**: `NotificacionStrategy` con implementaciones `EmailNotificacion` y `SmsNotificacion`
- **Factory Method**: `NotificacionFactory` para seleccionar el canal de envío
- **Event-Driven**: Desacoplamiento entre cancelación y reasignación via `ApplicationEventPublisher`
- **Circuit Breaker**: BFF usa Resilience4j para proteger llamadas al `ms-listas-espera`

### BFF (Backend for Frontend)

Actúa como proxy entre el frontend y el microservicio de listas de espera (puerto 8081). Incluye un circuit breaker que devuelve una lista vacía si el microservicio cae.

```
Frontend → BFF (:8080) ──→ ms-listas-espera (:8081)
                      └──→ [fallback: lista vacía] (si cae)
```

---

## Frontend

### Páginas Disponibles

| Ruta | Página | Acceso |
|---|---|---|
| `/` | Welcome | Público |
| `/login` | Login | Público |
| `/portal-paciente` | Portal del Paciente | Solo pacientes |
| `/dashboard` | Dashboard médico | Solo médicos |
| `/pacientes` | Listado y registro de pacientes | Solo médicos |
| `/pacientes/:id` | Detalle y edición de contacto | Solo médicos |
| `/lista-espera` | Gestión de lista de espera | Solo médicos |
| `/citas` | Cancelación de citas | Solo médicos |
| `/reasignacion` | Motor de reasignación manual | Solo médicos |
| `/notificaciones` | Consulta de notificaciones | Solo médicos |

### Autenticación

La autenticación es **simulada** (sin backend real de auth). Se gestiona con `AuthContext` usando un listado hardcodeado de usuarios. En producción debería reemplazarse por JWT u OAuth2.

---

## Base de Datos

### Esquema por Base de Datos

**`rednortedb`** (principal)
- `pacientes`: Datos personales, RUT, email, teléfono, género
- `especialidad`: Catálogo de especialidades médicas
- `citas_medicas`: Citas con estado (DISPONIBLE, RESERVADA, CANCELADA, COMPLETADA)

**`rednorte_espera`**
- `solicitud_espera`: Solicitudes con prioridad 1-5, tipo de atención, estado

**`rednorte_reasignaciones`**
- `reasignaciones_automaticas`: Historial de reasignaciones por cancelación

**`rednorte_notificaciones`**
- `notificaciones_pacientes`: Mensajes enviados a pacientes (leída/no leída)

### Script de Datos de Prueba

El archivo `Backend/src/main/resources/base de datos.txt` contiene un script SQL con datos de prueba: 10 especialidades, 10 pacientes, 15 citas médicas, 10 solicitudes de espera, 3 reasignaciones y 10 notificaciones.

---

## Infraestructura (AWS)

La infraestructura está definida como código con Terraform en la carpeta `Infra/`.

```
Internet
    │
    ▼
Internet Gateway
    │
    ▼ (10.0.1.0/24 - pública)
EC2 Frontend (Nginx) ←──── sg_frontend: 80/tcp, 22/tcp
    │
    │ (solo puerto 8080)
    ▼ (10.0.2.0/24 - privada)
EC2 Backend (Java) ←──── sg_backend: 8080/tcp desde sg_frontend, 22/tcp admin
    │
    │ (solo puerto 3306)
    ▼
EC2 Data (MySQL) ←──── sg_data: 3306/tcp desde sg_backend, 22/tcp admin
```

**Subredes:**
- Pública (`10.0.1.0/24`): Frontend con IP pública
- Privada (`10.0.2.0/24`): Backend y Base de Datos, sin acceso directo desde internet. Salen via NAT Gateway

---

## CI/CD

El pipeline de GitHub Actions (`.github/workflows/ci.yml`) se ejecuta en cada Pull Request y push a `develop`.

```
┌──────────────────────┐    ┌──────────────────────┐
│   frontTest-build    │    │   backTest-build      │
│   npm test           │    │   ./mvnw test         │
└──────────┬───────────┘    └──────────┬────────────┘
           │                           │
           ▼                           ▼
┌──────────────────────┐    ┌──────────────────────┐
│     front-build      │    │     back-build        │
│   npm run build      │    │ ./mvnw clean package  │
└──────────────────────┘    └──────────────────────┘
```

Los jobs de build solo se ejecutan si los tests pasan (`needs`).

---

## Instalación y Ejecución Local

### Requisitos

- Java 21+
- Node.js 20+
- MySQL 8
- Maven 3.9+

### 1. Base de Datos

Crea las 4 bases de datos en MySQL:

```sql
CREATE DATABASE rednortedb;
CREATE DATABASE rednorte_espera;
CREATE DATABASE rednorte_notificaciones;
CREATE DATABASE rednorte_reasignaciones;
```

Luego ejecuta el script de datos de prueba en `Backend/src/main/resources/base de datos.txt`.

### 2. Backend

```bash
cd Backend
chmod +x mvnw
./mvnw spring-boot:run
```

El servidor arranca en `http://localhost:8080`.

La documentación Swagger estará disponible en: `http://localhost:8080/swagger-ui.html`

### 3. Frontend

```bash
cd Frontend
npm install
npm run dev
```

La aplicación estará disponible en `http://localhost:5173`.

---

## Ejecución con Docker Compose

La forma más rápida de levantar todo el sistema:

```bash
docker compose up --build
```

Servicios levantados:

| Servicio | Puerto | URL |
|---|---|---|
| MySQL | 3306 | — |
| Backend (Spring Boot) | 8080 | `http://localhost:8080` |
| Frontend (Nginx) | 3000 | `http://localhost:3000` |

> El `docker-compose.yml` inicializa automáticamente las 4 bases de datos usando `init-dbs.sql` y pasa las variables de entorno de conexión al backend.

---

## API Endpoints

### Pacientes — `GET|POST /api/pacientes`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/pacientes` | Listar todos los pacientes |
| GET | `/api/pacientes/{id}` | Buscar paciente por ID |
| GET | `/api/pacientes/rut/{rut}` | Buscar paciente por RUT |
| POST | `/api/pacientes/registrar` | Registrar nuevo paciente |
| PUT | `/api/pacientes/{id}/contacto` | Actualizar email y teléfono |

### Lista de Espera — `/api/listaEspera`

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/listaEspera/registrar` | Nueva solicitud de espera |
| GET | `/api/listaEspera/criticos` | Pacientes por mayor tiempo de espera |
| GET | `/api/listaEspera/paciente/{id}` | Estado de un paciente específico |

### Citas Médicas — `/api/citas`

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/citas/{id}/cancelar` | Cancelar cita y activar reasignación |

### Reasignación — `/api/reasignacion`

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/reasignacion/procesar-cancelacion/{citaId}` | Reasignación manual |

### Notificaciones — `/api/notificaciones`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/notificaciones/paciente/{id}/pendientes` | Mensajes no leídos de un paciente |

---

## Credenciales de Prueba

La autenticación es simulada. Contraseña para todos los usuarios: **`1234`**

| RUT | Nombre | Rol |
|---|---|---|
| `DOCTOR-001` | Dr. Héctor Vidal | Médico |
| `DOCTOR-002` | Dra. Laura Espinoza | Médico |
| `12345678-9` | Carlos Mendoza | Paciente (ID: 1) |
| `98765432-1` | María González | Paciente (ID: 2) |

---

## Tests

El proyecto incluye tests unitarios con JUnit 5 y Mockito para los servicios principales:

```bash
cd Backend
./mvnw test
```

Tests disponibles:
- `CitaMedicaServiceTest`: Verifica cancelación de citas y publicación de eventos
- `PacienteServiceTest`: Verifica CRUD de pacientes con repositorio mockeado

---

## Equipo

Proyecto desarrollado como parte del sistema de salud **RedNorte**.

---

> **Nota:** Las credenciales de base de datos en los archivos de configuración son para desarrollo local. En producción, usar variables de entorno (ya soportadas en `application.properties` con el patrón `${ENV_VAR:default}`).
