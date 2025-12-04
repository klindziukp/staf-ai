package gov.uspto.api.utils;

import gov.uspto.api.models.request.SearchRequest;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for generating test data
 */
@UtilityClass
public class TestDataGenerator {

    /**
     * Generate default search request with wildcard criteria
     */
    public static SearchRequest getDefaultSearchRequest() {
        return SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();
    }

    /**
     * Generate search request with pagination
     */
    public static SearchRequest getSearchRequestWithPagination(int start, int rows) {
        return SearchRequest.builder()
                .criteria("*:*")
                .start(start)
                .rows(rows)
                .build();
    }

    /**
     * Generate search request with custom criteria
     */
    public static SearchRequest getSearchRequestWithCriteria(String criteria) {
        return SearchRequest.builder()
                .criteria(criteria)
                .start(0)
                .rows(10)
                .build();
    }

    /**
     * Generate search request with all parameters
     */
    public static SearchRequest getCompleteSearchRequest(String criteria, int start, int rows) {
        return SearchRequest.builder()
                .criteria(criteria)
                .start(start)
                .rows(rows)
                .build();
    }

    /**
     * Generate invalid search request with negative values
     */
    public static SearchRequest getInvalidSearchRequest() {
        return SearchRequest.builder()
                .criteria("*:*")
                .start(-1)
                .rows(-10)
                .build();
    }

    /**
     * Generate search request with boundary values
     */
    public static SearchRequest getBoundarySearchRequest(int start, int rows) {
        return SearchRequest.builder()
                .criteria("*:*")
                .start(start)
                .rows(rows)
                .build();
    }

    /**
     * Get test dataset names
     */
    public static String[] getTestDatasets() {
        return new String[]{
                "oa_citations",
                "cancer_moonshot"
        };
    }

    /**
     * Get test versions
     */
    public static String[] getTestVersions() {
        return new String[]{"v1", "v2"};
    }

    /**
     * Get invalid dataset names for negative testing
     */
    public static String[] getInvalidDatasets() {
        return new String[]{
                "invalid_dataset",
                "test@#$%",
                "../etc/passwd",
                "'; DROP TABLE users;--",
                "<script>alert('xss')</script>",
                ""
        };
    }

    /**
     * Get invalid versions for negative testing
     */
    public static String[] getInvalidVersions() {
        return new String[]{
                "v999",
                "invalid",
                "1.0",
                ""
        };
    }

    /**
     * Get Lucene query examples
     */
    public static String[] getLuceneQueries() {
        return new String[]{
                "*:*",
                "field:value",
                "field:[1 TO 100]",
                "field1:value1 AND field2:value2",
                "field1:value1 OR field2:value2",
                "field:\"exact phrase\"",
                "field:val*",
                "field:val?e"
        };
    }

    /**
     * Get boundary values for pagination
     */
    public static Map<String, Integer> getBoundaryPaginationValues() {
        Map<String, Integer> values = new HashMap<>();
        values.put("minStart", 0);
        values.put("maxStart", Integer.MAX_VALUE);
        values.put("minRows", 0);
        values.put("maxRows", 10000);
        return values;
    }
}
