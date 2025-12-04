# Tic Tac Toe API Test Automation Framework

A comprehensive test automation framework for the Tic Tac Toe API, built with Java, REST Assured, and TestNG.

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
- [API Documentation](#api-documentation)
- [Test Coverage](#test-coverage)

## 🎯 Overview

This framework provides automated testing for the Tic Tac Toe API, covering:
- Board state retrieval
- Single square operations
- Mark placement
- Game logic validation
- Authentication and authorization
- Error handling and validation

## ✨ Features

- **REST Assured** for API testing
- **TestNG** for test execution and organization
- **Allure** for comprehensive test reporting
- **Lombok** for reducing boilerplate code
- **Jackson** for JSON serialization/deserialization
- **Log4j2** for logging
- **AssertJ** for fluent assertions
- **Page Object Model** pattern for API clients
- **Data-driven testing** with TestNG DataProviders
- **Parallel test execution** support
- **Multiple environment** configuration
- **Authentication** handling (API Key, JWT, OAuth2)

## 🛠 Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming Language |
| Gradle | 8.5 | Build Tool |
| REST Assured | 5.5.0 | API Testing |
| TestNG | 7.11.0 | Test Framework |
| Allure | 2.29.1 | Test Reporting |
| Lombok | 1.18.34 | Code Generation |
| Jackson | 2.18.2 | JSON Processing |
| Log4j2 | 3.0.0-beta2 | Logging |
| AssertJ | 3.27.3 | Assertions |

## 📁 Project Structure

```
tic-tac-toe-api-tests/
├── src/
│   ├── main/
│   │   ├── java/com/tictactoe/api/
│   │   │   ├── clients/          # API client classes
│   │   │   ├── config/           # Configuration management
│   │   │   ├── models/           # POJO models
│   │   │   └── utils/            # Utility classes
│   │   └── resources/
│   └── test/
│       ├── java/com/tictactoe/api/
│       │   ├── base/             # Base test class
│       │   └── tests/            # Test classes
│       └── resources/
├── build.gradle
├── gradle.properties
└── README.md
```

## 📋 Prerequisites

- **Java 17** or higher
- **Gradle 8.5** or higher (or use included Gradle wrapper)
- **Allure CLI** (optional, for local report generation)

## 🚀 Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/klindziukp/staf-ai.git
   cd staf-ai
   git checkout feature/tic-tac-toe-api-framework
   ```

2. **Build the project:**
   ```bash
   ./gradlew clean build
   ```

## ⚙️ Configuration

### Environment Configuration

The framework supports multiple environments through property files:
- `config.properties` - Default configuration
- `config-dev.properties` - Development environment

### Configuration Properties

```properties
# API Configuration
api.base.url=https://api.tictactoe.example.com
api.connection.timeout=10000

# Authentication
auth.api.key=your-api-key
auth.jwt.token=your-jwt-token
```

## 🧪 Running Tests

### Run All Tests
```bash
./gradlew test
```

### Run Specific Test Suite
```bash
./gradlew test --tests "com.tictactoe.api.tests.board.*"
```

### Run with Specific Environment
```bash
./gradlew test -Denv=dev
```

## 📊 Test Reports

### Allure Reports
```bash
./gradlew allureReport
./gradlew allureServe
```

## 📖 API Documentation

### Endpoints

#### GET /board
Retrieves the current state of the board and the winner.

#### GET /board/{row}/{column}
Retrieves a single square from the board.

#### PUT /board/{row}/{column}
Places a mark on the board.

## 📈 Test Coverage

### Test Categories

| Category | Test Count | Description |
|----------|------------|-------------|
| Board Tests | 6 | Board state retrieval and validation |
| Square Tests | 15+ | Single square operations |
| Game Logic | 5 | Win conditions and game flow |
| Security | 5 | Authentication and authorization |

---

For more information, visit the [project repository](https://github.com/klindziukp/staf-ai).
