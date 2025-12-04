package com.tictactoe.api.clients;

import com.tictactoe.api.models.Status;
import com.tictactoe.api.utils.AuthenticationHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;

/**
 * API client for square-related endpoints.
 * Handles GET and PUT /board/{row}/{column} operations.
 */
@Slf4j
public class SquareApiClient {
    private static final String SQUARE_ENDPOINT = "/board/{row}/{column}";

    /**
     * Retrieves a single square from the board.
     *
     * @param row    row number (1-3)
     * @param column column number (1-3)
     * @return Response object
     */
    @Step("Get square at position ({row}, {column})")
    public Response getSquare(int row, int column) {
        log.info("Getting square at position ({}, {})", row, column);
        return given()
                .spec(AuthenticationHelper.getAuthenticatedSpec())
                .pathParam("row", row)
                .pathParam("column", column)
                .when()
                .get(SQUARE_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * Retrieves a single square without authentication.
     *
     * @param row    row number (1-3)
     * @param column column number (1-3)
     * @return Response object
     */
    @Step("Get square at position ({row}, {column}) without authentication")
    public Response getSquareWithoutAuth(int row, int column) {
        log.info("Getting square at position ({}, {}) without auth", row, column);
        return given()
                .contentType("application/json")
                .pathParam("row", row)
                .pathParam("column", column)
                .when()
                .get(SQUARE_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * Places a mark on the board at specified position.
     *
     * @param row    row number (1-3)
     * @param column column number (1-3)
     * @param mark   mark to place (X or O)
     * @return Response object
     */
    @Step("Place mark '{mark}' at position ({row}, {column})")
    public Response placeMark(int row, int column, String mark) {
        log.info("Placing mark '{}' at position ({}, {})", mark, row, column);
        return given()
                .spec(AuthenticationHelper.getAuthenticatedSpec())
                .pathParam("row", row)
                .pathParam("column", column)
                .body("\"" + mark + "\"")
                .when()
                .put(SQUARE_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * Places a mark without authentication.
     *
     * @param row    row number (1-3)
     * @param column column number (1-3)
     * @param mark   mark to place
     * @return Response object
     */
    @Step("Place mark '{mark}' at position ({row}, {column}) without authentication")
    public Response placeMarkWithoutAuth(int row, int column, String mark) {
        log.info("Placing mark '{}' at position ({}, {}) without auth", mark, row, column);
        return given()
                .contentType("application/json")
                .pathParam("row", row)
                .pathParam("column", column)
                .body("\"" + mark + "\"")
                .when()
                .put(SQUARE_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * Places a mark and returns the updated board status.
     *
     * @param row    row number (1-3)
     * @param column column number (1-3)
     * @param mark   mark to place
     * @return Status object
     */
    @Step("Place mark and get status")
    public Status placeMarkAndGetStatus(int row, int column, String mark) {
        log.info("Placing mark and getting status");
        return placeMark(row, column, mark)
                .then()
                .statusCode(200)
                .extract()
                .as(Status.class);
    }
}
