# Tic Tac Toe API Test Framework Architecture

## Architecture Overview

This test automation framework follows a layered architecture pattern with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                      Test Layer                              │
│  (Apache HTTP, RestAssured, Retrofit Test Classes)          │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                   Service Layer                              │
│         (ResponseVerificationService)                        │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                   Client Layer                               │
│     (STAF API Clients: Apache, RestAssured, Retrofit)       │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                   Model Layer                                │
│    (BoardStatus, Mark, MarkRequest, ErrorResponse)          │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                   Utility Layer                              │
│         (GsonUtil, IgnoreUtil, Constants)                    │
└─────────────────────────────────────────────────────────────┘
```

## Design Patterns

### 1. Builder Pattern
Used extensively for creating API requests and model objects:
```java
ApiRequest.<JsonObject>builder()
    .responseBodyType(JsonObject.class)
    .baseUrl(ServiceConfig.BASE_URL)
    .path(RequestPath.GET_BOARD)
    .httpMethod(Method.GET)
    .build();
```

### 2. Strategy Pattern
Serialization strategies are interchangeable:
- `GsonSerializationStrategy`
- `JacksonSerializationStrategy`

### 3. Template Method Pattern
`BaseApiTest` provides template methods that subclasses can override:
```java
public abstract class BaseApiTest {
    protected abstract void deletePlayerAndVerify(String playerId);
}
```

### 4. Factory Pattern
Client creation follows factory pattern through STAF configuration:
```java
ApacheHttpProgrammaticConfig config = new ApacheHttpProgrammaticConfig();
IApiClient client = new ApacheHttpClient(config);
```

### 5. Singleton Pattern
Configuration classes use static final fields for singleton behavior:
```java
public static final String BASE_URL = System.getProperty("base.url");
```

## Component Responsibilities

### Test Layer
**Responsibility**: Execute test scenarios and verify outcomes
- Contains test classes organized by HTTP client type
- Uses TestNG annotations for test configuration
- Implements Allure annotations for reporting
- Each test class focuses on specific client implementation

**Key Classes**:
- `TicTacToeApacheHttpTest`
- `TicTacToeRestAssuredTest`
- `TicTacToeRetrofitTest`
- `TicTacToeIntegrationTest`

### Service Layer
**Responsibility**: Provide reusable verification and business logic
- Encapsulates response verification logic
- Provides multiple verification strategies
- Uses soft assertions for comprehensive validation

**Key Classes**:
- `ResponseVerificationService`

### Client Layer
**Responsibility**: Handle HTTP communication
- Abstracted through STAF framework
- Supports multiple HTTP client implementations
- Provides consistent API across different clients

**Key Interfaces**:
- `IApiClient`
- `ISerializationStrategy`

### Model Layer
**Responsibility**: Represent API data structures
- Uses Lombok for boilerplate reduction
- Immutable where appropriate
- Clear mapping to API schemas

**Key Classes**:
- `BoardStatus` - Represents complete board state
- `Mark` - Enum for board marks (X, O, .)
- `MarkRequest` - Request payload for placing marks
- `ErrorResponse` - Error response structure

### Utility Layer
**Responsibility**: Provide common utilities
- JSON manipulation utilities
- Constant definitions
- Helper methods

**Key Classes**:
- `GsonUtil` - JSON conversion utilities
- `IgnoreUtil` - JSON comparison utilities
- `RequestPath` - API endpoint constants
- `ServiceConfig` - Configuration management

## Data Flow

### Request Flow
```
Test Method
    ↓
Create ApiRequest (Builder Pattern)
    ↓
Serialize Request Body (Strategy Pattern)
    ↓
Send via IApiClient
    ↓
HTTP Client (Apache/RestAssured/Retrofit)
    ↓
API Server
```

### Response Flow
```
API Server
    ↓
HTTP Client receives response
    ↓
Deserialize Response Body
    ↓
Create ApiResponse object
    ↓
ResponseVerificationService
    ↓
Assertions (TestNG/AssertJ)
    ↓
Test Result
```

## Thread Safety

### ThreadLocal Usage
Listeners use ThreadLocal for thread-safe test context:
```java
private static final ThreadLocal<ITestNGMethod> currentMethods = new ThreadLocal<>();
```

This ensures:
- Parallel test execution safety
- Isolated test contexts
- No cross-test contamination

## Configuration Management

### Hierarchical Configuration
1. **System Properties** (Highest Priority)
   ```bash
   -Dbase.url=https://custom-url.com
   ```

2. **Default Values** (Fallback)
   ```java
   public static final String BASE_URL = System.getProperty("base.url", "https://learn.openapis.org");
   ```

### Configuration Sources
- Command line arguments
- Environment variables
- Property files (future enhancement)

## Error Handling Strategy

### Layered Error Handling
1. **Client Layer**: HTTP errors, connection issues
2. **Service Layer**: Validation errors, assertion failures
3. **Test Layer**: Test-specific error handling

### Error Propagation
```
HTTP Error
    ↓
ApiResponse (with error status)
    ↓
ResponseVerificationService (validates status)
    ↓
Assertion Failure (if unexpected)
    ↓
Test Failure (with detailed message)
```

## Extensibility Points

### Adding New HTTP Client
1. Create new test class extending `BaseApiTest`
2. Configure client in `@BeforeClass`
3. Implement test methods
4. Add to TestNG suite

### Adding New Endpoints
1. Add path constant to `RequestPath`
2. Create/update model classes if needed
3. Add test methods in appropriate test class
4. Update documentation

### Adding New Verification Methods
1. Add method to `ResponseVerificationService`
2. Use in test classes
3. Document usage

## Testing Strategy

### Test Pyramid
```
        ┌─────────────┐
        │   E2E Tests │  (Integration Tests)
        └─────────────┘
       ┌───────────────┐
       │  API Tests    │  (Main Test Classes)
       └───────────────┘
      ┌─────────────────┐
      │  Unit Tests     │  (Utility Tests)
      └─────────────────┘
```

### Test Categories
1. **Smoke Tests**: Basic functionality verification
2. **Functional Tests**: Complete feature testing
3. **Integration Tests**: End-to-end scenarios
4. **Negative Tests**: Error handling validation
5. **Data-Driven Tests**: Parameterized testing

## Reporting Architecture

### Multi-Level Reporting
1. **Console Output**: Real-time feedback
2. **Log Files**: Detailed execution logs
3. **TestNG Reports**: HTML test results
4. **Allure Reports**: Rich interactive reports

### Allure Integration
```
Test Execution
    ↓
Allure Annotations (@Epic, @Feature, @Story)
    ↓
Allure Results (JSON)
    ↓
Allure Report Generation
    ↓
Interactive HTML Report
```

## Best Practices Implemented

### Code Organization
- Package by feature/layer
- Clear naming conventions
- Consistent code style

### Test Design
- Independent tests
- Repeatable tests
- Fast execution
- Clear assertions

### Maintainability
- DRY principle
- Single Responsibility
- Open/Closed principle
- Dependency Injection

### Documentation
- Inline comments
- JavaDoc
- Architecture docs
- README files

## Performance Considerations

### Optimization Strategies
1. **Connection Pooling**: Reuse HTTP connections
2. **Parallel Execution**: TestNG parallel support
3. **Lazy Initialization**: Create clients only when needed
4. **Resource Cleanup**: Proper teardown methods

### Scalability
- Stateless test design
- No shared mutable state
- Thread-safe implementations
- Configurable parallelism

## Security Considerations

### Authentication Support
- API Key authentication
- Bearer token authentication
- OAuth2 support (via STAF)

### Sensitive Data
- Configuration externalization
- No hardcoded credentials
- Environment-based configuration

## Future Enhancements

### Planned Features
1. Database validation
2. Performance testing integration
3. Contract testing
4. Mock server support
5. CI/CD pipeline enhancements
6. Test data management
7. API versioning support
8. Rate limiting tests

### Technical Debt
- Add more unit tests for utilities
- Implement retry mechanism
- Add request/response logging
- Enhance error messages
- Add test data generators

## Conclusion

This architecture provides:
- **Flexibility**: Easy to add new clients/tests
- **Maintainability**: Clear structure and separation
- **Scalability**: Supports parallel execution
- **Reliability**: Comprehensive error handling
- **Usability**: Well-documented and intuitive
