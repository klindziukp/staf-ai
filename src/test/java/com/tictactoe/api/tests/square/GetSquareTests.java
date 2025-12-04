package com.tictactoe.api.tests.square;

import com.tictactoe.api.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for GET /board/{row}/{column} endpoint.
 * Tests single square retrieval functionality.
 */
@Epic("Tic Tac Toe API")
@Feature("Square Management")
@Story("Get Single Square")
public class GetSquareTests extends BaseTest {

    @Test(description = "Verify successful square retrieval")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test retrieves a single square and validates the response")
    public void testGetSquareSuccess() {
        Response response = squareApiClient.getSquare(1, 1);

        assertThat(response.getStatusCode())
                .as("Status code should be 200")
                .isEqualTo(200);

        String mark = response.asString().replace("\"", "");
        assertThat(mark)
                .as("Mark should be valid")
                .matches("^[.XO]$");
    }

    @DataProvider(name = "validCoordinates")
    public Object[][] validCoordinates() {
        return new Object[][]{
                {1, 1}, {1, 2}, {1, 3},
                {2, 1}, {2, 2}, {2, 3},
                {3, 1}, {3, 2}, {3, 3}
        };
    }

    @Test(dataProvider = "validCoordinates", description = "Verify all valid coordinates work")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that all valid board positions can be retrieved")
    public void testGetSquareValidCoordinates(int row, int column) {
        Response response = squareApiClient.getSquare(row, column);

        assertThat(response.getStatusCode())
                .as("Status code should be 200 for valid coordinates")
                .isEqualTo(200);
    }

    @DataProvider(name = "invalidCoordinates")
    public Object[][] invalidCoordinates() {
        return new Object[][]{
                {0, 1}, {1, 0}, {0, 0},
                {4, 1}, {1, 4}, {4, 4},
                {-1, 1}, {1, -1}
        };
    }

    @Test(dataProvider = "invalidCoordinates", description = "Verify invalid coordinates return 400")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that invalid coordinates return 400 Bad Request")
    public void testGetSquareInvalidCoordinates(int row, int column) {
        Response response = squareApiClient.getSquare(row, column);

        assertThat(response.getStatusCode())
                .as("Status code should be 400 for invalid coordinates")
                .isEqualTo(400);

        assertThat(response.asString())
                .as("Error message should mention illegal coordinates")
                .containsIgnoringCase("illegal coordinates");
    }

    @Test(description = "Verify unauthorized access without authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that accessing square without authentication returns 401")
    public void testGetSquareWithoutAuth() {
        Response response = squareApiClient.getSquareWithoutAuth(1, 1);

        assertThat(response.getStatusCode())
                .as("Status code should be 401 for unauthorized access")
                .isIn(401, 403);
    }

    @Test(description = "Verify boundary values for coordinates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test validates boundary values (1 and 3) for coordinates")
    public void testGetSquareBoundaryValues() {
        // Test lower boundary
        Response response1 = squareApiClient.getSquare(1, 1);
        assertThat(response1.getStatusCode()).isEqualTo(200);

        // Test upper boundary
        Response response2 = squareApiClient.getSquare(3, 3);
        assertThat(response2.getStatusCode()).isEqualTo(200);
    }
}
