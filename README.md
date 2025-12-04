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

## Contact
For questions or issues, please contact: developer@uspto.gov
