package com.tictactoe.api.tests.board;

import com.tictactoe.api.models.Mark;
import com.tictactoe.api.models.Status;
import com.tictactoe.api.tests.base.BaseTest;
import com.tictactoe.api.utils.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for PUT /board/{row}/{column} endpoint.
 */
@Epic("Tic Tac Toe API")
@Feature("Board Management")
@Story("Place Mark")
public class PutSquareTests extends BaseTest {

    @Test(description = "Verify placing X mark on empty square")
    @Description("Test that PUT /board/{row}/{column} successfully places X mark")
    @Severity(SeverityLevel.CRITICAL)
    public void testPlaceXOnEmptySquare() {
        Response response = apiClient.putSquare(1, 1, Mark.X);

        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateContentType(response, "application/json");

        Status status = response.as(Status.class);
        assertThat(status).isNotNull();
        assertThat(status.getMark(1, 1)).isEqualTo(Mark.X);
    }

    @Test(description = "Verify placing O mark on empty square")
    @Description("Test that PUT /board/{row}/{column} successfully places O mark")
    @Severity(SeverityLevel.CRITICAL)
    public void testPlaceOOnEmptySquare() {
        Response response = apiClient.putSquare(2, 2, Mark.O);

        ResponseValidator.validateStatusCode(response, 200);

        Status status = response.as(Status.class);
        assertThat(status).isNotNull();
        assertThat(status.getMark(2, 2)).isEqualTo(Mark.O);
    }

    @Test(description = "Verify placing mark on occupied square returns error")
    @Description("Test that PUT /board/{row}/{column} returns 400 when square is occupied")
    @Severity(SeverityLevel.CRITICAL)
    public void testPlaceMarkOnOccupiedSquare() {
        // Place first mark
        apiClient.putSquare(1, 1, Mark.X);

        // Try to place another mark on the same square
        Response response = apiClient.putSquare(1, 1, Mark.O);

        ResponseValidator.validateStatusCode(response, 400);
        
        String errorMessage = response.asString();
        assertThat(errorMessage)
                .as("Error message should indicate square is not empty")
                .containsIgnoringCase("not empty");
    }

    @DataProvider(name = "invalidMarks")
    public Object[][] invalidMarks() {
        return new Object[][] {
            {"\"Z\"", "Invalid letter"},
            {"\"\"", "Empty string"},
            {"\"XX\"", "Too long"},
            {"\"x\"", "Lowercase x"},
            {"\"o\"", "Lowercase o"},
            {"123", "Number"},
            {"null", "Null value"}
        };
    }

    @Test(dataProvider = "invalidMarks", description = "Verify invalid mark values return error")
    @Description("Test that PUT /board/{row}/{column} returns 400 for invalid marks")
    @Severity(SeverityLevel.CRITICAL)
    public void testPlaceInvalidMark(String invalidMark, String description) {
        Response response = apiClient.putSquareWithInvalidData(2, 2, invalidMark);

        ResponseValidator.validateStatusCode(response, 400);
        
        String errorMessage = response.asString();
        assertThat(errorMessage)
                .as("Error message should indicate invalid mark for: " + description)
                .containsIgnoringCase("invalid");
    }

    @DataProvider(name = "invalidCoordinatesForPut")
    public Object[][] invalidCoordinatesForPut() {
        return new Object[][] {
            {"0", "1", Mark.X},
            {"1", "0", Mark.O},
            {"4", "2", Mark.X},
            {"2", "4", Mark.O},
            {"-1", "2", Mark.X},
            {"2", "-1", Mark.O}
        };
    }

    @Test(dataProvider = "invalidCoordinatesForPut", description = "Verify invalid coordinates return error")
    @Description("Test that PUT /board/{row}/{column} returns 400 for invalid coordinates")
    @Severity(SeverityLevel.CRITICAL)
    public void testPlaceMarkWithInvalidCoordinates(String row, String column, Mark mark) {
        Response response = apiClient.putSquareWithInvalidCoordinates(row, column, mark);

        ResponseValidator.validateStatusCode(response, 400);
        
        String errorMessage = response.asString();
        assertThat(errorMessage)
                .as("Error message should indicate illegal coordinates")
                .containsIgnoringCase("illegal coordinates");
    }

    @Test(description = "Verify placing multiple marks updates board correctly")
    @Description("Test that multiple PUT operations update the board state correctly")
    @Severity(SeverityLevel.CRITICAL)
    public void testPlaceMultipleMarks() {
        apiClient.putSquare(1, 1, Mark.X);
        apiClient.putSquare(1, 2, Mark.O);
        apiClient.putSquare(2, 1, Mark.X);

        Status status = apiClient.getBoardAsStatus();

        assertThat(status.getMark(1, 1)).isEqualTo(Mark.X);
        assertThat(status.getMark(1, 2)).isEqualTo(Mark.O);
        assertThat(status.getMark(2, 1)).isEqualTo(Mark.X);
    }

    @Test(description = "Verify response includes updated board state")
    @Description("Test that PUT response contains the complete updated board")
    @Severity(SeverityLevel.NORMAL)
    public void testPutResponseIncludesBoard() {
        Response response = apiClient.putSquare(2, 2, Mark.X);

        ResponseValidator.validateStatusCode(response, 200);

        Status status = response.as(Status.class);
        assertThat(status.getBoard()).isNotNull();
        assertThat(status.getBoard()).hasSize(3);
        assertThat(status.getWinner()).isNotNull();
    }

    @DataProvider(name = "allValidPositions")
    public Object[][] allValidPositions() {
        Object[][] data = new Object[9][3];
        int index = 0;
        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 3; col++) {
                data[index++] = new Object[]{row, col, index % 2 == 0 ? Mark.X : Mark.O};
            }
        }
        return data;
    }

    @Test(dataProvider = "allValidPositions", description = "Verify all board positions can accept marks")
    @Description("Test that marks can be placed on all valid board positions")
    @Severity(SeverityLevel.NORMAL)
    public void testPlaceMarkOnAllPositions(int row, int column, Mark mark) {
        Response response = apiClient.putSquare(row, column, mark);

        ResponseValidator.validateStatusCode(response, 200);
        
        Status status = response.as(Status.class);
        assertThat(status.getMark(row, column)).isEqualTo(mark);
    }

    @Test(description = "Verify response time for placing mark")
    @Description("Test that PUT /board/{row}/{column} responds within acceptable time")
    @Severity(SeverityLevel.NORMAL)
    public void testPutSquareResponseTime() {
        Response response = apiClient.putSquare(1, 1, Mark.X);

        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateResponseTime(response, 2000);
    }
}
