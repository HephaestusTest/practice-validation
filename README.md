# User API Service

A Spring Boot REST API for user management with email and Slack notification support.

## Features

- CRUD operations for user accounts
- Role-based user management (USER, ADMIN)
- Email notifications for account events
- Slack integration for team notifications
- Custom exception handling with meaningful error messages

## Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Maven

## Quick Start

### Prerequisites

- JDK 21+
- Maven 3.9+

### Running Locally

```bash
mvn spring-boot:run
```

The server starts on `http://localhost:8080`.

### Running Tests

```bash
mvn test
```

## API Documentation

See the [API Guide](docs/API_GUIDE.md) for detailed endpoint documentation,
request/response examples, and error handling information.

## Project Structure

```
src/
  main/java/com/example/userapi/
    controller/      # REST controllers
    exception/       # Custom exceptions
    model/           # Domain entities
    repository/      # Data access layer
    service/         # Business logic
  test/java/com/example/userapi/
    service/         # Unit tests
```

## Contributing

1. Create a feature branch from `main`
2. Follow conventional commit message format
3. Include tests for new functionality
4. Open a pull request with a clear description
