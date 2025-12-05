/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.test.restassured;

import com.tictactoe.test.config.ServiceConfig;
import com.tictactoe.test.constant.RequestPath;
import com.tictactoe.test.model.BoardStatus;
import com.tictactoe.test.model.Mark;
import com.tictactoe.test.model.MarkRequest;
import com.tictactoe.test.test.BaseApiTest;
import com.staf.api.core.model.ApiRequest;
import com.staf.api.core.model.ApiResponse;
import com.staf.api.core.model.Method;
import com.staf.api.core.model.ObjectMappingType;
import com.staf.api.core.serialization.impl.JacksonSerializationStrategy;
import com.staf.api.restassured.config.RestAssuredProgrammaticConfig;
import com.staf.api.restassured.core.RestAssuredClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test class for Tic Tac Toe API using RestAssured client.
 * Demonstrates RestAssured integration with STAF framework.
 */
@Epic("Tic Tac Toe API")
@Feature("RestAssured Client")
public class TicTacToeRestAssuredTest extends BaseApiTest {

    @BeforeClass
    public void beforeClass() {
        final RestAssuredProgrammaticConfig restAssuredConfig =
                new RestAssuredProgrammaticConfig();
        restAssuredConfig
                .setBaseUrl(ServiceConfig.BASE_URL)
                .setObjectMappingType(ObjectMappingType.JACKSON);
        iApiClient = new RestAssuredClient(restAssuredConfig);
        serializationStrategy = new JacksonSerializationStrategy();
    }

    @Test
    @Story("Board Retrieval")
    @Description("Verify board retrieval using RestAssured client")
    public void getBoardRestAssuredTest() {
        final ApiRequest<BoardStatus> getBoardRequest =
                ApiRequest.<BoardStatus>builder()
                        .responseBodyType(BoardStatus.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(RequestPath.GET_BOARD)
                        .headers(getDefaultHeaders())
                        .httpMethod(Method.GET)
                        .build();

        final ApiResponse<BoardStatus> getBoardResponse = iApiClient.sendRequest(getBoardRequest);
        
        rvs.verifyStatusCode(HttpStatus.SC_OK, getBoardResponse);
        rvs.verifyResponseBodyNotNull(getBoardResponse);
    }

    @Test
    @Story("Square Retrieval")
    @Description("Verify single square retrieval using RestAssured client")
    public void getSquareRestAssuredTest() {
        final int row = 2;
        final int column = 3;
        
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

    @Test
    @Story("Mark Placement")
    @Description("Verify mark placement using RestAssured client")
    public void putMarkRestAssuredTest() {
        final int row = 1;
        final int column = 1;
        final MarkRequest markRequest = MarkRequest.builder()
                .mark(Mark.X.getValue())
                .build();
        
        final String requestBody = serializationStrategy.serialize(markRequest);
        
        final ApiRequest<BoardStatus> putMarkRequest =
                ApiRequest.<BoardStatus>builder()
                        .responseBodyType(BoardStatus.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.PUT_SQUARE, row, column))
                        .headers(getBearerAuthHeaders())
                        .body(requestBody)
                        .httpMethod(Method.PUT)
                        .build();

        final ApiResponse<BoardStatus> putMarkResponse = iApiClient.sendRequest(putMarkRequest);
        
        rvs.verifyStatusCode(HttpStatus.SC_OK, putMarkResponse);
        rvs.verifyResponseBodyNotNull(putMarkResponse);
    }

    @Test
    @Story("Error Handling")
    @Description("Verify error handling for invalid coordinates using RestAssured")
    public void invalidCoordinatesRestAssuredTest() {
        final int row = 0;
        final int column = 0;
        
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

    @Test
    @Story("Game Flow")
    @Description("Verify sequential mark placement using RestAssured")
    public void sequentialMarkPlacementTest() {
        // Place X at (1,1)
        placeMarkAndVerify(1, 1, Mark.X);
        
        // Place O at (1,2)
        placeMarkAndVerify(1, 2, Mark.O);
        
        // Place X at (2,2)
        placeMarkAndVerify(2, 2, Mark.X);
    }

    private void placeMarkAndVerify(int row, int column, Mark mark) {
        final MarkRequest markRequest = MarkRequest.builder()
                .mark(mark.getValue())
                .build();
        
        final String requestBody = serializationStrategy.serialize(markRequest);
        
        final ApiRequest<BoardStatus> putMarkRequest =
                ApiRequest.<BoardStatus>builder()
                        .responseBodyType(BoardStatus.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.PUT_SQUARE, row, column))
                        .headers(getBearerAuthHeaders())
                        .body(requestBody)
                        .httpMethod(Method.PUT)
                        .build();

        final ApiResponse<BoardStatus> putMarkResponse = iApiClient.sendRequest(putMarkRequest);
        rvs.verifyStatusCode(HttpStatus.SC_OK, putMarkResponse);
    }
}
