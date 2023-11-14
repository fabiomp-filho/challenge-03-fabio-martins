
# MS User API

This project is a Spring Boot API for user management. It uses Spring Security for authentication and authorization and provides interactive API documentation with Swagger (Springdoc-OpenAPI).

## Features

- CRUD operations for users.
- Authentication and authorization with Spring Security.
- API documentation with Swagger (Springdoc-OpenAPI).

## Technologies Used

- Spring Boot
- Spring Security
- Spring Data JPA
- Springdoc-OpenAPI (Swagger)
- H2 Database (in-memory) for testing

## Prerequisites

- Java 11 or higher
- Maven (for project construction and management)

## How to Execute

To run the application, follow these steps:

1. Clone the repository:

   ```bash
   git clone [[Repository URL]](https://github.com/fabiomp-filho/challenge-03-fabio-martins.git)
   ```

2. Navigate to the project folder:

   ```bash
   cd ms-user
   ```

3. Execute the application using Maven:

   ```bash
   mvn spring-boot:run
   ```

4. The application will be available at `http://localhost:8080`.

## Accessing the API Documentation

After starting the application, you can access the Swagger API documentation through the following URL:

```
http://localhost:8080/swagger-ui/index.html
```

## API Endpoints

The API offers the following endpoints:

- `POST /v1/login`: Endpoint for user authentication.
- `GET /v1/users/{id}`: Endpoint to obtain details of a specific user.
- `POST /v1/users`: Endpoint for creating a new user.
- `PUT /v1/users/{id}`: Endpoint for updating an existing user.
- `PUT /v1/users/{id}/password`: Endpoint for updating a user's password.

## Security

The API uses Spring Security for user authentication and authorization. Credentials are passed via JWT Token.

## Contact

Fabio Martins - fabio.filho0820@gmail.com
