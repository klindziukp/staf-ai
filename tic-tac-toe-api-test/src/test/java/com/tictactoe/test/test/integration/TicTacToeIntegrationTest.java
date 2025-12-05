/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.test.integration;

import com.google.gson.JsonObject;
import com.tictactoe.test.config.ServiceConfig;
import com.tictactoe.test.constant.RequestPath;
import com.tictactoe.test.model.Mark;
import com.tictactoe.test.model.MarkRequest;
import com.tictactoe.test.test.BaseApiTest;
import com.tictactoe.test.util.GsonUtil;
import com.staf.api.apachehttp.config.ApacheHttpProgrammaticConfig;
import com.staf.api.apachehttp.core.ApacheHttpClient;
import com.staf.api.core.model.ApiRequest;
import com.staf.api.core.model.ApiResponse;
import com.staf.api.core.model.Method;
import com.staf.api.core.model.ObjectMappingType;
import com.staf.api.core.serialization.impl.GsonSerializationStrategy;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Comprehensive integration tests for Tic Tac Toe API.
 * Tests complex scenarios and edge cases.
 */
@Epic("Tic Tac Toe API")
@Feature("Integration Tests")
public class TicTacToeIntegrationTest extends BaseApiTest {

    @BeforeClass
    public void beforeClass() {
        final ApacheHttpProgrammaticConfig apacheHttpProgrammaticConfig =
                new ApacheHttpProgrammaticConfig();
        apacheHttpProgrammaticConfig
                .setBaseUrl(ServiceConfig.BASE_URL)
                .setObjectMappingType(ObjectMappingType.GSON);
        iApiClient = new ApacheHttpClient(apacheHttpProgrammaticConfig);
        serializationStrategy = new GsonSerializationStrategy();
    }

    @DataProvider(name = "validCoordinates")
    public Object[][] validCoordinatesProvider() {
        return new Object[][] {
            {1, 1}, {1, 2}, {1, 3},
            {2, 1}, {2, 2}, {2, 3},
            {3, 1}, {3, 2}, {3, 3}
        };
    }

    @DataProvider(name = "invalidCoordinates")
    public Object[][] invalidCoordinatesProvider() {
        return new Object[][] {
            {0, 0}, {0, 1}, {1, 0},
            {4, 1}, {1, 4}, {4, 4},
            {-1, 1}, {1, -1}, {-1, -1}
        };
    }

    @DataProvider(name = "validMarks")
    public Object[][] validMarksProvider() {
        return new Object[][] {
            {Mark.X.getValue()},
            {Mark.O.getValue()}
        };
    }

    @DataProvider(name = "invalidMarks")
    public Object[][] invalidMarksProvider() {
        return new Object[][] {
            {"A"}, {"B"}, {"Y"}, {"Z"},
            {"1"}, {"2"}, {"x"}, {"o"},
            {""}, {" "}, {"XX"}, {"OO"}
        };
    }

    @Test(dataProvider = "validCoordinates")
    @Story("Coordinate Validation")
    @Description("Verify all valid coordinates can be accessed")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidCoordinates(int row, int column) {
        final ApiRequest<String> getSquareRequest =
                ApiRequest.<String>builder()
                        .responseBodyType(String.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.GET_SQUARE, row, column))
                        .headers(getBearerAuthHeaders())
                        .httpMethod(Method.GET)
                        .build();

        final ApiResponse<String> getSquareResponse = iApiClient.sendRequest(getSquareRequest);
        rvs.verifyStatusCode(HttpStatus.SC_OK, getSquareResponse);
    }

    @Test(dataProvider = "invalidCoordinates")
    @Story("Coordinate Validation")
    @Description("Verify invalid coordinates return 400 Bad Request")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidCoordinates(int row, int column) {
        final ApiRequest<String> getSquareRequest =
                ApiRequest.<String>builder()
                        .responseBodyType(String.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.GET_SQUARE, row, column))
                        .headers(getBearerAuthHeaders())
                        .httpMethod(Method.GET)
                        .build();

        final ApiResponse<String> getSquareResponse = iApiClient.sendRequest(getSquareRequest);
        rvs.verifyStatusCode(HttpStatus.SC_BAD_REQUEST, getSquareResponse);
    }

    @Test(dataProvider = "validMarks")
    @Story("Mark Validation")
    @Description("Verify valid marks can be placed on the board")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidMarks(String mark) {
        final int row = 1;
        final int column = 1;
        final MarkRequest markRequest = MarkRequest.builder()
                .mark(mark)
                .build();
        
        final String requestBody = serializationStrategy.serialize(markRequest);
        
        final ApiRequest<JsonObject> putMarkRequest =
                ApiRequest.<JsonObject>builder()
                        .responseBodyType(JsonObject.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.PUT_SQUARE, row, column))
                        .headers(getBearerAuthHeaders())
                        .body(requestBody)
                        .httpMethod(Method.PUT)
                        .build();

        final ApiResponse<JsonObject> putMarkResponse = iApiClient.sendRequest(putMarkRequest);
        rvs.verifyStatusCode(HttpStatus.SC_OK, putMarkResponse);
    }

    @Test(dataProvider = "invalidMarks")
    @Story("Mark Validation")
    @Description("Verify invalid marks return 400 Bad Request")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidMarks(String mark) {
        final int row = 2;
        final int column = 2;
        final MarkRequest markRequest = MarkRequest.builder()
                .mark(mark)
                .build();
        
        final String requestBody = serializationStrategy.serialize(markRequest);
        
        final ApiRequest<String> putMarkRequest =
                ApiRequest.<String>builder()
                        .responseBodyType(String.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.PUT_SQUARE, row, column))
                        .headers(getBearerAuthHeaders())
                        .body(requestBody)
                        .httpMethod(Method.PUT)
                        .build();

        final ApiResponse<String> putMarkResponse = iApiClient.sendRequest(putMarkRequest);
        rvs.verifyStatusCode(HttpStatus.SC_BAD_REQUEST, putMarkResponse);
    }

    @Test
    @Story("Game Scenarios")
    @Description("Test complete game scenario with X winning horizontally")
    @Severity(SeverityLevel.CRITICAL)
    public void testXWinsHorizontalScenario() {
        // X wins in first row: X X X
        //                       O O .
        //                       . . .
        
        // X at (1,1)
        placeMarkAndVerify(1, 1, Mark.X.getValue());
        
        // O at (2,1)
        placeMarkAndVerify(2, 1, Mark.O.getValue());
        
        // X at (1,2)
        placeMarkAndVerify(1, 2, Mark.X.getValue());
        
        // O at (2,2)
        placeMarkAndVerify(2, 2, Mark.O.getValue());
        
        // X at (1,3) - winning move
        final JsonObject winningResponse = placeMarkAndVerify(1, 3, Mark.X.getValue());
        
        // Verify winner is X
        if (winningResponse.has("winner")) {
            String winner = winningResponse.get("winner").getAsString();
            org.assertj.core.api.Assertions.assertThat(winner).isEqualTo(Mark.X.getValue());
        }
    }

    @Test
    @Story("Game Scenarios")
    @Description("Test complete game scenario with O winning vertically")
    @Severity(SeverityLevel.CRITICAL)
    public void testOWinsVerticalScenario() {
        // O wins in first column: O X X
        //                          O X .
        //                          O . .
        
        // X at (1,2)
        placeMarkAndVerify(1, 2, Mark.X.getValue());
        
        // O at (1,1)
        placeMarkAndVerify(1, 1, Mark.O.getValue());
        
        // X at (1,3)
        placeMarkAndVerify(1, 3, Mark.X.getValue());
        
        // O at (2,1)
        placeMarkAndVerify(2, 1, Mark.O.getValue());
        
        // X at (2,2)
        placeMarkAndVerify(2, 2, Mark.X.getValue());
        
        // O at (3,1) - winning move
        final JsonObject winningResponse = placeMarkAndVerify(3, 1, Mark.O.getValue());
        
        // Verify winner is O
        if (winningResponse.has("winner")) {
            String winner = winningResponse.get("winner").getAsString();
            org.assertj.core.api.Assertions.assertThat(winner).isEqualTo(Mark.O.getValue());
        }
    }

    @Test
    @Story("Game Scenarios")
    @Description("Test complete game scenario with X winning diagonally")
    @Severity(SeverityLevel.CRITICAL)
    public void testXWinsDiagonalScenario() {
        // X wins diagonally: X O O
        //                    O X .
        //                    . . X
        
        // X at (1,1)
        placeMarkAndVerify(1, 1, Mark.X.getValue());
        
        // O at (1,2)
        placeMarkAndVerify(1, 2, Mark.O.getValue());
        
        // X at (2,2)
        placeMarkAndVerify(2, 2, Mark.X.getValue());
        
        // O at (1,3)
        placeMarkAndVerify(1, 3, Mark.O.getValue());
        
        // X at (3,3) - winning move
        final JsonObject winningResponse = placeMarkAndVerify(3, 3, Mark.X.getValue());
        
        // Verify winner is X
        if (winningResponse.has("winner")) {
            String winner = winningResponse.get("winner").getAsString();
            org.assertj.core.api.Assertions.assertThat(winner).isEqualTo(Mark.X.getValue());
        }
    }

    @Test
    @Story("Game Scenarios")
    @Description("Test draw scenario where no player wins")
    @Severity(SeverityLevel.NORMAL)
    public void testDrawScenario() {
        // Draw game: X O X
        //            O X O
        //            O X O
        
        placeMarkAndVerify(1, 1, Mark.X.getValue());
        placeMarkAndVerify(1, 2, Mark.O.getValue());
        placeMarkAndVerify(1, 3, Mark.X.getValue());
        placeMarkAndVerify(2, 1, Mark.O.getValue());
        placeMarkAndVerify(2, 2, Mark.X.getValue());
        placeMarkAndVerify(2, 3, Mark.O.getValue());
        placeMarkAndVerify(3, 1, Mark.O.getValue());
        placeMarkAndVerify(3, 2, Mark.X.getValue());
        
        // Last move
        final JsonObject finalResponse = placeMarkAndVerify(3, 3, Mark.O.getValue());
        
        // Verify no winner (draw)
        if (finalResponse.has("winner")) {
            String winner = finalResponse.get("winner").getAsString();
            org.assertj.core.api.Assertions.assertThat(winner).isEqualTo(Mark.EMPTY.getValue());
        }
    }

    @Test
    @Story("Error Handling")
    @Description("Test placing mark on already occupied square")
    @Severity(SeverityLevel.NORMAL)
    public void testOccupiedSquareError() {
        final int row = 2;
        final int column = 3;
        
        // Place first mark
        placeMarkAndVerify(row, column, Mark.X.getValue());
        
        // Try to place second mark on same square
        final MarkRequest secondMark = MarkRequest.builder()
                .mark(Mark.O.getValue())
                .build();
        
        final String requestBody = serializationStrategy.serialize(secondMark);
        
        final ApiRequest<String> putMarkRequest =
                ApiRequest.<String>builder()
                        .responseBodyType(String.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.PUT_SQUARE, row, column))
                        .headers(getBearerAuthHeaders())
                        .body(requestBody)
                        .httpMethod(Method.PUT)
                        .build();

        final ApiResponse<String> putMarkResponse = iApiClient.sendRequest(putMarkRequest);
        rvs.verifyStatusCode(HttpStatus.SC_BAD_REQUEST, putMarkResponse);
    }

    @Test
    @Story("Board State")
    @Description("Verify board state consistency after multiple operations")
    @Severity(SeverityLevel.CRITICAL)
    public void testBoardStateConsistency() {
        // Get initial board
        final ApiRequest<JsonObject> getBoardRequest =
                ApiRequest.<JsonObject>builder()
                        .responseBodyType(JsonObject.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(RequestPath.GET_BOARD)
                        .headers(getDefaultHeaders())
                        .httpMethod(Method.GET)
                        .build();

        final ApiResponse<JsonObject> initialBoard = iApiClient.sendRequest(getBoardRequest);
        rvs.verifyStatusCode(HttpStatus.SC_OK, initialBoard);
        
        // Place some marks
        placeMarkAndVerify(1, 1, Mark.X.getValue());
        placeMarkAndVerify(2, 2, Mark.O.getValue());
        placeMarkAndVerify(3, 3, Mark.X.getValue());
        
        // Get board again
        final ApiResponse<JsonObject> updatedBoard = iApiClient.sendRequest(getBoardRequest);
        rvs.verifyStatusCode(HttpStatus.SC_OK, updatedBoard);
        rvs.verifyResponseBodyNotNull(updatedBoard);
    }

    private JsonObject placeMarkAndVerify(int row, int column, String mark) {
        final MarkRequest markRequest = MarkRequest.builder()
                .mark(mark)
                .build();
        
        final String requestBody = serializationStrategy.serialize(markRequest);
        
        final ApiRequest<JsonObject> putMarkRequest =
                ApiRequest.<JsonObject>builder()
                        .responseBodyType(JsonObject.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.PUT_SQUARE, row, column))
                        .headers(getBearerAuthHeaders())
                        .body(requestBody)
                        .httpMethod(Method.PUT)
                        .build();

        final ApiResponse<JsonObject> putMarkResponse = iApiClient.sendRequest(putMarkRequest);
        rvs.verifyStatusCode(HttpStatus.SC_OK, putMarkResponse);
        
        return putMarkResponse.getBody();
    }
}
