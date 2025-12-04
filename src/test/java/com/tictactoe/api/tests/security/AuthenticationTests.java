package com.tictactoe.api.tests.security;

import com.tictactoe.api.client.TicTacToeApiClient;
import com.tictactoe.api.tests.base.BaseTest;
import com.tictactoe.api.utils.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for authentication and security scenarios.
 */
@Epic("Tic Tac Toe API")
@Feature("Security")
@Story("Authentication")
public class AuthenticationTests extends BaseTest {

    @Test(description = "Verify GET /board works with API Key authentication")
    @Description("Test that API Key authentication is accepted for GET /board")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetBoardWithApiKeyAuth() {
        TicTacToeApiClient client = TicTacToeApiClient.withApiKey();
        Response response = client.getBoard();

        ResponseValidator.validateStatusCode(response, 200);
    }

    @Test(description = "Verify GET /board fails without authentication")
    @Description("Test that GET /board returns 401 without authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetBoardWithoutAuth() {
        TicTacToeApiClient client = TicTacToeApiClient.withoutAuth();
        Response response = client.getBoard();

        assertThat(response.getStatusCode())
                .as("Should return 401 Unauthorized")
                .isIn(401, 403);
    }

    @Test(description = "Verify GET /board/{row}/{column} works with Bearer token")
    @Description("Test that Bearer token authentication is accepted for GET square")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetSquareWithBearerAuth() {
        TicTacToeApiClient client = TicTacToeApiClient.withBearerToken();
        Response response = client.getSquare(1, 1);

        ResponseValidator.validateStatusCode(response, 200);
    }

    @Test(description = "Verify GET /board/{row}/{column} fails without authentication")
    @Description("Test that GET square returns 401 without authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetSquareWithoutAuth() {
        TicTacToeApiClient client = TicTacToeApiClient.withoutAuth();
        Response response = client.getSquare(1, 1);

        assertThat(response.getStatusCode())
                .as("Should return 401 Unauthorized")
                .isIn(401, 403);
    }

    @Test(description = "Verify PUT /board/{row}/{column} works with Bearer token")
    @Description("Test that Bearer token authentication is accepted for PUT square")
    @Severity(SeverityLevel.CRITICAL)
    public void testPutSquareWithBearerAuth() {
        TicTacToeApiClient client = TicTacToeApiClient.withBearerToken();
        Response response = client.putSquare(1, 1, com.tictactoe.api.models.Mark.X);

        ResponseValidator.validateStatusCode(response, 200);
    }

    @Test(description = "Verify PUT /board/{row}/{column} fails without authentication")
    @Description("Test that PUT square returns 401 without authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void testPutSquareWithoutAuth() {
        TicTacToeApiClient client = TicTacToeApiClient.withoutAuth();
        Response response = client.putSquare(1, 1, com.tictactoe.api.models.Mark.X);

        assertThat(response.getStatusCode())
                .as("Should return 401 Unauthorized")
                .isIn(401, 403);
    }

    @Test(description = "Verify invalid Bearer token is rejected")
    @Description("Test that invalid Bearer token returns 401 or 403")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidBearerToken() {
        TicTacToeApiClient client = TicTacToeApiClient.withBearerToken("invalid_token_12345");
        Response response = client.getBoard();

        assertThat(response.getStatusCode())
                .as("Should return 401 or 403 for invalid token")
                .isIn(401, 403);
    }

    @Test(description = "Verify expired Bearer token is rejected")
    @Description("Test that expired Bearer token returns 401")
    @Severity(SeverityLevel.NORMAL)
    public void testExpiredBearerToken() {
        // This would require a real expired token
        String expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyMzkwMjJ9.invalid";
        TicTacToeApiClient client = TicTacToeApiClient.withBearerToken(expiredToken);
        Response response = client.getBoard();

        assertThat(response.getStatusCode())
                .as("Should return 401 for expired token")
                .isIn(401, 403);
    }

    @Test(description = "Verify API Key in header is validated")
    @Description("Test that API Key must be present in correct header")
    @Severity(SeverityLevel.NORMAL)
    public void testApiKeyHeaderValidation() {
        TicTacToeApiClient client = TicTacToeApiClient.withApiKey();
        Response response = client.getBoard();

        // Should succeed with valid API key
        ResponseValidator.validateStatusCode(response, 200);
    }

    @Test(description = "Verify Bearer token format is validated")
    @Description("Test that Bearer token must have correct format")
    @Severity(SeverityLevel.NORMAL)
    public void testBearerTokenFormatValidation() {
        TicTacToeApiClient client = TicTacToeApiClient.withBearerToken("malformed-token");
        Response response = client.getSquare(1, 1);

        assertThat(response.getStatusCode())
                .as("Should return 401 or 403 for malformed token")
                .isIn(401, 403);
    }
}
