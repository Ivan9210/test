# Financial Transactions Microservice

This repository contains a high-performance microservice developed with **Spring Boot 3.4.1**, designed for the centralized management of internal financial transactions. The architecture is built upon **Clean Code**, **SOLID** principles, and robust security standards to ensure data integrity, atomicity, and full operational traceability.

---

## Key Features

* **Bank-Grade Security:** Implements **Spring Security** with stateless **JWT (JSON Web Tokens)** for authentication and authorization.
* **Decoupled Architecture:** Utilizes the **DTO (Data Transfer Object)** pattern to prevent direct exposure of database entities.
* **Strict Validation:** Ensures data quality using **Jakarta Bean Validation** for all incoming request payloads.
* **Enterprise Documentation:** Fully integrated **Swagger/OpenAPI 3** interface for API contract auditing.
* **Operational Traceability:** Structured logging via **SLF4J (Lombok)** for auditing business events and security incidents.

---

## Configuration & Environment Variables

This microservice follows the **Twelve-Factor App** methodology by using environment variables for dynamic configuration. Please ensure the following variables are defined in your environment:

* **`SERVER_PORT`**: The port on which the service listens.
    * *Example:* `8080`
* **`APP_JWTSECRET`**: High-entropy secret key used for JWT signing and validation.
    * *Example:* `f8D9!sQ2ZK7v@R3M#LwA6XcP0nH$eJYB4m%tU^F5a`
* **`SPRING_DATASOURCE_URL`**: The connection endpoint for the PostgreSQL database.
    * *Example:* `jdbc:postgresql://localhost:5432/postgres`
* **`SPRING_DATASOURCE_USERNAME`**: The administrative username for database access.
    * *Example:* `skd_financiera`
* **`SPRING_DATASOURCE_PASSWORD`**: The administrative password for database access.
    * *Example:* `F1nanci3r4P0stgres`

---

## API Documentation (Swagger UI)

The project includes an interactive **Swagger UI** for integration testing and technical review.

### Accessing the Interface
URL: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Authentication Workflow
Due to security policies, all transaction endpoints are protected. To test via Swagger:

1.  **Generate Credentials:** Navigate to the `auth-controller`, use the `/api/v1/auth/login` endpoint, and copy the `accessToken` from the response.
2.  **Inject Security Context:**
    * Click the **"Authorize"** button (lock icon) at the top right of the Swagger page.
    * Paste the token into the value field.
    * Click **Authorize** and then **Close**.
3.  **Consume API:** All secured methods will now be enabled for execution.

---

## Technology Stack

* **Language:** Java 17 (LTS)
* **Framework:** Spring Boot 3.4.1
* **Persistence:** Spring Data JPA + PostgreSQL
* **Security:** Spring Security + JWT
* **Logging:** SLF4J / Logback
* **Documentation:** SpringDoc OpenAPI 3

---

## Local Execution

1. Ensure a **PostgreSQL** instance is running and the database is created.
2. Configure the environment variables in your system or IDE.
3. Build and run using Maven:

```bash
mvn clean install
mvn spring-boot:run
```

---

## Docker Deployment

This microservice is containerized using a **multi-stage build** to ensure a lightweight, secure, and production-ready image.

### 1. Build the Image
From the project root, run the following command to build the Docker image:

```bash
docker build -t financiera-app .

docker run -p 8080:8080 \
  -e SERVER_PORT=8080 \
  -e APP_JWTSECRET=f8D9!sQ2ZK7v@R3M#LwA6XcP0nH$eJYB4m%tU^F5a \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/postgres \
  -e SPRING_DATASOURCE_USERNAME=skd_financiera \
  -e SPRING_DATASOURCE_PASSWORD=F1nanci3r4P0stgres \
  financiera-app
```

---

## Testing Strategy

The microservice features a comprehensive automated testing suite designed to ensure resilience across all architectural layers. We utilize **JUnit 5** as the core engine and **Mockito** for component isolation.

### Unit Tests (Business Logic)
Focused on the `Service` layer to validate core financial rules and calculations.

* **Isolation:** Leveraging `@ExtendWith(MockitoExtension.class)` to mock repositories and external dependencies.
* **Scenario Coverage:** Tests include success flows and edge cases such as insufficient funds, account not found, and data integrity.

### Integration Tests (Web Layer)
Focused on API contract validation using `@WebMvcTest`.

* **HTTP Simulation:** Usage of `MockMvc` to verify REST status codes (`201 Created`, `404 Not Found`, `400 Bad Request`).
* **Security Integration:** Ensuring that security filters correctly intercept unauthorized requests and validate JWT structures.

### Running the Suite
To execute all automated tests and generate a summary report, run:
```bash
mvn test

---

## Test Execution Configuration

To run the test suite correctly in modern environments (JDK 17+), certain JVM arguments are required for **Mockito** to perform bytecode manipulation and dynamic agent loading.

### Required JVM Arguments
If you are running tests from an IDE (IntelliJ/Eclipse) or manual CLI, ensure the following arguments are included:

```bash
-ea
-javaagent:${M2_REPO}/org/mockito/mockito-core/5.14.2/mockito-core-5.14.2.jar
-XX:+EnableDynamicAgentLoading
--add-opens java.base/java.lang=ALL-UNNAMED
```

---

## Project Structure

The codebase follows a standard multi-tier architecture to ensure maintainability, testability, and scalability:

```bash
.
├── src/
│   ├── main/java/com/financiera/
│   │   ├── config/         # Global configurations (OpenAPI, Security, Beans).
│   │   ├── controller/     # Entry layer (REST Controllers) with V1 versioning.
│   │   ├── dto/            # Data Transfer Objects (Request/Response schemas).
│   │   ├── exception/      # Global Exception Handling and custom error models.
│   │   ├── model/          # Persistence layer (JPA/Hibernate Entities).
│   │   ├── repository/     # Data Access Objects (Spring Data JPA).
│   │   ├── security/       # Security components (JWT Filters, Providers).
│   │   ├── service/        # Business logic interfaces and orchestration.
│   │   │   └── impl/       # Concrete service implementations.
│   │   └── utils/          # Common utility classes and constants.
│   └── test/java/com/financiera/
│       ├── controller/     # Integration tests for REST endpoints (MockMvc).
│       ├── service/        # Unit tests for business logic (Mockito).
│       └── test/           # Test context configuration and application anchors.
├── Dockerfile              # Multi-stage Docker build configuration.
├── pom.xml                 # Maven dependencies and build configuration.
└── README.md               # Project documentation and setup guide.
```