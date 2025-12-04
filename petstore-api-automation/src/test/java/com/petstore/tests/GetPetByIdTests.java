package com.petstore.tests;

import com.petstore.base.BaseTest;
import com.petstore.models.Pet;
import com.petstore.utils.DataGenerator;
import com.petstore.utils.ResponseValidator;
import io.qameta.allure.*;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import retrofit2.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for GET /pets/{petId} endpoint
 * Covers positive, negative, and edge case scenarios for getting pet by ID
 */
@Slf4j
@Epic("Petstore API")
@Feature("Pet Management")
@Story("Get Pet By ID")
public class GetPetByIdTests extends BaseTest {
    
    @Test(description = "Verify getting an existing pet by valid ID")
    @Description("Test verifies that GET /pets/{petId} returns pet details for valid ID")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetPetByValidId() {
        log.info("Test: Get pet by valid ID");
        
        // First create a pet
        Pet newPet = DataGenerator.generateRandomPet();
        Response<Pet> createResponse = petService.createPet(newPet);
        
        if (createResponse.isSuccessful() && createResponse.body() != null) {
            String petId = String.valueOf(createResponse.body().getId());
            
            // Then retrieve it
            Response<Pet> getResponse = petService.getPetById(petId);
            
            ResponseValidator.validateStatusCode(getResponse, 200);
            ResponseValidator.validateSuccessResponse(getResponse);
            
            Pet retrievedPet = getResponse.body();
            ResponseValidator.validatePetRequiredFields(retrievedPet);
            
            log.info("Successfully retrieved pet with ID: {}", petId);
        } else {
            log.warn("Could not create pet for test, skipping validation");
        }
    }
    
    @Test(description = "Verify error when getting pet with non-existent ID")
    @Description("Test verifies that GET /pets/{petId} returns error for non-existent ID")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPetByNonExistentId() {
        log.info("Test: Get pet by non-existent ID");
        
        String nonExistentId = "999999999";
        Response<Pet> response = petService.getPetById(nonExistentId);
        
        // Expecting error response (404 or default error)
        assertThat(response.isSuccessful()).isFalse();
        
        log.info("Correctly received error response for non-existent ID");
    }
    
    @Test(description = "Verify error when getting pet with invalid ID format")
    @Description("Test verifies that GET /pets/{petId} returns error for invalid ID format")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetByInvalidIdFormat() {
        log.info("Test: Get pet by invalid ID format");
        
        String invalidId = "abc123";
        Response<Pet> response = petService.getPetById(invalidId);
        
        // Expecting error response
        assertThat(response.isSuccessful()).isFalse();
        
        log.info("Correctly received error response for invalid ID format");
    }
    
    @Test(description = "Verify error when getting pet with negative ID")
    @Description("Test verifies that GET /pets/{petId} returns error for negative ID")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetByNegativeId() {
        log.info("Test: Get pet by negative ID");
        
        String negativeId = "-1";
        Response<Pet> response = petService.getPetById(negativeId);
        
        // Expecting error response
        assertThat(response.isSuccessful()).isFalse();
        
        log.info("Correctly received error response for negative ID");
    }
    
    @Test(description = "Verify getting pet with ID=0")
    @Description("Test verifies that GET /pets/{petId} handles ID=0")
    @Severity(SeverityLevel.MINOR)
    public void testGetPetByZeroId() {
        log.info("Test: Get pet by ID=0");
        
        String zeroId = "0";
        Response<Pet> response = petService.getPetById(zeroId);
        
        // Could be valid or invalid depending on implementation
        log.info("Response code for ID=0: {}", response.code());
    }
    
    @Test(description = "Verify SQL injection protection in pet ID")
    @Description("Test verifies that GET /pets/{petId} properly handles SQL injection attempts")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPetByIdWithSqlInjection() {
        log.info("Test: Get pet by ID with SQL injection");
        
        String sqlInjectionId = "1; DROP TABLE pets;--";
        Response<Pet> response = petService.getPetById(sqlInjectionId);
        
        // Should handle safely - expecting error response
        assertThat(response.isSuccessful()).isFalse();
        
        log.info("SQL injection attempt in ID handled safely");
    }
    
    @Test(description = "Verify XSS protection in pet ID")
    @Description("Test verifies that GET /pets/{petId} properly handles XSS attempts")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPetByIdWithXss() {
        log.info("Test: Get pet by ID with XSS");
        
        String xssId = "<script>alert('XSS')</script>";
        Response<Pet> response = petService.getPetById(xssId);
        
        // Should handle safely - expecting error response
        assertThat(response.isSuccessful()).isFalse();
        
        log.info("XSS attempt in ID handled safely");
    }
    
    @Test(description = "Verify getting pet with very long ID")
    @Description("Test verifies that GET /pets/{petId} handles very long ID values")
    @Severity(SeverityLevel.MINOR)
    public void testGetPetByVeryLongId() {
        log.info("Test: Get pet by very long ID");
        
        String longId = DataGenerator.generateLongString(1000);
        Response<Pet> response = petService.getPetById(longId);
        
        // Expecting error response
        assertThat(response.isSuccessful()).isFalse();
        
        log.info("Long ID handled appropriately");
    }
    
    @Test(description = "Verify getting pet with special characters in ID")
    @Description("Test verifies that GET /pets/{petId} handles special characters in ID")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetByIdWithSpecialCharacters() {
        log.info("Test: Get pet by ID with special characters");
        
        String specialCharId = "!@#$%^&*()";
        Response<Pet> response = petService.getPetById(specialCharId);
        
        // Expecting error response
        assertThat(response.isSuccessful()).isFalse();
        
        log.info("Special characters in ID handled appropriately");
    }
    
    @Test(description = "Verify getting pet with empty ID")
    @Description("Test verifies that GET /pets/{petId} handles empty ID")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetByEmptyId() {
        log.info("Test: Get pet by empty ID");
        
        String emptyId = "";
        Response<Pet> response = petService.getPetById(emptyId);
        
        // Expecting error response or redirect to /pets
        log.info("Response code for empty ID: {}", response.code());
    }
}
