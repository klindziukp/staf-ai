# Petstore API Test Automation Framework

## Overview
This is a comprehensive Test Automation Framework for the Swagger Petstore API, built using Java, REST Assured, and TestNG. The framework follows industry best practices and implements a modular, maintainable architecture.

## Technology Stack
- **Language**: Java 11
- **Build Tool**: Gradle 8.x
- **Testing Framework**: TestNG 7.11.0
- **API Testing**: REST Assured 5.5.0
- **Reporting**: Allure 2.29.1
- **Logging**: SLF4J + Log4j2
- **Assertions**: AssertJ 3.27.3
- **JSON Processing**: Jackson 2.18.2
- **Code Generation**: Lombok 1.18.36

## Project Structure
```
staf-ai/
├── src/
│   ├── main/
│   │   ├── java/com/staf/ai/
│   │   │   ├── client/          # API client classes
│   │   │   ├── config/          # Configuration management
│   │   │   ├── model/           # POJO models
│   │   │   └── utils/           # Utility classes
│   │   └── resources/
│   │       └── log4j2.xml       # Logging configuration
│   └── test/
│       ├── java/com/staf/ai/
│       │   ├── base/            # Base test class
│       │   └── tests/           # Test classes
│       └── resources/
│           ├── config.properties # Test configuration
│           ├── testng.xml       # TestNG suite configuration
│           └── allure.properties # Allure configuration
├── build.gradle                 # Gradle build configuration
├── settings.gradle              # Gradle settings
├── gradle.properties            # Gradle properties
└── README.md                    # This file
```

## API Endpoints Covered
1. **GET /pets** - List all pets (with optional limit parameter)
2. **POST /pets** - Create a new pet
3. **GET /pets/{petId}** - Get a specific pet by ID

## Test Coverage
The framework includes **37 comprehensive test cases** covering:
- ✅ Positive scenarios (happy path)
- ✅ Negative scenarios (invalid inputs, missing fields)
- ✅ Boundary value testing
- ✅ Data validation
- ✅ Schema validation
- ✅ Error handling
- ✅ Integration tests
- ✅ Performance tests

### Test Categories
- **Smoke Tests**: Critical functionality tests
- **Regression Tests**: Comprehensive test suite
- **Negative Tests**: Error handling and validation
- **Boundary Tests**: Edge case testing
- **Integration Tests**: End-to-end workflows

## Prerequisites
- Java 11 or higher
- Gradle 8.x (or use included Gradle wrapper)
- Internet connection (for downloading dependencies)

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd staf-ai
```

### 2. Verify Java Installation
```bash
java -version
```

### 3. Build the Project
```bash
./gradlew clean build
```

## Running Tests

### Run All Tests
```bash
./gradlew test
```

### Run Smoke Tests Only
```bash
./gradlew smokeTests
```

### Run Regression Tests Only
```bash
./gradlew regressionTests
```

### Run Specific Test Class
```bash
./gradlew test --tests "com.staf.ai.tests.PetsApiTest"
```

### Run Tests with Specific Groups
```bash
./gradlew test -Dgroups="smoke"
./gradlew test -Dgroups="regression"
./gradlew test -Dgroups="negative"
```

## Configuration

### API Configuration
Edit `src/test/resources/config.properties` to modify API settings:
```properties
api.base.url=http://petstore.swagger.io
api.base.path=/v1
api.connection.timeout=10000
api.socket.timeout=10000
api.logging.enabled=true
api.log.level=INFO
```

### Override Configuration at Runtime
```bash
./gradlew test -Dapi.base.url=http://custom-url.com
```

## Reporting

### Generate Allure Report
```bash
# Run tests
./gradlew test

# Generate and open Allure report
./gradlew allureServe
```

### View Test Results
- **TestNG HTML Report**: `build/reports/tests/test/index.html`
- **Allure Results**: `build/allure-results/`
- **Logs**: `logs/api-tests.log`

## Framework Features

### 1. Modular Architecture
- Separation of concerns (client, model, config, tests)
- Reusable components
- Easy to extend and maintain

### 2. Configuration Management
- Centralized configuration
- Environment-specific settings
- Runtime property override

### 3. Comprehensive Logging
- Structured logging with Log4j2
- Request/Response logging
- Rolling file appender

### 4. Allure Reporting
- Rich test reports with screenshots
- Test execution history
- Categorization and tagging

### 5. Data-Driven Testing Support
- TestNG data providers
- External test data support
- Parameterized tests

### 6. Assertions
- AssertJ for fluent assertions
- REST Assured matchers
- JSON schema validation

### 7. Best Practices
- Page Object Model (POM) pattern for API clients
- Builder pattern for request construction
- Singleton pattern for configuration
- SOLID principles
- Clean code practices

## Test Cases Summary

### GET /pets Tests (12 test cases)
- List all pets with/without limit
- Boundary value testing (limit 1, 100, 101)
- Negative testing (negative limit, invalid values)
- Schema validation
- Performance testing

### POST /pets Tests (12 test cases)
- Create pet with required/all fields
- Missing required fields validation
- Invalid data type testing
- Null value handling
- Boundary value testing
- Schema validation

### GET /pets/{petId} Tests (10 test cases)
- Get pet by valid/invalid ID
- Non-existent pet handling
- Invalid ID format testing
- Boundary value testing
- Integration testing (create then retrieve)
- Error schema validation

### Integration Tests (3 test cases)
- Create and retrieve workflow
- Multiple pets creation and listing
- Empty state handling

## CI/CD Integration

### GitHub Actions Example
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

**Issue**: Tests fail with connection timeout
```bash
# Solution: Increase timeout in config.properties
api.connection.timeout=30000
api.socket.timeout=30000
```

**Issue**: Gradle build fails
```bash
# Solution: Clean and rebuild
./gradlew clean build --refresh-dependencies
```

**Issue**: Allure report not generating
```bash
# Solution: Install Allure CLI
brew install allure  # macOS
# or download from https://github.com/allure-framework/allure2/releases
```

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## Best Practices for Adding New Tests
1. Extend `BaseTest` class
2. Use appropriate Allure annotations (@Epic, @Feature, @Story)
3. Add test to appropriate groups (smoke, regression, etc.)
4. Follow naming conventions (testMethodName_ExpectedBehavior)
5. Add comprehensive assertions
6. Include proper logging
7. Update documentation

## Maintenance
- Regularly update dependencies
- Review and refactor tests
- Monitor test execution time
- Keep documentation up-to-date

## Contact
For questions or issues, please contact the QA team or create an issue in the repository.

## License
MIT License

---
**Version**: 1.0.0  
**Last Updated**: 2024  
**Maintained by**: STAF AI Team
