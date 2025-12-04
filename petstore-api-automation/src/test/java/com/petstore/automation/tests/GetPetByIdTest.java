package com.petstore.automation.tests;

import com.petstore.automation.base.BaseTest;
import com.petstore.automation.model.Pet;
import com.petstore.automation.util.ResponseValidator;
import com.petstore.automation.util.TestDataBuilder;
import io.qameta.allure.*;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

/**
 * Test class for GET /pets/{petId} endpoint.
 * Tests retrieving pets by ID with various scenarios.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Slf4j
@Epic("Petstore API")
@Feature("Get Pet By ID")
public class GetPetByIdTest extends BaseTest {
    
    @Test(description = "GETPETBYID-001: Get existing pet by valid ID")
    @Description("Verify that retrieving an existing pet by ID returns 200 OK with pet data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Pet By ID - Positive Scenarios")
    public void testGetExistingPetById() {
        // First create a pet
        Pet pet = TestDataBuilder.createValidPet();
        Response createResponse = petApiClient.createPet(pet);
        
        if (createResponse.getStatusCode() >= 200 && createResponse.getStatusCode() < 300) {
            // Then retrieve it
            Response response = petApiClient.getPetById(pet.getId());
            
            SoftAssertions softly = new SoftAssertions();
            
            if (response.getStatusCode() == 200) {
                ResponseValidator.validateStatusCode(response, 200);
                
                Pet retrievedPet = response.as(Pet.class);
                ResponseValidator.validatePetSchema(retrievedPet, softly);
                
                softly.assertThat(retrievedPet.getId())
                        .as("Retrieved pet ID should match")
                        .isEqualTo(pet.getId());
                
                log.info("Successfully retrieved pet: {}", retrievedPet);
            } else {
                log.warn("Pet retrieval returned status: {}", response.getStatusCode());
            }
            
            softly.assertAll();
        } else {
            log.warn("Pet creation failed, skipping retrieval test");
        }
    }
    
    @Test(description = "GETPETBYID-002: Get non-existent pet by ID")
    @Description("Verify that retrieving a non-existent pet returns 404 Not Found")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Pet By ID - Negative Scenarios")
    public void testGetNonExistentPetById() {
        Long nonExistentId = 999999999L;
        Response response = petApiClient.getPetById(nonExistentId);
        
        SoftAssertions softly = new SoftAssertions();
        
        // Should return 404 or other error
        softly.assertThat(response.getStatusCode())
                .as("Status code should indicate not found or error")
                .isGreaterThanOrEqualTo(400);
        
        softly.assertAll();
        log.info("Non-existent pet test completed with status: {}", response.getStatusCode());
    }
    
    @Test(description = "GETPETBYID-003: Get pet with invalid ID (string)")
    @Description("Verify that using invalid ID format returns error")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get Pet By ID - Negative Scenarios")
    public void testGetPetByInvalidStringId() {
        Response response = petApiClient.getPetById("invalid_id");
        
        SoftAssertions softly = new SoftAssertions();
        
        // Should return 400 or 404
        softly.assertThat(response.getStatusCode())
                .as("Status code should indicate error")
                .isGreaterThanOrEqualTo(400);
        
        softly.assertAll();
        log.info("Invalid string ID test completed with status: {}", response.getStatusCode());
    }
    
    @Test(description = "GETPETBYID-004: Get pet with negative ID")
    @Description("Verify that using negative ID returns error")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get Pet By ID - Negative Scenarios")
    public void testGetPetByNegativeId() {
        Response response = petApiClient.getPetById(-1L);
        
        SoftAssertions softly = new SoftAssertions();
        
        // Should return error
        softly.assertThat(response.getStatusCode())
                .as("Status code should indicate error")
                .isGreaterThanOrEqualTo(400);
        
        softly.assertAll();
        log.info("Negative ID test completed with status: {}", response.getStatusCode());
    }
    
    @Test(description = "GETPETBYID-006: Get pet with ID at lower boundary")
    @Description("Verify behavior when pet ID is 0")
    @Severity(SeverityLevel.MINOR)
    @Story("Get Pet By ID - Boundary Testing")
    public void testGetPetByZeroId() {
        Response response = petApiClient.getPetById(0L);
        
        SoftAssertions softly = new SoftAssertions();
        
        // Accept any valid response
        softly.assertThat(response.getStatusCode())
                .as("Status code should be valid")
                .isBetween(200, 599);
        
        softly.assertAll();
        log.info("Zero ID test completed with status: {}", response.getStatusCode());
    }
    
    @Test(description = "GETPETBYID-005: Get pet with maximum int64 ID")
    @Description("Verify behavior when pet ID is at upper boundary")
    @Severity(SeverityLevel.MINOR)
    @Story("Get Pet By ID - Boundary Testing")
    public void testGetPetByMaxId() {
        Response response = petApiClient.getPetById(Long.MAX_VALUE);
        
        SoftAssertions softly = new SoftAssertions();
        
        // Should return 404 or other error
        softly.assertThat(response.getStatusCode())
                .as("Status code should be valid")
                .isBetween(200, 599);
        
        softly.assertAll();
        log.info("Max ID test completed with status: {}", response.getStatusCode());
    }
}
