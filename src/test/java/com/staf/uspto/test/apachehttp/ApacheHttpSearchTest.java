package com.staf.uspto.test.apachehttp;

import com.google.gson.reflect.TypeToken;
import com.staf.uspto.model.SearchRequest;
import com.staf.uspto.test.BaseApiTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for USPTO Data Set API search functionality using Apache HTTP Client.
 * Tests the search/records endpoint with various search criteria.
 */
@Slf4j
public class ApacheHttpSearchTest extends BaseApiTest {
    
    private CloseableHttpClient httpClient;
    
    @BeforeClass
    public void setUp() {
        log.info("Setting up Apache HTTP Client for Search tests");
        httpClient = HttpClients.createDefault();
    }
    
    @AfterClass
    public void tearDown() throws IOException {
        if (httpClient != null) {
            httpClient.close();
            log.info("Apache HTTP Client closed");
        }
    }
    
    @Test(description = "Verify search with default criteria returns results")
    public void testSearchWithDefaultCriteria() throws IOException {
        // Arrange
        String url = buildSearchUrl(DEFAULT_DATASET, DEFAULT_VERSION);
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        HttpPost request = createSearchRequest(url, searchRequest);
        logRequest("POST", url, gson.toJson(searchRequest));
        
        // Act
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logResponse(response.getCode(), responseBody);
            
            // Assert
            verificationService.verifyStatusCode(response, 200);
            verificationService.verifyValidJson(responseBody);
            
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> searchResponse = gson.fromJson(responseBody, type);
            verificationService.verifySearchResponse(searchResponse);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> docs = (List<Map<String, Object>>) searchResponse.get("docs");
            verificationService.verifyRecordsCount(docs, 1);
            
            log.info("Search returned {} records", docs.size());
        }
    }
    
    @Test(description = "Verify search with specific criteria filters results")
    public void testSearchWithSpecificCriteria() throws IOException {
        // Arrange
        String url = buildSearchUrl(DEFAULT_DATASET, DEFAULT_VERSION);
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(5)
                .build();
        
        HttpPost request = createSearchRequest(url, searchRequest);
        logRequest("POST", url, gson.toJson(searchRequest));
        
        // Act
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logResponse(response.getCode(), responseBody);
            
            // Assert
            verificationService.verifyStatusCode(response, 200);
            
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> searchResponse = gson.fromJson(responseBody, type);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> docs = (List<Map<String, Object>>) searchResponse.get("docs");
            
            assertThat(docs)
                    .as("Search results")
                    .hasSizeLessThanOrEqualTo(5);
            
            log.info("Search with specific criteria returned {} records", docs.size());
        }
    }
    
    @Test(description = "Verify search pagination works correctly")
    public void testSearchPagination() throws IOException {
        // Arrange
        String url = buildSearchUrl(DEFAULT_DATASET, DEFAULT_VERSION);
        
        // First page
        SearchRequest firstPageRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(5)
                .build();
        
        HttpPost firstRequest = createSearchRequest(url, firstPageRequest);
        logRequest("POST", url, "First page: " + gson.toJson(firstPageRequest));
        
        // Act & Assert - First Page
        try (CloseableHttpResponse response = httpClient.execute(firstRequest)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> searchResponse = gson.fromJson(responseBody, type);
            
            Double numFound = (Double) searchResponse.get("numFound");
            Double start = (Double) searchResponse.get("start");
            
            assertThat(start.intValue())
                    .as("Start index for first page")
                    .isEqualTo(0);
            
            log.info("First page - Total records: {}, Start: {}", numFound.intValue(), start.intValue());
        }
        
        // Second page
        SearchRequest secondPageRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(5)
                .rows(5)
                .build();
        
        HttpPost secondRequest = createSearchRequest(url, secondPageRequest);
        logRequest("POST", url, "Second page: " + gson.toJson(secondPageRequest));
        
        // Act & Assert - Second Page
        try (CloseableHttpResponse response = httpClient.execute(secondRequest)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> searchResponse = gson.fromJson(responseBody, type);
            
            Double start = (Double) searchResponse.get("start");
            
            assertThat(start.intValue())
                    .as("Start index for second page")
                    .isEqualTo(5);
            
            log.info("Second page - Start: {}", start.intValue());
        }
    }
    
    @Test(description = "Verify search with zero rows returns no documents")
    public void testSearchWithZeroRows() throws IOException {
        // Arrange
        String url = buildSearchUrl(DEFAULT_DATASET, DEFAULT_VERSION);
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(0)
                .build();
        
        HttpPost request = createSearchRequest(url, searchRequest);
        logRequest("POST", url, gson.toJson(searchRequest));
        
        // Act
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logResponse(response.getCode(), responseBody);
            
            // Assert
            verificationService.verifyStatusCode(response, 200);
            
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> searchResponse = gson.fromJson(responseBody, type);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> docs = (List<Map<String, Object>>) searchResponse.get("docs");
            
            assertThat(docs)
                    .as("Documents with zero rows")
                    .isEmpty();
            
            log.info("Search with zero rows correctly returned no documents");
        }
    }
    
    @Test(description = "Verify search for non-existent dataset returns 404")
    public void testSearchNonExistentDataset() throws IOException {
        // Arrange
        String url = buildSearchUrl("non_existent_dataset", DEFAULT_VERSION);
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        HttpPost request = createSearchRequest(url, searchRequest);
        logRequest("POST", url, gson.toJson(searchRequest));
        
        // Act
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logResponse(response.getCode(), responseBody);
            
            // Assert
            verificationService.verifyStatusCode(response, 404);
            log.info("Correctly received 404 for non-existent dataset search");
        }
    }
    
    /**
     * Helper method to create a POST request with form data.
     *
     * @param url the request URL
     * @param searchRequest the search request parameters
     * @return configured HttpPost request
     */
    private HttpPost createSearchRequest(String url, SearchRequest searchRequest) {
        HttpPost httpPost = new HttpPost(url);
        
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("criteria", searchRequest.getCriteria()));
        params.add(new BasicNameValuePair("start", searchRequest.getStart().toString()));
        params.add(new BasicNameValuePair("rows", searchRequest.getRows().toString()));
        
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        
        return httpPost;
    }
}
