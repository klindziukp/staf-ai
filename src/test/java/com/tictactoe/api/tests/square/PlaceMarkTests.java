package com.tictactoe.api.tests.square;

import com.tictactoe.api.base.BaseTest;
import com.tictactoe.api.models.Status;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for PUT /board/{row}/{column} endpoint.
 * Tests mark placement functionality.
 */
@Epic("Tic Tac Toe API")
@Feature("Square Management")
@Story("Place Mark on Board")
public class PlaceMarkTests extends BaseTest {

    @Test(description = "Verify successful mark placement")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test places a mark on empty square and validates the response")
    public void testPlaceMarkSuccess() {
        Response response = squareApiClient.placeMark(1, 1, "X");

        assertThat(response.getStatusCode())
                .as("Status code should be 200")
                .isEqualTo(200);

        Status status = response.as(Status.class);
        assertThat(status).isNotNull();
        assertThat(status.getBoard()).isNotNull();
        assertThat(status.getMark(1, 1))
                .as("Mark should be placed correctly")
                .isEqualTo("X");
    }

    @DataProvider(name = "validMarks")
    public Object[][] validMarks() {
        return new Object[][]{
                {"X"},
                {"O"}
        };
    }

    @Test(dataProvider = "validMarks", description = "Verify valid marks can be placed")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that both X and O marks can be placed")
    public void testPlaceValidMarks(String mark) {
        Response response = squareApiClient.placeMark(2, 2, mark);

        assertThat(response.getStatusCode())
                .as("Status code should be 200 for valid mark")
                .isEqualTo(200);
    }

    @DataProvider(name = "invalidMarks")
    public Object[][] invalidMarks() {
        return new Object[][]{
                {"A"}, {"Z"}, {"1"}, {"@"}, {""}
        };
    }

    @Test(dataProvider = "invalidMarks", description = "Verify invalid marks return 400")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that invalid marks return 400 Bad Request")
    public void testPlaceInvalidMarks(String mark) {
        Response response = squareApiClient.placeMark(1, 1, mark);

        assertThat(response.getStatusCode())
                .as("Status code should be 400 for invalid mark")
                .isEqualTo(400);

        assertThat(response.asString())
                .as("Error message should mention invalid mark")
                .containsIgnoringCase("invalid mark");
    }

    @Test(description = "Verify placing mark on occupied square returns error")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that placing mark on occupied square returns 400")
    public void testPlaceMarkOnOccupiedSquare() {
        // Place first mark
        squareApiClient.placeMark(1, 1, "X");

        // Try to place second mark on same square
        Response response = squareApiClient.placeMark(1, 1, "O");

        assertThat(response.getStatusCode())
                .as("Status code should be 400 for occupied square")
                .isEqualTo(400);

        assertThat(response.asString())
                .as("Error message should mention square not empty")
                .containsIgnoringCase("not empty");
    }

    @DataProvider(name = "invalidCoordinates")
    public Object[][] invalidCoordinates() {
        return new Object[][]{
                {0, 1}, {1, 0}, {4, 1}, {1, 4}
        };
    }

    @Test(dataProvider = "invalidCoordinates", description = "Verify invalid coordinates return 400")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that invalid coordinates return 400 Bad Request")
    public void testPlaceMarkInvalidCoordinates(int row, int column) {
        Response response = squareApiClient.placeMark(row, column, "X");

        assertThat(response.getStatusCode())
                .as("Status code should be 400 for invalid coordinates")
                .isEqualTo(400);

        assertThat(response.asString())
                .as("Error message should mention illegal coordinates")
                .containsIgnoringCase("illegal coordinates");
    }

    @Test(description = "Verify unauthorized access without authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that placing mark without authentication returns 401")
    public void testPlaceMarkWithoutAuth() {
        Response response = squareApiClient.placeMarkWithoutAuth(1, 1, "X");

        assertThat(response.getStatusCode())
                .as("Status code should be 401 for unauthorized access")
                .isIn(401, 403);
    }

    @Test(description = "Verify error message length is within limit")
    @Severity(SeverityLevel.MINOR)
    @Description("Test validates that error messages don't exceed 256 characters")
    public void testErrorMessageLength() {
        Response response = squareApiClient.placeMark(0, 0, "X");

        if (response.getStatusCode() == 400) {
            String errorMessage = response.asString();
            assertThat(errorMessage.length())
                    .as("Error message should not exceed 256 characters")
                    .isLessThanOrEqualTo(256);
        }
    }
}
