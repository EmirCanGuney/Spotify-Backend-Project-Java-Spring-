# Spotify Backend API

A REST API for a music streaming platform built with Spring Boot, featuring user authentication, playlist management, and role-based authorization.

## Features

- User registration and authentication with JWT
- Music library management (artists, albums, songs)
- Personal playlist creation and management
- Role-based access control (User/Admin)
- Global exception handling
- Input validation

## Tech Stack

- Spring Boot 3.x
- Spring Security + JWT
- PostgreSQL
- Spring Data JPA
- JUnit 5 + Mockito
- Maven

## Project Structure

```
src/main/java/com/etiya/spotify/
├── controller/     # REST Controllers
├── dto/           # Request/Response DTOs
├── entity/        # JPA Entities
├── exception/     # Custom Exceptions
├── handler/       # Global Exception Handler
├── repository/    # JPA Repositories
├── security/      # JWT Security Config
└── service/       # Business Logic
```

## Architecture & Principles

This project follows modern Spring Boot best practices:

- **Layered Architecture**: Clean separation of Controller-Service-Repository layers
- **SOLID Principles**: Single responsibility, dependency injection, interface segregation
- **DTO Pattern**: Proper data transfer objects for API contracts
- **Exception Handling**: Global exception handling with @ControllerAdvice
- **Validation**: Bean validation with custom error messages
- **Security**: JWT authentication with BCrypt password hashing
- **Clean Code**: Meaningful method/variable names, DRY principle
- **Testing**: Unit tests for service layer with Mockito

## Setup

### Prerequisites
- Java 17+
- PostgreSQL 12+
- Maven 3.6+

### Installation
1. Clone repository
2. Create PostgreSQL database named `spotify`
3. Run `mvn clean install`
4. Run `mvn spring-boot:run`

Application starts on `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Music Library
- `GET /api/artists` - Get all artists
- `GET /api/albums` - Get all albums  
- `GET /api/songs` - Get all songs
- `POST /api/artists` - Create artist (Admin only)

### Playlists
- `GET /api/playlists/users/{userId}` - Get user playlists
- `POST /api/playlists` - Create playlist
- `PUT /api/playlists/{id}` - Update playlist
- `POST /api/playlists/{playlistId}/songs` - Add songs

### Users
- `GET /api/users/{id}` - Get user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user (Admin only)

## Security
<img width="1702" height="851" alt="Ekran görüntüsü 2025-09-25 151426" src="https://github.com/user-attachments/assets/f4e90976-a389-4199-9cf5-2f3c3a225452" />
JWT-based authentication with two roles:
- **USER**: Manage own playlists, view music
- **ADMIN**: Full system access

## Testing

Run tests: `mvn test`

Includes unit tests for all service classes with comprehensive coverage.

## Database

Entity relationships:
- User ↔ Role (Many-to-Many)
- User → Playlist (One-to-Many)
- Artist → Album (One-to-Many)
- Album → Song (One-to-Many)
- Playlist ↔ Song (Many-to-Many)
- 
<img width="361" height="328" alt="Ekran görüntüsü 2025-09-25 151516" src="https://github.com/user-attachments/assets/3ef7491c-77f9-47e5-aa98-bca517af8216" />



