package gov.uspto.api.tests;

import gov.uspto.api.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for Fields API endpoints
 */
@Epic("USPTO Data Set API")
@Feature("Fields Management")
public class FieldsTests extends BaseTest {

    private static final String TEST_DATASET = "oa_citations";
    private static final String TEST_VERSION = "v1";

    @Test(description = "Verify get fields returns 200 OK for valid dataset")
    @Story("Get Searchable Fields")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify that GET /{dataset}/{version}/fields returns 200 for valid parameters")
    public void testGetFieldsReturns200() {
        Response response = fieldsService.getFields(TEST_DATASET, TEST_VERSION);
        
        response.then()
                .statusCode(200)
                .contentType("application/json");
        
        log.info("Successfully retrieved fields for dataset: {}, version: {}", TEST_DATASET, TEST_VERSION);
    }

    @Test(description = "Verify get fields returns 404 for invalid dataset")
    @Story("Get Searchable Fields")
    @Severity(SeverityLevel.NORMAL)
    public void testGetFieldsReturns404ForInvalidDataset() {
        String invalidDataset = "invalid_dataset_name";
        Response response = fieldsService.getFields(invalidDataset, TEST_VERSION);
        
        response.then()
                .statusCode(404);
        
        log.info("Correctly received 404 for invalid dataset: {}", invalidDataset);
    }

    @Test(description = "Verify get fields returns 404 for invalid version")
    @Story("Get Searchable Fields")
    @Severity(SeverityLevel.NORMAL)
    public void testGetFieldsReturns404ForInvalidVersion() {
        String invalidVersion = "v999";
        Response response = fieldsService.getFields(TEST_DATASET, invalidVersion);
        
        response.then()
                .statusCode(404);
        
        log.info("Correctly received 404 for invalid version: {}", invalidVersion);
    }

    @Test(description = "Verify fields response time is acceptable")
    @Story("Get Searchable Fields")
    @Severity(SeverityLevel.NORMAL)
    public void testFieldsResponseTime() {
        Response response = fieldsService.getFields(TEST_DATASET, TEST_VERSION);
        
        long responseTime = response.getTime();
        int expectedTime = config.getExpectedResponseTime();
        
        assertThat(responseTime)
                .as("Response time should be less than %d ms", expectedTime)
                .isLessThan(expectedTime);
        
        log.info("Fields response time: {} ms (expected < {} ms)", responseTime, expectedTime);
    }

    @Test(description = "Verify fields response is not empty")
    @Story("Get Searchable Fields")
    @Severity(SeverityLevel.CRITICAL)
    public void testFieldsResponseNotEmpty() {
        Response response = fieldsService.getFields(TEST_DATASET, TEST_VERSION);
        
        String responseBody = response.getBody().asString();
        
        assertThat(responseBody)
                .as("Response body should not be empty")
                .isNotNull()
                .isNotEmpty();
        
        log.info("Fields response contains data");
    }

    @Test(description = "Verify fields endpoint with special characters in dataset name")
    @Story("Get Searchable Fields")
    @Severity(SeverityLevel.MINOR)
    public void testFieldsWithSpecialCharacters() {
        String specialDataset = "test@#$%";
        Response response = fieldsService.getFields(specialDataset, TEST_VERSION);
        
        // Should return 404 or 400 for invalid characters
        assertThat(response.getStatusCode())
                .as("Should return error status for special characters")
                .isIn(400, 404);
        
        log.info("Correctly handled special characters in dataset name");
    }
}
