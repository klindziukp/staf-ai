/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.test.retrofit;

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
import com.staf.api.core.serialization.impl.GsonSerializationStrategy;
import com.staf.api.retrofit.config.RetrofitProgrammaticConfig;
import com.staf.api.retrofit.core.RetrofitClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test class for Tic Tac Toe API using Retrofit client.
 * Demonstrates Retrofit integration with STAF framework.
 */
@Epic("Tic Tac Toe API")
@Feature("Retrofit Client")
public class TicTacToeRetrofitTest extends BaseApiTest {

    @BeforeClass
    public void beforeClass() {
        final RetrofitProgrammaticConfig retrofitConfig =
                new RetrofitProgrammaticConfig();
        retrofitConfig
                .setBaseUrl(ServiceConfig.BASE_URL)
                .setObjectMappingType(ObjectMappingType.GSON);
        iApiClient = new RetrofitClient(retrofitConfig);
        serializationStrategy = new GsonSerializationStrategy();
    }

    @Test
    @Story("Board Retrieval")
    @Description("Verify board retrieval using Retrofit client")
    public void getBoardRetrofitTest() {
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
    @Description("Verify single square retrieval using Retrofit client")
    public void getSquareRetrofitTest() {
        final int row = 3;
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
    }

    @Test
    @Story("Mark Placement")
    @Description("Verify mark placement using Retrofit client")
    public void putMarkRetrofitTest() {
        final int row = 2;
        final int column = 1;
        final MarkRequest markRequest = MarkRequest.builder()
                .mark(Mark.O.getValue())
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
    @Description("Verify error handling for occupied square using Retrofit")
    public void putMarkOnOccupiedSquareTest() {
        final int row = 1;
        final int column = 1;
        
        // Place first mark
        final MarkRequest firstMark = MarkRequest.builder()
                .mark(Mark.X.getValue())
                .build();
        
        final String firstBody = serializationStrategy.serialize(firstMark);
        
        final ApiRequest<BoardStatus> firstRequest =
                ApiRequest.<BoardStatus>builder()
                        .responseBodyType(BoardStatus.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.PUT_SQUARE, row, column))
                        .headers(getBearerAuthHeaders())
                        .body(firstBody)
                        .httpMethod(Method.PUT)
                        .build();

        iApiClient.sendRequest(firstRequest);
        
        // Try to place second mark on same square
        final MarkRequest secondMark = MarkRequest.builder()
                .mark(Mark.O.getValue())
                .build();
        
        final String secondBody = serializationStrategy.serialize(secondMark);
        
        final ApiRequest<String> secondRequest =
                ApiRequest.<String>builder()
                        .responseBodyType(String.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.PUT_SQUARE, row, column))
                        .headers(getBearerAuthHeaders())
                        .body(secondBody)
                        .httpMethod(Method.PUT)
                        .build();

        final ApiResponse<String> secondResponse = iApiClient.sendRequest(secondRequest);
        
        rvs.verifyResponseContains(HttpStatus.SC_BAD_REQUEST, secondResponse, "not empty");
    }

    @Test
    @Story("Game Flow")
    @Description("Verify winning condition detection using Retrofit")
    public void winningConditionTest() {
        // Place marks to create a winning condition for X
        // X at (1,1)
        placeMarkAndVerify(1, 1, Mark.X);
        
        // O at (2,1)
        placeMarkAndVerify(2, 1, Mark.O);
        
        // X at (1,2)
        placeMarkAndVerify(1, 2, Mark.X);
        
        // O at (2,2)
        placeMarkAndVerify(2, 2, Mark.O);
        
        // X at (1,3) - winning move
        final MarkRequest winningMark = MarkRequest.builder()
                .mark(Mark.X.getValue())
                .build();
        
        final String requestBody = serializationStrategy.serialize(winningMark);
        
        final ApiRequest<BoardStatus> putMarkRequest =
                ApiRequest.<BoardStatus>builder()
                        .responseBodyType(BoardStatus.class)
                        .baseUrl(ServiceConfig.BASE_URL)
                        .path(String.format(RequestPath.PUT_SQUARE, 1, 3))
                        .headers(getBearerAuthHeaders())
                        .body(requestBody)
                        .httpMethod(Method.PUT)
                        .build();

        final ApiResponse<BoardStatus> putMarkResponse = iApiClient.sendRequest(putMarkRequest);
        
        rvs.verifyStatusCode(HttpStatus.SC_OK, putMarkResponse);
        // Winner should be X
        rvs.verifyResponseBodyNotNull(putMarkResponse);
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
