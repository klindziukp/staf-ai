package com.klindziuk.staf.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Service for verifying HTTP responses.
 * Provides methods to validate status codes, response bodies, and JSON content.
 */
@Slf4j
public class ResponseVerificationService {

    private static final Gson GSON = new Gson();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Verifies that the response status code matches the expected value.
     *
     * @param response     the HTTP response
     * @param expectedCode the expected status code
     */
    public static void verifyStatusCode(ClassicHttpResponse response, int expectedCode) {
        int actualCode = response.getCode();
        log.info("Verifying status code. Expected: {}, Actual: {}", expectedCode, actualCode);
        assertThat(actualCode)
                .as("Response status code should be %d", expectedCode)
                .isEqualTo(expectedCode);
    }

    /**
     * Verifies that the response status code is 200 OK.
     *
     * @param response the HTTP response
     */
    public static void verifyStatusOk(ClassicHttpResponse response) {
        verifyStatusCode(response, HttpStatus.SC_OK);
    }

    /**
     * Verifies that the response status code is 404 Not Found.
     *
     * @param response the HTTP response
     */
    public static void verifyStatusNotFound(ClassicHttpResponse response) {
        verifyStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }

    /**
     * Verifies that the response body is not null or empty.
     *
     * @param responseBody the response body as string
     */
    public static void verifyResponseBodyNotEmpty(String responseBody) {
        log.info("Verifying response body is not empty");
        assertThat(responseBody)
                .as("Response body should not be null or empty")
                .isNotNull()
                .isNotEmpty();
    }

    /**
     * Verifies that the response body contains the expected text.
     *
     * @param responseBody the response body as string
     * @param expectedText the expected text
     */
    public static void verifyResponseBodyContains(String responseBody, String expectedText) {
        log.info("Verifying response body contains: {}", expectedText);
        assertThat(responseBody)
                .as("Response body should contain '%s'", expectedText)
                .contains(expectedText);
    }

    /**
     * Verifies that the JSON response contains the specified field.
     *
     * @param responseBody the response body as string
     * @param fieldName    the field name to check
     */
    public static void verifyJsonFieldExists(String responseBody, String fieldName) {
        log.info("Verifying JSON field exists: {}", fieldName);
        JsonObject jsonObject = GSON.fromJson(responseBody, JsonObject.class);
        assertThat(jsonObject.has(fieldName))
                .as("JSON response should contain field '%s'", fieldName)
                .isTrue();
    }

    /**
     * Verifies that the JSON response field has the expected value.
     *
     * @param responseBody  the response body as string
     * @param fieldName     the field name
     * @param expectedValue the expected value
     */
    public static void verifyJsonFieldValue(String responseBody, String fieldName, Object expectedValue) {
        log.info("Verifying JSON field '{}' has value: {}", fieldName, expectedValue);
        JsonObject jsonObject = GSON.fromJson(responseBody, JsonObject.class);
        JsonElement actualValue = jsonObject.get(fieldName);
        
        assertThat(actualValue)
                .as("JSON field '%s' should exist", fieldName)
                .isNotNull();

        if (expectedValue instanceof String) {
            assertThat(actualValue.getAsString())
                    .as("JSON field '%s' should have value '%s'", fieldName, expectedValue)
                    .isEqualTo(expectedValue);
        } else if (expectedValue instanceof Integer) {
            assertThat(actualValue.getAsInt())
                    .as("JSON field '%s' should have value %d", fieldName, expectedValue)
                    .isEqualTo(expectedValue);
        } else if (expectedValue instanceof Boolean) {
            assertThat(actualValue.getAsBoolean())
                    .as("JSON field '%s' should have value %b", fieldName, expectedValue)
                    .isEqualTo(expectedValue);
        }
    }

    /**
     * Extracts the response body as a string.
     *
     * @param response the HTTP response
     * @return the response body as string
     * @throws IOException if reading the response fails
     */
    public static String extractResponseBody(ClassicHttpResponse response) throws IOException {
        if (response.getEntity() == null) {
            log.warn("Response entity is null");
            return "";
        }
        
        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        log.debug("Response body: {}", body);
        return body;
    }

    /**
     * Deserializes the response body to the specified class.
     *
     * @param responseBody the response body as string
     * @param clazz        the target class
     * @param <T>          the type parameter
     * @return the deserialized object
     */
    public static <T> T deserializeResponse(String responseBody, Class<T> clazz) {
        log.info("Deserializing response to class: {}", clazz.getSimpleName());
        try {
            return OBJECT_MAPPER.readValue(responseBody, clazz);
        } catch (IOException e) {
            log.error("Failed to deserialize response", e);
            throw new RuntimeException("Failed to deserialize response to " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Verifies that the response can be deserialized to the specified class.
     *
     * @param responseBody the response body as string
     * @param clazz        the target class
     * @param <T>          the type parameter
     * @return the deserialized object
     */
    public static <T> T verifyAndDeserialize(String responseBody, Class<T> clazz) {
        verifyResponseBodyNotEmpty(responseBody);
        return deserializeResponse(responseBody, clazz);
    }
}
