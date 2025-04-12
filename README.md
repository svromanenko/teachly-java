# TEACHLY Education Platform - Backend Service

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.4-green.svg)
![Java](https://img.shields.io/badge/Java-22-blue.svg)
![H2](https://img.shields.io/badge/Database-H2-informational.svg)
![JWT](https://img.shields.io/badge/Security-JWT-orange.svg)

Backend service for the teacher-student education platform, providing authentication and profile management APIs compatible with the [iOS client application](https://github.com/svromanenko/teachly-auth-ios.git).

## Key Features

- JWT-based authentication
- Role-based access (Teacher/Student)
- H2 in-memory database
- BCrypt password encryption
- RESTful API endpoints

## Prerequisites

- Java 22
- Spring Boot 3.4.4

## Configuration

Set your JWT secret in `application.properties`:
```properties
   app.jwt.secret=your-256-bit-secret-base64-encoded
```

## Endpoints

| Method | Endpoint | Description | Required Headers |
|---------|---------|---------|---------|
| POST | /auth/register | Register new user | - |
| POST | /auth/login | Authenticate user | - |
| GET | /auth/profile | Get user profile | Authorization: Bearer {token} |
| POST | /auth/profile | Update profile | Authorization: Bearer {token} |

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Security
- Spring Boot Starter Data JPA
- Spring Boot Starter Validation
- H2 Database
- Lombok
- JJWT (JSON Web Tokens)
- iOS Client Integration

## Request/Response Examples

```
Registration request:

POST /auth/register
Content-Type: application/json

{
  "username": "John Doe",
  "password": "*****"
}

Response:
{
    "token": "eyJhbG...",
    "profile": {
        "id": 1,
        "name": null,
        "role": "STUDENT",
        "subjects": [],
        "about": null
    },
    "classes": [],
    "chats": []
}
```

## Security Configuration

- CSRF disabled
- Password encoder: BCryptPasswordEncoder(12)
- Required headers for authenticated endpoints:
    - auth/profile
- Authorization: Bearer your.jwt.token

## Getting Started

1. Clone the repository:
```
git clone https://github.com/your-repo/backend.git
```
2. Build and run:
```
mvn spring-boot:run
```
3. Access H2 console (if enabled):
```http://localhost:8080/h2-console```
