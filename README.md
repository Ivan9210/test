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
    * *Example:* `Sk1cedF1nanci3r4P0stgres`

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

## Project Structure

The codebase follows a standard multi-tier architecture to ensure maintainability, testability, and scalability:

```text
src/main/java/com/financiera/
├── config/             # Global configurations (OpenAPI, Security, Beans).
├── controller/         # Entry layer (REST Controllers) with V1 versioning.
├── dto/                # Data Transfer Objects (Request/Response schemas).
├── exception/          # Global Exception Handling and custom error models.
├── model/              # Persistence layer (JPA/Hibernate Entities).
├── repository/         # Data Access Objects (Spring Data JPA).
├── security/           # Security components (JWT Filters, Providers, Auth Managers).
├── service/            # Business logic interfaces and orchestration.
│   └── impl/           # Concrete service implementations.
└── utils/              # Common utility classes and constants.