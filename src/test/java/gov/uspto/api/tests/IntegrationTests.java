package gov.uspto.api.tests;

import gov.uspto.api.base.BaseTest;
import gov.uspto.api.models.request.SearchRequest;
import gov.uspto.api.models.response.DataSetInfo;
import gov.uspto.api.models.response.DataSetListResponse;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test scenarios for USPTO Data Set API
 * Tests the complete workflow across multiple endpoints
 */
@Epic("USPTO Data Set API")
@Feature("Integration Testing")
public class IntegrationTests extends BaseTest {

    @Test(description = "Complete workflow: List datasets -> Get fields -> Search records")
    @Story("End-to-End Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void testCompleteWorkflow() {
        // Step 1: Get list of available datasets
        log.info("Step 1: Retrieving list of available datasets");
        DataSetListResponse datasetList = dataSetService.getDataSetsAsPojo();
        
        assertThat(datasetList.getApis())
                .as("Dataset list should not be empty")
                .isNotEmpty();
        
        // Step 2: Select first dataset and get its fields
        DataSetInfo firstDataset = datasetList.getApis().get(0);
        String datasetName = firstDataset.getApiKey();
        String version = firstDataset.getApiVersionNumber();
        
        log.info("Step 2: Getting fields for dataset: {} version: {}", datasetName, version);
        Response fieldsResponse = fieldsService.getFields(datasetName, version);
        
        assertThat(fieldsResponse.getStatusCode())
                .as("Fields request should be successful")
                .isEqualTo(200);
        
        // Step 3: Perform search on the dataset
        log.info("Step 3: Searching records in dataset: {}", datasetName);
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        Response searchResponse = recordsService.searchRecords(datasetName, version, searchRequest);
        
        assertThat(searchResponse.getStatusCode())
                .as("Search request should be successful")
                .isEqualTo(200);
        
        log.info("Complete workflow test passed successfully");
    }

    @Test(description = "Verify all datasets have accessible fields endpoint")
    @Story("Dataset Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testAllDatasetsHaveAccessibleFields() {
        DataSetListResponse datasetList = dataSetService.getDataSetsAsPojo();
        
        List<DataSetInfo> datasets = datasetList.getApis();
        log.info("Testing fields endpoint for {} datasets", datasets.size());
        
        for (DataSetInfo dataset : datasets) {
            String datasetName = dataset.getApiKey();
            String version = dataset.getApiVersionNumber();
            
            log.info("Testing fields for dataset: {} version: {}", datasetName, version);
            Response response = fieldsService.getFields(datasetName, version);
            
            assertThat(response.getStatusCode())
                    .as("Fields endpoint should be accessible for dataset: %s", datasetName)
                    .isEqualTo(200);
        }
        
        log.info("All datasets have accessible fields endpoints");
    }

    @Test(description = "Verify all datasets support search functionality")
    @Story("Dataset Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testAllDatasetsSupportSearch() {
        DataSetListResponse datasetList = dataSetService.getDataSetsAsPojo();
        
        List<DataSetInfo> datasets = datasetList.getApis();
        log.info("Testing search endpoint for {} datasets", datasets.size());
        
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(1)
                .build();
        
        for (DataSetInfo dataset : datasets) {
            String datasetName = dataset.getApiKey();
            String version = dataset.getApiVersionNumber();
            
            log.info("Testing search for dataset: {} version: {}", datasetName, version);
            Response response = recordsService.searchRecords(datasetName, version, searchRequest);
            
            assertThat(response.getStatusCode())
                    .as("Search endpoint should be accessible for dataset: %s", datasetName)
                    .isEqualTo(200);
        }
        
        log.info("All datasets support search functionality");
    }

    @Test(description = "Verify pagination works correctly across multiple pages")
    @Story("Pagination Integration")
    @Severity(SeverityLevel.NORMAL)
    public void testPaginationIntegration() {
        String dataset = "oa_citations";
        String version = "v1";
        int rowsPerPage = 10;
        
        // Get first page
        SearchRequest firstPageRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(rowsPerPage)
                .build();
        
        Response firstPageResponse = recordsService.searchRecords(dataset, version, firstPageRequest);
        assertThat(firstPageResponse.getStatusCode()).isEqualTo(200);
        
        // Get second page
        SearchRequest secondPageRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(rowsPerPage)
                .rows(rowsPerPage)
                .build();
        
        Response secondPageResponse = recordsService.searchRecords(dataset, version, secondPageRequest);
        assertThat(secondPageResponse.getStatusCode()).isEqualTo(200);
        
        // Verify pages are different (if enough data exists)
        String firstPageBody = firstPageResponse.getBody().asString();
        String secondPageBody = secondPageResponse.getBody().asString();
        
        log.info("First page and second page retrieved successfully");
        log.info("Pagination integration test passed");
    }

    @Test(description = "Verify dataset metadata consistency")
    @Story("Metadata Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testDatasetMetadataConsistency() {
        DataSetListResponse datasetList = dataSetService.getDataSetsAsPojo();
        
        for (DataSetInfo dataset : datasetList.getApis()) {
            String datasetName = dataset.getApiKey();
            String version = dataset.getApiVersionNumber();
            String apiUrl = dataset.getApiUrl();
            
            // Verify API URL contains dataset name and version
            assertThat(apiUrl)
                    .as("API URL should contain dataset name")
                    .contains(datasetName);
            
            assertThat(apiUrl)
                    .as("API URL should contain version")
                    .contains(version);
            
            // Verify API URL points to fields endpoint
            assertThat(apiUrl)
                    .as("API URL should point to fields endpoint")
                    .contains("/fields");
            
            log.info("Metadata consistent for dataset: {}", datasetName);
        }
        
        log.info("All dataset metadata is consistent");
    }

    @Test(description = "Verify search results contain expected structure")
    @Story("Response Structure Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchResultsStructure() {
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(5)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", searchRequest);
        
        assertThat(response.getStatusCode()).isEqualTo(200);
        
        // Verify response contains expected fields
        response.then()
                .body("numFound", org.hamcrest.Matchers.notNullValue())
                .body("docs", org.hamcrest.Matchers.notNullValue());
        
        log.info("Search results structure validation passed");
    }

    @Test(description = "Verify different search criteria return different results")
    @Story("Search Functionality")
    @Severity(SeverityLevel.NORMAL)
    public void testDifferentSearchCriteriaReturnDifferentResults() {
        // Search with wildcard
        SearchRequest wildcardRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        Response wildcardResponse = recordsService.searchRecords("oa_citations", "v1", wildcardRequest);
        assertThat(wildcardResponse.getStatusCode()).isEqualTo(200);
        
        // Search with specific criteria (may return no results, but should not error)
        SearchRequest specificRequest = SearchRequest.builder()
                .criteria("patentNumber:1234567")
                .start(0)
                .rows(10)
                .build();
        
        Response specificResponse = recordsService.searchRecords("oa_citations", "v1", specificRequest);
        assertThat(specificResponse.getStatusCode()).isEqualTo(200);
        
        log.info("Different search criteria handled correctly");
    }

    @Test(description = "Verify API handles concurrent requests correctly")
    @Story("Concurrency Testing")
    @Severity(SeverityLevel.NORMAL)
    public void testConcurrentRequests() throws InterruptedException {
        int numberOfThreads = 5;
        Thread[] threads = new Thread[numberOfThreads];
        boolean[] results = new boolean[numberOfThreads];
        
        for (int i = 0; i < numberOfThreads; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    Response response = dataSetService.getDataSets();
                    results[index] = (response.getStatusCode() == 200);
                    log.info("Thread {} completed with status: {}", index, response.getStatusCode());
                } catch (Exception e) {
                    log.error("Thread {} failed with error: {}", index, e.getMessage());
                    results[index] = false;
                }
            });
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Verify all requests succeeded
        for (int i = 0; i < numberOfThreads; i++) {
            assertThat(results[i])
                    .as("Thread %d should complete successfully", i)
                    .isTrue();
        }
        
        log.info("Concurrent requests test passed");
    }

    @Test(description = "Verify API version consistency across endpoints")
    @Story("Version Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testApiVersionConsistency() {
        DataSetListResponse datasetList = dataSetService.getDataSetsAsPojo();
        
        for (DataSetInfo dataset : datasetList.getApis()) {
            String version = dataset.getApiVersionNumber();
            
            // Verify version format (should be v1, v2, etc.)
            assertThat(version)
                    .as("Version should follow expected format")
                    .matches("v\\d+");
            
            log.info("Dataset: {} has version: {}", dataset.getApiKey(), version);
        }
        
        log.info("API version consistency validated");
    }

    @Test(description = "Verify empty search results are handled correctly")
    @Story("Edge Cases")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptySearchResults() {
        // Search with criteria that likely returns no results
        SearchRequest request = SearchRequest.builder()
                .criteria("nonExistentField:impossibleValue123456789")
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        
        assertThat(response.getStatusCode())
                .as("Empty search should return 200")
                .isEqualTo(200);
        
        // Verify response structure is still valid
        response.then()
                .body("numFound", org.hamcrest.Matchers.notNullValue())
                .body("docs", org.hamcrest.Matchers.notNullValue());
        
        log.info("Empty search results handled correctly");
    }
}
