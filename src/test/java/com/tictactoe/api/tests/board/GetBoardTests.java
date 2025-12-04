package com.tictactoe.api.tests.board;

import com.tictactoe.api.models.Mark;
import com.tictactoe.api.models.Status;
import com.tictactoe.api.tests.base.BaseTest;
import com.tictactoe.api.utils.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for GET /board endpoint.
 */
@Epic("Tic Tac Toe API")
@Feature("Board Management")
@Story("Get Board State")
public class GetBoardTests extends BaseTest {

    @Test(description = "Verify getting empty board returns correct initial state")
    @Description("Test that GET /board returns a 3x3 board with all empty squares and no winner")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetEmptyBoard() {
        Response response = apiClient.getBoard();

        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateContentType(response, "application/json");

        Status status = response.as(Status.class);

        assertThat(status).isNotNull();
        assertThat(status.getWinner()).isEqualTo(Mark.EMPTY);
        assertThat(status.getBoard()).isNotNull();
        assertThat(status.getBoard()).hasSize(3);
        
        // Verify all squares are empty
        for (int i = 0; i < 3; i++) {
            assertThat(status.getBoard()[i]).hasSize(3);
            for (int j = 0; j < 3; j++) {
                assertThat(status.getBoard()[i][j])
                        .as("Square at [%d][%d] should be empty", i, j)
                        .isEqualTo(Mark.EMPTY);
            }
        }
    }

    @Test(description = "Verify getting board after placing marks shows correct state")
    @Description("Test that GET /board returns updated state after marks are placed")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetBoardAfterPlacingMarks() {
        // Place some marks
        apiClient.putSquare(1, 1, Mark.X);
        apiClient.putSquare(1, 2, Mark.O);
        apiClient.putSquare(2, 2, Mark.X);

        Response response = apiClient.getBoard();

        ResponseValidator.validateStatusCode(response, 200);
        Status status = response.as(Status.class);

        assertThat(status).isNotNull();
        assertThat(status.getMark(1, 1)).isEqualTo(Mark.X);
        assertThat(status.getMark(1, 2)).isEqualTo(Mark.O);
        assertThat(status.getMark(2, 2)).isEqualTo(Mark.X);
    }

    @Test(description = "Verify board response time is acceptable")
    @Description("Test that GET /board responds within acceptable time limits")
    @Severity(SeverityLevel.NORMAL)
    public void testGetBoardResponseTime() {
        Response response = apiClient.getBoard();

        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateResponseTime(response, 2000);
    }

    @Test(description = "Verify getting board with API Key authentication")
    @Description("Test that GET /board works with API Key authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetBoardWithApiKey() {
        Response response = apiClient.getBoard();

        ResponseValidator.validateStatusCode(response, 200);
        assertThat(response.as(Status.class)).isNotNull();
    }

    @Test(description = "Verify board structure is valid")
    @Description("Test that the board structure conforms to expected format")
    @Severity(SeverityLevel.CRITICAL)
    public void testBoardStructureIsValid() {
        Response response = apiClient.getBoard();

        ResponseValidator.validateStatusCode(response, 200);
        
        response.then()
                .assertThat()
                .body("size()", org.hamcrest.Matchers.is(2))
                .body("containsKey('winner')", org.hamcrest.Matchers.is(true))
                .body("containsKey('board')", org.hamcrest.Matchers.is(true))
                .body("board.size()", org.hamcrest.Matchers.is(3));
    }

    @Test(description = "Verify game in progress state")
    @Description("Test that board correctly identifies game in progress")
    @Severity(SeverityLevel.NORMAL)
    public void testGameInProgressState() {
        // Place one mark
        apiClient.putSquare(1, 1, Mark.X);

        Status status = apiClient.getBoardAsStatus();

        assertThat(status.isInProgress()).isTrue();
        assertThat(status.hasWinner()).isFalse();
        assertThat(status.isDraw()).isFalse();
    }
}
