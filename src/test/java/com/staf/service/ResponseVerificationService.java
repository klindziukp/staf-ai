package com.staf.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staf.constant.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Service for verifying API responses.
 * Provides comprehensive validation methods for different response types.
 */
@Slf4j
public class ResponseVerificationService {

    private final ObjectMapper objectMapper;

    public ResponseVerificationService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Verify HTTP status code.
     *
     * @param actualStatus   actual status code
     * @param expectedStatus expected status code
     */
    public void verifyStatusCode(int actualStatus, int expectedStatus) {
        log.info("Verifying status code - Expected: {}, Actual: {}", expectedStatus, actualStatus);
        assertThat(actualStatus)
                .as("HTTP Status Code")
                .isEqualTo(expectedStatus);
    }

    /**
     * Verify response is not null and not empty.
     *
     * @param response response body
     */
    public void verifyResponseNotEmpty(String response) {
        log.info("Verifying response is not empty");
        assertThat(response)
                .as("Response body")
                .isNotNull()
                .isNotEmpty();
    }

    /**
     * Verify response contains expected field.
     *
     * @param response      response body
     * @param expectedField expected field name
     */
    public void verifyResponseContainsField(String response, String expectedField) {
        log.info("Verifying response contains field: {}", expectedField);
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            assertThat(jsonNode.has(expectedField))
                    .as("Response contains field: " + expectedField)
                    .isTrue();
        } catch (Exception e) {
            log.error("Error parsing response JSON", e);
            throw new AssertionError("Failed to parse response JSON: " + e.getMessage());
        }
    }

    /**
     * Verify response contains multiple expected fields.
     *
     * @param response       response body
     * @param expectedFields list of expected field names
     */
    public void verifyResponseContainsFields(String response, List<String> expectedFields) {
        log.info("Verifying response contains fields: {}", expectedFields);
        SoftAssertions softly = new SoftAssertions();
        
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            for (String field : expectedFields) {
                softly.assertThat(jsonNode.has(field))
                        .as("Response contains field: " + field)
                        .isTrue();
            }
            softly.assertAll();
        } catch (Exception e) {
            log.error("Error parsing response JSON", e);
            throw new AssertionError("Failed to parse response JSON: " + e.getMessage());
        }
    }

    /**
     * Verify DataSetList response structure.
     *
     * @param response response body
     */
    public void verifyDataSetListResponse(String response) {
        log.info("Verifying DataSetList response structure");
        SoftAssertions softly = new SoftAssertions();
        
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            
            softly.assertThat(jsonNode.has(ApiConstants.FIELD_TOTAL))
                    .as("Response contains 'total' field")
                    .isTrue();
            
            softly.assertThat(jsonNode.has(ApiConstants.FIELD_APIS))
                    .as("Response contains 'apis' field")
                    .isTrue();
            
            if (jsonNode.has(ApiConstants.FIELD_APIS)) {
                JsonNode apisNode = jsonNode.get(ApiConstants.FIELD_APIS);
                softly.assertThat(apisNode.isArray())
                        .as("'apis' field is an array")
                        .isTrue();
                
                if (apisNode.isArray() && apisNode.size() > 0) {
                    JsonNode firstApi = apisNode.get(0);
                    softly.assertThat(firstApi.has(ApiConstants.FIELD_API_KEY))
                            .as("API info contains 'apiKey'")
                            .isTrue();
                    softly.assertThat(firstApi.has(ApiConstants.FIELD_API_VERSION_NUMBER))
                            .as("API info contains 'apiVersionNumber'")
                            .isTrue();
                    softly.assertThat(firstApi.has(ApiConstants.FIELD_API_URL))
                            .as("API info contains 'apiUrl'")
                            .isTrue();
                    softly.assertThat(firstApi.has(ApiConstants.FIELD_API_DOCUMENTATION_URL))
                            .as("API info contains 'apiDocumentationUrl'")
                            .isTrue();
                }
            }
            
            softly.assertAll();
        } catch (Exception e) {
            log.error("Error verifying DataSetList response", e);
            throw new AssertionError("Failed to verify DataSetList response: " + e.getMessage());
        }
    }

    /**
     * Verify search response structure.
     *
     * @param response response body
     */
    public void verifySearchResponse(String response) {
        log.info("Verifying search response structure");
        verifyResponseContainsFields(response, 
                List.of(ApiConstants.FIELD_NUM_FOUND, ApiConstants.FIELD_DOCS));
    }

    /**
     * Verify field value in response.
     *
     * @param response      response body
     * @param fieldName     field name
     * @param expectedValue expected value
     */
    public void verifyFieldValue(String response, String fieldName, Object expectedValue) {
        log.info("Verifying field '{}' has value: {}", fieldName, expectedValue);
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode fieldNode = jsonNode.get(fieldName);
            
            assertThat(fieldNode)
                    .as("Field '" + fieldName + "' exists")
                    .isNotNull();
            
            Object actualValue = objectMapper.convertValue(fieldNode, expectedValue.getClass());
            assertThat(actualValue)
                    .as("Field '" + fieldName + "' value")
                    .isEqualTo(expectedValue);
        } catch (Exception e) {
            log.error("Error verifying field value", e);
            throw new AssertionError("Failed to verify field value: " + e.getMessage());
        }
    }

    /**
     * Verify response is valid JSON.
     *
     * @param response response body
     */
    public void verifyValidJson(String response) {
        log.info("Verifying response is valid JSON");
        try {
            objectMapper.readTree(response);
            log.info("Response is valid JSON");
        } catch (Exception e) {
            log.error("Response is not valid JSON", e);
            throw new AssertionError("Response is not valid JSON: " + e.getMessage());
        }
    }

    /**
     * Verify response content type.
     *
     * @param contentType        actual content type
     * @param expectedContentType expected content type
     */
    public void verifyContentType(String contentType, String expectedContentType) {
        log.info("Verifying content type - Expected: {}, Actual: {}", expectedContentType, contentType);
        assertThat(contentType)
                .as("Content-Type header")
                .contains(expectedContentType);
    }
}
