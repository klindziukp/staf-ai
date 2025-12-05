# Contributing to Tic Tac Toe API Test Framework

Thank you for your interest in contributing to the Tic Tac Toe API Test Framework! This document provides guidelines and best practices for contributing.

## Table of Contents
1. [Code of Conduct](#code-of-conduct)
2. [Getting Started](#getting-started)
3. [Development Setup](#development-setup)
4. [Coding Standards](#coding-standards)
5. [Testing Guidelines](#testing-guidelines)
6. [Pull Request Process](#pull-request-process)
7. [Commit Message Guidelines](#commit-message-guidelines)

## Code of Conduct

### Our Standards
- Be respectful and inclusive
- Accept constructive criticism gracefully
- Focus on what is best for the community
- Show empathy towards other community members

## Getting Started

### Prerequisites
- Java 11 or higher
- Gradle 7.x or higher
- Git
- IDE (IntelliJ IDEA recommended)

### Fork and Clone
```bash
# Fork the repository on GitHub
# Clone your fork
git clone https://github.com/YOUR_USERNAME/staf-ai.git
cd staf-ai/tic-tac-toe-api-test
```

## Development Setup

### 1. Install Dependencies
```bash
./gradlew build
```

### 2. Run Tests
```bash
./gradlew executeTicTacToeTest
```

### 3. Generate Reports
```bash
./gradlew allureReport
```

## Coding Standards

### Java Naming Conventions

#### Classes
```java
// Use PascalCase for class names
public class TicTacToeApacheHttpTest { }
public class ResponseVerificationService { }
```

#### Methods
```java
// Use camelCase for method names
// Use descriptive names that indicate action
public void getBoardTest() { }
public void verifyResponse(int status, ApiResponse response) { }
```

#### Variables
```java
// Use camelCase for variables
// Use meaningful names
private final ResponseVerificationService rvs;
private IApiClient iApiClient;
```

#### Constants
```java
// Use UPPER_SNAKE_CASE for constants
public static final String GET_BOARD = "/board";
public static final String BASE_URL = "https://api.example.com";
```

### Lombok Usage

#### Always Use Lombok for Model Classes
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

#### Lombok Annotations Guide
- `@Data`: Use for DTOs and model classes
- `@Builder`: Use for complex object creation
- `@Slf4j`: Use for logging
- `@NoArgsConstructor`: Required for serialization
- `@AllArgsConstructor`: Works with @Builder

### Code Formatting

#### Indentation
- Use 4 spaces for indentation
- No tabs

#### Line Length
- Maximum 120 characters per line
- Break long lines appropriately

#### Imports
```java
// Order: Java, javax, third-party, project
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.testng.annotations.Test;
import com.tictactoe.test.model.BoardStatus;
```

#### Braces
```java
// Always use braces, even for single-line blocks
if (condition) {
    doSomething();
}

// Opening brace on same line
public void method() {
    // code
}
```

### Documentation

#### JavaDoc for Public Methods
```java
/**
 * Verifies API response with expected status code and body.
 *
 * @param httpStatus expected HTTP status code
 * @param apiResponse actual API response
 * @param expected expected response body
 * @param <T> type of response body
 */
public <T> void verifyResponse(int httpStatus, ApiResponse<T> apiResponse, T expected) {
    // implementation
}
```

#### Inline Comments
```java
// Use comments to explain WHY, not WHAT
// Good:
// Retry logic needed due to eventual consistency
retryOperation();

// Bad:
// Call retry operation
retryOperation();
```

## Testing Guidelines

### Test Structure

#### Follow AAA Pattern
```java
@Test
public void testExample() {
    // Arrange
    final MarkRequest request = MarkRequest.builder()
            .mark(Mark.X.getValue())
            .build();
    
    // Act
    final ApiResponse<BoardStatus> response = iApiClient.sendRequest(request);
    
    // Assert
    rvs.verifyStatusCode(HttpStatus.SC_OK, response);
}
```

### Test Naming

#### Use Descriptive Names
```java
// Good
@Test
public void getBoardReturnsValidBoardState() { }

@Test
public void putMarkWithInvalidCoordinatesReturns400() { }

// Bad
@Test
public void test1() { }

@Test
public void testBoard() { }
```

### Allure Annotations

#### Always Add Allure Annotations
```java
@Test
@Epic("Tic Tac Toe API")
@Feature("Board Operations")
@Story("Board Retrieval")
@Description("Verify that the entire board state can be retrieved successfully")
@Severity(SeverityLevel.CRITICAL)
public void getBoardTest() {
    // test implementation
}
```

### Data Providers

#### Use Data Providers for Parameterized Tests
```java
@DataProvider(name = "validCoordinates")
public Object[][] validCoordinatesProvider() {
    return new Object[][] {
        {1, 1}, {1, 2}, {1, 3},
        {2, 1}, {2, 2}, {2, 3},
        {3, 1}, {3, 2}, {3, 3}
    };
}

@Test(dataProvider = "validCoordinates")
public void testValidCoordinates(int row, int column) {
    // test implementation
}
```

### Assertions

#### Use Appropriate Assertion Libraries
```java
// Use AssertJ for fluent assertions
Assertions.assertThat(response.getStatusCode()).isEqualTo(200);

// Use SoftAssertions for multiple checks
SoftAssertions softly = new SoftAssertions();
softly.assertThat(response.getStatusCode()).isEqualTo(200);
softly.assertThat(response.getBody()).isNotNull();
softly.assertAll();

// Use JsonUnit for JSON comparison
JsonAssertions.assertThatJson(actual).isEqualTo(expected);
```

## Pull Request Process

### 1. Create Feature Branch
```bash
git checkout -b feature/your-feature-name
```

### 2. Make Changes
- Write code following standards
- Add tests for new functionality
- Update documentation

### 3. Run Tests Locally
```bash
./gradlew clean build
./gradlew executeTicTacToeTest
```

### 4. Commit Changes
```bash
git add .
git commit -m "feat: add new test for board validation"
```

### 5. Push to Fork
```bash
git push origin feature/your-feature-name
```

### 6. Create Pull Request
- Go to GitHub
- Create Pull Request from your branch
- Fill in PR template
- Link related issues

### PR Checklist
- [ ] Code follows style guidelines
- [ ] Tests added/updated
- [ ] All tests pass
- [ ] Documentation updated
- [ ] Commit messages follow convention
- [ ] No merge conflicts

## Commit Message Guidelines

### Format
```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

### Examples
```bash
# Feature
git commit -m "feat(tests): add integration tests for game scenarios"

# Bug fix
git commit -m "fix(verification): correct status code validation logic"

# Documentation
git commit -m "docs(readme): update installation instructions"

# Test
git commit -m "test(apache): add negative test cases for invalid marks"
```

### Scope Examples
- `tests`: Test-related changes
- `models`: Model class changes
- `utils`: Utility changes
- `config`: Configuration changes
- `docs`: Documentation changes

## Adding New Features

### Adding New Test Class

1. **Create Test Class**
```java
@Epic("Tic Tac Toe API")
@Feature("New Feature")
public class NewFeatureTest extends BaseApiTest {
    
    @BeforeClass
    public void beforeClass() {
        // Setup client
    }
    
    @Test
    @Story("Feature Story")
    @Description("Test description")
    public void newFeatureTest() {
        // Test implementation
    }
}
```

2. **Add to TestNG Suite**
```xml
<test name="New Feature Tests">
    <classes>
        <class name="com.tictactoe.test.test.newfeature.NewFeatureTest"/>
    </classes>
</test>
```

3. **Update Documentation**
- Add to README.md
- Update ARCHITECTURE.md if needed

### Adding New Model Class

1. **Create Model with Lombok**
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewModel {
    private String field1;
    private Integer field2;
}
```

2. **Add JavaDoc**
```java
/**
 * Model representing new feature data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewModel {
    /** Description of field1 */
    private String field1;
    
    /** Description of field2 */
    private Integer field2;
}
```

### Adding New Utility Method

1. **Add to Appropriate Util Class**
```java
/**
 * Utility method description.
 *
 * @param param parameter description
 * @return return value description
 */
public static ReturnType utilityMethod(ParamType param) {
    // implementation
}
```

2. **Add Unit Tests**
```java
@Test
public void testUtilityMethod() {
    // test implementation
}
```

## Code Review Guidelines

### For Reviewers
- Be constructive and respectful
- Explain reasoning for suggestions
- Approve when standards are met
- Request changes if needed

### For Contributors
- Respond to all comments
- Make requested changes
- Ask for clarification if needed
- Be open to feedback

## Questions or Issues?

- Open an issue on GitHub
- Tag with appropriate labels
- Provide detailed description
- Include code examples if applicable

## Recognition

Contributors will be recognized in:
- CONTRIBUTORS.md file
- Release notes
- Project documentation

Thank you for contributing! 🎉
