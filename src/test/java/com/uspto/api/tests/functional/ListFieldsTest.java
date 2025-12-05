package com.uspto.api.tests.functional;

import com.uspto.api.constants.HttpStatusCodes;
import com.uspto.api.tests.base.BaseTest;
import com.uspto.api.utils.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Functional tests for listing searchable fields endpoint.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Epic("USPTO Data Set API")
@Feature("List Fields")
public class ListFieldsTest extends BaseTest {

    @Test(description = "Verify list fields endpoint with valid dataset and version")
    @Story("List searchable fields for valid dataset")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that GET /{dataset}/{version}/fields returns searchable fields with status 200")
    public void testListFields_ValidDatasetAndVersion() {
        // Execute API call with default dataset and version
        Response response = dataSetService.listFields(getDefaultDataset(), getDefaultVersion());

        // Validate status code
        ResponseValidator.validateStatusCode(response, HttpStatusCodes.OK);

        // Validate response body is not empty
        ResponseValidator.validateResponseBodyNotEmpty(response);

        // Validate content type
        ResponseValidator.validateContentType(response, "application/json");
    }

    @Test(description = "Verify list fields with oa_citations dataset")
    @Story("List fields for oa_citations dataset")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies fields can be retrieved for oa_citations dataset")
    public void testListFields_OaCitationsDataset() {
        // Execute API call with oa_citations dataset
        Response response = dataSetService.listFields("oa_citations", "v1");

        // Validate status code
        ResponseValidator.validateStatusCode(response, HttpStatusCodes.OK);

        // Validate response is valid JSON
        ResponseValidator.validateIsValidJson(response);
    }

    @Test(description = "Verify list fields response time is acceptable")
    @Story("Performance validation for list fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that the list fields API responds within acceptable time")
    public void testListFields_ResponseTime() {
        // Execute API call
        Response response = dataSetService.listFields(getDefaultDataset(), getDefaultVersion());

        // Validate response time (should be less than 5 seconds)
        long responseTime = response.getTime();
        assertThat(responseTime)
                .as("Response time should be less than 5000ms")
                .isLessThan(5000L);
    }

    @Test(description = "Verify list fields with invalid dataset returns 404")
    @Story("Error handling for invalid dataset")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that invalid dataset returns 404 Not Found")
    public void testListFields_InvalidDataset() {
        // Execute API call with invalid dataset
        Response response = dataSetService.listFields("invalid_dataset", "v1");

        // Validate status code is 404
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 404 for invalid dataset")
                .isEqualTo(HttpStatusCodes.NOT_FOUND);
    }

    @Test(description = "Verify list fields with invalid version returns 404")
    @Story("Error handling for invalid version")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that invalid version returns 404 Not Found")
    public void testListFields_InvalidVersion() {
        // Execute API call with invalid version
        Response response = dataSetService.listFields(getDefaultDataset(), "v999");

        // Validate status code is 404
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 404 for invalid version")
                .isEqualTo(HttpStatusCodes.NOT_FOUND);
    }

    @Test(description = "Verify list fields with special characters in dataset name")
    @Story("Input validation for dataset name")
    @Severity(SeverityLevel.MINOR)
    @Description("Test verifies API handles special characters in dataset name")
    public void testListFields_SpecialCharactersInDataset() {
        // Execute API call with special characters
        Response response = dataSetService.listFields("test@#$%", "v1");

        // Validate status code is 404 or 400
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 404 or 400 for invalid dataset with special characters")
                .isIn(HttpStatusCodes.NOT_FOUND, HttpStatusCodes.BAD_REQUEST);
    }

    @Test(description = "Verify list fields returns valid JSON format")
    @Story("Response format validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that the response is in valid JSON format")
    public void testListFields_ValidJsonFormat() {
        // Execute API call
        Response response = dataSetService.listFields(getDefaultDataset(), getDefaultVersion());

        // Validate response is valid JSON
        ResponseValidator.validateIsValidJson(response);
    }
}
