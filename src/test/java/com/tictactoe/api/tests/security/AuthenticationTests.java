package com.tictactoe.api.tests.security;

import com.tictactoe.api.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for authentication and authorization scenarios.
 * Tests various authentication methods and security controls.
 */
@Epic("Tic Tac Toe API")
@Feature("Security")
@Story("Authentication")
public class AuthenticationTests extends BaseTest {

    @Test(description = "Verify API key authentication works")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that valid API key allows access to endpoints")
    public void testApiKeyAuthentication() {
        Response response = boardApiClient.getBoardWithApiKey("test-api-key");

        assertThat(response.getStatusCode())
                .as("Valid API key should allow access")
                .isEqualTo(200);
    }

    @Test(description = "Verify invalid API key is rejected")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that invalid API key returns 401 or 403")
    public void testInvalidApiKey() {
        Response response = boardApiClient.getBoardWithApiKey("invalid-key-12345");

        assertThat(response.getStatusCode())
                .as("Invalid API key should be rejected")
                .isIn(401, 403);
    }

    @Test(description = "Verify missing authentication is rejected")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that requests without authentication are rejected")
    public void testMissingAuthentication() {
        Response response = boardApiClient.getBoardWithoutAuth();

        assertThat(response.getStatusCode())
                .as("Missing authentication should be rejected")
                .isIn(401, 403);
    }

    @Test(description = "Verify read operations require authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that GET operations require authentication")
    public void testReadOperationsRequireAuth() {
        Response boardResponse = boardApiClient.getBoardWithoutAuth();
        Response squareResponse = squareApiClient.getSquareWithoutAuth(1, 1);

        assertThat(boardResponse.getStatusCode())
                .as("GET /board should require authentication")
                .isIn(401, 403);

        assertThat(squareResponse.getStatusCode())
                .as("GET /board/{row}/{column} should require authentication")
                .isIn(401, 403);
    }

    @Test(description = "Verify write operations require authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that PUT operations require authentication")
    public void testWriteOperationsRequireAuth() {
        Response response = squareApiClient.placeMarkWithoutAuth(1, 1, "X");

        assertThat(response.getStatusCode())
                .as("PUT /board/{row}/{column} should require authentication")
                .isIn(401, 403);
    }
}
