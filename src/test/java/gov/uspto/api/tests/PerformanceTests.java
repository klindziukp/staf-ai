package gov.uspto.api.tests;

import gov.uspto.api.base.BaseTest;
import gov.uspto.api.models.request.SearchRequest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Performance test scenarios for USPTO Data Set API
 */
@Epic("USPTO Data Set API")
@Feature("Performance Testing")
public class PerformanceTests extends BaseTest {

    private static final int ACCEPTABLE_RESPONSE_TIME_MS = 5000;
    private static final int FAST_RESPONSE_TIME_MS = 2000;

    @Test(description = "Verify dataset list endpoint response time")
    @Story("Response Time")
    @Severity(SeverityLevel.NORMAL)
    public void testDataSetListResponseTime() {
        long startTime = System.currentTimeMillis();
        Response response = dataSetService.getDataSets();
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        assertThat(response.getStatusCode())
                .as("Request should be successful")
                .isEqualTo(200);
        
        assertThat(responseTime)
                .as("Response time should be less than %d ms", ACCEPTABLE_RESPONSE_TIME_MS)
                .isLessThan(ACCEPTABLE_RESPONSE_TIME_MS);
        
        log.info("Dataset list response time: {} ms", responseTime);
    }

    @Test(description = "Verify fields endpoint response time")
    @Story("Response Time")
    @Severity(SeverityLevel.NORMAL)
    public void testFieldsEndpointResponseTime() {
        long startTime = System.currentTimeMillis();
        Response response = fieldsService.getFields("oa_citations", "v1");
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        assertThat(response.getStatusCode())
                .as("Request should be successful")
                .isEqualTo(200);
        
        assertThat(responseTime)
                .as("Response time should be less than %d ms", ACCEPTABLE_RESPONSE_TIME_MS)
                .isLessThan(ACCEPTABLE_RESPONSE_TIME_MS);
        
        log.info("Fields endpoint response time: {} ms", responseTime);
    }

    @Test(description = "Verify search endpoint response time with small result set")
    @Story("Response Time")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchSmallResultSetResponseTime() {
        SearchRequest request = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        long startTime = System.currentTimeMillis();
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        assertThat(response.getStatusCode())
                .as("Request should be successful")
                .isEqualTo(200);
        
        assertThat(responseTime)
                .as("Response time should be less than %d ms", ACCEPTABLE_RESPONSE_TIME_MS)
                .isLessThan(ACCEPTABLE_RESPONSE_TIME_MS);
        
        log.info("Search (10 rows) response time: {} ms", responseTime);
    }

    @Test(description = "Verify search endpoint response time with large result set")
    @Story("Response Time")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchLargeResultSetResponseTime() {
        SearchRequest request = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(100)
                .build();
        
        long startTime = System.currentTimeMillis();
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        assertThat(response.getStatusCode())
                .as("Request should be successful")
                .isEqualTo(200);
        
        assertThat(responseTime)
                .as("Response time should be less than %d ms", ACCEPTABLE_RESPONSE_TIME_MS * 2)
                .isLessThan(ACCEPTABLE_RESPONSE_TIME_MS * 2);
        
        log.info("Search (100 rows) response time: {} ms", responseTime);
    }

    @Test(description = "Verify consecutive requests performance")
    @Story("Load Testing")
    @Severity(SeverityLevel.NORMAL)
    public void testConsecutiveRequestsPerformance() {
        int numberOfRequests = 10;
        List<Long> responseTimes = new ArrayList<>();
        
        for (int i = 0; i < numberOfRequests; i++) {
            long startTime = System.currentTimeMillis();
            Response response = dataSetService.getDataSets();
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            
            assertThat(response.getStatusCode())
                    .as("Request %d should be successful", i + 1)
                    .isEqualTo(200);
            
            responseTimes.add(responseTime);
            log.info("Request {} response time: {} ms", i + 1, responseTime);
        }
        
        double averageResponseTime = responseTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
        
        assertThat(averageResponseTime)
                .as("Average response time should be acceptable")
                .isLessThan(ACCEPTABLE_RESPONSE_TIME_MS);
        
        log.info("Average response time for {} requests: {} ms", numberOfRequests, averageResponseTime);
    }

    @Test(description = "Verify API stability under repeated searches")
    @Story("Load Testing")
    @Severity(SeverityLevel.NORMAL)
    public void testRepeatedSearchesStability() {
        int numberOfSearches = 20;
        SearchRequest request = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
        
        List<Long> responseTimes = new ArrayList<>();
        int successCount = 0;
        
        for (int i = 0; i < numberOfSearches; i++) {
            long startTime = System.currentTimeMillis();
            Response response = recordsService.searchRecords("oa_citations", "v1", request);
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            
            if (response.getStatusCode() == 200) {
                successCount++;
                responseTimes.add(responseTime);
            }
            
            log.info("Search {} - Status: {}, Time: {} ms", 
                    i + 1, response.getStatusCode(), responseTime);
        }
        
        assertThat(successCount)
                .as("All searches should be successful")
                .isEqualTo(numberOfSearches);
        
        double averageResponseTime = responseTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
        
        log.info("Stability test completed. Success rate: {}/{}, Average time: {} ms", 
                successCount, numberOfSearches, averageResponseTime);
    }

    @Test(description = "Verify pagination performance across pages")
    @Story("Pagination Performance")
    @Severity(SeverityLevel.NORMAL)
    public void testPaginationPerformance() {
        int rowsPerPage = 10;
        int numberOfPages = 5;
        
        List<Long> responseTimes = new ArrayList<>();
        
        for (int page = 0; page < numberOfPages; page++) {
            SearchRequest request = SearchRequest.builder()
                    .criteria("*:*")
                    .start(page * rowsPerPage)
                    .rows(rowsPerPage)
                    .build();
            
            long startTime = System.currentTimeMillis();
            Response response = recordsService.searchRecords("oa_citations", "v1", request);
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            
            assertThat(response.getStatusCode())
                    .as("Page %d request should be successful", page + 1)
                    .isEqualTo(200);
            
            responseTimes.add(responseTime);
            log.info("Page {} response time: {} ms", page + 1, responseTime);
        }
        
        double averageResponseTime = responseTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
        
        assertThat(averageResponseTime)
                .as("Average pagination response time should be acceptable")
                .isLessThan(ACCEPTABLE_RESPONSE_TIME_MS);
        
        log.info("Average pagination response time: {} ms", averageResponseTime);
    }

    @Test(description = "Verify complex query performance")
    @Story("Query Performance")
    @Severity(SeverityLevel.NORMAL)
    public void testComplexQueryPerformance() {
        String complexQuery = "(field1:value1 OR field2:value2) AND field3:[100 TO 200]";
        SearchRequest request = SearchRequest.builder()
                .criteria(complexQuery)
                .start(0)
                .rows(10)
                .build();
        
        long startTime = System.currentTimeMillis();
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        assertThat(response.getStatusCode())
                .as("Complex query should be processed")
                .isIn(200, 400); // 400 if fields don't exist
        
        assertThat(responseTime)
                .as("Complex query response time should be acceptable")
                .isLessThan(ACCEPTABLE_RESPONSE_TIME_MS * 2);
        
        log.info("Complex query response time: {} ms", responseTime);
    }

    @Test(description = "Verify wildcard search performance")
    @Story("Query Performance")
    @Severity(SeverityLevel.NORMAL)
    public void testWildcardSearchPerformance() {
        String wildcardQuery = "*:*";
        SearchRequest request = SearchRequest.builder()
                .criteria(wildcardQuery)
                .start(0)
                .rows(50)
                .build();
        
        long startTime = System.currentTimeMillis();
        Response response = recordsService.searchRecords("oa_citations", "v1", request);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        assertThat(response.getStatusCode())
                .as("Wildcard search should be successful")
                .isEqualTo(200);
        
        assertThat(responseTime)
                .as("Wildcard search response time should be acceptable")
                .isLessThan(ACCEPTABLE_RESPONSE_TIME_MS);
        
        log.info("Wildcard search response time: {} ms", responseTime);
    }

    @Test(description = "Verify API response time consistency")
    @Story("Consistency Testing")
    @Severity(SeverityLevel.NORMAL)
    public void testResponseTimeConsistency() {
        int numberOfRequests = 10;
        List<Long> responseTimes = new ArrayList<>();
        
        for (int i = 0; i < numberOfRequests; i++) {
            long startTime = System.currentTimeMillis();
            Response response = dataSetService.getDataSets();
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            
            responseTimes.add(responseTime);
            
            // Small delay between requests
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        double average = responseTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
        long min = responseTimes.stream().mapToLong(Long::longValue).min().orElse(0);
        long max = responseTimes.stream().mapToLong(Long::longValue).max().orElse(0);
        
        double variance = responseTimes.stream()
                .mapToDouble(time -> Math.pow(time - average, 2))
                .average()
                .orElse(0.0);
        double standardDeviation = Math.sqrt(variance);
        
        log.info("Response time statistics:");
        log.info("  Average: {} ms", average);
        log.info("  Min: {} ms", min);
        log.info("  Max: {} ms", max);
        log.info("  Standard Deviation: {} ms", standardDeviation);
        
        // Standard deviation should not be too high (indicating inconsistent performance)
        assertThat(standardDeviation)
                .as("Response time should be consistent")
                .isLessThan(average * 0.5); // SD should be less than 50% of average
    }
}
