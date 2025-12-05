package com.klindziuk.staf.test;

import com.klindziuk.staf.constant.RequestPath;
import com.klindziuk.staf.model.DataSetList;
import com.klindziuk.staf.service.ResponseVerificationService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for USPTO Data Set API endpoints.
 * Tests metadata and search functionality.
 */
@Slf4j
@Epic("USPTO Data Set API")
@Feature("Metadata Operations")
public class UsptoDataSetApiTest extends BaseApiTest {

    @Test(description = "Verify listing all available data sets")
    @Description("Test that the root endpoint returns a list of all available data sets")
    @Story("List Data Sets")
    @Severity(SeverityLevel.CRITICAL)
    public void testListDataSets() throws IOException {
        log.info("Testing list data sets endpoint");

        // Execute GET request to root endpoint
        ClassicHttpResponse response = executeGet(RequestPath.ROOT);

        // Verify response status
        ResponseVerificationService.verifyStatusOk(response);

        // Extract and verify response body
        String responseBody = ResponseVerificationService.extractResponseBody(response);
        ResponseVerificationService.verifyResponseBodyNotEmpty(responseBody);

        // Verify JSON structure
        ResponseVerificationService.verifyJsonFieldExists(responseBody, "total");
        ResponseVerificationService.verifyJsonFieldExists(responseBody, "apis");

        // Deserialize and verify data
        DataSetList dataSetList = ResponseVerificationService.deserializeResponse(responseBody, DataSetList.class);
        
        assertThat(dataSetList.getTotal())
                .as("Total number of data sets should be greater than 0")
                .isGreaterThan(0);

        assertThat(dataSetList.getApis())
                .as("APIs list should not be empty")
                .isNotEmpty()
                .hasSize(dataSetList.getTotal());

        log.info("Successfully verified {} data sets", dataSetList.getTotal());
    }

    @Test(description = "Verify getting searchable fields for oa_citations dataset")
    @Description("Test that the fields endpoint returns searchable fields for the oa_citations dataset")
    @Story("Get Searchable Fields")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetSearchableFieldsForOaCitations() throws IOException {
        log.info("Testing get searchable fields for oa_citations dataset");

        // Build request path
        String path = buildFieldsPath(RequestPath.DEFAULT_DATASET, RequestPath.DEFAULT_VERSION);

        // Execute GET request
        ClassicHttpResponse response = executeGet(path);

        // Verify response
        ResponseVerificationService.verifyStatusOk(response);

        String responseBody = ResponseVerificationService.extractResponseBody(response);
        ResponseVerificationService.verifyResponseBodyNotEmpty(responseBody);

        // Verify response contains fields information
        ResponseVerificationService.verifyResponseBodyContains(responseBody, "fields");

        log.info("Successfully retrieved searchable fields for oa_citations");
    }

    @Test(description = "Verify getting searchable fields for cancer_moonshot dataset")
    @Description("Test that the fields endpoint returns searchable fields for the cancer_moonshot dataset")
    @Story("Get Searchable Fields")
    @Severity(SeverityLevel.NORMAL)
    public void testGetSearchableFieldsForCancerMoonshot() throws IOException {
        log.info("Testing get searchable fields for cancer_moonshot dataset");

        // Build request path
        String path = buildFieldsPath(RequestPath.CANCER_MOONSHOT_DATASET, RequestPath.DEFAULT_VERSION);

        // Execute GET request
        ClassicHttpResponse response = executeGet(path);

        // Verify response
        ResponseVerificationService.verifyStatusOk(response);

        String responseBody = ResponseVerificationService.extractResponseBody(response);
        ResponseVerificationService.verifyResponseBodyNotEmpty(responseBody);

        // Verify response contains fields information
        ResponseVerificationService.verifyResponseBodyContains(responseBody, "fields");

        log.info("Successfully retrieved searchable fields for cancer_moonshot");
    }

    @Test(description = "Verify 404 response for non-existent dataset")
    @Description("Test that requesting fields for a non-existent dataset returns 404")
    @Story("Error Handling")
    @Severity(SeverityLevel.NORMAL)
    public void testGetFieldsForNonExistentDataset() throws IOException {
        log.info("Testing get fields for non-existent dataset");

        // Build request path with invalid dataset
        String path = buildFieldsPath("non_existent_dataset", RequestPath.DEFAULT_VERSION);

        // Execute GET request
        ClassicHttpResponse response = executeGet(path);

        // Verify 404 response
        ResponseVerificationService.verifyStatusNotFound(response);

        log.info("Successfully verified 404 response for non-existent dataset");
    }

    @Test(description = "Verify 404 response for invalid version")
    @Description("Test that requesting fields with an invalid version returns 404")
    @Story("Error Handling")
    @Severity(SeverityLevel.NORMAL)
    public void testGetFieldsForInvalidVersion() throws IOException {
        log.info("Testing get fields for invalid version");

        // Build request path with invalid version
        String path = buildFieldsPath(RequestPath.DEFAULT_DATASET, "v999");

        // Execute GET request
        ClassicHttpResponse response = executeGet(path);

        // Verify 404 response
        ResponseVerificationService.verifyStatusNotFound(response);

        log.info("Successfully verified 404 response for invalid version");
    }
}
