# USPTO Data Set API - Test Automation Framework

A comprehensive test automation framework for the USPTO Data Set API, built using STAF (Simple Test Automation Framework) and Gradle.

## Overview

This framework provides automated testing for the USPTO Data Set API, which allows public users to discover and search USPTO exported data sets. The framework tests both metadata operations and search functionality.

## Features

- **Gradle Build System**: Modern build configuration with Kotlin DSL
- **TestNG Framework**: Robust test execution and reporting
- **Apache HTTP Client 5**: Latest HTTP client for API testing
- **Lombok**: Reduced boilerplate code in model classes
- **AssertJ**: Fluent assertions for better test readability
- **Allure Reporting**: Comprehensive test reporting
- **Custom Listeners**: Test execution tracking and logging
- **CI/CD Integration**: GitHub Actions workflow for automated testing

## Project Structure

```
staf-ai/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/klindziuk/staf/
│   │           ├── constant/
│   │           │   └── RequestPath.java
│   │           └── model/
│   │               ├── ApiInfo.java
│   │               ├── DataSetList.java
│   │               ├── SearchRequest.java
│   │               └── SearchResponse.java
│   └── test/
│       ├── java/
│       │   └── com/klindziuk/staf/
│       │       ├── listener/
│       │       │   ├── TestMethodCaptureListener.java
│       │       │   └── TestSuiteListener.java
│       │       ├── service/
│       │       │   └── ResponseVerificationService.java
│       │       └── test/
│       │           ├── BaseApiTest.java
│       │           ├── UsptoDataSetApiTest.java
│       │           └── UsptoSearchApiTest.java
│       └── resources/
│           ├── testng.xml
│           └── logback-test.xml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Prerequisites

- Java 17 or higher
- Gradle 7.x or higher (wrapper included)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/klindziukp/staf-ai.git
cd staf-ai
```

### Build the Project

```bash
./gradlew clean build
```

### Run Tests

Run all tests:
```bash
./gradlew test
```

Run specific test suite:
```bash
./gradlew apiTest
```

## Test Coverage

### Metadata Operations
- List all available data sets
- Get searchable fields for datasets
- Error handling for non-existent datasets
- Version validation

### Search Operations
- Search with default criteria
- Pagination support
- Custom search criteria
- Large result sets
- Offset handling
- Multiple dataset support

## API Endpoints Tested

1. **GET /** - List available data sets
2. **GET /{dataset}/{version}/fields** - Get searchable fields
3. **POST /{dataset}/{version}/records** - Search records

## Configuration

### TestNG Configuration
Test execution is configured in `src/test/resources/testng.xml`:
- Test suite organization
- Listener configuration
- Test execution order

### Logging Configuration
Logging is configured in `src/test/resources/logback-test.xml`:
- Console output
- File logging
- Log levels per package

## CI/CD

The framework includes a GitHub Actions workflow (`.github/workflows/api-tests.yml`) that:
- Triggers on pull requests
- Runs all API tests
- Generates test reports
- Uploads test artifacts
- Publishes test summaries

## Dependencies

- **STAF Core**: Test automation framework
- **Apache HTTP Client 5**: HTTP client library
- **TestNG**: Testing framework
- **Lombok**: Code generation
- **Gson & Jackson**: JSON processing
- **AssertJ**: Assertion library
- **Allure**: Test reporting
- **Logback**: Logging framework

## Best Practices

1. **Model Classes**: Use Lombok annotations for cleaner code
2. **Constants**: Centralized in `RequestPath` class
3. **Verification**: Dedicated `ResponseVerificationService` for assertions
4. **Logging**: Comprehensive logging at all levels
5. **Listeners**: Custom listeners for test lifecycle tracking
6. **Base Test**: Common functionality in `BaseApiTest`

## Contributing

1. Create a feature branch
2. Make your changes
3. Run tests locally
4. Submit a pull request

## License

This project is licensed under the MIT License.

## Contact

For questions or issues, please open an issue on GitHub.
