# 🚀 User Service - Enterprise Order Platform

Microservicio de gestión de usuarios desarrollado con Spring Boot.

## 🛠️ Tecnologías

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- H2 Database (desarrollo)
- Lombok
- Maven

## 📁 Arquitectura
```
Arquitectura por Capas (Layered Architecture)

┌─────────────────────┐
│    Controller       │  ← API REST
├─────────────────────┤
│     Service         │  ← Lógica de negocio
├─────────────────────┤
│    Repository       │  ← Acceso a datos
├─────────────────────┤
│    Database         │  ← H2 / PostgreSQL
└─────────────────────┘
```

## 🚀 Cómo ejecutar

1. Clonar el repositorio
2. Abrir en IntelliJ IDEA
3. Ejecutar `UserServiceApplication.java`
4. La API estará disponible en `http://localhost:8081`

## 📌 Endpoints

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/users | Obtener todos los usuarios |
| GET | /api/users/{id} | Obtener usuario por ID |
| POST | /api/users | Crear usuario |
| PUT | /api/users/{id} | Actualizar usuario |
| DELETE | /api/users/{id} | Eliminar usuario |

## 📝 Ejemplo de petición

### Crear usuario:
```bash
POST http://localhost:8081/api/users
Content-Type: application/json

{
    "name": "Angel",
    "email": "angel@test.com",
    "password": "123456"
}
```

## 👤 Autor

Angel - Proyecto de aprendizaje Java Backend