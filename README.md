# USPTO Data Set API Test Automation Framework

## 📋 Overview

This is a comprehensive Test Automation Framework for the **USPTO Data Set API** built using Java, REST Assured, TestNG, and Allure Reports. The framework follows industry best practices and design patterns to ensure maintainability, scalability, and reliability.

## 🎯 Features

- ✅ **REST API Testing** with REST Assured
- ✅ **TestNG** for test execution and management
- ✅ **Allure Reports** for comprehensive test reporting
- ✅ **Page Object Model** (Service Object Pattern for APIs)
- ✅ **Data-Driven Testing** with TestNG DataProviders
- ✅ **Logging** with Log4j2
- ✅ **Configuration Management** with properties files
- ✅ **JSON Schema Validation**
- ✅ **Parallel Test Execution**
- ✅ **CI/CD Ready**

## 🏗️ Framework Architecture

```
uspto-dataset-api-automation/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── uspto/
│   │               └── api/
│   │                   ├── client/          # REST client wrapper
│   │                   ├── config/          # Configuration management
│   │                   ├── constants/       # API constants
│   │                   ├── models/          # POJO classes
│   │                   ├── services/        # API service classes
│   │                   └── utils/           # Utility classes
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── uspto/
│       │           └── api/
│       │               ├── listeners/       # TestNG listeners
│       │               └── tests/
│       │                   ├── base/        # Base test class
│       │                   ├── functional/  # Functional tests
│       │                   ├── negative/    # Negative tests
│       │                   ├── schema/      # Schema validation tests
│       │                   └── smoke/       # Smoke tests
│       └── resources/
│           ├── config.properties            # Configuration file
│           ├── log4j2.xml                   # Logging configuration
│           ├── testng.xml                   # TestNG suite configuration
│           └── testdata/                    # Test data files
├── pom.xml                                  # Maven configuration
├── TESTCASES.md                             # Test cases documentation
└── README.md                                # This file
```

## 🚀 Getting Started

### Prerequisites

- **Java 11** or higher
- **Maven 3.6+**
- **Git**

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/klindziukp/staf-ai.git
   cd staf-ai
   ```

2. **Install dependencies:**
   ```bash
   mvn clean install -DskipTests
   ```

3. **Verify installation:**
   ```bash
   mvn --version
   java --version
   ```

## ⚙️ Configuration

### config.properties

Update `src/test/resources/config.properties` with your configuration:

```properties
# API Base URL
api.base.url=https://developer.uspto.gov/ds-api

# Default Dataset Configuration
api.default.dataset=oa_citations
api.default.version=v1

# Timeouts (in milliseconds)
api.request.timeout=30000
api.connection.timeout=10000

# Logging
logging.request.enabled=true
logging.response.enabled=true
```

## 🧪 Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run Specific Test Suite

```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Run Specific Test Class

```bash
mvn clean test -Dtest=ListDataSetsTest
```

### Run Tests in Parallel

```bash
mvn clean test -Dparallel=methods -DthreadCount=5
```

### Run with Custom Configuration

```bash
mvn clean test -Dapi.base.url=https://custom-url.com
```

## 📊 Test Reports

### Generate Allure Report

1. **Run tests:**
   ```bash
   mvn clean test
   ```

2. **Generate Allure report:**
   ```bash
   mvn allure:serve
   ```

   This will automatically open the report in your default browser.

3. **Generate static Allure report:**
   ```bash
   mvn allure:report
   ```

   Report will be available at: `target/site/allure-maven-plugin/index.html`

### View Logs

Logs are available at:
- **All logs:** `target/logs/uspto-api-tests.log`
- **Error logs:** `target/logs/uspto-api-errors.log`

## 📝 Test Coverage

### Endpoints Covered

1. **GET /** - List available datasets
2. **GET /{dataset}/{version}/fields** - List searchable fields
3. **POST /{dataset}/{version}/records** - Search records

### Test Types

- ✅ **Smoke Tests** - Basic functionality validation
- ✅ **Functional Tests** - Detailed feature testing
- ✅ **Negative Tests** - Error handling and edge cases
- ✅ **Schema Validation Tests** - Response structure validation
- ✅ **Performance Tests** - Response time validation
- ✅ **Security Tests** - Input validation and injection attempts

### Test Statistics

- **Total Test Cases:** 30+
- **Smoke Tests:** 5
- **Functional Tests:** 15
- **Negative Tests:** 10
- **Schema Validation Tests:** 6

For detailed test case documentation, see [TESTCASES.md](TESTCASES.md)

## 🔧 Framework Components

### REST Client

Generic REST client wrapper providing:
- GET, POST, PUT, DELETE, PATCH methods
- Path and query parameter support
- Form parameter support
- Custom header support
- Automatic logging and Allure integration

### Service Classes

- **DataSetService** - Dataset-related operations
- **SearchService** - Search-related operations

### Models (POJOs)

- **DataSetListResponse** - List datasets response
- **FieldsResponse** - List fields response
- **SearchRequest** - Search request body
- **SearchResponse** - Search response

### Utilities

- **JsonUtils** - JSON serialization/deserialization
- **ResponseValidator** - Response validation utilities

### Listeners

- **TestSuiteListener** - Suite-level event handling
- **TestMethodListener** - Test-level event handling

## 🎨 Design Patterns

1. **Singleton Pattern** - ConfigurationManager
2. **Builder Pattern** - Model classes
3. **Service Object Pattern** - API service classes
4. **Factory Pattern** - REST client creation

## 📚 Best Practices

1. ✅ **Separation of Concerns** - Clear separation between test logic and API calls
2. ✅ **DRY Principle** - Reusable components and utilities
3. ✅ **Meaningful Naming** - Descriptive class, method, and variable names
4. ✅ **Comprehensive Logging** - Detailed logging at all levels
5. ✅ **Error Handling** - Proper exception handling
6. ✅ **Documentation** - JavaDoc comments for all public methods
7. ✅ **Test Data Management** - Externalized test data
8. ✅ **Assertions** - Clear and descriptive assertions

## 🔍 Code Quality

### Run Code Quality Checks

```bash
# Compile and verify
mvn clean verify

# Run tests with coverage
mvn clean test jacoco:report
```

## 🐛 Troubleshooting

### Common Issues

1. **Tests failing with connection timeout:**
   - Increase timeout in `config.properties`
   - Check network connectivity
   - Verify API base URL

2. **Allure report not generating:**
   - Ensure Allure is installed: `mvn allure:serve`
   - Check `target/allure-results` directory exists

3. **Compilation errors:**
   - Verify Java version: `java --version`
   - Clean and rebuild: `mvn clean install`

## 📖 API Documentation

Official USPTO Data Set API Documentation:
- [OpenAPI Specification](https://learn.openapis.org/examples/v3.0/uspto.html)
- [Developer Portal](https://developer.uspto.gov)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Authors

- **USPTO API Test Team**

## 📞 Contact

For questions or support:
- Email: developer@uspto.gov
- GitHub Issues: [Create an issue](https://github.com/klindziukp/staf-ai/issues)

## 🙏 Acknowledgments

- USPTO for providing the public API
- REST Assured community
- TestNG community
- Allure Framework team

---

**Happy Testing! 🚀**
