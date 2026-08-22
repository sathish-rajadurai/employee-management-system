# Employee Management System

A RESTful Employee Management System built using Java 21, Spring Boot 4, Spring Data JPA, Hibernate and PostgreSQL.

## Features

- Create employee
- Get all employees
- Get employee by ID
- Update employee
- Delete employee
- Email uniqueness validation
- Request validation
- Global exception handling
- Pagination
- Sorting
- Employee search
- Swagger/OpenAPI documentation
- Unit testing with JUnit and Mockito
- Controller testing with MockMvc

## Technology Stack

- Java 21
- Spring Boot 4.0.7
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- JUnit 5
- Mockito
- MockMvc
- Swagger/OpenAPI
- Git & GitHub
- IntelliJ IDEA

## Project Structure

```text
src/main/java/com/sathish/employee_management

├── config
├── controller
├── dto
├── entity
├── exception
├── mapper
├── repository
└── service
```

## Database

- Database: `employee_db`
- Schema: `practice`
- Table: `employees`

The application uses PostgreSQL with Spring Data JPA and Hibernate.

The database credentials are configured in `application.yml`.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/employees` | Create employee |
| GET | `/api/employees` | Get employees with pagination, search and sorting |
| GET | `/api/employees/{id}` | Get employee by ID |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |

### Pagination

GET `/api/employees?page=0&size=10`

### Search

GET `/api/employees?search=IT`

### Sorting

GET `/api/employees?sortBy=salary&sortDir=desc`

### Combined

GET `/api/employees?search=IT&page=0&size=5&sortBy=salary&sortDir=desc`

## Exception Handling

The application uses centralized exception handling with `@RestControllerAdvice`.

Handled scenarios include:

- `400 Bad Request` - Validation errors
- `404 Not Found` - Employee not found
- `409 Conflict` - Duplicate email
- `500 Internal Server Error` - Unexpected errors

Example validation response:

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "salary": "Salary must be greater than zero"
  }
}
```

## Testing

The project includes automated tests using:

- JUnit 5
- Mockito
- MockMvc

### Service Tests

- Create employee
- Duplicate email validation
- Employee not found

### Controller Tests

- Successful employee creation
- Invalid request validation
- Get employee by ID
- Employee not found

## Running the Application

### Prerequisites

- Java 21
- Maven
- PostgreSQL
- Git

### Database Setup

Create a PostgreSQL database:

```text
practice
```

Create a PostgreSQL database schema:

```text
employee_db
```

### Run the Application
```text
mvn spring-boot:run
```
or
```text
mvn spring-boot:run "-Dspring-boot.run.arguments=--DB_USERNAME=postgres_user --DB_PASSWORD=postgres_pass"
```

### Application runs on
http://localhost:8080

### Swagger
http://localhost:8080/swagger-ui/index.html


---

## Future Improvements

- Spring Security authentication and authorization
- Role-based access control
- Dockerize the application
- CI/CD pipeline with GitHub Actions
- Advanced employee filtering
- Audit logging
- Production deployment
- API versioning
- Monitoring and health checks