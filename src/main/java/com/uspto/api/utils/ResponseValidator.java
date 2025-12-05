package com.uspto.api.utils;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;

import java.util.List;
import java.util.Map;

/**
 * Utility class for validating API responses.
 * Provides methods for common response validations.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Slf4j
public final class ResponseValidator {

    /**
     * Private constructor to prevent instantiation.
     */
    private ResponseValidator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Validates response status code.
     *
     * @param response API response
     * @param expectedStatusCode expected status code
     */
    public static void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            log.error("Status code mismatch. Expected: {}, Actual: {}", expectedStatusCode, actualStatusCode);
            throw new AssertionError(String.format("Expected status code %d but got %d", 
                    expectedStatusCode, actualStatusCode));
        }
        log.info("Status code validation passed: {}", actualStatusCode);
    }

    /**
     * Validates response content type.
     *
     * @param response API response
     * @param expectedContentType expected content type
     */
    public static void validateContentType(Response response, String expectedContentType) {
        String actualContentType = response.getContentType();
        if (!actualContentType.contains(expectedContentType)) {
            log.error("Content type mismatch. Expected: {}, Actual: {}", expectedContentType, actualContentType);
            throw new AssertionError(String.format("Expected content type %s but got %s", 
                    expectedContentType, actualContentType));
        }
        log.info("Content type validation passed: {}", actualContentType);
    }

    /**
     * Validates response time is within limit.
     *
     * @param response API response
     * @param maxResponseTime maximum allowed response time in milliseconds
     */
    public static void validateResponseTime(Response response, long maxResponseTime) {
        long actualResponseTime = response.getTime();
        if (actualResponseTime > maxResponseTime) {
            log.warn("Response time exceeded limit. Expected: {} ms, Actual: {} ms", 
                    maxResponseTime, actualResponseTime);
        } else {
            log.info("Response time validation passed: {} ms", actualResponseTime);
        }
    }

    /**
     * Validates response body is not empty.
     *
     * @param response API response
     */
    public static void validateResponseBodyNotEmpty(Response response) {
        String body = response.getBody().asString();
        if (body == null || body.trim().isEmpty()) {
            log.error("Response body is empty");
            throw new AssertionError("Response body should not be empty");
        }
        log.info("Response body is not empty");
    }

    /**
     * Validates JSON path exists in response.
     *
     * @param response API response
     * @param jsonPath JSON path to validate
     */
    public static void validateJsonPathExists(Response response, String jsonPath) {
        try {
            Object value = response.jsonPath().get(jsonPath);
            if (value == null) {
                log.error("JSON path does not exist: {}", jsonPath);
                throw new AssertionError("JSON path does not exist: " + jsonPath);
            }
            log.info("JSON path exists: {}", jsonPath);
        } catch (Exception e) {
            log.error("Error validating JSON path: {}", jsonPath, e);
            throw new AssertionError("JSON path validation failed: " + jsonPath, e);
        }
    }

    /**
     * Validates JSON path value equals expected value.
     *
     * @param response API response
     * @param jsonPath JSON path
     * @param expectedValue expected value
     */
    public static void validateJsonPathValue(Response response, String jsonPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(jsonPath);
        if (!expectedValue.equals(actualValue)) {
            log.error("JSON path value mismatch. Path: {}, Expected: {}, Actual: {}", 
                    jsonPath, expectedValue, actualValue);
            throw new AssertionError(String.format("Expected value %s but got %s for path %s", 
                    expectedValue, actualValue, jsonPath));
        }
        log.info("JSON path value validation passed for path: {}", jsonPath);
    }

    /**
     * Validates response header exists.
     *
     * @param response API response
     * @param headerName header name
     */
    public static void validateHeaderExists(Response response, String headerName) {
        String headerValue = response.getHeader(headerName);
        if (headerValue == null) {
            log.error("Header does not exist: {}", headerName);
            throw new AssertionError("Header does not exist: " + headerName);
        }
        log.info("Header exists: {}", headerName);
    }

    /**
     * Validates response header value.
     *
     * @param response API response
     * @param headerName header name
     * @param expectedValue expected value
     */
    public static void validateHeaderValue(Response response, String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        if (!expectedValue.equals(actualValue)) {
            log.error("Header value mismatch. Header: {}, Expected: {}, Actual: {}", 
                    headerName, expectedValue, actualValue);
            throw new AssertionError(String.format("Expected header value %s but got %s for header %s", 
                    expectedValue, actualValue, headerName));
        }
        log.info("Header value validation passed for header: {}", headerName);
    }

    /**
     * Validates list is not empty.
     *
     * @param response API response
     * @param jsonPath JSON path to list
     */
    public static void validateListNotEmpty(Response response, String jsonPath) {
        List<?> list = response.jsonPath().getList(jsonPath);
        if (list == null || list.isEmpty()) {
            log.error("List is empty at path: {}", jsonPath);
            throw new AssertionError("List should not be empty at path: " + jsonPath);
        }
        log.info("List is not empty at path: {}. Size: {}", jsonPath, list.size());
    }

    /**
     * Validates list size.
     *
     * @param response API response
     * @param jsonPath JSON path to list
     * @param expectedSize expected size
     */
    public static void validateListSize(Response response, String jsonPath, int expectedSize) {
        List<?> list = response.jsonPath().getList(jsonPath);
        int actualSize = list != null ? list.size() : 0;
        if (actualSize != expectedSize) {
            log.error("List size mismatch. Path: {}, Expected: {}, Actual: {}", 
                    jsonPath, expectedSize, actualSize);
            throw new AssertionError(String.format("Expected list size %d but got %d for path %s", 
                    expectedSize, actualSize, jsonPath));
        }
        log.info("List size validation passed for path: {}. Size: {}", jsonPath, actualSize);
    }

    /**
     * Performs multiple validations using soft assertions.
     *
     * @param response API response
     * @param validations map of JSON paths and expected values
     */
    public static void validateMultiple(Response response, Map<String, Object> validations) {
        SoftAssertions softly = new SoftAssertions();
        
        validations.forEach((jsonPath, expectedValue) -> {
            Object actualValue = response.jsonPath().get(jsonPath);
            softly.assertThat(actualValue)
                    .as("Validation for path: " + jsonPath)
                    .isEqualTo(expectedValue);
        });
        
        softly.assertAll();
        log.info("Multiple validations passed");
    }

    /**
     * Validates response is valid JSON.
     *
     * @param response API response
     */
    public static void validateIsValidJson(Response response) {
        String body = response.getBody().asString();
        if (!JsonUtils.isValidJson(body)) {
            log.error("Response is not valid JSON");
            throw new AssertionError("Response body is not valid JSON");
        }
        log.info("Response is valid JSON");
    }
}
