package com.petstore.automation.tests;

import com.petstore.automation.base.BaseTest;
import com.petstore.automation.model.Pet;
import com.petstore.automation.util.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class for GET /pets endpoint.
 * Tests listing pets with various scenarios.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Slf4j
@Epic("Petstore API")
@Feature("List Pets")
public class ListPetsTest extends BaseTest {
    
    @Test(description = "GETPETS-001: List all pets without limit parameter")
    @Description("Verify that listing all pets without limit returns 200 OK with array of pets")
    @Severity(SeverityLevel.CRITICAL)
    @Story("List Pets - Positive Scenarios")
    public void testListAllPetsWithoutLimit() {
        Response response = petApiClient.listPets(null);
        
        SoftAssertions softly = new SoftAssertions();
        ResponseValidator.validateStatusCode(response, 200);
        
        List<Pet> pets = response.jsonPath().getList("", Pet.class);
        softly.assertThat(pets)
                .as("Pets list should not be null")
                .isNotNull();
        
        if (pets != null && !pets.isEmpty()) {
            softly.assertThat(pets.size())
                    .as("Pets list should not exceed 100 items")
                    .isLessThanOrEqualTo(100);
            
            ResponseValidator.validatePetsList(pets, softly);
        }
        
        softly.assertAll();
        log.info("Successfully listed {} pets", pets != null ? pets.size() : 0);
    }
    
    @Test(description = "GETPETS-002: List pets with valid limit parameter")
    @Description("Verify that listing pets with limit=10 returns at most 10 pets")
    @Severity(SeverityLevel.CRITICAL)
    @Story("List Pets - Positive Scenarios")
    public void testListPetsWithValidLimit() {
        int limit = 10;
        Response response = petApiClient.listPets(limit);
        
        ResponseValidator.validateStatusCode(response, 200);
        
        List<Pet> pets = response.jsonPath().getList("", Pet.class);
        SoftAssertions softly = new SoftAssertions();
        
        softly.assertThat(pets)
                .as("Pets list should not be null")
                .isNotNull();
        
        if (pets != null) {
            softly.assertThat(pets.size())
                    .as("Pets list size should not exceed limit")
                    .isLessThanOrEqualTo(limit);
        }
        
        softly.assertAll();
        log.info("Successfully listed {} pets with limit {}", pets != null ? pets.size() : 0, limit);
    }
    
    @Test(description = "GETPETS-006: Test lower boundary with limit=0")
    @Description("Verify behavior when limit is set to 0")
    @Severity(SeverityLevel.NORMAL)
    @Story("List Pets - Boundary Testing")
    public void testListPetsWithZeroLimit() {
        Response response = petApiClient.listPets(0);
        
        // Accept either 200 with empty array or error response
        int statusCode = response.getStatusCode();
        SoftAssertions softly = new SoftAssertions();
        
        softly.assertThat(statusCode)
                .as("Status code should be 200 or 4xx")
                .isIn(200, 400, 422);
        
        if (statusCode == 200) {
            List<Pet> pets = response.jsonPath().getList("", Pet.class);
            softly.assertThat(pets)
                    .as("Pets list should be empty or null")
                    .satisfiesAnyOf(
                            list -> org.assertj.core.api.Assertions.assertThat(list).isNull(),
                            list -> org.assertj.core.api.Assertions.assertThat(list).isEmpty()
                    );
        }
        
        softly.assertAll();
        log.info("Tested limit=0 boundary condition");
    }
    
    @Test(description = "GETPETS-007: Test upper boundary with limit=100")
    @Description("Verify that limit=100 (max allowed) works correctly")
    @Severity(SeverityLevel.NORMAL)
    @Story("List Pets - Boundary Testing")
    public void testListPetsWithMaxLimit() {
        int limit = 100;
        Response response = petApiClient.listPets(limit);
        
        ResponseValidator.validateStatusCode(response, 200);
        
        List<Pet> pets = response.jsonPath().getList("", Pet.class);
        SoftAssertions softly = new SoftAssertions();
        
        softly.assertThat(pets)
                .as("Pets list should not be null")
                .isNotNull();
        
        if (pets != null) {
            softly.assertThat(pets.size())
                    .as("Pets list size should not exceed max limit")
                    .isLessThanOrEqualTo(limit);
        }
        
        softly.assertAll();
        log.info("Successfully tested max limit boundary");
    }
    
    @Test(description = "GETPETS-003: Test with negative limit")
    @Description("Verify that negative limit returns error response")
    @Severity(SeverityLevel.NORMAL)
    @Story("List Pets - Negative Scenarios")
    public void testListPetsWithNegativeLimit() {
        Response response = petApiClient.listPets(-1);
        
        // Should return error (4xx or 5xx)
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(response.getStatusCode())
                .as("Status code should indicate error")
                .isGreaterThanOrEqualTo(400);
        
        softly.assertAll();
        log.info("Negative limit test completed with status: {}", response.getStatusCode());
    }
    
    @Test(description = "GETPETS-005: Test with limit exceeding maximum")
    @Description("Verify that limit > 100 returns error or is capped at 100")
    @Severity(SeverityLevel.NORMAL)
    @Story("List Pets - Negative Scenarios")
    public void testListPetsWithExcessiveLimit() {
        Response response = petApiClient.listPets(101);
        
        int statusCode = response.getStatusCode();
        SoftAssertions softly = new SoftAssertions();
        
        // Either error or success with capped results
        if (statusCode == 200) {
            List<Pet> pets = response.jsonPath().getList("", Pet.class);
            softly.assertThat(pets)
                    .as("Pets list should not exceed 100 items")
                    .hasSizeLessThanOrEqualTo(100);
        } else {
            softly.assertThat(statusCode)
                    .as("Status code should indicate error")
                    .isGreaterThanOrEqualTo(400);
        }
        
        softly.assertAll();
        log.info("Excessive limit test completed");
    }
}
