/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.test.apachehttp;

import com.google.gson.JsonObject;
import com.tictactoe.test.config.ServiceConfig;
import com.tictactoe.test.constant.RequestPath;
import com.tictactoe.test.model.BoardStatus;
import com.tictactoe.test.model.Mark;
import com.tictactoe.test.model.MarkRequest;
import com.tictactoe.test.storage.BoardDataStorage;
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
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test class for Tic Tac Toe API using Apache HTTP client.
 * Tests all gameplay endpoints including board retrieval and mark placement.
 */
@Epic("Tic Tac Toe API")
@Feature("Apache HTTP Client")
public class TicTacToeApacheHttpTest extends BaseApiTest {

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

    @Test
    @Story("Board Retrieval")
    @Description("Verify that the entire board state can be retrieved successfully")
    public void getBoardTest() {
        final ApiRequest<JsonObject> getBoardRequest =
                ApiRequest.<JsonObject>builder()
                        .responseBodyType(JsonObject.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(RequestPath.GET_BOARD)
                        .headers(getDefaultHeaders())
                        .httpMethod(Method.GET)
                        .build();

        final ApiResponse<JsonObject> getBoardResponse = iApiClient.sendRequest(getBoardRequest);
        
        rvs.verifyStatusCode(HttpStatus.SC_OK, getBoardResponse);
        rvs.verifyResponseBodyNotNull(getBoardResponse);
    }

    @Test
    @Story("Square Retrieval")
    @Description("Verify that a single board square can be retrieved successfully")
    public void getSquareTest() {
        final int row = 1;
        final int column = 1;
        
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
        rvs.verifyResponseBodyNotNull(getSquareResponse);
    }

    @Test
    @Story("Square Retrieval")
    @Description("Verify that invalid coordinates return 400 Bad Request")
    public void getSquareWithInvalidCoordinatesTest() {
        final int row = 0;
        final int column = 4;
        
        final ApiRequest<String> getSquareRequest =
                ApiRequest.<String>builder()
                        .responseBodyType(String.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.GET_SQUARE, row, column))
                        .headers(getBearerAuthHeaders())
                        .httpMethod(Method.GET)
                        .build();

        final ApiResponse<String> getSquareResponse = iApiClient.sendRequest(getSquareRequest);
        
        rvs.verifyResponseContains(HttpStatus.SC_BAD_REQUEST, getSquareResponse, "Illegal coordinates");
    }

    @Test
    @Story("Mark Placement")
    @Description("Verify that a mark can be placed on an empty square successfully")
    public void putMarkOnEmptySquareTest() {
        final int row = 2;
        final int column = 2;
        final MarkRequest markRequest = MarkRequest.builder()
                .mark(Mark.X.getValue())
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
        rvs.verifyResponseBodyNotNull(putMarkResponse);
    }

    @Test
    @Story("Mark Placement")
    @Description("Verify that placing a mark with invalid coordinates returns 400 Bad Request")
    public void putMarkWithInvalidCoordinatesTest() {
        final int row = 5;
        final int column = 5;
        final MarkRequest markRequest = MarkRequest.builder()
                .mark(Mark.O.getValue())
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
        
        rvs.verifyResponseContains(HttpStatus.SC_BAD_REQUEST, putMarkResponse, "Illegal coordinates");
    }

    @Test
    @Story("Mark Placement")
    @Description("Verify that placing an invalid mark returns 400 Bad Request")
    public void putInvalidMarkTest() {
        final int row = 1;
        final int column = 3;
        final MarkRequest markRequest = MarkRequest.builder()
                .mark("Z")
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
        
        rvs.verifyResponseContains(HttpStatus.SC_BAD_REQUEST, putMarkResponse, "Invalid Mark");
    }

    @Test
    @Story("Game Flow")
    @Description("Verify complete game flow: get board, place marks, check winner")
    public void completeGameFlowTest() {
        // Get initial board state
        final ApiRequest<JsonObject> getBoardRequest =
                ApiRequest.<JsonObject>builder()
                        .responseBodyType(JsonObject.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(RequestPath.GET_BOARD)
                        .headers(getDefaultHeaders())
                        .httpMethod(Method.GET)
                        .build();

        final ApiResponse<JsonObject> initialBoardResponse = iApiClient.sendRequest(getBoardRequest);
        rvs.verifyStatusCode(HttpStatus.SC_OK, initialBoardResponse);

        // Place first mark (X)
        final MarkRequest xMark = MarkRequest.builder().mark(Mark.X.getValue()).build();
        final String xMarkBody = serializationStrategy.serialize(xMark);
        
        final ApiRequest<JsonObject> putXMarkRequest =
                ApiRequest.<JsonObject>builder()
                        .responseBodyType(JsonObject.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.PUT_SQUARE, 1, 1))
                        .headers(getBearerAuthHeaders())
                        .body(xMarkBody)
                        .httpMethod(Method.PUT)
                        .build();

        final ApiResponse<JsonObject> xMarkResponse = iApiClient.sendRequest(putXMarkRequest);
        rvs.verifyStatusCode(HttpStatus.SC_OK, xMarkResponse);

        // Place second mark (O)
        final MarkRequest oMark = MarkRequest.builder().mark(Mark.O.getValue()).build();
        final String oMarkBody = serializationStrategy.serialize(oMark);
        
        final ApiRequest<JsonObject> putOMarkRequest =
                ApiRequest.<JsonObject>builder()
                        .responseBodyType(JsonObject.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.PUT_SQUARE, 1, 2))
                        .headers(getBearerAuthHeaders())
                        .body(oMarkBody)
                        .httpMethod(Method.PUT)
                        .build();

        final ApiResponse<JsonObject> oMarkResponse = iApiClient.sendRequest(putOMarkRequest);
        rvs.verifyStatusCode(HttpStatus.SC_OK, oMarkResponse);

        // Get final board state
        final ApiResponse<JsonObject> finalBoardResponse = iApiClient.sendRequest(getBoardRequest);
        rvs.verifyStatusCode(HttpStatus.SC_OK, finalBoardResponse);
        rvs.verifyResponseBodyNotNull(finalBoardResponse);
    }

    @Test
    @Story("Board Validation")
    @Description("Verify that all valid coordinates can be accessed")
    public void validateAllCoordinatesTest() {
        for (int row = 1; row <= 3; row++) {
            for (int column = 1; column <= 3; column++) {
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
        }
    }
}
