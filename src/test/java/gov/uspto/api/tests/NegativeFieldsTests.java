package gov.uspto.api.tests;

import gov.uspto.api.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Negative test scenarios for Fields API endpoints
 */
@Epic("USPTO Data Set API")
@Feature("Fields Management - Negative Scenarios")
public class NegativeFieldsTests extends BaseTest {

    @Test(description = "Verify 404 for non-existent dataset")
    @Story("Invalid Dataset")
    @Severity(SeverityLevel.CRITICAL)
    public void testNonExistentDataset() {
        String invalidDataset = "non_existent_dataset_12345";
        Response response = fieldsService.getFields(invalidDataset, "v1");
        
        assertThat(response.getStatusCode())
                .as("Non-existent dataset should return 404")
                .isEqualTo(404);
        
        log.info("Non-existent dataset correctly returned 404");
    }

    @Test(description = "Verify 404 for non-existent version")
    @Story("Invalid Version")
    @Severity(SeverityLevel.CRITICAL)
    public void testNonExistentVersion() {
        String invalidVersion = "v999";
        Response response = fieldsService.getFields("oa_citations", invalidVersion);
        
        assertThat(response.getStatusCode())
                .as("Non-existent version should return 404")
                .isEqualTo(404);
        
        log.info("Non-existent version correctly returned 404");
    }

    @DataProvider(name = "invalidDatasetNames")
    public Object[][] invalidDatasetNames() {
        return new Object[][] {
            {""},
            {" "},
            {"dataset with spaces"},
            {"dataset@special#chars"},
            {"dataset/with/slashes"},
            {"dataset\\with\\backslashes"},
            {"<script>alert('xss')</script>"},
            {"' OR '1'='1"},
            {"../../../etc/passwd"},
            {"%00null%00byte"},
            {"a".repeat(1000)}
        };
    }

    @Test(dataProvider = "invalidDatasetNames", description = "Verify handling of invalid dataset names")
    @Story("Invalid Dataset Names")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidDatasetNames(String invalidDataset) {
        Response response = fieldsService.getFields(invalidDataset, "v1");
        
        assertThat(response.getStatusCode())
                .as("Invalid dataset name should return error")
                .isIn(400, 404);
        
        log.info("Invalid dataset '{}' handled with status: {}", invalidDataset, response.getStatusCode());
    }

    @DataProvider(name = "invalidVersions")
    public Object[][] invalidVersions() {
        return new Object[][] {
            {""},
            {" "},
            {"version 1"},
            {"v@1"},
            {"v/1"},
            {"<script>"},
            {"' OR '1'='1"},
            {"../v1"},
            {"%00"},
            {"v".repeat(500)}
        };
    }

    @Test(dataProvider = "invalidVersions", description = "Verify handling of invalid versions")
    @Story("Invalid Versions")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidVersions(String invalidVersion) {
        Response response = fieldsService.getFields("oa_citations", invalidVersion);
        
        assertThat(response.getStatusCode())
                .as("Invalid version should return error")
                .isIn(400, 404);
        
        log.info("Invalid version '{}' handled with status: {}", invalidVersion, response.getStatusCode());
    }

    @Test(description = "Verify missing dataset parameter")
    @Story("Missing Parameters")
    @Severity(SeverityLevel.NORMAL)
    public void testMissingDatasetParameter() {
        Response response = fieldsService.getFieldsWithMissingParams("", "v1");
        
        assertThat(response.getStatusCode())
                .as("Missing dataset parameter should return error")
                .isIn(400, 404);
        
        log.info("Missing dataset parameter handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify missing version parameter")
    @Story("Missing Parameters")
    @Severity(SeverityLevel.NORMAL)
    public void testMissingVersionParameter() {
        Response response = fieldsService.getFieldsWithMissingParams("oa_citations", "");
        
        assertThat(response.getStatusCode())
                .as("Missing version parameter should return error")
                .isIn(400, 404);
        
        log.info("Missing version parameter handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify POST method not allowed on fields endpoint")
    @Story("Invalid HTTP Methods")
    @Severity(SeverityLevel.NORMAL)
    public void testPostMethodNotAllowed() {
        Response response = fieldsService.postFields("oa_citations", "v1");
        
        assertThat(response.getStatusCode())
                .as("POST method should not be allowed")
                .isIn(405, 404);
        
        log.info("POST method correctly rejected with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify case sensitivity of dataset name")
    @Story("Case Sensitivity")
    @Severity(SeverityLevel.NORMAL)
    public void testDatasetNameCaseSensitivity() {
        Response lowerCase = fieldsService.getFields("oa_citations", "v1");
        Response upperCase = fieldsService.getFields("OA_CITATIONS", "v1");
        Response mixedCase = fieldsService.getFields("Oa_Citations", "v1");
        
        // Document the API's case sensitivity behavior
        log.info("Lowercase status: {}", lowerCase.getStatusCode());
        log.info("Uppercase status: {}", upperCase.getStatusCode());
        log.info("Mixed case status: {}", mixedCase.getStatusCode());
        
        // At least one should work (lowercase is expected)
        assertThat(lowerCase.getStatusCode())
                .as("Lowercase dataset name should work")
                .isEqualTo(200);
    }

    @Test(description = "Verify Unicode characters in dataset name")
    @Story("Special Characters")
    @Severity(SeverityLevel.MINOR)
    public void testUnicodeCharactersInDataset() {
        String unicodeDataset = "oa_citations_测试";
        Response response = fieldsService.getFields(unicodeDataset, "v1");
        
        assertThat(response.getStatusCode())
                .as("Unicode characters should be handled")
                .isIn(400, 404);
        
        log.info("Unicode dataset name handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify null byte injection attempt")
    @Story("Security Testing")
    @Severity(SeverityLevel.CRITICAL)
    public void testNullByteInjection() {
        String maliciousDataset = "oa_citations%00.txt";
        Response response = fieldsService.getFields(maliciousDataset, "v1");
        
        assertThat(response.getStatusCode())
                .as("Null byte injection should be rejected")
                .isIn(400, 404);
        
        log.info("Null byte injection handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify path traversal attempt")
    @Story("Security Testing")
    @Severity(SeverityLevel.CRITICAL)
    public void testPathTraversalAttempt() {
        String maliciousDataset = "../../etc/passwd";
        Response response = fieldsService.getFields(maliciousDataset, "v1");
        
        assertThat(response.getStatusCode())
                .as("Path traversal should be rejected")
                .isIn(400, 404);
        
        log.info("Path traversal attempt handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify extremely long dataset name")
    @Story("Boundary Testing")
    @Severity(SeverityLevel.NORMAL)
    public void testExtremelyLongDatasetName() {
        String longDataset = "a".repeat(10000);
        Response response = fieldsService.getFields(longDataset, "v1");
        
        assertThat(response.getStatusCode())
                .as("Extremely long dataset name should be rejected")
                .isIn(400, 404, 414);
        
        log.info("Long dataset name handled with status: {}", response.getStatusCode());
    }
}
