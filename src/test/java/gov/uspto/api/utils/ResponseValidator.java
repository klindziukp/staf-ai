package gov.uspto.api.utils;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Utility class for validating API responses
 */
@Slf4j
@UtilityClass
public class ResponseValidator {

    /**
     * Validate response status code
     */
    @Step("Validate response status code is {expectedStatusCode}")
    public static void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        assertThat(actualStatusCode)
                .as("Expected status code %d but got %d", expectedStatusCode, actualStatusCode)
                .isEqualTo(expectedStatusCode);
        log.info("Status code validation passed: {}", actualStatusCode);
    }

    /**
     * Validate response content type
     */
    @Step("Validate response content type is {expectedContentType}")
    public static void validateContentType(Response response, String expectedContentType) {
        String actualContentType = response.getContentType();
        assertThat(actualContentType)
                .as("Expected content type %s but got %s", expectedContentType, actualContentType)
                .contains(expectedContentType);
        log.info("Content type validation passed: {}", actualContentType);
    }

    /**
     * Validate response time is within limit
     */
    @Step("Validate response time is less than {maxTimeMs} ms")
    public static void validateResponseTime(Response response, long maxTimeMs) {
        long actualTime = response.getTime();
        assertThat(actualTime)
                .as("Response time %d ms exceeds maximum allowed %d ms", actualTime, maxTimeMs)
                .isLessThan(maxTimeMs);
        log.info("Response time validation passed: {} ms (max: {} ms)", actualTime, maxTimeMs);
    }

    /**
     * Validate response body is not empty
     */
    @Step("Validate response body is not empty")
    public static void validateResponseBodyNotEmpty(Response response) {
        String body = response.getBody().asString();
        assertThat(body)
                .as("Response body should not be empty")
                .isNotNull()
                .isNotEmpty();
        log.info("Response body is not empty");
    }

    /**
     * Validate response body is empty
     */
    @Step("Validate response body is empty")
    public static void validateResponseBodyEmpty(Response response) {
        String body = response.getBody().asString();
        assertThat(body)
                .as("Response body should be empty")
                .isEmpty();
        log.info("Response body is empty as expected");
    }

    /**
     * Validate response contains specific field
     */
    @Step("Validate response contains field: {fieldPath}")
    public static void validateFieldExists(Response response, String fieldPath) {
        response.then()
                .body(fieldPath, notNullValue());
        log.info("Field '{}' exists in response", fieldPath);
    }

    /**
     * Validate response field has expected value
     */
    @Step("Validate field {fieldPath} has value {expectedValue}")
    public static void validateFieldValue(Response response, String fieldPath, Object expectedValue) {
        response.then()
                .body(fieldPath, equalTo(expectedValue));
        log.info("Field '{}' has expected value: {}", fieldPath, expectedValue);
    }

    /**
     * Validate response field matches pattern
     */
    @Step("Validate field {fieldPath} matches pattern")
    public static void validateFieldPattern(Response response, String fieldPath, String pattern) {
        response.then()
                .body(fieldPath, matchesPattern(pattern));
        log.info("Field '{}' matches pattern: {}", fieldPath, pattern);
    }

    /**
     * Validate response is valid JSON
     */
    @Step("Validate response is valid JSON")
    public static void validateJsonResponse(Response response) {
        try {
            response.then().contentType(ContentType.JSON);
            log.info("Response is valid JSON");
        } catch (AssertionError e) {
            log.error("Response is not valid JSON");
            throw e;
        }
    }

    /**
     * Validate response array size
     */
    @Step("Validate array {arrayPath} has size {expectedSize}")
    public static void validateArraySize(Response response, String arrayPath, int expectedSize) {
        response.then()
                .body(arrayPath, hasSize(expectedSize));
        log.info("Array '{}' has expected size: {}", arrayPath, expectedSize);
    }

    /**
     * Validate response array is not empty
     */
    @Step("Validate array {arrayPath} is not empty")
    public static void validateArrayNotEmpty(Response response, String arrayPath) {
        response.then()
                .body(arrayPath, not(empty()));
        log.info("Array '{}' is not empty", arrayPath);
    }

    /**
     * Validate response contains error message
     */
    @Step("Validate response contains error message")
    public static void validateErrorMessage(Response response) {
        response.then()
                .body("error", notNullValue())
                .noRoot()
                .body("message", notNullValue());
        log.info("Response contains error message");
    }

    /**
     * Validate multiple status codes
     */
    @Step("Validate response status code is one of: {expectedStatusCodes}")
    public static void validateStatusCodeIn(Response response, int... expectedStatusCodes) {
        int actualStatusCode = response.getStatusCode();
        assertThat(actualStatusCode)
                .as("Status code %d is not in expected list", actualStatusCode)
                .isIn(expectedStatusCodes);
        log.info("Status code {} is in expected list", actualStatusCode);
    }

    /**
     * Validate response header exists
     */
    @Step("Validate header {headerName} exists")
    public static void validateHeaderExists(Response response, String headerName) {
        String headerValue = response.getHeader(headerName);
        assertThat(headerValue)
                .as("Header '%s' should exist", headerName)
                .isNotNull();
        log.info("Header '{}' exists with value: {}", headerName, headerValue);
    }

    /**
     * Validate response header value
     */
    @Step("Validate header {headerName} has value {expectedValue}")
    public static void validateHeaderValue(Response response, String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        assertThat(actualValue)
                .as("Header '%s' should have value '%s' but got '%s'", headerName, expectedValue, actualValue)
                .isEqualTo(expectedValue);
        log.info("Header '{}' has expected value: {}", headerName, expectedValue);
    }
}
