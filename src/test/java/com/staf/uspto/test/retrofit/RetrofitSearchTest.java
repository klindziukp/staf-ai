package com.staf.uspto.test.retrofit;

import com.staf.uspto.api.UsptoApiService;
import com.staf.uspto.model.SearchRequest;
import com.staf.uspto.test.BaseApiTest;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for USPTO Data Set API search functionality using Retrofit.
 * Tests the search/records endpoint with various search criteria.
 */
@Slf4j
public class RetrofitSearchTest extends BaseApiTest {
    
    private UsptoApiService apiService;
    
    @BeforeClass
    public void setUp() {
        log.info("Setting up Retrofit client for Search tests");
        
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        
        apiService = retrofit.create(UsptoApiService.class);
        log.info("Retrofit client configured successfully");
    }
    
    @Test(description = "Verify search with default criteria returns results using Retrofit")
    public void testSearchWithDefaultCriteriaUsingRetrofit() throws IOException {
        // Arrange
        String url = buildSearchUrl(DEFAULT_DATASET, DEFAULT_VERSION);
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        logRequest("POST", url, gson.toJson(searchRequest));
        
        // Act
        Response<Map<String, Object>> response = apiService.searchRecords(
                DEFAULT_DATASET,
                DEFAULT_VERSION,
                searchRequest.getCriteria(),
                searchRequest.getStart(),
                searchRequest.getRows()
        ).execute();
        
        // Assert
        assertThat(response.isSuccessful())
                .as("Response is successful")
                .isTrue();
        
        assertThat(response.code())
                .as("HTTP Status Code")
                .isEqualTo(200);
        
        Map<String, Object> searchResponse = response.body();
        assertThat(searchResponse).isNotNull();
        
        verificationService.verifySearchResponse(searchResponse);
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> docs = (List<Map<String, Object>>) searchResponse.get("docs");
        verificationService.verifyRecordsCount(docs, 1);
        
        logResponse(response.code(), gson.toJson(searchResponse));
        log.info("Search returned {} records using Retrofit", docs.size());
    }
    
    @Test(description = "Verify search with specific row limit using Retrofit")
    public void testSearchWithRowLimitUsingRetrofit() throws IOException {
        // Arrange
        String url = buildSearchUrl(DEFAULT_DATASET, DEFAULT_VERSION);
        int rowLimit = 5;
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(rowLimit)
                .build();
        
        logRequest("POST", url, gson.toJson(searchRequest));
        
        // Act
        Response<Map<String, Object>> response = apiService.searchRecords(
                DEFAULT_DATASET,
                DEFAULT_VERSION,
                searchRequest.getCriteria(),
                searchRequest.getStart(),
                searchRequest.getRows()
        ).execute();
        
        // Assert
        assertThat(response.isSuccessful()).isTrue();
        
        Map<String, Object> searchResponse = response.body();
        assertThat(searchResponse).isNotNull();
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> docs = (List<Map<String, Object>>) searchResponse.get("docs");
        
        assertThat(docs)
                .as("Search results")
                .hasSizeLessThanOrEqualTo(rowLimit);
        
        log.info("Search with row limit {} returned {} records using Retrofit", rowLimit, docs.size());
    }
    
    @Test(description = "Verify search pagination using Retrofit")
    public void testSearchPaginationUsingRetrofit() throws IOException {
        // Arrange
        String url = buildSearchUrl(DEFAULT_DATASET, DEFAULT_VERSION);
        
        // First page
        SearchRequest firstPageRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(5)
                .build();
        
        logRequest("POST", url, "First page: " + gson.toJson(firstPageRequest));
        
        // Act & Assert - First Page
        Response<Map<String, Object>> firstResponse = apiService.searchRecords(
                DEFAULT_DATASET,
                DEFAULT_VERSION,
                firstPageRequest.getCriteria(),
                firstPageRequest.getStart(),
                firstPageRequest.getRows()
        ).execute();
        
        assertThat(firstResponse.isSuccessful()).isTrue();
        
        Map<String, Object> firstSearchResponse = firstResponse.body();
        assertThat(firstSearchResponse).isNotNull();
        
        Double numFound = (Double) firstSearchResponse.get("numFound");
        Double firstStart = (Double) firstSearchResponse.get("start");
        
        assertThat(firstStart.intValue())
                .as("Start index for first page")
                .isEqualTo(0);
        
        log.info("First page - Total records: {}, Start: {} using Retrofit", 
                numFound.intValue(), firstStart.intValue());
        
        // Second page
        SearchRequest secondPageRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(5)
                .rows(5)
                .build();
        
        logRequest("POST", url, "Second page: " + gson.toJson(secondPageRequest));
        
        // Act & Assert - Second Page
        Response<Map<String, Object>> secondResponse = apiService.searchRecords(
                DEFAULT_DATASET,
                DEFAULT_VERSION,
                secondPageRequest.getCriteria(),
                secondPageRequest.getStart(),
                secondPageRequest.getRows()
        ).execute();
        
        assertThat(secondResponse.isSuccessful()).isTrue();
        
        Map<String, Object> secondSearchResponse = secondResponse.body();
        assertThat(secondSearchResponse).isNotNull();
        
        Double secondStart = (Double) secondSearchResponse.get("start");
        
        assertThat(secondStart.intValue())
                .as("Start index for second page")
                .isEqualTo(5);
        
        log.info("Second page - Start: {} using Retrofit", secondStart.intValue());
    }
    
    @Test(description = "Verify search with zero rows returns no documents using Retrofit")
    public void testSearchWithZeroRowsUsingRetrofit() throws IOException {
        // Arrange
        String url = buildSearchUrl(DEFAULT_DATASET, DEFAULT_VERSION);
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(0)
                .build();
        
        logRequest("POST", url, gson.toJson(searchRequest));
        
        // Act
        Response<Map<String, Object>> response = apiService.searchRecords(
                DEFAULT_DATASET,
                DEFAULT_VERSION,
                searchRequest.getCriteria(),
                searchRequest.getStart(),
                searchRequest.getRows()
        ).execute();
        
        // Assert
        assertThat(response.isSuccessful()).isTrue();
        
        Map<String, Object> searchResponse = response.body();
        assertThat(searchResponse).isNotNull();
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> docs = (List<Map<String, Object>>) searchResponse.get("docs");
        
        assertThat(docs)
                .as("Documents with zero rows")
                .isEmpty();
        
        log.info("Search with zero rows correctly returned no documents using Retrofit");
    }
    
    @Test(description = "Verify search for non-existent dataset returns 404 using Retrofit")
    public void testSearchNonExistentDatasetUsingRetrofit() throws IOException {
        // Arrange
        String nonExistentDataset = "non_existent_dataset";
        String url = buildSearchUrl(nonExistentDataset, DEFAULT_VERSION);
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        logRequest("POST", url, gson.toJson(searchRequest));
        
        // Act
        Response<Map<String, Object>> response = apiService.searchRecords(
                nonExistentDataset,
                DEFAULT_VERSION,
                searchRequest.getCriteria(),
                searchRequest.getStart(),
                searchRequest.getRows()
        ).execute();
        
        // Assert
        assertThat(response.isSuccessful())
                .as("Response should not be successful")
                .isFalse();
        
        assertThat(response.code())
                .as("HTTP Status Code")
                .isEqualTo(404);
        
        log.info("Correctly received 404 for non-existent dataset search using Retrofit");
    }
    
    @Test(description = "Verify search response structure using Retrofit")
    public void testSearchResponseStructureUsingRetrofit() throws IOException {
        // Arrange
        String url = buildSearchUrl(DEFAULT_DATASET, DEFAULT_VERSION);
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(1)
                .build();
        
        logRequest("POST", url, gson.toJson(searchRequest));
        
        // Act
        Response<Map<String, Object>> response = apiService.searchRecords(
                DEFAULT_DATASET,
                DEFAULT_VERSION,
                searchRequest.getCriteria(),
                searchRequest.getStart(),
                searchRequest.getRows()
        ).execute();
        
        // Assert
        assertThat(response.isSuccessful()).isTrue();
        
        Map<String, Object> searchResponse = response.body();
        assertThat(searchResponse).isNotNull();
        
        // Verify response contains expected fields
        assertThat(searchResponse)
                .as("Search response structure")
                .containsKeys("numFound", "start", "docs");
        
        log.info("Search response structure verified using Retrofit");
    }
}
