# 🏢 Enterprise Order Platform

Plataforma de microservicios empresarial para gestión de pedidos, construida con **Java 21**, **Spring Boot 3.5**, y **Spring Cloud 2025.0.1**.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-green?style=flat-square&logo=springboot)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.1-green?style=flat-square&logo=spring)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=flat-square&logo=docker)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)

---

## 📐 Arquitectura

```
                    ┌──────────────┐
                    │   Cliente    │
                    │  (Postman)   │
                    └──────┬───────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │   API GATEWAY   │  Puerto 8080
                  │  (Spring Cloud  │
                  │    Gateway)     │
                  └────────┬────────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
              ▼            ▼            ▼
     ┌──────────────┐                 (próximamente)
     │ USER SERVICE │                 Order Service
     │  Puerto 8081 │                 Product Service
     └──────┬───────┘
            │
            ▼
     ┌──────────────┐
     │  PostgreSQL   │  Puerto 5432
     │   (Docker)    │
     └──────────────┘

     Todos los servicios se registran en:
     ┌──────────────────┐
     │  EUREKA SERVER   │  Puerto 8761
     │  (Service        │
     │   Discovery)     │
     └──────────────────┘
```

---

## 🧩 Microservicios

| Servicio | Puerto | Descripción | Estado |
|----------|--------|-------------|--------|
| **Eureka Server** | 8761 | Servidor de descubrimiento de servicios | ✅ Completo |
| **API Gateway** | 8080 | Punto de entrada único, enrutamiento a microservicios | ✅ Completo |
| **User Service** | 8081 | Gestión de usuarios, autenticación JWT, CRUD | ✅ Completo |
| **Order Service** | 8082 | Gestión de pedidos | 🔜 Próximamente |
| **Product Service** | 8083 | Catálogo de productos | 🔜 Próximamente |

---

## 🛠️ Tecnologías

### Backend
- **Java 21** - Lenguaje principal
- **Spring Boot 3.5.10** - Framework base
- **Spring Cloud 2025.0.1** - Microservicios
- **Spring Cloud Gateway** - API Gateway (WebFlux)
- **Spring Cloud Netflix Eureka** - Service Discovery
- **Spring Security + JWT** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM

### Base de Datos
- **PostgreSQL 16** - Base de datos relacional
- **Docker Compose** - Contenedorización de PostgreSQL

### Testing
- **JUnit 5** - Tests unitarios
- **Mockito** - Mocking de dependencias

### Herramientas
- **Maven** - Gestión de dependencias
- **Docker** - Contenedorización
- **Postman** - Testing de APIs
- **Git/GitHub** - Control de versiones

---

## 🚀 Cómo Ejecutar

### Prerrequisitos

- Java 21
- Maven 3.9+
- Docker Desktop

### 1. Clonar el repositorio

```bash
git clone https://github.com/angelfits/-enterprise-order-platform.git
cd -enterprise-order-platform
```

### 2. Iniciar PostgreSQL con Docker

```bash
docker-compose up -d
```

### 3. Iniciar los servicios (en este orden)

**Eureka Server (primero):**
```bash
cd eureka-server
mvn spring-boot:run
```

**User Service (segundo):**
```bash
cd user-service
mvn spring-boot:run
```

**API Gateway (tercero):**
```bash
cd api-gateway
mvn spring-boot:run
```

### 4. Verificar

- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080

---

## 📡 API Endpoints

Todos los endpoints se acceden a través del **API Gateway** (puerto 8080).

### Autenticación

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Registrar usuario | ❌ No |
| POST | `/api/auth/login` | Iniciar sesión (retorna JWT) | ❌ No |

### Usuarios

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/api/users` | Listar todos los usuarios | ✅ JWT |
| GET | `/api/users/{id}` | Obtener usuario por ID | ✅ JWT |
| PUT | `/api/users/{id}` | Actualizar usuario | ✅ JWT |
| DELETE | `/api/users/{id}` | Eliminar usuario | ✅ JWT |

### Ejemplos con Postman

**Registrar usuario:**
```json
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
    "name": "Angel",
    "email": "angel@test.com",
    "password": "123456"
}
```

**Login (obtener token):**
```json
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
    "email": "angel@test.com",
    "password": "123456"
}
```

**Respuesta:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Consultar usuarios (con JWT):**
```
GET http://localhost:8080/api/users
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 📁 Estructura del Proyecto

```
enterprise-order-platform/
│
├── eureka-server/                    # Service Discovery
│   ├── src/main/java/
│   │   └── com/enterprise/eureka/
│   │       └── EurekaServerApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── api-gateway/                      # API Gateway
│   ├── src/main/java/
│   │   └── com/enterprise/gateway/
│   │       └── ApiGatewayApplication.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
├── user-service/                     # User Microservice
│   ├── src/main/java/
│   │   └── com/enterprise/user/
│   │       ├── UserServiceApplication.java
│   │       ├── controller/
│   │       │   └── UserController.java
│   │       ├── model/
│   │       │   └── User.java
│   │       ├── repository/
│   │       │   └── UserRepository.java
│   │       ├── service/
│   │       │   └── UserService.java
│   │       ├── dto/
│   │       ├── security/
│   │       │   ├── JwtFilter.java
│   │       │   ├── JwtUtil.java
│   │       │   └── SecurityConfig.java
│   │       └── exception/
│   ├── src/test/java/
│   │   └── com/enterprise/user/
│   │       └── service/
│   │           └── UserServiceTest.java
│   ├── Dockerfile
│   └── pom.xml
│
└── docker-compose.yml                # PostgreSQL container
```

---

## 🧪 Testing

### Ejecutar tests del User Service

```bash
cd user-service
mvn test
```

### Tests incluidos

- **UserServiceTest** - Tests unitarios con JUnit 5 y Mockito
    - Registro de usuarios
    - Login y generación de JWT
    - CRUD de usuarios
    - Validaciones y manejo de errores

---

## 🔐 Seguridad

- **JWT (JSON Web Token)** para autenticación stateless
- **BCrypt** para encriptación de contraseñas
- **Spring Security** para protección de endpoints
- Endpoints públicos: `/api/auth/register`, `/api/auth/login`
- Endpoints protegidos: `/api/users/**` (requieren token JWT)

---

## 🐳 Docker

### PostgreSQL

```bash
# Iniciar
docker-compose up -d

# Verificar
docker ps

# Detener
docker-compose down
```

### docker-compose.yml

```yaml
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: enterprise_db
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin123
    ports:
      - "5432:5432"
```

---

## 📚 Patrones y Conceptos Aplicados

- **Microservices Architecture** - Servicios independientes y desacoplados
- **API Gateway Pattern** - Punto de entrada único
- **Service Discovery** - Registro y descubrimiento automático con Eureka
- **Client-Side Load Balancing** - Balanceo con Spring Cloud LoadBalancer
- **DTO Pattern** - Separación entre entidades y transferencia de datos
- **Repository Pattern** - Abstracción de acceso a datos con Spring Data JPA
- **JWT Authentication** - Autenticación stateless entre microservicios
- **Layered Architecture** - Controller → Service → Repository

---

## 👤 Autor

**Angel** - [GitHub](https://github.com/angelfits)

---

## 📝 Licencia

Este proyecto es de uso educativo y de aprendizaje.