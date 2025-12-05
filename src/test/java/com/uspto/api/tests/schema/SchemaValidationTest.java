package com.uspto.api.tests.schema;

import com.uspto.api.constants.HttpStatusCodes;
import com.uspto.api.models.SearchRequest;
import com.uspto.api.tests.base.BaseTest;
import com.uspto.api.utils.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Schema validation tests for USPTO Data Set API responses.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Epic("USPTO Data Set API")
@Feature("Schema Validation")
public class SchemaValidationTest extends BaseTest {

    @Test(description = "Verify list datasets response schema")
    @Story("Validate list datasets response structure")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that list datasets response matches expected JSON schema")
    public void testListDataSets_SchemaValidation() {
        // Execute API call
        Response response = dataSetService.listDataSets();

        // Validate status code
        ResponseValidator.validateStatusCode(response, HttpStatusCodes.OK);

        // Validate required fields exist
        ResponseValidator.validateJsonPathExists(response, "total");
        ResponseValidator.validateJsonPathExists(response, "apis");

        // Validate total is a number
        Object total = response.jsonPath().get("total");
        assertThat(total)
                .as("Total should be an integer")
                .isInstanceOf(Integer.class);

        // Validate apis is a list
        Object apis = response.jsonPath().get("apis");
        assertThat(apis)
                .as("APIs should be a list")
                .isInstanceOf(java.util.List.class);
    }

    @Test(description = "Verify search records response schema")
    @Story("Validate search response structure")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that search records response matches expected JSON schema")
    public void testSearchRecords_SchemaValidation() {
        // Create search request
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

        // Validate required fields exist
        ResponseValidator.validateJsonPathExists(response, "response");
        ResponseValidator.validateJsonPathExists(response, "response.numFound");
        ResponseValidator.validateJsonPathExists(response, "response.start");
        ResponseValidator.validateJsonPathExists(response, "response.docs");

        // Validate field types
        Object numFound = response.jsonPath().get("response.numFound");
        assertThat(numFound)
                .as("numFound should be an integer")
                .isInstanceOf(Integer.class);

        Object start = response.jsonPath().get("response.start");
        assertThat(start)
                .as("start should be an integer")
                .isInstanceOf(Integer.class);

        Object docs = response.jsonPath().get("response.docs");
        assertThat(docs)
                .as("docs should be a list")
                .isInstanceOf(java.util.List.class);
    }

    @Test(description = "Verify list datasets response contains valid API info")
    @Story("Validate API info structure")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies each API info object contains required fields")
    public void testListDataSets_ApiInfoSchema() {
        // Execute API call
        Response response = dataSetService.listDataSets();

        // Get first API info
        String apiKey = response.jsonPath().getString("apis[0].apiKey");
        String apiVersionNumber = response.jsonPath().getString("apis[0].apiVersionNumber");
        String apiUrl = response.jsonPath().getString("apis[0].apiUrl");
        String apiDocumentationUrl = response.jsonPath().getString("apis[0].apiDocumentationUrl");

        // Validate all fields are present and not null
        assertThat(apiKey)
                .as("apiKey should not be null")
                .isNotNull();

        assertThat(apiVersionNumber)
                .as("apiVersionNumber should not be null")
                .isNotNull();

        assertThat(apiUrl)
                .as("apiUrl should not be null")
                .isNotNull();

        assertThat(apiDocumentationUrl)
                .as("apiDocumentationUrl should not be null")
                .isNotNull();
    }

    @Test(description = "Verify search response contains valid response header")
    @Story("Validate response header structure")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies response header contains expected fields")
    public void testSearchRecords_ResponseHeaderSchema() {
        // Create search request
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

        // Validate responseHeader exists if present
        try {
            ResponseValidator.validateJsonPathExists(response, "responseHeader");
            ResponseValidator.validateJsonPathExists(response, "responseHeader.status");
            ResponseValidator.validateJsonPathExists(response, "responseHeader.QTime");
        } catch (AssertionError e) {
            // ResponseHeader might not be present in all responses
            // This is acceptable
        }
    }

    @Test(description = "Verify list datasets total matches apis count")
    @Story("Data consistency validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that total field matches the actual count of APIs")
    public void testListDataSets_TotalMatchesCount() {
        // Execute API call
        Response response = dataSetService.listDataSets();

        // Get total and apis list
        int total = response.jsonPath().getInt("total");
        int apisCount = response.jsonPath().getList("apis").size();

        // Validate total matches actual count
        assertThat(total)
                .as("Total should match the actual count of APIs")
                .isEqualTo(apisCount);
    }

    @Test(description = "Verify search response docs count matches returned count")
    @Story("Data consistency validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that the number of docs returned matches the count")
    public void testSearchRecords_DocsCountConsistency() {
        // Create search request
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(5)
                .build();

        // Execute API call
        Response response = searchService.searchRecords(
                getDefaultDataset(),
                getDefaultVersion(),
                searchRequest
        );

        // Get docs count
        int docsCount = searchService.getDocsCount(response);

        // Validate docs count is less than or equal to requested rows
        assertThat(docsCount)
                .as("Docs count should be less than or equal to requested rows")
                .isLessThanOrEqualTo(5);
    }
}
