package com.staf.test.apachehttp;

import com.google.gson.Gson;
import com.staf.constant.ApiConstants;
import com.staf.constant.RequestPath;
import com.staf.model.DataSetList;
import com.staf.test.BaseApiTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for USPTO Data Set API using Apache HTTP Client.
 */
@Slf4j
public class ApacheHttpUsptoApiTest extends BaseApiTest {

    private CloseableHttpClient httpClient;
    private Gson gson;

    @BeforeClass
    public void setUp() {
        log.info("Setting up Apache HTTP Client tests");
        httpClient = HttpClients.createDefault();
        gson = new Gson();
    }

    @AfterClass
    public void tearDown() throws IOException {
        log.info("Closing Apache HTTP Client");
        if (httpClient != null) {
            httpClient.close();
        }
    }

    @Test(description = "Test listing available datasets using Apache HTTP Client")
    public void testListDataSets() throws IOException {
        logTestStep("List all available datasets");
        
        // Prepare request
        String url = baseUrl + RequestPath.ROOT;
        HttpGet request = new HttpGet(url);
        request.setHeader(ApiConstants.HEADER_ACCEPT, ApiConstants.CONTENT_TYPE_JSON);
        
        logRequest("GET", url);
        
        // Execute request
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());
        
        logResponse(statusCode, responseBody);
        
        // Verify response
        verifySuccessfulResponse(statusCode, responseBody);
        verificationService.verifyDataSetListResponse(responseBody);
        
        // Parse and verify structure
        DataSetList dataSetList = gson.fromJson(responseBody, DataSetList.class);
        assertThat(dataSetList.getTotal())
                .as("Total datasets count")
                .isGreaterThan(0);
        assertThat(dataSetList.getApis())
                .as("APIs list")
                .isNotEmpty();
        
        log.info("Successfully retrieved {} datasets", dataSetList.getTotal());
    }

    @Test(description = "Test getting searchable fields for a dataset using Apache HTTP Client")
    public void testGetSearchableFields() throws IOException {
        logTestStep("Get searchable fields for dataset: " + defaultDataset);
        
        // Prepare request
        String path = buildPath(RequestPath.DATASET_FIELDS);
        String url = baseUrl + path;
        HttpGet request = new HttpGet(url);
        request.setHeader(ApiConstants.HEADER_ACCEPT, ApiConstants.CONTENT_TYPE_JSON);
        
        logRequest("GET", url);
        
        // Execute request
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());
        
        logResponse(statusCode, responseBody);
        
        // Verify response
        verifySuccessfulResponse(statusCode, responseBody);
        
        log.info("Successfully retrieved searchable fields for dataset: {}", defaultDataset);
    }

    @Test(description = "Test searching dataset records using Apache HTTP Client")
    public void testSearchDataSetRecords() throws IOException {
        logTestStep("Search dataset records with default criteria");
        
        // Prepare request
        String path = buildPath(RequestPath.DATASET_RECORDS);
        String url = baseUrl + path;
        HttpPost request = new HttpPost(url);
        request.setHeader(ApiConstants.HEADER_CONTENT_TYPE, ApiConstants.CONTENT_TYPE_FORM_URLENCODED);
        request.setHeader(ApiConstants.HEADER_ACCEPT, ApiConstants.CONTENT_TYPE_JSON);
        
        // Set form data
        String formData = String.format("criteria=%s&start=%d&rows=%d",
                ApiConstants.DEFAULT_CRITERIA,
                ApiConstants.DEFAULT_START,
                10);
        request.setEntity(new StringEntity(formData));
        
        logRequest("POST", url);
        log.info("Request body: {}", formData);
        
        // Execute request
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());
        
        logResponse(statusCode, responseBody);
        
        // Verify response
        verifySuccessfulResponse(statusCode, responseBody);
        verificationService.verifySearchResponse(responseBody);
        
        log.info("Successfully searched dataset records");
    }

    @Test(description = "Test searching with specific criteria using Apache HTTP Client")
    public void testSearchWithSpecificCriteria() throws IOException {
        logTestStep("Search dataset records with specific criteria");
        
        // Prepare request
        String path = buildPath(RequestPath.DATASET_RECORDS);
        String url = baseUrl + path;
        HttpPost request = new HttpPost(url);
        request.setHeader(ApiConstants.HEADER_CONTENT_TYPE, ApiConstants.CONTENT_TYPE_FORM_URLENCODED);
        request.setHeader(ApiConstants.HEADER_ACCEPT, ApiConstants.CONTENT_TYPE_JSON);
        
        // Set form data with specific criteria
        String criteria = "*:*";
        String formData = String.format("criteria=%s&start=%d&rows=%d",
                criteria, 0, 5);
        request.setEntity(new StringEntity(formData));
        
        logRequest("POST", url);
        log.info("Request body: {}", formData);
        
        // Execute request
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());
        
        logResponse(statusCode, responseBody);
        
        // Verify response
        verifySuccessfulResponse(statusCode, responseBody);
        verificationService.verifySearchResponse(responseBody);
        verificationService.verifyResponseContainsFields(responseBody,
                List.of(ApiConstants.FIELD_NUM_FOUND, ApiConstants.FIELD_DOCS));
        
        log.info("Successfully searched with specific criteria");
    }

    @Test(description = "Test handling non-existent dataset using Apache HTTP Client")
    public void testNonExistentDataset() throws IOException {
        logTestStep("Attempt to get fields for non-existent dataset");
        
        // Prepare request with invalid dataset
        String path = buildPath(RequestPath.DATASET_FIELDS, "invalid_dataset", "v1");
        String url = baseUrl + path;
        HttpGet request = new HttpGet(url);
        request.setHeader(ApiConstants.HEADER_ACCEPT, ApiConstants.CONTENT_TYPE_JSON);
        
        logRequest("GET", url);
        
        // Execute request
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());
        
        logResponse(statusCode, responseBody);
        
        // Verify 404 response
        verifyNotFoundResponse(statusCode);
        
        log.info("Correctly received 404 for non-existent dataset");
    }
}
