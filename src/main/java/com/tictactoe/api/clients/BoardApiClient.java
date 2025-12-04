package com.tictactoe.api.clients;

import com.tictactoe.api.models.Status;
import com.tictactoe.api.utils.AuthenticationHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;

/**
 * API client for board-related endpoints.
 * Handles GET /board operations.
 */
@Slf4j
public class BoardApiClient {
    private static final String BOARD_ENDPOINT = "/board";

    /**
     * Retrieves the current board state.
     *
     * @return Response object
     */
    @Step("Get board state")
    public Response getBoard() {
        log.info("Getting board state");
        return given()
                .spec(AuthenticationHelper.getAuthenticatedSpec())
                .when()
                .get(BOARD_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * Retrieves the current board state with custom authentication.
     *
     * @param apiKey API key for authentication
     * @return Response object
     */
    @Step("Get board state with API key: {apiKey}")
    public Response getBoardWithApiKey(String apiKey) {
        log.info("Getting board state with API key");
        return given()
                .header("api-key", apiKey)
                .contentType("application/json")
                .when()
                .get(BOARD_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * Retrieves the current board state without authentication.
     *
     * @return Response object
     */
    @Step("Get board state without authentication")
    public Response getBoardWithoutAuth() {
        log.info("Getting board state without authentication");
        return given()
                .contentType("application/json")
                .when()
                .get(BOARD_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * Retrieves and deserializes the board state.
     *
     * @return Status object
     */
    @Step("Get board status")
    public Status getBoardStatus() {
        log.info("Getting board status");
        return getBoard()
                .then()
                .statusCode(200)
                .extract()
                .as(Status.class);
    }
}
