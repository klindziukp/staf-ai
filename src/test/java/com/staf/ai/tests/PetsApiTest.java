package com.staf.ai.tests;

import com.staf.ai.base.BaseTest;
import com.staf.ai.model.Error;
import com.staf.ai.model.Pet;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test class for GET /pets endpoint.
 * Tests listing all pets with various scenarios.
 */
@Epic("Petstore API")
@Feature("Pets Management")
@Story("List All Pets")
public class PetsApiTest extends BaseTest {
    
    @Test(groups = {"smoke", "regression"}, priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case 1: List all pets with no limit parameter")
    @Issue("TC-001")
    public void testListAllPetsWithNoLimit() {
        Response response = petsApiClient.getAllPets();
        
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("size()", lessThanOrEqualTo(100));
        
        List<Pet> pets = response.jsonPath().getList("", Pet.class);
        assertThat(pets).isNotNull();
        assertThat(pets.size()).isLessThanOrEqualTo(100);
    }
    
    @Test(groups = {"smoke", "regression"}, priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case 2: List pets with limit=10")
    @Issue("TC-002")
    public void testListPetsWithLimit10() {
        int limit = 10;
        Response response = petsApiClient.getAllPetsWithLimit(limit);
        
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("size()", lessThanOrEqualTo(limit));
        
        List<Pet> pets = response.jsonPath().getList("", Pet.class);
        assertThat(pets.size()).isLessThanOrEqualTo(limit);
    }
    
    @Test(groups = {"smoke", "regression"}, priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case 3: List pets with limit=100 (maximum)")
    @Issue("TC-003")
    public void testListPetsWithMaxLimit() {
        int limit = 100;
        Response response = petsApiClient.getAllPetsWithLimit(limit);
        
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("size()", lessThanOrEqualTo(limit));
        
        List<Pet> pets = response.jsonPath().getList("", Pet.class);
        assertThat(pets.size()).isLessThanOrEqualTo(limit);
    }
    
    @Test(groups = {"regression", "negative"}, priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 4: List pets with limit exceeding maximum (101)")
    @Issue("TC-004")
    public void testListPetsWithLimitExceedingMax() {
        int limit = 101;
        Response response = petsApiClient.getAllPetsWithLimit(limit);
        
        // Expecting error response for limit > 100
        assertThat(response.getStatusCode()).isNotEqualTo(200);
    }
    
    @Test(groups = {"regression", "negative"}, priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 5: List pets with negative limit")
    @Issue("TC-005")
    public void testListPetsWithNegativeLimit() {
        int limit = -1;
        Response response = petsApiClient.getAllPetsWithLimit(limit);
        
        // Expecting error response for negative limit
        assertThat(response.getStatusCode()).isNotEqualTo(200);
    }
    
    @Test(groups = {"regression", "boundary"}, priority = 8)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 8: List pets with limit=1 (lower boundary)")
    @Issue("TC-008")
    public void testListPetsWithLimitLowerBoundary() {
        int limit = 1;
        Response response = petsApiClient.getAllPetsWithLimit(limit);
        
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("size()", lessThanOrEqualTo(limit));
        
        List<Pet> pets = response.jsonPath().getList("", Pet.class);
        assertThat(pets.size()).isLessThanOrEqualTo(limit);
    }
    
    @Test(groups = {"regression", "validation"}, priority = 10)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case 10: Validate Pet schema in response")
    @Issue("TC-010")
    public void testValidatePetSchemaInResponse() {
        Response response = petsApiClient.getAllPets();
        
        response.then()
                .statusCode(200)
                .contentType("application/json");
        
        List<Pet> pets = response.jsonPath().getList("", Pet.class);
        
        if (!pets.isEmpty()) {
            Pet pet = pets.get(0);
            assertThat(pet.getId()).isNotNull();
            assertThat(pet.getName()).isNotNull().isNotEmpty();
            // Tag is optional, so we don't assert it
        }
    }
    
    @Test(groups = {"regression", "validation"}, priority = 11)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 11: Validate response array size with limit=50")
    @Issue("TC-011")
    public void testValidateResponseArraySizeWithLimit() {
        int limit = 50;
        Response response = petsApiClient.getAllPetsWithLimit(limit);
        
        response.then()
                .statusCode(200)
                .body("size()", lessThanOrEqualTo(limit));
        
        List<Pet> pets = response.jsonPath().getList("", Pet.class);
        assertThat(pets.size()).isLessThanOrEqualTo(limit);
    }
    
    @Test(groups = {"regression", "performance"}, priority = 12)
    @Severity(SeverityLevel.MINOR)
    @Description("Validate response time is acceptable")
    @Issue("TC-PERF-001")
    public void testResponseTime() {
        Response response = petsApiClient.getAllPets();
        
        response.then()
                .statusCode(200)
                .time(lessThan(5000L)); // Response time should be less than 5 seconds
        
        long responseTime = response.getTime();
        assertThat(responseTime).isLessThan(5000L);
    }
}
