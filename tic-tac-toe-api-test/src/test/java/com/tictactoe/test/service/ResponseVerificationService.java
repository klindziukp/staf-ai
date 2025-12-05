/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.service;

import com.staf.api.core.model.ApiResponse;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;

/**
 * Service for verifying API responses.
 * Provides methods for standard and JSON-based response verification.
 */
public class ResponseVerificationService {

    /**
     * Verifies API response with expected status code and body.
     *
     * @param httpStatus expected HTTP status code
     * @param apiResponse actual API response
     * @param expected expected response body
     * @param <T> type of response body
     */
    public <T> void verifyResponse(int httpStatus, ApiResponse<T> apiResponse, T expected) {
        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(apiResponse.getStatusCode()).isEqualTo(httpStatus);
        softAssertions.assertThat(apiResponse.getBody()).isEqualTo(expected);
        softAssertions.assertAll();
    }

    /**
     * Verifies API response for Gson objects using JSON comparison.
     *
     * @param httpStatus expected HTTP status code
     * @param apiResponse actual API response
     * @param expected expected response body
     * @param <T> type of response body
     */
    public <T> void verifyGsonResponse(int httpStatus, ApiResponse<T> apiResponse, T expected) {
        Assertions.assertThat(apiResponse.getStatusCode()).isEqualTo(httpStatus);
        JsonAssertions.assertThatJson(apiResponse.getBody()).isEqualTo(expected);
    }

    /**
     * Verifies only the HTTP status code of the response.
     *
     * @param httpStatus expected HTTP status code
     * @param apiResponse actual API response
     * @param <T> type of response body
     */
    public <T> void verifyStatusCode(int httpStatus, ApiResponse<T> apiResponse) {
        Assertions.assertThat(apiResponse.getStatusCode()).isEqualTo(httpStatus);
    }

    /**
     * Verifies API response with expected status code and checks if body contains expected text.
     *
     * @param httpStatus expected HTTP status code
     * @param apiResponse actual API response
     * @param expectedText expected text in response body
     */
    public void verifyResponseContains(int httpStatus, ApiResponse<String> apiResponse, String expectedText) {
        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(apiResponse.getStatusCode()).isEqualTo(httpStatus);
        softAssertions.assertThat(apiResponse.getBody()).contains(expectedText);
        softAssertions.assertAll();
    }

    /**
     * Verifies that response body is not null.
     *
     * @param apiResponse actual API response
     * @param <T> type of response body
     */
    public <T> void verifyResponseBodyNotNull(ApiResponse<T> apiResponse) {
        Assertions.assertThat(apiResponse.getBody()).isNotNull();
    }
}
