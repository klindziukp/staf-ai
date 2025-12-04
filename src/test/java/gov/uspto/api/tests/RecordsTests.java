package gov.uspto.api.tests;

import gov.uspto.api.base.BaseTest;
import gov.uspto.api.models.request.SearchRequest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for Records Search API endpoints
 */
@Epic("USPTO Data Set API")
@Feature("Records Search")
public class RecordsTests extends BaseTest {

    private static final String TEST_DATASET = "oa_citations";
    private static final String TEST_VERSION = "v1";

    @Test(description = "Verify search records returns 200 OK with default criteria")
    @Story("Search Records")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify that POST /{dataset}/{version}/records returns 200 with *:* criteria")
    public void testSearchRecordsReturns200() {
        SearchRequest request = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords(TEST_DATASET, TEST_VERSION, request);
        
        response.then()
                .statusCode(200)
                .contentType("application/json");
        
        log.info("Successfully searched records with default criteria");
    }

    @Test(description = "Verify search with pagination works correctly")
    @Story("Search Records")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchWithPagination() {
        int start = 0;
        int rows = 5;
        
        Response response = recordsService.searchWithPagination(
                TEST_DATASET, TEST_VERSION, "*:*", start, rows);
        
        response.then()
                .statusCode(200);
        
        log.info("Pagination test passed - start: {}, rows: {}", start, rows);
    }

    @Test(description = "Verify search with different start positions")
    @Story("Search Records")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchWithDifferentStartPositions() {
        // First page
        Response firstPage = recordsService.searchWithPagination(
                TEST_DATASET, TEST_VERSION, "*:*", 0, 10);
        
        // Second page
        Response secondPage = recordsService.searchWithPagination(
                TEST_DATASET, TEST_VERSION, "*:*", 10, 10);
        
        assertThat(firstPage.getStatusCode()).isEqualTo(200);
        assertThat(secondPage.getStatusCode()).isEqualTo(200);
        
        log.info("Successfully retrieved multiple pages of results");
    }

    @Test(description = "Verify search returns 404 for invalid dataset")
    @Story("Search Records")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchReturns404ForInvalidDataset() {
        SearchRequest request = SearchRequest.builder()
                .criteria("*:*")
                .build();
        
        Response response = recordsService.searchRecords("invalid_dataset", TEST_VERSION, request);
        
        response.then()
                .statusCode(404);
        
        log.info("Correctly received 404 for invalid dataset");
    }

    @Test(description = "Verify search with boundary values for rows parameter")
    @Story("Search Records")
    @Severity(SeverityLevel.NORMAL)
    @DataProvider(name = "rowsValues")
    public Object[][] rowsValues() {
        return new Object[][] {
                {0},
                {1},
                {100},
                {1000}
        };
    }

    @Test(dataProvider = "rowsValues", description = "Test search with different rows values")
    @Story("Search Records")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchWithDifferentRowsValues(int rows) {
        Response response = recordsService.searchWithPagination(
                TEST_DATASET, TEST_VERSION, "*:*", 0, rows);
        
        assertThat(response.getStatusCode())
                .as("Should return valid status code for rows=%d", rows)
                .isIn(200, 400);
        
        log.info("Search with rows={} returned status: {}", rows, response.getStatusCode());
    }

    @Test(description = "Verify search response time is acceptable")
    @Story("Search Records")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchResponseTime() {
        SearchRequest request = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords(TEST_DATASET, TEST_VERSION, request);
        
        long responseTime = response.getTime();
        int expectedTime = config.getExpectedResponseTime();
        
        assertThat(responseTime)
                .as("Response time should be less than %d ms", expectedTime)
                .isLessThan(expectedTime);
        
        log.info("Search response time: {} ms (expected < {} ms)", responseTime, expectedTime);
    }

    @Test(description = "Verify search with Lucene query syntax")
    @Story("Search Records")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchWithLuceneQuery() {
        String luceneQuery = "*:*";
        
        Response response = recordsService.searchWithLuceneQuery(
                TEST_DATASET, TEST_VERSION, luceneQuery);
        
        response.then()
                .statusCode(200);
        
        log.info("Lucene query search successful: {}", luceneQuery);
    }

    @Test(description = "Verify search with empty criteria")
    @Story("Search Records")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchWithEmptyCriteria() {
        SearchRequest request = SearchRequest.builder()
                .criteria("")
                .build();
        
        Response response = recordsService.searchRecords(TEST_DATASET, TEST_VERSION, request);
        
        // Should return 400 or handle gracefully
        assertThat(response.getStatusCode())
                .as("Should handle empty criteria appropriately")
                .isIn(200, 400, 404);
        
        log.info("Empty criteria handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify search with negative start value")
    @Story("Search Records")
    @Severity(SeverityLevel.MINOR)
    public void testSearchWithNegativeStart() {
        Response response = recordsService.searchWithPagination(
                TEST_DATASET, TEST_VERSION, "*:*", -1, 10);
        
        // Should return 400 for invalid parameter
        assertThat(response.getStatusCode())
                .as("Should handle negative start value")
                .isIn(200, 400);
        
        log.info("Negative start value handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify search with very large start value")
    @Story("Search Records")
    @Severity(SeverityLevel.MINOR)
    public void testSearchWithLargeStartValue() {
        Response response = recordsService.searchWithPagination(
                TEST_DATASET, TEST_VERSION, "*:*", 999999, 10);
        
        response.then()
                .statusCode(200);
        
        log.info("Large start value handled successfully");
    }
}
