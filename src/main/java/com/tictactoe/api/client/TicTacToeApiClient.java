package com.tictactoe.api.client;

import com.tictactoe.api.models.Mark;
import com.tictactoe.api.models.Status;
import com.tictactoe.api.utils.RequestSpecificationBuilder;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;

/**
 * API client for Tic Tac Toe game operations.
 * Provides methods for interacting with the Tic Tac Toe API endpoints.
 */
@Slf4j
public class TicTacToeApiClient {
    private static final String BOARD_ENDPOINT = "/board";
    private static final String SQUARE_ENDPOINT = "/board/{row}/{column}";

    private final RequestSpecification requestSpec;

    /**
     * Creates a new TicTacToeApiClient with the specified request specification.
     *
     * @param requestSpec the request specification to use
     */
    public TicTacToeApiClient(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    /**
     * Creates a new TicTacToeApiClient with API Key authentication.
     *
     * @return a new client instance
     */
    public static TicTacToeApiClient withApiKey() {
        RequestSpecification spec = new RequestSpecificationBuilder()
                .withApiKeyAuth()
                .build();
        return new TicTacToeApiClient(spec);
    }

    /**
     * Creates a new TicTacToeApiClient with Bearer token authentication.
     *
     * @return a new client instance
     */
    public static TicTacToeApiClient withBearerToken() {
        RequestSpecification spec = new RequestSpecificationBuilder()
                .withBearerAuth()
                .build();
        return new TicTacToeApiClient(spec);
    }

    /**
     * Creates a new TicTacToeApiClient with custom Bearer token authentication.
     *
     * @param token the bearer token
     * @return a new client instance
     */
    public static TicTacToeApiClient withBearerToken(String token) {
        RequestSpecification spec = new RequestSpecificationBuilder()
                .withBearerAuth(token)
                .build();
        return new TicTacToeApiClient(spec);
    }

    /**
     * Creates a new TicTacToeApiClient with OAuth2 authentication.
     *
     * @param accessToken the OAuth2 access token
     * @return a new client instance
     */
    public static TicTacToeApiClient withOAuth2(String accessToken) {
        RequestSpecification spec = new RequestSpecificationBuilder()
                .withOAuth2(accessToken)
                .build();
        return new TicTacToeApiClient(spec);
    }

    /**
     * Creates a new TicTacToeApiClient without authentication.
     *
     * @return a new client instance
     */
    public static TicTacToeApiClient withoutAuth() {
        RequestSpecification spec = new RequestSpecificationBuilder().build();
        return new TicTacToeApiClient(spec);
    }

    /**
     * Retrieves the current state of the board.
     *
     * @return the response containing board status
     */
    @Step("Get board state")
    public Response getBoard() {
        log.info("Getting board state");
        return given()
                .spec(requestSpec)
                .when()
                .get(BOARD_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * Retrieves the current state of the board and returns it as a Status object.
     *
     * @return the board status
     */
    @Step("Get board state as Status object")
    public Status getBoardAsStatus() {
        Response response = getBoard();
        return response.as(Status.class);
    }

    /**
     * Retrieves the mark at the specified square.
     *
     * @param row    the row (1-3)
     * @param column the column (1-3)
     * @return the response containing the mark
     */
    @Step("Get square at row {row}, column {column}")
    public Response getSquare(int row, int column) {
        log.info("Getting square at row {}, column {}", row, column);
        return given()
                .spec(requestSpec)
                .pathParam("row", row)
                .pathParam("column", column)
                .when()
                .get(SQUARE_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * Retrieves the mark at the specified square as a Mark enum.
     *
     * @param row    the row (1-3)
     * @param column the column (1-3)
     * @return the mark at the square
     */
    @Step("Get square mark at row {row}, column {column}")
    public Mark getSquareAsMark(int row, int column) {
        Response response = getSquare(row, column);
        String markValue = response.asString().replace("\"", "");
        return Mark.fromValue(markValue);
    }

    /**
     * Places a mark on the board at the specified square.
     *
     * @param row    the row (1-3)
     * @param column the column (1-3)
     * @param mark   the mark to place
     * @return the response containing updated board status
     */
    @Step("Place mark {mark} at row {row}, column {column}")
    public Response putSquare(int row, int column, Mark mark) {
        log.info("Placing mark {} at row {}, column {}", mark, row, column);
        return given()
                .spec(requestSpec)
                .pathParam("row", row)
                .pathParam("column", column)
                .body("\"" + mark.getValue() + "\"")
                .when()
                .put(SQUARE_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * Places a mark on the board and returns the updated status.
     *
     * @param row    the row (1-3)
     * @param column the column (1-3)
     * @param mark   the mark to place
     * @return the updated board status
     */
    @Step("Place mark {mark} at row {row}, column {column} and get status")
    public Status putSquareAndGetStatus(int row, int column, Mark mark) {
        Response response = putSquare(row, column, mark);
        return response.as(Status.class);
    }

    /**
     * Places a mark with invalid data (for negative testing).
     *
     * @param row    the row
     * @param column the column
     * @param body   the request body
     * @return the response
     */
    @Step("Place mark with invalid data at row {row}, column {column}")
    public Response putSquareWithInvalidData(int row, int column, String body) {
        log.info("Placing mark with invalid data at row {}, column {}", row, column);
        return given()
                .spec(requestSpec)
                .pathParam("row", row)
                .pathParam("column", column)
                .body(body)
                .when()
                .put(SQUARE_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * Makes a GET request to a square with string path parameters (for negative testing).
     *
     * @param row    the row parameter
     * @param column the column parameter
     * @return the response
     */
    @Step("Get square with invalid coordinates: row={row}, column={column}")
    public Response getSquareWithInvalidCoordinates(String row, String column) {
        log.info("Getting square with invalid coordinates: row={}, column={}", row, column);
        return given()
                .spec(requestSpec)
                .pathParam("row", row)
                .pathParam("column", column)
                .when()
                .get(SQUARE_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * Makes a PUT request to a square with string path parameters (for negative testing).
     *
     * @param row    the row parameter
     * @param column the column parameter
     * @param mark   the mark to place
     * @return the response
     */
    @Step("Put square with invalid coordinates: row={row}, column={column}")
    public Response putSquareWithInvalidCoordinates(String row, String column, Mark mark) {
        log.info("Putting square with invalid coordinates: row={}, column={}", row, column);
        return given()
                .spec(requestSpec)
                .pathParam("row", row)
                .pathParam("column", column)
                .body("\"" + mark.getValue() + "\"")
                .when()
                .put(SQUARE_ENDPOINT)
                .then()
                .extract()
                .response();
    }
}
