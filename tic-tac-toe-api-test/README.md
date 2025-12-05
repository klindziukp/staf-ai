# Tic Tac Toe API Test Automation Framework

## Overview

This is a comprehensive Test Automation Framework for the Tic Tac Toe API, built using the STAF (Scalable Test Automation Framework) and following industry best practices for API testing.

## Features

- **Multiple HTTP Clients**: Support for Apache HTTP, RestAssured, and Retrofit
- **Lombok Integration**: Clean and concise model classes
- **TestNG Framework**: Robust test execution and reporting
- **Allure Reporting**: Beautiful and detailed test reports
- **Gradle Build**: Modern build system with dependency management
- **CI/CD Ready**: GitHub Actions workflow included
- **Comprehensive Test Coverage**: All API endpoints tested

## Project Structure

```
tic-tac-toe-api-test/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/tictactoe/test/
│   │           ├── config/          # Configuration classes
│   │           ├── constant/        # API path constants
│   │           ├── model/           # Data models with Lombok
│   │           ├── storage/         # Test data storage
│   │           └── util/            # Utility classes
│   └── test/
│       ├── java/
│       │   └── com/tictactoe/test/
│       │       ├── listener/        # TestNG listeners
│       │       ├── service/         # Verification services
│       │       └── test/            # Test classes
│       │           ├── apachehttp/  # Apache HTTP tests
│       │           ├── restassured/ # RestAssured tests
│       │           └── retrofit/    # Retrofit tests
│       └── resources/
│           ├── suite/               # TestNG suite files
│           └── log4j2.xml          # Logging configuration
├── build.gradle.kts                 # Gradle build file
└── README.md                        # This file
```

## API Endpoints Tested

### 1. Get Board
- **Endpoint**: `GET /board`
- **Description**: Retrieves the current state of the board and the winner
- **Authentication**: API Key or OAuth2

### 2. Get Square
- **Endpoint**: `GET /board/{row}/{column}`
- **Description**: Retrieves the requested square
- **Parameters**: 
  - `row`: Board row (1-3)
  - `column`: Board column (1-3)
- **Authentication**: Bearer Token or OAuth2

### 3. Put Square
- **Endpoint**: `PUT /board/{row}/{column}`
- **Description**: Places a mark on the board
- **Parameters**: 
  - `row`: Board row (1-3)
  - `column`: Board column (1-3)
- **Body**: `{"mark": "X"}` or `{"mark": "O"}`
- **Authentication**: Bearer Token or OAuth2

## Prerequisites

- Java 11 or higher
- Gradle 7.x or higher
- STAF modules installed in local Maven repository:
  - `module-taf-api`
  - `module-taf-apache-http`
  - `module-taf-retrofit`
  - `module-taf-restassured`

## Configuration

Set the following system properties or environment variables:

```bash
# Base URL for the API
-Dbase.url=https://learn.openapis.org

# API Key for authentication
-Dapi.key=your-api-key

# Bearer token for authentication
-Dbearer.token=your-bearer-token
```

## Running Tests

### Run all tests
```bash
./gradlew executeTicTacToeTest
```

### Run specific test class
```bash
./gradlew test --tests "com.tictactoe.test.test.apachehttp.TicTacToeApacheHttpTest"
```

### Run with custom configuration
```bash
./gradlew executeTicTacToeTest -Dbase.url=https://custom-url.com -Dapi.key=your-key
```

## Test Categories

### Apache HTTP Tests
Tests using Apache HTTP client for low-level HTTP operations:
- Board retrieval
- Square retrieval with validation
- Mark placement
- Error handling
- Complete game flow
- Coordinate validation

### RestAssured Tests
Tests using RestAssured for BDD-style API testing:
- Board state verification
- Sequential mark placement
- Error scenarios
- Game flow validation

### Retrofit Tests
Tests using Retrofit for type-safe HTTP client:
- Board operations
- Winning condition detection
- Occupied square handling
- Type-safe request/response handling

## Test Data

The framework includes pre-defined board states in `BoardDataStorage`:
- Empty board
- X wins horizontal
- O wins vertical
- X wins diagonal
- Draw board

## Reporting

### Allure Reports
After test execution, generate Allure report:
```bash
./gradlew allureReport
```

View the report:
```bash
./gradlew allureServe
```

### TestNG Reports
TestNG HTML reports are generated automatically in:
```
build/reports/tests/test/index.html
```

## Logging

Logs are configured using Log4j2 and are written to:
- Console output
- File: `logs/tic-tac-toe-test.log`

Log levels can be adjusted in `src/test/resources/log4j2.xml`

## CI/CD Integration

The framework includes a GitHub Actions workflow (`.github/workflows/tic-tac-toe-test.yml`) that:
- Runs on push to the test branch
- Executes all tests
- Generates Allure reports
- Uploads test artifacts

## Best Practices Implemented

1. **Separation of Concerns**: Models, utilities, and tests are properly separated
2. **DRY Principle**: Common functionality in base classes
3. **Lombok Usage**: Reduces boilerplate code in models
4. **Thread Safety**: ThreadLocal usage in listeners
5. **Comprehensive Logging**: Detailed logging at all levels
6. **Error Handling**: Proper exception handling and validation
7. **Test Independence**: Each test can run independently
8. **Allure Annotations**: Rich test documentation with @Epic, @Feature, @Story

## Model Classes

All model classes use Lombok annotations:
- `@Data`: Generates getters, setters, toString, equals, and hashCode
- `@Builder`: Provides builder pattern
- `@NoArgsConstructor`: Generates no-args constructor
- `@AllArgsConstructor`: Generates all-args constructor

Example:
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardStatus {
    private String winner;
    private String[][] board;
}
```

## Listeners

### TestMethodCaptureListener
Captures current test method and result in ThreadLocal for thread-safe access.

### TestSuiteListener
Provides logging for test suite and test method lifecycle events.

## Verification Service

`ResponseVerificationService` provides multiple verification methods:
- `verifyResponse()`: Standard response verification
- `verifyGsonResponse()`: JSON-based verification
- `verifyStatusCode()`: Status code only verification
- `verifyResponseContains()`: Partial content verification
- `verifyResponseBodyNotNull()`: Null check verification

## Contributing

1. Follow Java naming conventions
2. Use Lombok for model classes
3. Add Allure annotations to tests
4. Update README for new features
5. Ensure all tests pass before committing

## License

Copyright (c) Tic Tac Toe Test Framework

## Contact

For questions or issues, please create an issue in the repository.
