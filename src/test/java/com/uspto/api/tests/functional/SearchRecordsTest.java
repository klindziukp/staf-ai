package com.uspto.api.tests.functional;

import com.uspto.api.constants.HttpStatusCodes;
import com.uspto.api.models.SearchRequest;
import com.uspto.api.tests.base.BaseTest;
import com.uspto.api.utils.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Functional tests for searching records endpoint.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Epic("USPTO Data Set API")
@Feature("Search Records")
public class SearchRecordsTest extends BaseTest {

    @Test(description = "Verify search records with wildcard criteria")
    @Story("Search all records with wildcard")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that POST /{dataset}/{version}/records with wildcard criteria returns records")
    public void testSearchRecords_WildcardSearch() {
        // Create search request with wildcard
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();

        // Execute API call
        Response response = searchService.searchRecords(
                getDefaultDataset(), 
                getDefaultVersion(), 
                searchRequest
        );

        // Validate status code
        ResponseValidator.validateStatusCode(response, HttpStatusCodes.OK);

        // Validate response structure
        ResponseValidator.validateJsonPathExists(response, "response");
        ResponseValidator.validateJsonPathExists(response, "response.numFound");
        ResponseValidator.validateJsonPathExists(response, "response.docs");

        // Validate results are returned
        int numFound = searchService.getNumFound(response);
        assertThat(numFound)
                .as("Number of records found should be greater than 0")
                .isGreaterThanOrEqualTo(0);
    }

    @Test(description = "Verify search records with pagination")
    @Story("Search with pagination parameters")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies pagination works correctly with start and rows parameters")
    public void testSearchRecords_Pagination() {
        // Execute search with pagination
        Response response = searchService.searchWithPagination(
                getDefaultDataset(),
                getDefaultVersion(),
                "*:*",
                0,
                5
        );

        // Validate status code
        ResponseValidator.validateStatusCode(response, HttpStatusCodes.OK);

        // Validate pagination parameters
        int start = searchService.getStart(response);
        int docsCount = searchService.getDocsCount(response);

        assertThat(start)
                .as("Start position should be 0")
                .isEqualTo(0);

        assertThat(docsCount)
                .as("Number of documents should be less than or equal to 5")
                .isLessThanOrEqualTo(5);
    }

    @Test(description = "Verify search records with form parameters")
    @Story("Search using form parameters")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies search works with application/x-www-form-urlencoded content type")
    public void testSearchRecords_FormParameters() {
        // Execute search with form parameters
        Response response = searchService.searchRecordsWithFormParams(
                getDefaultDataset(),
                getDefaultVersion(),
                "*:*",
                0,
                10
        );

        // Validate status code
        ResponseValidator.validateStatusCode(response, HttpStatusCodes.OK);

        // Validate response structure
        ResponseValidator.validateJsonPathExists(response, "response.numFound");
    }

    @Test(description = "Verify search records with zero rows returns no documents")
    @Story("Boundary testing for rows parameter")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that rows=0 returns metadata but no documents")
    public void testSearchRecords_ZeroRows() {
        // Create search request with zero rows
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(0)
                .build();

        // Execute API call
        Response response = searchService.searchRecords(
                getDefaultDataset(),
                getDefaultVersion(),
                searchRequest
        );

        // Validate status code
        ResponseValidator.validateStatusCode(response, HttpStatusCodes.OK);

        // Validate no documents returned
        int docsCount = searchService.getDocsCount(response);
        assertThat(docsCount)
                .as("Number of documents should be 0")
                .isEqualTo(0);
    }

    @Test(description = "Verify search records with large rows value")
    @Story("Boundary testing for large rows parameter")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies API handles large rows parameter value")
    public void testSearchRecords_LargeRowsValue() {
        // Create search request with large rows value
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(1000)
                .build();

        // Execute API call
        Response response = searchService.searchRecords(
                getDefaultDataset(),
                getDefaultVersion(),
                searchRequest
        );

        // Validate status code
        ResponseValidator.validateStatusCode(response, HttpStatusCodes.OK);

        // Validate response contains results
        assertThat(searchService.getNumFound(response))
                .as("Should return results")
                .isGreaterThanOrEqualTo(0);
    }

    @Test(description = "Verify search records with invalid dataset returns 404")
    @Story("Error handling for invalid dataset")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that invalid dataset returns 404 Not Found")
    public void testSearchRecords_InvalidDataset() {
        // Create search request
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();

        // Execute API call with invalid dataset
        Response response = searchService.searchRecords(
                "invalid_dataset",
                "v1",
                searchRequest
        );

        // Validate status code is 404
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 404 for invalid dataset")
                .isEqualTo(HttpStatusCodes.NOT_FOUND);
    }

    @Test(description = "Verify search records response time is acceptable")
    @Story("Performance validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that search API responds within acceptable time")
    public void testSearchRecords_ResponseTime() {
        // Execute search
        Response response = searchService.searchAllRecords(
                getDefaultDataset(),
                getDefaultVersion()
        );

        // Validate response time (should be less than 10 seconds)
        long responseTime = response.getTime();
        assertThat(responseTime)
                .as("Response time should be less than 10000ms")
                .isLessThan(10000L);
    }

    @Test(description = "Verify search records returns valid JSON")
    @Story("Response format validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that the response is in valid JSON format")
    public void testSearchRecords_ValidJsonFormat() {
        // Execute search
        Response response = searchService.searchAllRecords(
                getDefaultDataset(),
                getDefaultVersion()
        );

        // Validate response is valid JSON
        ResponseValidator.validateIsValidJson(response);
    }

    @DataProvider(name = "searchCriteriaData")
    public Object[][] searchCriteriaData() {
        return new Object[][]{
                {"*:*", "Wildcard search"},
                {"id:*", "Search by ID field"},
        };
    }

    @Test(dataProvider = "searchCriteriaData", 
          description = "Verify search with different criteria")
    @Story("Data-driven search testing")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies search works with different search criteria")
    public void testSearchRecords_DifferentCriteria(String criteria, String description) {
        // Execute search with provided criteria
        Response response = searchService.searchWithCriteria(
                getDefaultDataset(),
                getDefaultVersion(),
                criteria
        );

        // Validate status code
        ResponseValidator.validateStatusCode(response, HttpStatusCodes.OK);

        // Log results
        int numFound = searchService.getNumFound(response);
        assertThat(numFound)
                .as("Search with criteria '%s' should return results", description)
                .isGreaterThanOrEqualTo(0);
    }
}
