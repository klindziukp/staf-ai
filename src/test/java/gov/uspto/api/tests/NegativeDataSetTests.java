package gov.uspto.api.tests;

import gov.uspto.api.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Negative test scenarios for Dataset API endpoints
 */
@Epic("USPTO Data Set API")
@Feature("Dataset Management - Negative Scenarios")
public class NegativeDataSetTests extends BaseTest {

    @Test(description = "Verify POST method is not allowed on datasets endpoint")
    @Story("Invalid HTTP Methods")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify that POST method returns 405 Method Not Allowed")
    public void testPostMethodNotAllowed() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .post("/")
                .then()
                .extract()
                .response();
        
        assertThat(response.getStatusCode())
                .as("POST method should not be allowed")
                .isIn(405, 404);
        
        log.info("POST method correctly rejected with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify PUT method is not allowed on datasets endpoint")
    @Story("Invalid HTTP Methods")
    @Severity(SeverityLevel.NORMAL)
    public void testPutMethodNotAllowed() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .put("/")
                .then()
                .extract()
                .response();
        
        assertThat(response.getStatusCode())
                .as("PUT method should not be allowed")
                .isIn(405, 404);
        
        log.info("PUT method correctly rejected with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify DELETE method is not allowed on datasets endpoint")
    @Story("Invalid HTTP Methods")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteMethodNotAllowed() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .delete("/")
                .then()
                .extract()
                .response();
        
        assertThat(response.getStatusCode())
                .as("DELETE method should not be allowed")
                .isIn(405, 404);
        
        log.info("DELETE method correctly rejected with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify invalid Accept header handling")
    @Story("Invalid Headers")
    @Severity(SeverityLevel.MINOR)
    public void testInvalidAcceptHeader() {
        Response response = given()
                .spec(requestSpec)
                .header("Accept", "application/xml")
                .when()
                .get("/")
                .then()
                .extract()
                .response();
        
        // API should either return JSON anyway or return 406 Not Acceptable
        assertThat(response.getStatusCode())
                .as("Response should handle invalid Accept header")
                .isIn(200, 406);
        
        log.info("Invalid Accept header handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify malformed URL handling")
    @Story("Invalid URLs")
    @Severity(SeverityLevel.NORMAL)
    public void testMalformedUrl() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("//invalid//path")
                .then()
                .extract()
                .response();
        
        assertThat(response.getStatusCode())
                .as("Malformed URL should return error")
                .isIn(400, 404);
        
        log.info("Malformed URL correctly handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify special characters in URL handling")
    @Story("Invalid URLs")
    @Severity(SeverityLevel.MINOR)
    public void testSpecialCharactersInUrl() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/<script>alert('xss')</script>")
                .then()
                .extract()
                .response();
        
        assertThat(response.getStatusCode())
                .as("Special characters should be handled safely")
                .isIn(400, 404);
        
        log.info("Special characters in URL handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify extremely long URL handling")
    @Story("Boundary Testing")
    @Severity(SeverityLevel.MINOR)
    public void testExtremelyLongUrl() {
        String longPath = "/" + "a".repeat(10000);
        
        Response response = given()
                .spec(requestSpec)
                .when()
                .get(longPath)
                .then()
                .extract()
                .response();
        
        assertThat(response.getStatusCode())
                .as("Extremely long URL should be rejected")
                .isIn(400, 404, 414);
        
        log.info("Long URL handled with status: {}", response.getStatusCode());
    }

    @Test(description = "Verify SQL injection attempt is handled safely")
    @Story("Security Testing")
    @Severity(SeverityLevel.CRITICAL)
    public void testSqlInjectionAttempt() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/' OR '1'='1")
                .then()
                .extract()
                .response();
        
        assertThat(response.getStatusCode())
                .as("SQL injection attempt should be rejected")
                .isIn(400, 404);
        
        log.info("SQL injection attempt handled with status: {}", response.getStatusCode());
    }
}
