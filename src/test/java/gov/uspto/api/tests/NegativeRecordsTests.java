package gov.uspto.api.tests;

import gov.uspto.api.base.BaseTest;
import gov.uspto.api.models.request.SearchRequest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Negative test scenarios for Records Search API endpoints
 */
@Epic("USPTO Data Set API")
@Feature("Records Search - Negative Scenarios")
public class NegativeRecordsTests extends BaseTest {

    @Test(description = "Verify 404 for non-existent dataset in search")
    @Story("Invalid Dataset")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchNonExistentDataset() {
        SearchRequest request = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords("non_existent_dataset", "v1", request);
        
        assertThat(response.getStatusCode())
                .as("Non-existent dataset should return 404")
                .isEqualTo(404);
        
        log.info("Search on non-existent dataset correctly returned 404");
    }

    @Test(description = "Verify 404 for non-existent version in search")
    @Story("Invalid Version")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchNonExistentVersion() {
        SearchRequest request = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v999", request);
        
        assertThat(response.getStatusCode())
                .as("Non-existent version should return 404")
                .isEqualTo(404);
        
        log.info("Search on non-existent version correctly returned 404");
    }

    @DataProvider(name = "invalidLuceneQueries")
    public Object[][] invalidLuceneQueries() {
        return new Object[][] {
            {"fieldName: AND"},
            {"fieldName: OR"},
            {"fieldName: NOT"},
            {"(unclosed parenthesis"},
            {"unclosed parenthesis)"},
            {"[unclosed bracket"},
            {"unclosed bracket]"},
            {"{unclosed brace"},
            {"unclosed brace}"},
            {"fieldName:[TO 100]"},
            {"fieldName:[100 TO]"},
            {"fieldName:[TO]"},
            {"fieldName:[]"},
            {"fieldName:[100 TO 50]"}, // Invalid range
            {":::"},
            {"***"},
            {"???"},
            {"\\\\\\"},
            {"fieldName:\"unclosed quote"},
            {"fieldName:value\"extra quote\""}
        };
    }

    @Test(dataProvider = "invalidLuceneQueries", description = "Verify handling of invalid Lucene queries")
    @Story("Invalid Query Syntax")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidLuceneQueries(String invalidQuery) {
        SearchRequest request = SearchRequest.builder()
                .criteria(invalidQuery)
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        
        assertThat(response.getStatusCode())
                .as("Invalid Lucene query should return error")
                .isIn(400, 500);
        
        log.info("Invalid query '{}' handled with status: {}", invalidQuery, response.getStatusCode());
    }

    @DataProvider(name = "invalidPaginationParams")
    public Object[][] invalidPaginationParams() {
        return new Object[][] {
            {-1, 10},      // Negative start
            {0, -1},       // Negative rows
            {-10, -10},    // Both negative
            {0, 0},        // Zero rows
            {Integer.MAX_VALUE, 10},  // Extremely large start
            {0, Integer.MAX_VALUE}    // Extremely large rows
        };
    }

    @Test(dataProvider = "invalidPaginationParams", description = "Verify handling of invalid pagination parameters")
    @Story("Invalid Pagination")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidPaginationParameters(int start, int rows) {
        SearchRequest request = SearchRequest.builder()
                .criteria("*:*")
                .start(start)
                .rows(rows)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        
        // API should either handle gracefully (200 with empty results) or return 400
        assertThat(response.getStatusCode())
                .as("Invalid pagination should be handled")
                .isIn(200, 400);
        
        log.info("Invalid pagination (start={}, rows={}) handled with status: {}", 
                start, rows, response.getStatusCode());
    }

    @Test(description = "Verify missing required criteria parameter")
    @Story("Missing Parameters")
    @Severity(SeverityLevel.CRITICAL)
    public void testMissingCriteriaParameter() {
        SearchRequest request = SearchRequest.builder()
                .criteria(null)
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        
        assertThat(response.getStatusCode())
                .as("Missing criteria should return error")
                .isIn(400, 500);
        
        log.info("Missing criteria handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify empty criteria parameter")
    @Story("Invalid Parameters")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptyCriteriaParameter() {
        SearchRequest request = SearchRequest.builder()
                .criteria("")
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        
        assertThat(response.getStatusCode())
                .as("Empty criteria should be handled")
                .isIn(200, 400);
        
        log.info("Empty criteria handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify GET method not allowed on records endpoint")
    @Story("Invalid HTTP Methods")
    @Severity(SeverityLevel.NORMAL)
    public void testGetMethodNotAllowed() {
        Response response = recordsService.getRecords("oa_citations", "v1");
        
        assertThat(response.getStatusCode())
                .as("GET method should not be allowed")
                .isIn(405, 404);
        
        log.info("GET method correctly rejected with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify SQL injection attempt in criteria")
    @Story("Security Testing")
    @Severity(SeverityLevel.CRITICAL)
    public void testSqlInjectionInCriteria() {
        SearchRequest request = SearchRequest.builder()
                .criteria("' OR '1'='1")
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        
        // Should either return no results or error, but not expose data
        assertThat(response.getStatusCode())
                .as("SQL injection should be handled safely")
                .isIn(200, 400);
        
        log.info("SQL injection attempt handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify XSS attempt in criteria")
    @Story("Security Testing")
    @Severity(SeverityLevel.CRITICAL)
    public void testXssAttemptInCriteria() {
        SearchRequest request = SearchRequest.builder()
                .criteria("<script>alert('xss')</script>")
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        
        assertThat(response.getStatusCode())
                .as("XSS attempt should be handled safely")
                .isIn(200, 400);
        
        log.info("XSS attempt handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify extremely long criteria string")
    @Story("Boundary Testing")
    @Severity(SeverityLevel.NORMAL)
    public void testExtremelyLongCriteria() {
        String longCriteria = "fieldName:" + "a".repeat(100000);
        SearchRequest request = SearchRequest.builder()
                .criteria(longCriteria)
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        
        assertThat(response.getStatusCode())
                .as("Extremely long criteria should be handled")
                .isIn(200, 400, 413);
        
        log.info("Long criteria handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify special characters in criteria")
    @Story("Special Characters")
    @Severity(SeverityLevel.NORMAL)
    public void testSpecialCharactersInCriteria() {
        String specialChars = "fieldName:!@#$%^&*()_+-=[]{}|;':\",./<>?";
        SearchRequest request = SearchRequest.builder()
                .criteria(specialChars)
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        
        assertThat(response.getStatusCode())
                .as("Special characters should be handled")
                .isIn(200, 400);
        
        log.info("Special characters handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify Unicode characters in criteria")
    @Story("Special Characters")
    @Severity(SeverityLevel.MINOR)
    public void testUnicodeCharactersInCriteria() {
        String unicode = "fieldName:测试数据";
        SearchRequest request = SearchRequest.builder()
                .criteria(unicode)
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        
        assertThat(response.getStatusCode())
                .as("Unicode characters should be handled")
                .isIn(200, 400);
        
        log.info("Unicode characters handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify null byte injection in criteria")
    @Story("Security Testing")
    @Severity(SeverityLevel.CRITICAL)
    public void testNullByteInjectionInCriteria() {
        String malicious = "fieldName:value%00.txt";
        SearchRequest request = SearchRequest.builder()
                .criteria(malicious)
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        
        assertThat(response.getStatusCode())
                .as("Null byte injection should be handled safely")
                .isIn(200, 400);
        
        log.info("Null byte injection handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify maximum rows limit enforcement")
    @Story("Boundary Testing")
    @Severity(SeverityLevel.NORMAL)
    public void testMaximumRowsLimit() {
        SearchRequest request = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10000) // Attempt to retrieve excessive rows
                .build();
        
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        
        // API should either limit results or return error
        assertThat(response.getStatusCode())
                .as("Maximum rows limit should be enforced")
                .isIn(200, 400);
        
        log.info("Maximum rows limit handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify invalid Content-Type header")
    @Story("Invalid Headers")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidContentType() {
        SearchRequest request = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        Response response = recordsService.searchRecordsWithContentType(
                "oa_citations", "v1", request, "text/plain");
        
        assertThat(response.getStatusCode())
                .as("Invalid Content-Type should be handled")
                .isIn(200, 400, 415);
        
        log.info("Invalid Content-Type handled with status: {}", response.getStatusCode());
    }
}
