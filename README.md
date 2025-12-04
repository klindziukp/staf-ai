# USPTO Data Set API - Test Automation Framework

## Overview
This is a comprehensive test automation framework for the USPTO Data Set API, built using Java, REST Assured, TestNG, and Allure Reports.

## API Under Test
- **API Name**: USPTO Data Set API
- **Version**: 1.0.0
- **Base URL**: https://developer.uspto.gov/ds-api
- **Documentation**: https://developer.uspto.gov

## Framework Features
- ✅ REST API testing with REST Assured
- ✅ TestNG for test execution and management
- ✅ Allure Reports for comprehensive test reporting
- ✅ Page Object Model pattern adapted for APIs (Service Object Pattern)
- ✅ Environment-specific configuration management
- ✅ Comprehensive logging with Log4j2
- ✅ JSON schema validation
- ✅ Parallel test execution support
- ✅ CI/CD ready (GitHub Actions)
- ✅ Gradle build automation

## Prerequisites
- Java 11 or higher
- Gradle 8.11.1 (wrapper included)
- Allure CLI (for local report generation)

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd uspto-api-test-framework
```

### 2. Build the Project
```bash
./gradlew clean build
```

### 3. Run Tests
```bash
# Run all tests (default environment: dev)
./gradlew clean test

# Run tests for specific environment
./gradlew clean test -Denv=staging
```

## Test Coverage
- ✅ GET / - List available datasets
- ✅ GET /{dataset}/{version}/fields - List searchable fields
- ✅ POST /{dataset}/{version}/records - Search dataset records

## Test Execution

### Run All Tests
```bash
./gradlew clean test
```

### Run Specific Test Suites
```bash
# Smoke tests
./gradlew clean smokeTest

# Regression tests
./gradlew clean regressionTest

# Negative tests
./gradlew clean negativeTest
```

### Run Tests for Specific Environment
```bash
./gradlew clean test -Denv=dev
./gradlew clean test -Denv=staging
./gradlew clean test -Denv=prod
```

### Run Tests in Parallel
```bash
./gradlew clean test -Dparallel=true -DthreadCount=5
```

## Generate Allure Reports

### Generate and Open Report
```bash
./gradlew allureReport
./gradlew allureServe
```

### View Report
The report will automatically open in your default browser at `http://localhost:port`

## Project Structure
```
uspto-api-automation/
├── src/
│   ├── main/java/gov/uspto/api/
│   │   ├── client/          # API client classes
│   │   ├── config/          # Configuration management
│   │   ├── models/          # Request/Response POJOs
│   │   └── services/        # Service layer for API calls
│   ├── test/java/gov/uspto/api/
│   │   ├── base/            # Base test class
│   │   ├── tests/           # Test classes
│   │   └── utils/           # Test utilities
│   └── resources/
│       ├── config/          # Environment configs
│       ├── testng.xml       # TestNG suite configuration
│       └── log4j2.xml       # Logging configuration
├── build.gradle             # Gradle build configuration
└── README.md               # This file
```

## Test Categories

### Smoke Tests (@smoke)
Critical tests that verify basic functionality:
- List datasets endpoint availability
- Get fields endpoint availability
- Search records endpoint availability

### Regression Tests (@regression)
Comprehensive tests covering all features:
- All positive scenarios
- Data validation
- Response structure validation
- Performance validation

### Negative Tests (@negative)
Tests for error handling:
- Invalid dataset names
- Invalid versions
- Invalid parameters
- Boundary value testing
- Security testing

## API Endpoints Tested

### 1. GET / - List Available Datasets
**Test Coverage:**
- ✅ Returns 200 OK
- ✅ Response structure validation
- ✅ Response time validation
- ✅ Dataset existence validation
- ✅ URL format validation

### 2. GET /{dataset}/{version}/fields - List Searchable Fields
**Test Coverage:**
- ✅ Returns 200 OK for valid dataset/version
- ✅ Returns 404 for invalid dataset
- ✅ Returns 404 for invalid version
- ✅ Response time validation
- ✅ Response not empty validation
- ✅ Special characters handling

### 3. POST /{dataset}/{version}/records - Search Records
**Test Coverage:**
- ✅ Returns 200 OK with default criteria
- ✅ Pagination support
- ✅ Different start positions
- ✅ Returns 404 for invalid dataset
- ✅ Boundary values for rows parameter
- ✅ Response time validation
- ✅ Lucene query syntax support
- ✅ Empty criteria handling
- ✅ Negative values handling
- ✅ Large values handling

## Configuration

### Environment Configuration
Edit `src/test/resources/config/{env}.properties`:
```properties
base.url=https://developer.uspto.gov/ds-api
connection.timeout=30000
read.timeout=30000
expected.response.time=5000
```

### TestNG Configuration
Edit `src/test/resources/testng.xml` to customize:
- Test suites
- Parallel execution
- Thread count
- Test groups

## CI/CD Integration

### GitHub Actions
The framework includes a GitHub Actions workflow (`.github/workflows/test-execution.yml`) that:
- Runs on push and pull requests
- Executes all tests
- Generates Allure reports
- Archives test results

### Jenkins Integration
```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh './gradlew clean test'
            }
        }
        stage('Report') {
            steps {
                allure includeProperties: false, results: [[path: 'build/allure-results']]
            }
        }
    }
}
```

## Best Practices Implemented

1. **Service Object Pattern**: API calls are abstracted in service classes
2. **Configuration Management**: Environment-specific configurations
3. **Comprehensive Logging**: Detailed logs with Log4j2
4. **Allure Reporting**: Rich test reports with request/response details
5. **Parallel Execution**: Tests can run in parallel for faster execution
6. **Data-Driven Testing**: TestNG DataProviders for parameterized tests
7. **Assertions**: Multiple assertion libraries (AssertJ, Hamcrest)
8. **Error Handling**: Proper exception handling and logging
9. **Code Quality**: Lombok for reducing boilerplate code
10. **Maintainability**: Clear project structure and naming conventions

## Troubleshooting

### Common Issues

**Issue**: Tests fail with connection timeout
**Solution**: Increase timeout in config properties:
```properties
connection.timeout=60000
read.timeout=60000
```

**Issue**: Allure report not generating
**Solution**: Ensure Allure CLI is installed:
```bash
# macOS
brew install allure

# Windows
scoop install allure

# Linux
sudo apt-add-repository ppa:qameta/allure
sudo apt-get update
sudo apt-get install allure
```

**Issue**: Tests fail in parallel execution
**Solution**: Reduce thread count or disable parallel execution:
```bash
./gradlew test -DthreadCount=1
```

## Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new features
5. Ensure all tests pass
6. Submit a pull request

## License
This project is licensed under the MIT License.

## Contact
For questions or issues, please contact: developer@uspto.gov

## Additional Resources
- [USPTO Developer Portal](https://developer.uspto.gov)
- [REST Assured Documentation](https://rest-assured.io/)
- [TestNG Documentation](https://testng.org/doc/)
- [Allure Documentation](https://docs.qameta.io/allure/)
