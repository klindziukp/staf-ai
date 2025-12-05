package com.staf.uspto.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.staf.uspto.model.ApiInfo;
import com.staf.uspto.model.DataSetList;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Service class for verifying API responses.
 * Provides reusable verification methods for different response types.
 */
@Slf4j
public class ResponseVerificationService {
    
    private final Gson gson;
    
    public ResponseVerificationService() {
        this.gson = new Gson();
    }
    
    /**
     * Verifies that the HTTP response has the expected status code.
     *
     * @param response the HTTP response
     * @param expectedStatusCode the expected status code
     */
    public void verifyStatusCode(HttpResponse response, int expectedStatusCode) {
        int actualStatusCode = response.getCode();
        log.info("Verifying status code. Expected: {}, Actual: {}", expectedStatusCode, actualStatusCode);
        assertThat(actualStatusCode)
                .as("HTTP Status Code")
                .isEqualTo(expectedStatusCode);
    }
    
    /**
     * Verifies that the response body is valid JSON.
     *
     * @param responseBody the response body as string
     * @return JsonElement parsed from response body
     */
    public JsonElement verifyValidJson(String responseBody) {
        log.info("Verifying response is valid JSON");
        assertThat(responseBody)
                .as("Response body")
                .isNotNull()
                .isNotEmpty();
        
        JsonElement jsonElement = JsonParser.parseString(responseBody);
        assertThat(jsonElement)
                .as("Parsed JSON element")
                .isNotNull();
        
        return jsonElement;
    }
    
    /**
     * Verifies DataSetList response structure and content.
     *
     * @param dataSetList the dataset list to verify
     */
    public void verifyDataSetList(DataSetList dataSetList) {
        log.info("Verifying DataSetList structure");
        
        assertThat(dataSetList)
                .as("DataSetList object")
                .isNotNull();
        
        assertThat(dataSetList.getTotal())
                .as("Total datasets count")
                .isNotNull()
                .isGreaterThan(0);
        
        assertThat(dataSetList.getApis())
                .as("APIs list")
                .isNotNull()
                .isNotEmpty()
                .hasSize(dataSetList.getTotal());
    }
    
    /**
     * Verifies ApiInfo object structure and content.
     *
     * @param apiInfo the API info to verify
     */
    public void verifyApiInfo(ApiInfo apiInfo) {
        log.info("Verifying ApiInfo for dataset: {}", apiInfo.getApiKey());
        
        assertThat(apiInfo.getApiKey())
                .as("API Key")
                .isNotNull()
                .isNotEmpty();
        
        assertThat(apiInfo.getApiVersionNumber())
                .as("API Version Number")
                .isNotNull()
                .isNotEmpty();
        
        assertThat(apiInfo.getApiUrl())
                .as("API URL")
                .isNotNull()
                .isNotEmpty()
                .startsWith("https://");
        
        assertThat(apiInfo.getApiDocumentationUrl())
                .as("API Documentation URL")
                .isNotNull()
                .isNotEmpty()
                .startsWith("https://");
    }
    
    /**
     * Verifies that the fields list is not empty.
     *
     * @param fields the list of field names
     */
    public void verifyFieldsList(List<String> fields) {
        log.info("Verifying fields list. Count: {}", fields != null ? fields.size() : 0);
        
        assertThat(fields)
                .as("Fields list")
                .isNotNull()
                .isNotEmpty();
        
        fields.forEach(field -> 
            assertThat(field)
                    .as("Field name")
                    .isNotNull()
                    .isNotEmpty()
        );
    }
    
    /**
     * Verifies search response structure.
     *
     * @param searchResponse the search response map
     */
    public void verifySearchResponse(Map<String, Object> searchResponse) {
        log.info("Verifying search response structure");
        
        assertThat(searchResponse)
                .as("Search response")
                .isNotNull()
                .isNotEmpty();
        
        assertThat(searchResponse)
                .as("Search response contains 'numFound'")
                .containsKey("numFound");
        
        assertThat(searchResponse)
                .as("Search response contains 'docs'")
                .containsKey("docs");
    }
    
    /**
     * Verifies that the response contains expected number of records.
     *
     * @param docs the list of document records
     * @param expectedMinSize the minimum expected size
     */
    public void verifyRecordsCount(List<?> docs, int expectedMinSize) {
        log.info("Verifying records count. Expected min: {}, Actual: {}", 
                expectedMinSize, docs != null ? docs.size() : 0);
        
        assertThat(docs)
                .as("Documents list")
                .isNotNull()
                .hasSizeGreaterThanOrEqualTo(expectedMinSize);
    }
    
    /**
     * Verifies that the response content type is JSON.
     *
     * @param contentType the content type header value
     */
    public void verifyContentTypeJson(String contentType) {
        log.info("Verifying content type: {}", contentType);
        
        assertThat(contentType)
                .as("Content-Type header")
                .isNotNull()
                .containsIgnoringCase("application/json");
    }
}
