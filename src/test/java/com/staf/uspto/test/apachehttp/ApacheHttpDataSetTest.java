package com.staf.uspto.test.apachehttp;

import com.google.gson.reflect.TypeToken;
import com.staf.uspto.model.ApiInfo;
import com.staf.uspto.model.DataSetList;
import com.staf.uspto.test.BaseApiTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for USPTO Data Set API using Apache HTTP Client.
 * Tests the metadata endpoints for listing datasets and fields.
 */
@Slf4j
public class ApacheHttpDataSetTest extends BaseApiTest {
    
    private CloseableHttpClient httpClient;
    
    @BeforeClass
    public void setUp() {
        log.info("Setting up Apache HTTP Client for DataSet tests");
        httpClient = HttpClients.createDefault();
    }
    
    @AfterClass
    public void tearDown() throws IOException {
        if (httpClient != null) {
            httpClient.close();
            log.info("Apache HTTP Client closed");
        }
    }
    
    @Test(description = "Verify that the API returns a list of available datasets")
    public void testListDataSets() throws IOException {
        // Arrange
        String url = buildUrl("/");
        HttpGet request = new HttpGet(url);
        logRequest("GET", url);
        
        // Act
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logResponse(response.getCode(), responseBody);
            
            // Assert
            verificationService.verifyStatusCode(response, 200);
            verificationService.verifyValidJson(responseBody);
            
            DataSetList dataSetList = gson.fromJson(responseBody, DataSetList.class);
            verificationService.verifyDataSetList(dataSetList);
            
            log.info("Successfully retrieved {} datasets", dataSetList.getTotal());
        }
    }
    
    @Test(description = "Verify that each dataset has valid API information")
    public void testDataSetApiInfo() throws IOException {
        // Arrange
        String url = buildUrl("/");
        HttpGet request = new HttpGet(url);
        logRequest("GET", url);
        
        // Act
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            
            DataSetList dataSetList = gson.fromJson(responseBody, DataSetList.class);
            
            // Assert
            assertThat(dataSetList.getApis())
                    .as("APIs list")
                    .isNotEmpty();
            
            for (ApiInfo apiInfo : dataSetList.getApis()) {
                verificationService.verifyApiInfo(apiInfo);
                log.info("Verified API info for dataset: {}", apiInfo.getApiKey());
            }
        }
    }
    
    @Test(description = "Verify that searchable fields can be retrieved for a dataset")
    public void testGetSearchableFields() throws IOException {
        // Arrange
        String url = buildFieldsUrl(DEFAULT_DATASET, DEFAULT_VERSION);
        HttpGet request = new HttpGet(url);
        logRequest("GET", url);
        
        // Act
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logResponse(response.getCode(), responseBody);
            
            // Assert
            verificationService.verifyStatusCode(response, 200);
            verificationService.verifyValidJson(responseBody);
            
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> fieldsResponse = gson.fromJson(responseBody, type);
            
            assertThat(fieldsResponse)
                    .as("Fields response")
                    .containsKey("fields");
            
            @SuppressWarnings("unchecked")
            List<String> fields = (List<String>) fieldsResponse.get("fields");
            verificationService.verifyFieldsList(fields);
            
            log.info("Successfully retrieved {} searchable fields", fields.size());
        }
    }
    
    @Test(description = "Verify that 404 is returned for non-existent dataset")
    public void testGetFieldsForNonExistentDataset() throws IOException {
        // Arrange
        String url = buildFieldsUrl("non_existent_dataset", DEFAULT_VERSION);
        HttpGet request = new HttpGet(url);
        logRequest("GET", url);
        
        // Act
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logResponse(response.getCode(), responseBody);
            
            // Assert
            verificationService.verifyStatusCode(response, 404);
            log.info("Correctly received 404 for non-existent dataset");
        }
    }
    
    @Test(description = "Verify content type header is application/json")
    public void testContentTypeHeader() throws IOException {
        // Arrange
        String url = buildUrl("/");
        HttpGet request = new HttpGet(url);
        logRequest("GET", url);
        
        // Act
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String contentType = response.getEntity().getContentType();
            
            // Assert
            verificationService.verifyContentTypeJson(contentType);
            log.info("Content-Type header verified: {}", contentType);
        }
    }
}
