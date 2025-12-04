# Petstore API Test Automation Framework

A comprehensive, production-ready test automation framework for the Swagger Petstore API, built with Java, REST Assured, and TestNG.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Test Coverage](#test-coverage)
- [CI/CD Integration](#cicd-integration)
- [Contributing](#contributing)

## 🎯 Overview

This framework provides automated testing for the Swagger Petstore API (OpenAPI 3.0 specification), covering:
- **GET /pets** - List all pets with optional limit parameter
- **POST /pets** - Create a new pet
- **GET /pets/{petId}** - Retrieve a specific pet by ID

## ✨ Features

- ✅ **Comprehensive Test Coverage**: Positive, negative, boundary, and integration tests
- ✅ **REST Assured**: Industry-standard REST API testing library
- ✅ **TestNG Framework**: Powerful test orchestration and parallel execution
- ✅ **Allure Reporting**: Beautiful, detailed test reports with screenshots and logs
- ✅ **Lombok Integration**: Reduced boilerplate code
- ✅ **Configurable**: Externalized configuration via properties files
- ✅ **Logging**: Structured logging with Log4j2
- ✅ **Retry Mechanism**: Automatic retry for flaky tests
- ✅ **Parallel Execution**: Run tests in parallel for faster feedback
- ✅ **CI/CD Ready**: Easy integration with Jenkins, GitHub Actions, etc.

## 🛠 Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11+ | Programming Language |
| Gradle | 7.x+ | Build Tool |
| REST Assured | 5.3.2 | API Testing Library |
| TestNG | 7.8.0 | Testing Framework |
| Allure | 2.24.0 | Test Reporting |
| Jackson | 2.15.3 | JSON Processing |
| Lombok | 1.18.30 | Code Generation |
| Log4j2 | 2.21.1 | Logging Framework |
| AssertJ | 3.24.2 | Fluent Assertions |

## 📁 Project Structure

```
petstore-api-automation/
├── src/
│   ├── main/
│   │   ├── java/com/petstore/automation/
│   │   │   ├── client/          # API client classes
│   │   │   │   └── PetApiClient.java
│   │   │   ├── config/          # Configuration management
│   │   │   │   └── ConfigManager.java
│   │   │   ├── model/           # POJO models
│   │   │   │   ├── Pet.java
│   │   │   │   └── ApiError.java
│   │   │   └── util/            # Utility classes
│   │   │       ├── TestDataBuilder.java
│   │   │       └── ResponseValidator.java
│   │   └── resources/
│   │       ├── config.properties
│   │       └── log4j2.xml
│   └── test/
│       ├── java/com/petstore/automation/
│       │   ├── base/            # Base test classes
│       │   │   └── BaseTest.java
│       │   ├── listener/        # TestNG listeners
│       │   │   ├── TestSuiteListener.java
│       │   │   ├── TestMethodCaptureListener.java
│       │   │   └── RetryAnalyzer.java
│       │   └── tests/           # Test classes
│       │       ├── ListPetsTest.java
│       │       ├── CreatePetTest.java
│       │       ├── GetPetByIdTest.java
│       │       └── IntegrationTest.java
│       └── resources/
│           └── testng.xml
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

## 📦 Prerequisites

- **Java JDK 11 or higher**
- **Gradle 7.x or higher** (or use included Gradle wrapper)
- **Internet connection** (for downloading dependencies)

## 🚀 Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd petstore-api-automation
   ```

2. **Verify Java installation**:
   ```bash
   java -version
   ```

3. **Build the project**:
   ```bash
   ./gradlew clean build
   ```

## ⚙️ Configuration

Configuration is managed through `src/main/resources/config.properties`:

```properties
# API Configuration
api.base.url=http://petstore.swagger.io/v1
api.timeout.connect=30
api.timeout.read=30
api.timeout.write=30

# Test Configuration
test.retry.count=2
test.parallel.threads=3

# Logging
logging.level=INFO
logging.request.enabled=true
logging.response.enabled=true
```

### Override Configuration

You can override properties via system properties:

```bash
./gradlew test -Dapi.base.url=http://custom-url.com
```

## 🧪 Running Tests

### Run All Tests

```bash
./gradlew clean test
```

### Run Specific Test Class

```bash
./gradlew test --tests "com.petstore.automation.tests.ListPetsTest"
```

### Run Specific Test Method

```bash
./gradlew test --tests "com.petstore.automation.tests.ListPetsTest.testListAllPetsWithoutLimit"
```

### Run Tests with Tags

```bash
./gradlew test -Dgroups="smoke"
```

### Run Tests in Parallel

Tests are configured to run in parallel by default (3 threads). Modify `testng.xml` to adjust:

```xml
<suite name="Petstore API Test Suite" parallel="methods" thread-count="3">
```

## 📊 Test Reports

### Allure Reports

1. **Generate Allure report**:
   ```bash
   ./gradlew allureReport
   ```

2. **Open Allure report**:
   ```bash
   ./gradlew allureServe
   ```

The report will open automatically in your default browser.

### TestNG Reports

TestNG HTML reports are generated automatically in:
```
build/reports/tests/test/index.html
```

### Logs

Logs are generated in the `logs/` directory:
- `petstore-automation.log` - General application logs
- `api-calls.log` - API request/response logs

## 📈 Test Coverage

### Test Cases Summary

| Endpoint | Test Cases | Coverage |
|----------|------------|----------|
| GET /pets | 6 | Positive, Negative, Boundary |
| POST /pets | 7 | Positive, Negative, Boundary, Validation |
| GET /pets/{petId} | 6 | Positive, Negative, Boundary |
| Integration | 4 | End-to-End Workflows |
| **Total** | **23** | **Comprehensive** |

### Test Scenarios

#### GET /pets
- ✅ List all pets without limit
- ✅ List pets with valid limit
- ✅ Boundary testing (limit=0, limit=100)
- ✅ Negative testing (negative limit, excessive limit)
- ✅ Response schema validation

#### POST /pets
- ✅ Create pet with required fields
- ✅ Create pet with all fields
- ✅ Missing required fields (ID, name)
- ✅ Boundary testing (ID boundaries, empty name)
- ✅ Invalid data types

#### GET /pets/{petId}
- ✅ Get existing pet by valid ID
- ✅ Get non-existent pet
- ✅ Invalid ID formats (string, negative)
- ✅ Boundary testing (ID=0, max ID)

#### Integration Tests
- ✅ Create and retrieve pet workflow
- ✅ Create pet and verify in list
- ✅ Multiple pets creation and listing
- ✅ Data consistency validation

## 🔄 CI/CD Integration

### GitHub Actions

Create `.github/workflows/test.yml`:

```yaml
name: API Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
    
    - name: Run tests
      run: ./gradlew clean test
    
    - name: Generate Allure Report
      if: always()
      run: ./gradlew allureReport
    
    - name: Upload Allure Results
      if: always()
      uses: actions/upload-artifact@v3
      with:
        name: allure-results
        path: build/allure-results
```

### Jenkins

```groovy
pipeline {
    agent any
    
    tools {
        jdk 'JDK11'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }
        
        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }
        
        stage('Report') {
            steps {
                allure includeProperties: false,
                       jdk: '',
                       results: [[path: 'build/allure-results']]
            }
        }
    }
    
    post {
        always {
            junit 'build/test-results/test/*.xml'
        }
    }
}
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 Best Practices

- ✅ Follow Java naming conventions
- ✅ Write descriptive test names
- ✅ Use soft assertions for multiple validations
- ✅ Add Allure annotations for better reporting
- ✅ Keep tests independent and idempotent
- ✅ Use test data builders for object creation
- ✅ Log important test steps
- ✅ Handle test data cleanup

## 🐛 Troubleshooting

### Common Issues

**Issue**: Tests fail with connection timeout
```
Solution: Check api.base.url in config.properties and network connectivity
```

**Issue**: Gradle build fails
```
Solution: Ensure Java 11+ is installed and JAVA_HOME is set correctly
```

**Issue**: Allure report not generating
```
Solution: Run ./gradlew clean allureReport
```

## 📧 Contact

For questions or support, please contact the Petstore Automation Team.

## 📄 License

This project is licensed under the MIT License.

---

**Happy Testing! 🚀**
