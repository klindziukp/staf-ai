package com.tictactoe.api.tests.board;

import com.tictactoe.api.base.BaseTest;
import com.tictactoe.api.models.Status;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for GET /board endpoint.
 * Tests board retrieval functionality.
 */
@Epic("Tic Tac Toe API")
@Feature("Board Management")
@Story("Get Board State")
public class GetBoardTests extends BaseTest {

    @Test(description = "Verify successful board retrieval with valid authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test retrieves the board state with valid API key and validates the response")
    public void testGetBoardSuccess() {
        Response response = boardApiClient.getBoard();

        assertThat(response.getStatusCode())
                .as("Status code should be 200")
                .isEqualTo(200);

        Status status = response.as(Status.class);
        assertThat(status).isNotNull();
        assertThat(status.getBoard()).isNotNull();
        assertThat(status.getBoard()).hasSize(3);
        assertThat(status.getWinner()).isNotNull();
    }

    @Test(description = "Verify board response contains valid structure")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that board response has correct 3x3 structure")
    public void testGetBoardStructure() {
        Status status = boardApiClient.getBoardStatus();

        assertThat(status.getBoard())
                .as("Board should be 3x3")
                .hasSize(3)
                .allMatch(row -> row.size() == 3);
    }

    @Test(description = "Verify board marks are valid")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test validates that all board marks are either '.', 'X', or 'O'")
    public void testGetBoardValidMarks() {
        Status status = boardApiClient.getBoardStatus();

        status.getBoard().forEach(row ->
                row.forEach(mark ->
                        assertThat(mark)
                                .as("Mark should be valid")
                                .matches("^[.XO]$")
                )
        );
    }

    @Test(description = "Verify unauthorized access without authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that accessing board without authentication returns 401")
    public void testGetBoardWithoutAuth() {
        Response response = boardApiClient.getBoardWithoutAuth();

        assertThat(response.getStatusCode())
                .as("Status code should be 401 for unauthorized access")
                .isIn(401, 403);
    }

    @Test(description = "Verify invalid API key returns error")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that invalid API key returns appropriate error")
    public void testGetBoardWithInvalidApiKey() {
        Response response = boardApiClient.getBoardWithApiKey("invalid-key");

        assertThat(response.getStatusCode())
                .as("Status code should be 401 or 403 for invalid API key")
                .isIn(401, 403);
    }

    @Test(description = "Verify response content type is JSON")
    @Severity(SeverityLevel.MINOR)
    @Description("Test validates that response content type is application/json")
    public void testGetBoardContentType() {
        Response response = boardApiClient.getBoard();

        assertThat(response.getContentType())
                .as("Content type should be JSON")
                .contains("application/json");
    }
}
