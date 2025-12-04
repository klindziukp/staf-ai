package gov.uspto.api.tests;

import gov.uspto.api.base.BaseTest;
import gov.uspto.api.models.response.DataSetListResponse;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test class for Dataset API endpoints
 */
@Epic("USPTO Data Set API")
@Feature("Dataset Management")
public class DataSetTests extends BaseTest {

    @Test(description = "Verify list of available datasets returns 200 OK")
    @Story("List Datasets")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify that GET / endpoint returns 200 status code and valid response")
    public void testGetDataSetsReturns200() {
        Response response = dataSetService.getDataSets();
        
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("total", notNullValue());
        
        log.info("Successfully retrieved datasets list with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify datasets response contains required fields")
    @Story("List Datasets")
    @Severity(SeverityLevel.CRITICAL)
    public void testDataSetsResponseStructure() {
        DataSetListResponse response = dataSetService.getDataSetsAsPojo();
        
        assertThat(response).isNotNull();
        assertThat(response.getTotal()).isNotNull().isGreaterThanOrEqualTo(0);
        
        if (response.getTotal() > 0) {
            assertThat(response.getApis()).isNotEmpty();
            response.getApis().forEach(dataset -> {
                assertThat(dataset.getApiKey()).isNotNull().isNotEmpty();
                assertThat(dataset.getApiVersionNumber()).isNotNull().isNotEmpty();
                assertThat(dataset.getApiUrl()).isNotNull().isNotEmpty();
            });
        }
        
        log.info("Dataset response structure validation passed. Total datasets: {}", response.getTotal());
    }

    @Test(description = "Verify response time is within acceptable limits")
    @Story("List Datasets")
    @Severity(SeverityLevel.NORMAL)
    public void testDataSetsResponseTime() {
        Response response = dataSetService.getDataSets();
        
        long responseTime = response.getTime();
        int expectedTime = config.getExpectedResponseTime();
        
        assertThat(responseTime)
                .as("Response time should be less than %d ms", expectedTime)
                .isLessThan(expectedTime);
        
        log.info("Response time: {} ms (expected < {} ms)", responseTime, expectedTime);
    }

    @Test(description = "Verify specific dataset exists in the list")
    @Story("List Datasets")
    @Severity(SeverityLevel.NORMAL)
    public void testSpecificDatasetExists() {
        String expectedDataset = "oa_citations";
        boolean exists = dataSetService.datasetExists(expectedDataset);
        
        assertThat(exists)
                .as("Dataset '%s' should exist in the list", expectedDataset)
                .isTrue();
        
        log.info("Dataset '{}' found in the list", expectedDataset);
    }

    @Test(description = "Verify datasets list is not empty")
    @Story("List Datasets")
    @Severity(SeverityLevel.CRITICAL)
    public void testDataSetsListNotEmpty() {
        DataSetListResponse response = dataSetService.getDataSetsAsPojo();
        
        assertThat(response.getTotal())
                .as("Total datasets should be greater than 0")
                .isGreaterThan(0);
        
        assertThat(response.getApis())
                .as("APIs list should not be empty")
                .isNotEmpty()
                .hasSize(response.getTotal());
        
        log.info("Datasets list contains {} items", response.getTotal());
    }

    @Test(description = "Verify each dataset has valid URL format")
    @Story("List Datasets")
    @Severity(SeverityLevel.NORMAL)
    public void testDatasetUrlFormat() {
        DataSetListResponse response = dataSetService.getDataSetsAsPojo();
        
        response.getApis().forEach(dataset -> {
            assertThat(dataset.getApiUrl())
                    .as("API URL should be valid")
                    .startsWith("http")
                    .contains("developer.uspto.gov");
            
            if (dataset.getApiDocumentationUrl() != null) {
                assertThat(dataset.getApiDocumentationUrl())
                        .as("Documentation URL should be valid")
                        .startsWith("http");
            }
        });
        
        log.info("All dataset URLs are valid");
    }
}
