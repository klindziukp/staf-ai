# USPTO Data Set API - Test Automation Framework

A comprehensive Test Automation Framework for the USPTO Data Set API built with Gradle, STAF framework, and Java best practices.

## 🎯 Overview

This framework provides automated testing for the [USPTO Data Set API](https://developer.uspto.gov/ds-api), which allows public users to discover and search USPTO exported datasets. The framework implements tests using both Apache HTTP Client and Retrofit to ensure comprehensive API coverage.

## 🏗️ Architecture

### Technology Stack

- **Build Tool**: Gradle (Kotlin DSL)
- **Language**: Java 17
- **Test Framework**: TestNG
- **HTTP Clients**: 
  - Apache HTTP Client 5
  - Retrofit 2
- **Assertion Library**: AssertJ
- **JSON Processing**: Gson, Jackson
- **Logging**: SLF4J with Logback
- **Code Generation**: Lombok

### STAF Framework Dependencies

```kotlin
implementation("com.staf:module-taf-api:1.0-SNAPSHOT")
implementation("com.staf:module-taf-apache-http:1.0-SNAPSHOT")
implementation("com.staf:module-taf-retrofit:1.0-SNAPSHOT")
```

## 📁 Project Structure

```
staf-ai/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/staf/uspto/
│   │           ├── api/
│   │           │   └── UsptoApiService.java          # Retrofit API interface
│   │           ├── constant/
│   │           │   └── RequestPath.java              # API endpoint constants
│   │           └── model/
│   │               ├── ApiInfo.java                  # Dataset API info model
│   │               ├── DataSetList.java              # Dataset list model
│   │               ├── FieldsResponse.java           # Fields metadata model
│   │               ├── SearchRequest.java            # Search request model
│   │               └── SearchResponse.java           # Search response model
│   └── test/
│       ├── java/
│       │   └── com/staf/uspto/
│       │       ├── listener/
│       │       │   ├── TestMethodCaptureListener.java # Method-level logging
│       │       │   └── TestSuiteListener.java         # Suite-level logging
│       │       ├── service/
│       │       │   └── ResponseVerificationService.java # Response validation
│       │       └── test/
│       │           ├── BaseApiTest.java               # Base test class
│       │           ├── apachehttp/
│       │           │   ├── ApacheHttpDataSetTest.java # Apache HTTP dataset tests
│       │           │   └── ApacheHttpSearchTest.java  # Apache HTTP search tests
│       │           └── retrofit/
│       │               ├── RetrofitDataSetTest.java   # Retrofit dataset tests
│       │               └── RetrofitSearchTest.java    # Retrofit search tests
│       └── resources/
│           ├── logback-test.xml                       # Logging configuration
│           └── testng.xml                             # TestNG suite configuration
├── .github/
│   └── workflows/
│       └── api-tests.yml                              # GitHub Actions workflow
├── build.gradle.kts                                   # Gradle build configuration
└── settings.gradle.kts                                # Gradle settings
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 8.x (or use included wrapper)
- Internet connection (for API calls)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/klindziukp/staf-ai.git
cd staf-ai
```

2. Build the project:
```bash
./gradlew clean build
```

### Running Tests

#### Run all tests:
```bash
./gradlew test
```

#### Run specific test class:
```bash
./gradlew test --tests "com.staf.uspto.test.apachehttp.ApacheHttpDataSetTest"
```

#### Run tests with specific tag:
```bash
./gradlew test -Dgroups="smoke"
```

### Test Reports

After test execution, reports are available at:
- HTML Report: `build/reports/tests/test/index.html`
- XML Results: `build/test-results/test/`
- Logs: `target/test-logs/uspto-api-tests.log`

## 📋 Test Coverage

### Metadata Tests (Dataset Endpoints)

#### Apache HTTP Client Tests
- ✅ List all available datasets
- ✅ Verify dataset API information structure
- ✅ Retrieve searchable fields for a dataset
- ✅ Handle non-existent dataset (404 error)
- ✅ Verify Content-Type headers

#### Retrofit Tests
- ✅ List all available datasets
- ✅ Verify dataset API information structure
- ✅ Retrieve searchable fields for a dataset
- ✅ Handle non-existent dataset (404 error)
- ✅ Verify response headers

### Search Tests (Records Endpoints)

#### Apache HTTP Client Tests
- ✅ Search with default criteria (*:*)
- ✅ Search with specific criteria
- ✅ Pagination (start and rows parameters)
- ✅ Search with zero rows
- ✅ Handle non-existent dataset search

#### Retrofit Tests
- ✅ Search with default criteria
- ✅ Search with row limit
- ✅ Pagination functionality
- ✅ Search with zero rows
- ✅ Handle non-existent dataset search
- ✅ Verify response structure

## 🔧 Configuration

### API Configuration

Base URL and default values are configured in `RequestPath.java`:

```java
public static final String BASE_URL = "https://developer.uspto.gov/ds-api";
public static final String DEFAULT_DATASET = "oa_citations";
public static final String DEFAULT_VERSION = "v1";
```

### TestNG Configuration

Test execution is configured in `src/test/resources/testng.xml`:
- Parallel execution: 2 threads
- Test listeners for logging and reporting
- Organized test suites by HTTP client type

### Logging Configuration

Logging is configured in `src/test/resources/logback-test.xml`:
- Console output with formatted patterns
- File output to `target/test-logs/`
- Configurable log levels per package

## 🎭 Key Features

### 1. Lombok Integration
All model classes use Lombok annotations for clean, boilerplate-free code:
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSetList {
    private Integer total;
    private List<ApiInfo> apis;
}
```

### 2. TestNG Listeners

**TestMethodCaptureListener**: Captures method-level execution details
- Logs test method start/end
- Tracks execution duration
- Captures and logs exceptions

**TestSuiteListener**: Tracks suite-level execution
- Logs suite start/end
- Provides test summary (passed/failed/skipped)
- Calculates total execution time

### 3. Response Verification Service

Centralized verification methods for:
- HTTP status codes
- JSON structure validation
- Response body content
- Custom business logic validation

### 4. Dual HTTP Client Support

Tests implemented with both:
- **Apache HTTP Client 5**: Low-level HTTP operations
- **Retrofit 2**: Type-safe REST client with annotations

### 5. Fluent Assertions

Using AssertJ for readable and maintainable assertions:
```java
assertThat(dataSetList.getTotal())
    .as("Total datasets count")
    .isNotNull()
    .isGreaterThan(0);
```

## 🔄 CI/CD Integration

### GitHub Actions Workflow

The framework includes a GitHub Actions workflow (`.github/workflows/api-tests.yml`) that:
- ✅ Triggers on pull requests only
- ✅ Sets up Java 17 environment
- ✅ Builds the project
- ✅ Runs all tests
- ✅ Generates test reports
- ✅ Uploads artifacts (reports and logs)
- ✅ Provides test summary

### Workflow Triggers

```yaml
on:
  pull_request:
    branches:
      - main
      - develop
    paths:
      - 'src/**'
      - 'build.gradle.kts'
      - '.github/workflows/api-tests.yml'
```

## 📊 API Endpoints Tested

### 1. List Datasets
```
GET /
```
Returns a list of all available datasets.

### 2. Get Searchable Fields
```
GET /{dataset}/{version}/fields
```
Returns metadata about searchable fields for a specific dataset.

### 3. Search Records
```
POST /{dataset}/{version}/records
Content-Type: application/x-www-form-urlencoded

criteria=*:*&start=0&rows=100
```
Searches records using Lucene query syntax.

## 🧪 Example Test

```java
@Test(description = "Verify that the API returns a list of available datasets")
public void testListDataSets() throws IOException {
    // Arrange
    String url = buildUrl("/");
    HttpGet request = new HttpGet(url);
    logRequest("GET", url);
    
    // Act
    try (CloseableHttpResponse response = httpClient.execute(request)) {
        String responseBody = EntityUtils.toString(response.getEntity());
        logResponse(response.getCode(), responseBody);
        
        // Assert
        verificationService.verifyStatusCode(response, 200);
        verificationService.verifyValidJson(responseBody);
        
        DataSetList dataSetList = gson.fromJson(responseBody, DataSetList.class);
        verificationService.verifyDataSetList(dataSetList);
        
        log.info("Successfully retrieved {} datasets", dataSetList.getTotal());
    }
}
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 Best Practices Implemented

1. ✅ **Separation of Concerns**: Models, services, and tests are properly separated
2. ✅ **DRY Principle**: Common functionality in base classes and utility services
3. ✅ **Naming Conventions**: Java standard naming conventions followed
4. ✅ **Logging**: Comprehensive logging at all levels
5. ✅ **Error Handling**: Proper exception handling and reporting
6. ✅ **Code Documentation**: JavaDoc comments for all public methods
7. ✅ **Test Organization**: Logical grouping of tests by functionality
8. ✅ **Assertions**: Descriptive assertions with custom messages
9. ✅ **Configuration Management**: Externalized configuration
10. ✅ **CI/CD Ready**: GitHub Actions workflow included

## 📚 Resources

- [USPTO Data Set API Documentation](https://developer.uspto.gov/ds-api-docs/)
- [TestNG Documentation](https://testng.org/doc/)
- [AssertJ Documentation](https://assertj.github.io/doc/)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Apache HTTP Client Documentation](https://hc.apache.org/httpcomponents-client-5.2.x/)

## 📄 License

This project is part of the STAF (Software Test Automation Framework) ecosystem.

## 👥 Authors

- STAF Team

## 🙏 Acknowledgments

- USPTO for providing the public API
- STAF framework contributors
- Open source community

---

**Note**: This framework is designed for testing purposes and follows industry best practices for API test automation.
