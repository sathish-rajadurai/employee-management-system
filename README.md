# Employee Management System

A production-style RESTful Employee Management System built using Java 21, Spring Boot 4, Spring Data JPA, Hibernate and PostgreSQL.

The project demonstrates REST API development, authentication, authorization, validation, exception handling, database operations, testing, Docker, CI/CD, audit logging and basic application monitoring.

## Features

### Employee Management

* Create employee
* Get all employees
* Get employee by ID
* Update employee
* Delete employee
* Email uniqueness validation
* Request validation
* Pagination
* Sorting
* Employee search
* Advanced employee filtering

    * Department
    * Minimum salary
    * Maximum salary
    * Combined filters

### Authentication & Authorization

* User registration
* User login
* JWT-based authentication
* BCrypt password hashing
* Stateless authentication
* Role-based access control
* USER and ADMIN roles
* Protected employee APIs
* ADMIN-only create, update and delete operations
* USER/ADMIN access to employee GET operations

### API

* Versioned employee API using `/api/v1`
* RESTful API design
* Swagger/OpenAPI documentation
* Standard HTTP status codes
* Centralized API response handling

### Error Handling

* Global exception handling using `@RestControllerAdvice`
* Request validation errors
* Employee not found handling
* Duplicate email handling
* Invalid pagination handling
* Invalid sorting field handling
* Invalid sorting direction handling
* Unexpected exception handling

### Audit Logging

* Employee CREATE audit logs
* Employee UPDATE audit logs
* Employee DELETE audit logs
* Records authenticated username
* Records action
* Records entity name and entity ID
* Records timestamp

### Testing

* Unit testing with JUnit 5
* Mockito-based service testing
* Controller testing with MockMvc
* Validation testing
* Exception handling testing

### Docker

* Dockerized Spring Boot application
* Dockerized PostgreSQL
* Docker Compose configuration
* Persistent PostgreSQL volume
* PostgreSQL health check
* Application dependency on healthy PostgreSQL
* Environment-based configuration
* Container restart policy

### CI/CD

* GitHub Actions workflow
* Automated Maven build
* Automated test execution
* CI triggered on pushes to `main`
* CI triggered on pull requests to `main`

### Production Configuration

* Development and production Spring profiles
* Environment variable based database configuration
* Environment variable based JWT secret
* Production database schema validation
* Externalized configuration
* Secrets excluded from Git using `.gitignore`

### Monitoring

* Spring Boot Actuator
* Application health check
* JVM memory metrics
* CPU metrics
* HTTP request metrics
* HikariCP database connection metrics
* Protected monitoring endpoints

---

## Technology Stack

* Java 21
* Spring Boot 4.0.7
* Spring Security
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* JUnit 5
* Mockito
* MockMvc
* JWT / JJWT
* BCrypt
* Swagger/OpenAPI
* Spring Boot Actuator
* Docker
* Docker Compose
* Git
* GitHub
* GitHub Actions
* IntelliJ IDEA

---

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
├── security
├── service
└── specification
```

---

## Database

* Database: `practice`
* Schema: `employee_db`
* Employee table: `employees`
* User table: `users`
* Audit table: `audit_logs`

The application uses PostgreSQL with Spring Data JPA and Hibernate.

Database credentials and other sensitive configuration values are provided through environment variables.

---

## API Endpoints

### Authentication

| Method | Endpoint             | Description                        |
| ------ | -------------------- | ---------------------------------- |
| POST   | `/api/auth/register` | Register a new user                |
| POST   | `/api/auth/login`    | Authenticate user and generate JWT |

### Employee

| Method | Endpoint                 | Description                                                  |
| ------ | ------------------------ | ------------------------------------------------------------ |
| POST   | `/api/v1/employees`      | Create employee                                              |
| GET    | `/api/v1/employees`      | Get employees with pagination, search, sorting and filtering |
| GET    | `/api/v1/employees/{id}` | Get employee by ID                                           |
| PUT    | `/api/v1/employees/{id}` | Update employee                                              |
| DELETE | `/api/v1/employees/{id}` | Delete employee                                              |

### Monitoring

| Method | Endpoint            | Description               |
| ------ | ------------------- | ------------------------- |
| GET    | `/actuator/health`  | Application health status |
| GET    | `/actuator/metrics` | Application metrics       |

---

## Authorization

### USER

A USER can:

```text
GET /api/v1/employees
GET /api/v1/employees/{id}
```

A USER cannot:

```text
POST /api/v1/employees
PUT /api/v1/employees/{id}
DELETE /api/v1/employees/{id}
```

### ADMIN

An ADMIN can perform all employee operations.

---

## Pagination

```text
GET /api/v1/employees?page=0&size=10
```

---

## Search

```text
GET /api/v1/employees?search=IT
```

---

## Sorting

```text
GET /api/v1/employees?sortBy=salary&sortDir=desc
```

---

## Advanced Filtering

### Department

```text
GET /api/v1/employees?department=IT
```

### Minimum Salary

```text
GET /api/v1/employees?minSalary=50000
```

### Maximum Salary

```text
GET /api/v1/employees?maxSalary=100000
```

### Combined Filtering

```text
GET /api/v1/employees?department=IT&minSalary=50000&maxSalary=100000
```

### Combined Filtering + Pagination + Sorting

```text
GET /api/v1/employees?department=IT&minSalary=50000&maxSalary=100000&page=0&size=5&sortBy=salary&sortDir=desc
```

---

## Exception Handling

The application uses centralized exception handling with `@RestControllerAdvice`.

Handled scenarios include:

* `400 Bad Request` - Validation or invalid request parameters
* `401 Unauthorized` - Authentication required or invalid JWT
* `403 Forbidden` - Insufficient role/permission
* `404 Not Found` - Employee not found
* `409 Conflict` - Duplicate email
* `500 Internal Server Error` - Unexpected errors

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

---

## Audit Logging

Employee modification operations are recorded in the `employee_db.audit_logs` table.

Example audit information:

```text
Username
Action
Entity Name
Entity ID
Timestamp
```

Supported actions:

```text
CREATE
UPDATE
DELETE
```

---

## Testing

The project includes automated tests using:

* JUnit 5
* Mockito
* MockMvc

### Service Tests

* Create employee
* Duplicate email validation
* Employee not found
* Update employee
* Delete employee
* Employee filtering

### Controller Tests

* Successful employee creation
* Invalid request validation
* Get employee by ID
* Employee not found
* Employee API responses
* Validation error responses

The project uses `mvn clean verify` as the standard build and test command.

---

## Docker

The application can be run using Docker Compose.

The Docker setup contains:

```text
Spring Boot Application
        │
        │
        ▼
PostgreSQL
```

PostgreSQL uses a persistent Docker volume so database data survives container restarts.

The PostgreSQL container also includes a health check using `pg_isready`.

### Run with Docker

```bash
docker compose up --build
```

### Stop containers

```bash
docker compose down
```

The PostgreSQL volume is intentionally preserved when using `docker compose down`.

---

## Configuration

The application supports separate environments:

```text
application.properties
application-dev.properties
application-prod.properties
```

Sensitive values such as:

* Database password
* JWT secret
* Database credentials

are provided through environment variables.

A local `.env` file can be used for Docker development and is excluded from Git.

---

## CI/CD

GitHub Actions automatically runs the Maven build and tests.

The workflow runs on:

* Push to `main`
* Pull request to `main`

Build command:

```bash
mvn clean verify
```

This ensures that changes are automatically validated before being considered ready.

---

## Monitoring

Spring Boot Actuator provides basic application monitoring.

### Health

```text
GET /actuator/health
```

Example:

```json
{
  "status": "UP"
}
```

### Metrics

```text
GET /actuator/metrics
```

Available metrics include information about:

* JVM memory
* CPU usage
* HTTP requests
* HikariCP database connections

Health is publicly accessible for health monitoring, while other monitoring endpoints require authentication.

---

## Swagger

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger provides interactive API documentation and allows the REST APIs to be tested from the browser.

---

## Running the Application

### Prerequisites

* Java 21
* Maven
* PostgreSQL
* Git

### Database

Create:

```text
Database: practice
Schema: employee_db
```

Configure the required database credentials using environment variables.

### Run the Application

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

---

## GitHub Actions

The project uses GitHub Actions for continuous integration.

The CI pipeline:

```text
Git Push / Pull Request
        ↓
Checkout Code
        ↓
Setup Java 21
        ↓
Maven Build
        ↓
Run Tests
        ↓
Build Successful
```

---

## Future Improvements

Potential future improvements include:

* Refresh token support
* Password reset functionality
* Email notifications
* More advanced audit history
* Redis caching
* Rate limiting
* Prometheus and Grafana monitoring
* Centralized logging
* Automated database migrations with Flyway
* HTTPS / reverse proxy configuration
* Cloud deployment
