# Tic Tac Toe API Test Automation Framework

A comprehensive, production-ready test automation framework for the Tic Tac Toe API built with Java, REST Assured, and TestNG.

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

## 🎯 Overview

This framework provides automated testing for the Tic Tac Toe API, covering:
- Board state management
- Square operations (get/set)
- Game logic validation
- Authentication and security
- Winning conditions and draw scenarios

## ✨ Features

- **REST Assured** for API testing
- **TestNG** for test execution and management
- **Allure** for comprehensive test reporting
- **Multiple authentication** methods support (API Key, Bearer Token, OAuth2)
- **Parameterized tests** using DataProviders
- **Parallel test execution** for faster feedback
- **Environment-specific** configurations
- **Comprehensive logging** with Log4j2
- **Page Object Model** pattern (API Client pattern)
- **Fluent assertions** with AssertJ
- **JSON validation** and schema validation

## 🛠 Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11+ | Programming language |
| Gradle | 8.5 | Build tool |
| REST Assured | 5.3.0 | API testing framework |
| TestNG | 7.11.0 | Test execution framework |
| Allure | 2.29.1 | Test reporting |
| Log4j2 | 3.0.0-beta2 | Logging framework |
| Lombok | 1.18.34 | Boilerplate reduction |
| Jackson | 2.18.2 | JSON processing |
| AssertJ | 3.27.3 | Fluent assertions |

## 📁 Project Structure

```
tic-tac-toe-api-tests/
├── src/
│   ├── main/java/com/tictactoe/api/
│   │   ├── client/          # API clients
│   │   ├── models/          # Data models
│   │   ├── config/          # Configuration management
│   │   └── utils/           # Utility classes
│   └── test/java/com/tictactoe/api/tests/
│       ├── base/            # Base test classes
│       ├── board/           # Board operation tests
│       ├── gameplay/        # Game logic tests
│       └── security/        # Security tests
├── build.gradle
├── settings.gradle
└── README.md
```

## 📋 Prerequisites

- Java 11 or higher
- Gradle 8.5 or higher (or use included Gradle wrapper)

## 🚀 Installation

```bash
git clone https://github.com/klindziukp/staf-ai.git
cd staf-ai
./gradlew clean build
```

## 🧪 Running Tests

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

### Run Security Tests
```bash
./gradlew securityTests
```

### Run with Specific Environment
```bash
./gradlew test -Denvironment=staging
```

## 📊 Test Reports

Generate and view Allure report:
```bash
./gradlew allureReport
./gradlew allureServe
```

## 🧩 Test Coverage

### Board Management Tests
- ✅ Get empty board
- ✅ Get board after placing marks
- ✅ Get single square (empty, X, O)
- ✅ Place marks on empty squares
- ✅ Validate all board positions
- ✅ Boundary value testing

### Game Logic Tests
- ✅ Horizontal winning conditions
- ✅ Vertical winning conditions
- ✅ Diagonal winning conditions
- ✅ Draw scenarios
- ✅ Game in progress detection

### Negative Tests
- ✅ Invalid coordinates
- ✅ Invalid marks
- ✅ Occupied square validation

### Security Tests
- ✅ API Key authentication
- ✅ Bearer token authentication
- ✅ Missing authentication
- ✅ Invalid tokens

---

**Happy Testing! 🚀**
