package com.tictactoe.api.tests.board;

import com.tictactoe.api.models.Mark;
import com.tictactoe.api.tests.base.BaseTest;
import com.tictactoe.api.utils.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for GET /board/{row}/{column} endpoint.
 */
@Epic("Tic Tac Toe API")
@Feature("Board Management")
@Story("Get Square")
public class GetSquareTests extends BaseTest {

    @Test(description = "Verify getting empty square returns empty mark")
    @Description("Test that GET /board/{row}/{column} returns '.' for empty square")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetEmptySquare() {
        Response response = apiClient.getSquare(1, 1);

        ResponseValidator.validateStatusCode(response, 200);
        
        String mark = response.asString().replace("\"", "");
        assertThat(mark).isEqualTo(".");
    }

    @Test(description = "Verify getting square with X mark")
    @Description("Test that GET /board/{row}/{column} returns 'X' after placing X")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetSquareWithX() {
        apiClient.putSquare(2, 2, Mark.X);
        
        Response response = apiClient.getSquare(2, 2);

        ResponseValidator.validateStatusCode(response, 200);
        
        String mark = response.asString().replace("\"", "");
        assertThat(mark).isEqualTo("X");
    }

    @Test(description = "Verify getting square with O mark")
    @Description("Test that GET /board/{row}/{column} returns 'O' after placing O")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetSquareWithO() {
        apiClient.putSquare(3, 3, Mark.O);
        
        Response response = apiClient.getSquare(3, 3);

        ResponseValidator.validateStatusCode(response, 200);
        
        String mark = response.asString().replace("\"", "");
        assertThat(mark).isEqualTo("O");
    }

    @DataProvider(name = "validCoordinates")
    public Object[][] validCoordinates() {
        return new Object[][] {
            {1, 1}, {1, 2}, {1, 3},
            {2, 1}, {2, 2}, {2, 3},
            {3, 1}, {3, 2}, {3, 3}
        };
    }

    @Test(dataProvider = "validCoordinates", description = "Verify all valid coordinates are accessible")
    @Description("Test that all valid board positions can be retrieved")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetSquareWithValidCoordinates(int row, int column) {
        Response response = apiClient.getSquare(row, column);

        ResponseValidator.validateStatusCode(response, 200);
        assertThat(response.asString()).isNotEmpty();
    }

    @DataProvider(name = "invalidCoordinates")
    public Object[][] invalidCoordinates() {
        return new Object[][] {
            {"0", "1"},
            {"1", "0"},
            {"4", "2"},
            {"2", "4"},
            {"-1", "2"},
            {"2", "-1"},
            {"a", "1"},
            {"1", "b"}
        };
    }

    @Test(dataProvider = "invalidCoordinates", description = "Verify invalid coordinates return 400 error")
    @Description("Test that GET /board/{row}/{column} returns 400 for invalid coordinates")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetSquareWithInvalidCoordinates(String row, String column) {
        Response response = apiClient.getSquareWithInvalidCoordinates(row, column);

        ResponseValidator.validateStatusCode(response, 400);
        
        String errorMessage = response.asString();
        assertThat(errorMessage)
                .as("Error message should indicate illegal coordinates")
                .containsIgnoringCase("illegal coordinates");
    }

    @DataProvider(name = "boundaryCoordinates")
    public Object[][] boundaryCoordinates() {
        return new Object[][] {
            {1, 1, "Lower boundary"},
            {3, 3, "Upper boundary"},
            {1, 3, "Lower row, upper column"},
            {3, 1, "Upper row, lower column"}
        };
    }

    @Test(dataProvider = "boundaryCoordinates", description = "Verify boundary coordinates work correctly")
    @Description("Test boundary value coordinates")
    @Severity(SeverityLevel.NORMAL)
    public void testGetSquareBoundaryValues(int row, int column, String description) {
        Response response = apiClient.getSquare(row, column);

        ResponseValidator.validateStatusCode(response, 200);
        assertThat(response.asString()).isNotEmpty();
    }

    @Test(description = "Verify response time for getting square")
    @Description("Test that GET /board/{row}/{column} responds within acceptable time")
    @Severity(SeverityLevel.NORMAL)
    public void testGetSquareResponseTime() {
        Response response = apiClient.getSquare(2, 2);

        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateResponseTime(response, 1000);
    }
}
