package com.uspto.api.tests.smoke;

import com.uspto.api.constants.HttpStatusCodes;
import com.uspto.api.models.DataSetListResponse;
import com.uspto.api.tests.base.BaseTest;
import com.uspto.api.utils.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke tests for listing available datasets.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Epic("USPTO Data Set API")
@Feature("List Datasets")
public class ListDataSetsTest extends BaseTest {

    @Test(description = "Verify list datasets endpoint returns successful response")
    @Story("List all available datasets")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test verifies that GET / endpoint returns list of available datasets with status 200")
    public void testListDataSets_Success() {
        // Execute API call
        Response response = dataSetService.listDataSets();

        // Validate status code
        ResponseValidator.validateStatusCode(response, HttpStatusCodes.OK);

        // Validate response body is not empty
        ResponseValidator.validateResponseBodyNotEmpty(response);

        // Validate content type
        ResponseValidator.validateContentType(response, "application/json");

        // Validate response structure
        ResponseValidator.validateJsonPathExists(response, "total");
        ResponseValidator.validateJsonPathExists(response, "apis");

        // Validate total count is greater than 0
        int totalCount = dataSetService.getTotalDataSetsCount(response);
        assertThat(totalCount)
                .as("Total datasets count should be greater than 0")
                .isGreaterThan(0);
    }

    @Test(description = "Verify list datasets response contains required fields")
    @Story("Validate dataset response structure")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that each dataset in the response contains all required fields")
    public void testListDataSets_ResponseStructure() {
        // Execute API call
        Response response = dataSetService.listDataSets();

        // Parse response
        DataSetListResponse dataSetList = dataSetService.parseDataSetListResponse(response);

        // Validate response structure
        assertThat(dataSetList.getTotal())
                .as("Total field should not be null")
                .isNotNull();

        assertThat(dataSetList.getApis())
                .as("APIs list should not be null or empty")
                .isNotNull()
                .isNotEmpty();

        // Validate each API info contains required fields
        dataSetList.getApis().forEach(apiInfo -> {
            assertThat(apiInfo.getApiKey())
                    .as("API key should not be null or empty")
                    .isNotNull()
                    .isNotEmpty();

            assertThat(apiInfo.getApiVersionNumber())
                    .as("API version should not be null or empty")
                    .isNotNull()
                    .isNotEmpty();

            assertThat(apiInfo.getApiUrl())
                    .as("API URL should not be null or empty")
                    .isNotNull()
                    .isNotEmpty();

            assertThat(apiInfo.getApiDocumentationUrl())
                    .as("API documentation URL should not be null or empty")
                    .isNotNull()
                    .isNotEmpty();
        });
    }

    @Test(description = "Verify list datasets response time is acceptable")
    @Story("Performance validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that the API responds within acceptable time limit")
    public void testListDataSets_ResponseTime() {
        // Execute API call
        Response response = dataSetService.listDataSets();

        // Validate response time (should be less than 5 seconds)
        long responseTime = response.getTime();
        assertThat(responseTime)
                .as("Response time should be less than 5000ms")
                .isLessThan(5000L);
    }

    @Test(description = "Verify specific dataset exists in the list")
    @Story("Validate specific dataset availability")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that the default dataset exists in the available datasets list")
    public void testListDataSets_DefaultDatasetExists() {
        // Execute API call
        Response response = dataSetService.listDataSets();

        // Check if default dataset exists
        String defaultDataset = getDefaultDataset();
        boolean exists = dataSetService.isDataSetExists(response, defaultDataset);

        assertThat(exists)
                .as("Default dataset '%s' should exist in the list", defaultDataset)
                .isTrue();
    }

    @Test(description = "Verify list datasets returns valid JSON")
    @Story("Response format validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that the response is valid JSON format")
    public void testListDataSets_ValidJson() {
        // Execute API call
        Response response = dataSetService.listDataSets();

        // Validate response is valid JSON
        ResponseValidator.validateIsValidJson(response);
    }
}
