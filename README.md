# Patient Management Backend

A Spring Boot application for managing patient information and appointments with PostgreSQL database.

## Technologies

- Java 17
- Spring Boot 3.1.5
- PostgreSQL 15
- Gradle
- Docker
- Testcontainers

## Prerequisites

- **Java 17 or higher** (Install with SDKMAN - see below)
- Docker and Docker Compose
- Gradle (or use included gradlew)

### Installing Java with SDKMAN (Recommended)

```bash
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install Java 17
sdk install java 17.0.9-tem

# Verify
java -version
```

For detailed instructions, see [SDKMAN_JAVA_SETUP.md](SDKMAN_JAVA_SETUP.md)

## Setup

### 1. Start PostgreSQL Database

Using Docker Compose:
```bash
docker-compose up -d
```

### 2. Build the Application

```bash
./gradlew clean build
```

The Gradle wrapper will automatically download if needed on first run.

### 3. Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## API Documentation (Swagger)

Once the application is running, access the interactive API documentation:

**Swagger UI**: http://localhost:8080/swagger-ui.html

**OpenAPI Spec**: http://localhost:8080/v3/api-docs

## API Endpoints

### Patients

- `POST /api/patients` - Create a new patient
- `GET /api/patients` - Get all patients
- `GET /api/patients/{id}` - Get patient by ID
- `PUT /api/patients/{id}` - Update patient
- `DELETE /api/patients/{id}` - Delete patient

### Appointments

- `POST /api/appointments` - Create a new appointment
- `GET /api/appointments` - Get all appointments
- `GET /api/appointments?patientId={id}` - Get appointments by patient ID
- `GET /api/appointments/{id}` - Get appointment by ID
- `PATCH /api/appointments/{id}/status?status={STATUS}` - Update appointment status
- `DELETE /api/appointments/{id}` - Delete appointment

### Example Requests

Create a patient:
```json
POST /api/patients
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "555-1234",
  "dateOfBirth": "1990-05-15"
}
```

Create an appointment:
```json
POST /api/appointments
{
  "patientId": 1,
  "appointmentDateTime": "2024-12-15T10:00:00",
  "reason": "Regular checkup"
}
```

Update appointment status:
```
PATCH /api/appointments/1/status?status=COMPLETED
```

## Running Tests

**⚠️ IMPORTANT: Docker must be running before running tests!**

Check Docker is running:
```bash
docker ps
```

Run all tests:
```bash
./gradlew clean test
```

Or use the helper script (checks Docker first):
```bash
chmod +x run-tests.sh
./run-tests.sh
```

Tests use Testcontainers to spin up a real PostgreSQL instance, ensuring integration tests run against an actual database (not mocked).

### If tests fail with connection errors:
1. **Start Docker Desktop** - This is required!
2. Verify Docker is accessible: `docker ps`
3. Check Docker can pull images: `docker pull postgres:15-alpine`
4. Run with more info: `./gradlew test --info`

### If tests fail with "SQLGrammarException":
This usually means the container didn't start properly. Try:
```bash
./gradlew clean test --info
```

## Database Configuration

Default configuration (application.properties):
- URL: `jdbc:postgresql://localhost:5432/patientdb`
- Username: `admin`
- Password: `admin123`

## Project Structure

```
src/
├── main/
│   ├── java/com/healthcare/patient/
│   │   ├── Main.java
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   └── dto/
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/healthcare/patient/
    │   ├── BaseIntegrationTest.java
    │   ├── controller/
    │   ├── service/
    │   └── repository/
    └── resources/
        └── application.properties
```
