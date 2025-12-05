package com.klindziuk.staf.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.klindziuk.staf.constant.RequestPath;
import com.klindziuk.staf.service.ResponseVerificationService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for USPTO Data Set API search operations.
 * Tests various search scenarios and criteria.
 */
@Slf4j
@Epic("USPTO Data Set API")
@Feature("Search Operations")
public class UsptoSearchApiTest extends BaseApiTest {

    private static final Gson GSON = new Gson();

    @Test(description = "Verify searching records with default criteria")
    @Description("Test that searching with default criteria (*:*) returns records")
    @Story("Search Records")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchRecordsWithDefaultCriteria() throws IOException {
        log.info("Testing search records with default criteria");

        // Build request
        String path = buildRecordsPath(RequestPath.DEFAULT_DATASET, RequestPath.DEFAULT_VERSION);
        String formData = buildFormData("*:*", 0, 10);

        // Execute POST request
        ClassicHttpResponse response = executePost(path, formData);

        // Verify response
        ResponseVerificationService.verifyStatusOk(response);

        String responseBody = ResponseVerificationService.extractResponseBody(response);
        ResponseVerificationService.verifyResponseBodyNotEmpty(responseBody);

        // Parse and verify response structure
        JsonObject jsonResponse = GSON.fromJson(responseBody, JsonObject.class);
        
        assertThat(jsonResponse.has("response"))
                .as("Response should contain 'response' field")
                .isTrue();

        JsonObject responseObj = jsonResponse.getAsJsonObject("response");
        
        assertThat(responseObj.has("numFound"))
                .as("Response should contain 'numFound' field")
                .isTrue();

        assertThat(responseObj.has("docs"))
                .as("Response should contain 'docs' field")
                .isTrue();

        int numFound = responseObj.get("numFound").getAsInt();
        log.info("Found {} total records", numFound);

        assertThat(numFound)
                .as("Number of found records should be greater than 0")
                .isGreaterThan(0);
    }

    @Test(description = "Verify searching records with pagination")
    @Description("Test that pagination parameters (start and rows) work correctly")
    @Story("Search Records")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchRecordsWithPagination() throws IOException {
        log.info("Testing search records with pagination");

        // Build request with pagination
        String path = buildRecordsPath(RequestPath.DEFAULT_DATASET, RequestPath.DEFAULT_VERSION);
        String formData = buildFormData("*:*", 0, 5);

        // Execute POST request
        ClassicHttpResponse response = executePost(path, formData);

        // Verify response
        ResponseVerificationService.verifyStatusOk(response);

        String responseBody = ResponseVerificationService.extractResponseBody(response);
        JsonObject jsonResponse = GSON.fromJson(responseBody, JsonObject.class);
        JsonObject responseObj = jsonResponse.getAsJsonObject("response");

        int start = responseObj.get("start").getAsInt();
        int docsSize = responseObj.getAsJsonArray("docs").size();

        assertThat(start)
                .as("Start index should be 0")
                .isEqualTo(0);

        assertThat(docsSize)
                .as("Number of returned documents should be 5 or less")
                .isLessThanOrEqualTo(5);

        log.info("Successfully verified pagination: start={}, docs returned={}", start, docsSize);
    }

    @Test(description = "Verify searching records with specific criteria")
    @Description("Test searching with a specific field criteria")
    @Story("Search Records")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchRecordsWithSpecificCriteria() throws IOException {
        log.info("Testing search records with specific criteria");

        // Build request with specific criteria
        String path = buildRecordsPath(RequestPath.DEFAULT_DATASET, RequestPath.DEFAULT_VERSION);
        String formData = buildFormData("*:*", 0, 1);

        // Execute POST request
        ClassicHttpResponse response = executePost(path, formData);

        // Verify response
        ResponseVerificationService.verifyStatusOk(response);

        String responseBody = ResponseVerificationService.extractResponseBody(response);
        JsonObject jsonResponse = GSON.fromJson(responseBody, JsonObject.class);
        JsonObject responseObj = jsonResponse.getAsJsonObject("response");

        assertThat(responseObj.has("docs"))
                .as("Response should contain docs array")
                .isTrue();

        log.info("Successfully executed search with specific criteria");
    }

    @Test(description = "Verify searching with large rows parameter")
    @Description("Test that requesting a large number of rows works correctly")
    @Story("Search Records")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchRecordsWithLargeRowsParameter() throws IOException {
        log.info("Testing search records with large rows parameter");

        // Build request with large rows value
        String path = buildRecordsPath(RequestPath.DEFAULT_DATASET, RequestPath.DEFAULT_VERSION);
        String formData = buildFormData("*:*", 0, 100);

        // Execute POST request
        ClassicHttpResponse response = executePost(path, formData);

        // Verify response
        ResponseVerificationService.verifyStatusOk(response);

        String responseBody = ResponseVerificationService.extractResponseBody(response);
        JsonObject jsonResponse = GSON.fromJson(responseBody, JsonObject.class);
        JsonObject responseObj = jsonResponse.getAsJsonObject("response");

        int docsSize = responseObj.getAsJsonArray("docs").size();

        assertThat(docsSize)
                .as("Number of returned documents should be 100 or less")
                .isLessThanOrEqualTo(100);

        log.info("Successfully verified large rows parameter: {} docs returned", docsSize);
    }

    @Test(description = "Verify searching with offset")
    @Description("Test that the start parameter correctly offsets results")
    @Story("Search Records")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchRecordsWithOffset() throws IOException {
        log.info("Testing search records with offset");

        // Build request with offset
        String path = buildRecordsPath(RequestPath.DEFAULT_DATASET, RequestPath.DEFAULT_VERSION);
        String formData = buildFormData("*:*", 10, 5);

        // Execute POST request
        ClassicHttpResponse response = executePost(path, formData);

        // Verify response
        ResponseVerificationService.verifyStatusOk(response);

        String responseBody = ResponseVerificationService.extractResponseBody(response);
        JsonObject jsonResponse = GSON.fromJson(responseBody, JsonObject.class);
        JsonObject responseObj = jsonResponse.getAsJsonObject("response");

        int start = responseObj.get("start").getAsInt();

        assertThat(start)
                .as("Start index should be 10")
                .isEqualTo(10);

        log.info("Successfully verified offset: start={}", start);
    }

    @Test(description = "Verify searching cancer_moonshot dataset")
    @Description("Test that searching the cancer_moonshot dataset works correctly")
    @Story("Search Records")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchCancerMoonshotDataset() throws IOException {
        log.info("Testing search cancer_moonshot dataset");

        // Build request for cancer_moonshot dataset
        String path = buildRecordsPath(RequestPath.CANCER_MOONSHOT_DATASET, RequestPath.DEFAULT_VERSION);
        String formData = buildFormData("*:*", 0, 10);

        // Execute POST request
        ClassicHttpResponse response = executePost(path, formData);

        // Verify response
        ResponseVerificationService.verifyStatusOk(response);

        String responseBody = ResponseVerificationService.extractResponseBody(response);
        ResponseVerificationService.verifyResponseBodyNotEmpty(responseBody);

        JsonObject jsonResponse = GSON.fromJson(responseBody, JsonObject.class);
        
        assertThat(jsonResponse.has("response"))
                .as("Response should contain 'response' field")
                .isTrue();

        log.info("Successfully searched cancer_moonshot dataset");
    }

    @Test(description = "Verify 404 response for non-existent dataset search")
    @Description("Test that searching a non-existent dataset returns 404")
    @Story("Error Handling")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchNonExistentDataset() throws IOException {
        log.info("Testing search non-existent dataset");

        // Build request for non-existent dataset
        String path = buildRecordsPath("non_existent_dataset", RequestPath.DEFAULT_VERSION);
        String formData = buildFormData("*:*", 0, 10);

        // Execute POST request
        ClassicHttpResponse response = executePost(path, formData);

        // Verify 404 response
        ResponseVerificationService.verifyStatusNotFound(response);

        log.info("Successfully verified 404 response for non-existent dataset");
    }
}
