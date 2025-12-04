# Petstore API Test Automation Framework

## Overview

This is a comprehensive Test Automation Framework for the Swagger Petstore API, built using Java, Gradle, TestNG, Retrofit, and Allure Reports.

## Features

- ✅ **Modern Tech Stack**: Java 11+, Gradle, TestNG, Retrofit 2
- ✅ **Comprehensive Test Coverage**: Positive, negative, edge cases, and security tests
- ✅ **Allure Reporting**: Beautiful and detailed test reports
- ✅ **Parallel Execution**: Run tests in parallel for faster execution
- ✅ **CI/CD Ready**: GitHub Actions workflow included
- ✅ **Retry Mechanism**: Automatic retry for flaky tests
- ✅ **Logging**: Detailed logging with Log4j2
- ✅ **Data-Driven Testing**: Support for parameterized tests
- ✅ **Clean Architecture**: Layered structure with models, services, and utilities

## Project Structure

```
petstore-api-automation/
├── .github/
│   └── workflows/
│       └── test-execution.yml          # GitHub Actions CI/CD workflow
├── src/
│   ├── main/
│   │   ├── java/com/petstore/
│   │   │   ├── client/                 # API client interfaces
│   │   │   ├── config/                 # Configuration management
│   │   │   ├── models/                 # Data models (Pet, Error)
│   │   │   ├── services/               # Service layer for API operations
│   │   │   └── utils/                  # Utility classes
│   │   └── resources/
│   │       ├── config.properties       # Application configuration
│   │       └── log4j2.xml             # Logging configuration
│   └── test/
│       ├── java/com/petstore/
│       │   ├── base/                   # Base test class
│       │   ├── listeners/              # TestNG listeners
│       │   └── tests/                  # Test classes
│       └── resources/
│           ├── testng.xml              # Full regression suite
│           └── testng-smoke.xml        # Smoke test suite
├── build.gradle                        # Gradle build configuration
├── settings.gradle                     # Gradle settings
└── README.md                          # This file
```

## Prerequisites

- Java 11 or higher
- Gradle 7.x or higher (or use included Gradle wrapper)
- Internet connection (for API calls)

## Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd petstore-api-automation
```

2. Verify Java installation:
```bash
java -version
```

3. Build the project:
```bash
./gradlew clean build
```

## Running Tests

### Run All Tests
```bash
./gradlew test
```

### Run Smoke Tests
```bash
./gradlew smokeTests
```

### Run Regression Tests
```bash
./gradlew regressionTests
```

### Run Specific Test Class
```bash
./gradlew test --tests "com.petstore.tests.GetPetsTests"
```

### Run Specific Test Method
```bash
./gradlew test --tests "com.petstore.tests.GetPetsTests.testGetAllPetsWithoutLimit"
```

## Test Suites

### Smoke Test Suite
Quick validation of critical functionality:
- Get all pets
- Get pets with limit
- Create pet with required fields
- Create pet with all fields
- Get pet by valid ID

### Regression Test Suite
Comprehensive test coverage including:
- **GET /pets**: List all pets with various limit parameters
- **POST /pets**: Create pets with different data combinations
- **GET /pets/{petId}**: Retrieve pets by ID with various scenarios

## Test Coverage

### GET /pets
- ✅ Get all pets without limit
- ✅ Get pets with valid limit (1, 10, 100)
- ✅ Get pets with invalid limit (negative, exceeding max)
- ✅ Validate required fields in response
- ✅ Validate response schema

### POST /pets
- ✅ Create pet with all fields
- ✅ Create pet with required fields only
- ✅ Create pet without optional fields
- ✅ Create pet with special characters
- ✅ Create pet with very long name
- ✅ Error handling for missing required fields
- ✅ SQL injection protection
- ✅ XSS protection

### GET /pets/{petId}
- ✅ Get pet by valid ID
- ✅ Get pet by non-existent ID
- ✅ Get pet by invalid ID format
- ✅ Get pet by negative ID
- ✅ SQL injection protection in ID parameter
- ✅ XSS protection in ID parameter

## Generating Reports

### Allure Report

1. Run tests:
```bash
./gradlew test
```

2. Generate Allure report:
```bash
./gradlew allureReport
```

3. Open report:
```bash
./gradlew allureServe
```

The report will open automatically in your default browser.

## Configuration

### API Configuration
Edit `src/main/resources/config.properties`:

```properties
# API Base URL
api.base.url=http://petstore.swagger.io/v1/

# Connection Timeouts (milliseconds)
api.connection.timeout=30000
api.read.timeout=30000
api.write.timeout=30000

# Logging
api.logging.enabled=true

# Test Configuration
test.retry.max.attempts=2
```

### Logging Configuration
Edit `src/main/resources/log4j2.xml` to customize logging levels and appenders.

## CI/CD Integration

### GitHub Actions

The project includes a GitHub Actions workflow (`.github/workflows/test-execution.yml`) that:
- Runs on push, pull request, and scheduled triggers
- Executes tests on multiple Java versions (11, 17)
- Generates and uploads Allure reports
- Publishes test results

### Manual Workflow Trigger

You can manually trigger the workflow from GitHub Actions tab and choose:
- **smoke**: Run smoke tests only
- **regression**: Run regression tests
- **all**: Run all tests

## Best Practices

1. **Test Independence**: Each test is independent and can run in any order
2. **Clean Code**: Follow Java naming conventions and SOLID principles
3. **Logging**: Comprehensive logging for debugging
4. **Assertions**: Use AssertJ for fluent assertions
5. **Allure Steps**: Annotate methods with `@Step` for better reporting
6. **Error Handling**: Proper exception handling and error messages

## Troubleshooting

### Tests Failing Due to API Unavailability
- Check if the Petstore API is accessible: `http://petstore.swagger.io/v1/`
- Verify network connectivity
- Check firewall settings

### Build Failures
- Ensure Java 11+ is installed
- Clear Gradle cache: `./gradlew clean`
- Delete `.gradle` folder and rebuild

### Allure Report Not Generating
- Ensure tests have run at least once
- Check if `build/allure-results` directory exists
- Verify Allure plugin is properly configured in `build.gradle`

## Contributing

1. Create a feature branch
2. Make your changes
3. Run tests to ensure they pass
4. Submit a pull request

## Test Case Documentation

Detailed test cases are available in the test documentation. Each test includes:
- Test ID and description
- Preconditions
- Test steps
- Expected results
- Severity level
- Allure annotations

## Contact

For questions or issues, please create an issue in the repository.

## License

This project is licensed under the MIT License.
