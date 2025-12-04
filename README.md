# Petstore API Test Automation Framework

[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/Gradle-8.5-blue.svg)](https://gradle.org/)
[![TestNG](https://img.shields.io/badge/TestNG-7.8.0-red.svg)](https://testng.org/)
[![Allure](https://img.shields.io/badge/Allure-2.25.0-yellow.svg)](https://docs.qameta.io/allure/)

## Overview

This is a comprehensive Test Automation Framework for the **Swagger Petstore API**. The framework is built using Java, Gradle, TestNG, Retrofit, and Allure Reports, following industry best practices and design patterns.

## API Under Test

- **Base URL**: `http://petstore.swagger.io/v1`
- **API Documentation**: [Swagger Petstore](https://petstore.swagger.io/)

### Endpoints Covered

1. **GET /pets** - List all pets with optional limit parameter
2. **POST /pets** - Create a new pet
3. **GET /pets/{petId}** - Get a specific pet by ID

## Framework Architecture

### Design Patterns

- **Service Object Pattern**: API endpoints are abstracted into service classes
- **Builder Pattern**: Test data creation using builders
- **Singleton Pattern**: Configuration management
- **Factory Pattern**: Retrofit client creation

### Project Structure

```
petstore-api-tests/
├── gradle/                          # Gradle wrapper files
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/klindziuk/petstore/
│   │   │       ├── client/          # Retrofit API clients
│   │   │       ├── config/          # Configuration management
│   │   │       ├── model/           # POJO models (Pet, Error)
│   │   │       ├── service/         # API service layer
│   │   │       └── util/            # Utilities (TestDataBuilder)
│   │   └── resources/
│   │       └── config/              # Configuration files
│   └── test/
│       ├── java/
│       │   └── com/klindziuk/petstore/
│       │       ├── base/            # Base test class
│       │       └── tests/           # Test classes
│       └── resources/
│           ├── testng.xml           # TestNG suite configuration
│           └── allure.properties    # Allure configuration
├── build.gradle                     # Gradle build configuration
├── gradle.properties                # Gradle properties
├── settings.gradle                  # Gradle settings
└── README.md                        # This file
```

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11 | Programming language |
| Gradle | 8.5 | Build automation |
| TestNG | 7.8.0 | Testing framework |
| Retrofit | 2.9.0 | HTTP client |
| OkHttp | 4.12.0 | HTTP client implementation |
| Lombok | 1.18.30 | Reduce boilerplate code |
| AssertJ | 3.24.2 | Fluent assertions |
| Allure | 2.25.0 | Test reporting |
| Log4j2 | 2.22.0 | Logging framework |
| Gson | 2.10.1 | JSON serialization |

## Prerequisites

- **Java 11** or higher
- **Gradle 8.5** or higher (or use the included Gradle wrapper)
- **Git** for version control

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/klindziukp/staf-ai.git
cd staf-ai
git checkout feature/petstore-api-tests
```

### 2. Build the Project

```bash
./gradlew clean build
```

### 3. Run Tests

#### Run All Tests

```bash
./gradlew test
```

#### Run Specific Test Class

```bash
./gradlew test --tests "com.klindziuk.petstore.tests.GetPetsTest"
```

#### Run Tests with Specific Environment

```bash
./gradlew test -Denvironment=QA
```

### 4. Generate Allure Report

```bash
./gradlew allureReport
./gradlew allureServe
```

## Test Coverage

### Test Cases Summary

| Test ID | Description | Priority | Status |
|---------|-------------|----------|--------|
| GET-001 | List all pets without limit | Critical | ✅ |
| GET-002 | List pets with valid limit | Critical | ✅ |
| GET-003 | List pets with maximum limit (100) | Normal | ✅ |
| GET-004 | List pets with limit=0 | Normal | ✅ |
| GET-005 | List pets with limit=1 | Normal | ✅ |
| GET-006 | List pets with limit>100 | Normal | ✅ |
| GET-007 | List pets with negative limit | Normal | ✅ |
| GET-011 | Validate pets response schema | Critical | ✅ |
| POST-001 | Create pet with required fields | Critical | ✅ |
| POST-002 | Create pet with all fields | Critical | ✅ |
| POST-003 | Create pet without ID | Normal | ✅ |
| POST-004 | Create pet without name | Normal | ✅ |
| POST-009 | Create pet with ID=0 | Normal | ✅ |
| POST-010 | Create pet with max integer ID | Normal | ✅ |
| POST-011 | Create pet with empty name | Normal | ✅ |
| POST-014 | Validate create pet response schema | Critical | ✅ |
| GETID-001 | Get existing pet by valid ID | Critical | ✅ |
| GETID-002 | Get pet with non-existent ID | Normal | ✅ |
| GETID-005 | Get pet with negative ID | Normal | ✅ |
| GETID-006 | Get pet with ID=0 | Normal | ✅ |
| GETID-007 | Get pet with minimum valid ID | Normal | ✅ |
| GETID-008 | Get pet with maximum integer ID | Normal | ✅ |
| GETID-010 | Validate get pet response schema | Critical | ✅ |

### Test Categories

- **Positive Scenarios**: Happy path testing with valid inputs
- **Negative Scenarios**: Error handling with invalid inputs
- **Boundary Value Testing**: Edge cases and limits
- **Schema Validation**: Response structure validation

## Configuration

### Environment Configuration

Edit `src/main/resources/config/application.properties`:

```properties
# Environment: DEV, QA, PROD
environment=DEV

# API Configuration
api.timeout=30
api.max.retries=3
api.logging.enabled=true

# Test Configuration
test.parallel.threads=5
test.retry.count=1
```

### TestNG Configuration

Edit `src/test/resources/testng.xml` to customize test execution:

```xml
<suite name="Petstore API Test Suite" parallel="classes" thread-count="3">
    <test name="Petstore API Tests">
        <classes>
            <class name="com.klindziuk.petstore.tests.GetPetsTest"/>
            <class name="com.klindziuk.petstore.tests.CreatePetTest"/>
            <class name="com.klindziuk.petstore.tests.GetPetByIdTest"/>
        </classes>
    </test>
</suite>
```

## Key Features

### 1. **Comprehensive Test Coverage**
- Positive, negative, and boundary value test scenarios
- Schema validation for all responses
- Error handling verification

### 2. **Robust Framework Design**
- Service Object Pattern for API abstraction
- Centralized configuration management
- Reusable test data builders
- Comprehensive logging

### 3. **Advanced Reporting**
- Allure Reports with detailed test execution information
- Step-by-step test execution tracking
- Attachments for debugging
- Historical trends

### 4. **Best Practices**
- Java naming conventions
- SOLID principles
- Clean code practices
- Comprehensive documentation

### 5. **CI/CD Ready**
- Gradle wrapper included
- Parallel test execution support
- Environment-based configuration
- Easy integration with CI/CD pipelines

## Usage Examples

### Creating Test Data

```java
// Create random pet
Pet pet = TestDataBuilder.createRandomPet();

// Create pet with specific name
Pet pet = TestDataBuilder.createPetWithName("Buddy");

// Create pet with specific ID
Pet pet = TestDataBuilder.createPetWithId(123L);

// Create pet without optional tag
Pet pet = TestDataBuilder.createPetWithoutTag();
```

### Making API Calls

```java
// Get all pets
Response<List<Pet>> response = petService.getAllPets();

// Get pets with limit
Response<List<Pet>> response = petService.getPets(10);

// Create pet
Response<Pet> response = petService.createPet(pet);

// Get pet by ID
Response<Pet> response = petService.getPetById(1L);
```

### Assertions

```java
assertThat(response.isSuccessful())
    .as("Response should be successful")
    .isTrue();

assertThat(response.code())
    .as("Status code should be 200")
    .isEqualTo(200);

assertThat(pets)
    .as("Pets list should not be empty")
    .isNotEmpty();
```

## Continuous Integration

### GitHub Actions (Example)

```yaml
name: API Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      - name: Run tests
        run: ./gradlew test
      - name: Generate Allure Report
        run: ./gradlew allureReport
```

## Troubleshooting

### Common Issues

1. **Gradle build fails**
   - Ensure Java 11+ is installed
   - Run `./gradlew clean build --refresh-dependencies`

2. **Tests fail to connect to API**
   - Verify the base URL in configuration
   - Check network connectivity
   - Ensure API is accessible

3. **Allure report not generating**
   - Run `./gradlew cleanAllure allureReport`
   - Check build/allure-results directory

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please open an issue in the GitHub repository.

## Acknowledgments

- Swagger Petstore API for providing the test API
- TestNG, Retrofit, and Allure communities for excellent tools
- Contributors and maintainers of this framework
