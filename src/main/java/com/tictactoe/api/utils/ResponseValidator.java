package com.tictactoe.api.utils;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Utility class for validating API responses.
 */
@Slf4j
public class ResponseValidator {

    /**
     * Validates that the response has the expected status code.
     *
     * @param response           the response to validate
     * @param expectedStatusCode the expected status code
     */
    public static void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        log.debug("Validating status code. Expected: {}, Actual: {}", expectedStatusCode, actualStatusCode);
        assertThat(actualStatusCode)
                .as("Response status code")
                .isEqualTo(expectedStatusCode);
    }

    /**
     * Validates that the response has a successful status code (2xx).
     *
     * @param response the response to validate
     */
    public static void validateSuccessfulResponse(Response response) {
        int statusCode = response.getStatusCode();
        log.debug("Validating successful response. Status code: {}", statusCode);
        assertThat(statusCode)
                .as("Response status code should be successful (2xx)")
                .isBetween(200, 299);
    }

    /**
     * Validates that the response contains the expected content type.
     *
     * @param response            the response to validate
     * @param expectedContentType the expected content type
     */
    public static void validateContentType(Response response, String expectedContentType) {
        String actualContentType = response.getContentType();
        log.debug("Validating content type. Expected: {}, Actual: {}", expectedContentType, actualContentType);
        assertThat(actualContentType)
                .as("Response content type")
                .contains(expectedContentType);
    }

    /**
     * Validates that the response time is within acceptable limits.
     *
     * @param response           the response to validate
     * @param maxResponseTimeMs  the maximum acceptable response time in milliseconds
     */
    public static void validateResponseTime(Response response, long maxResponseTimeMs) {
        long actualResponseTime = response.getTime();
        log.debug("Validating response time. Max: {}ms, Actual: {}ms", maxResponseTimeMs, actualResponseTime);
        assertThat(actualResponseTime)
                .as("Response time should be less than " + maxResponseTimeMs + "ms")
                .isLessThanOrEqualTo(maxResponseTimeMs);
    }

    /**
     * Validates that the response body is not empty.
     *
     * @param response the response to validate
     */
    public static void validateResponseBodyNotEmpty(Response response) {
        String body = response.getBody().asString();
        log.debug("Validating response body is not empty");
        assertThat(body)
                .as("Response body should not be empty")
                .isNotEmpty();
    }

    /**
     * Validates multiple response attributes using soft assertions.
     *
     * @param response           the response to validate
     * @param expectedStatusCode the expected status code
     * @param expectedContentType the expected content type
     */
    public static void validateResponse(Response response, int expectedStatusCode, String expectedContentType) {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(response.getStatusCode())
                .as("Response status code")
                .isEqualTo(expectedStatusCode);

        softly.assertThat(response.getContentType())
                .as("Response content type")
                .contains(expectedContentType);

        softly.assertAll();
    }
}
