package com.klindziuk.petstore.tests;

import com.klindziuk.petstore.base.BaseTest;
import com.klindziuk.petstore.model.Pet;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import retrofit2.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for GET /pets/{petId} endpoint
 */
@Epic("Petstore API")
@Feature("Get Pet By ID")
public class GetPetByIdTest extends BaseTest {
    
    @Test(description = "GETID-001: Get existing pet by valid id")
    @Story("Get pet by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that GET /pets/{petId} endpoint returns pet for valid ID")
    public void testGetExistingPetById() {
        Long petId = 1L;
        Response<Pet> response = petService.getPetById(petId);
        
        if (response.isSuccessful()) {
            assertThat(response.code())
                    .as("Status code should be 200")
                    .isEqualTo(200);
            
            Pet pet = response.body();
            assertThat(pet)
                    .as("Pet should not be null")
                    .isNotNull();
            
            assertThat(pet.getId())
                    .as("Pet ID should match requested ID")
                    .isEqualTo(petId);
            
            assertThat(pet.getName())
                    .as("Pet name should not be null or empty")
                    .isNotBlank();
        } else {
            // If pet doesn't exist, should return 404
            assertThat(response.code())
                    .as("Should return 404 if pet not found")
                    .isEqualTo(404);
        }
    }
    
    @Test(description = "GETID-002: Get pet with non-existent id")
    @Story("Get pet by ID - negative scenarios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that GET /pets/{petId} endpoint returns error for non-existent ID")
    public void testGetNonExistentPetById() {
        Long petId = 99999999L;
        Response<Pet> response = petService.getPetById(petId);
        
        assertThat(response.code())
                .as("Should return 404 for non-existent pet")
                .isEqualTo(404);
    }
    
    @Test(description = "GETID-005: Get pet with negative id")
    @Story("Get pet by ID - negative scenarios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that GET /pets/{petId} endpoint handles negative ID")
    public void testGetPetWithNegativeId() {
        Long petId = -1L;
        Response<Pet> response = petService.getPetById(petId);
        
        // API should return error for negative ID
        assertThat(response.code())
                .as("Should return error code for negative ID")
                .isGreaterThanOrEqualTo(400);
    }
    
    @Test(description = "GETID-006: Get pet with id=0")
    @Story("Get pet by ID - boundary testing")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies boundary value testing with id=0")
    public void testGetPetWithIdZero() {
        Long petId = 0L;
        Response<Pet> response = petService.getPetById(petId);
        
        // API may accept or reject id=0 depending on business rules
        if (response.isSuccessful()) {
            assertThat(response.code())
                    .as("Status code should be 200 if id=0 is valid")
                    .isEqualTo(200);
        } else {
            assertThat(response.code())
                    .as("Should return error code if id=0 is not valid")
                    .isIn(400, 404);
        }
    }
    
    @Test(description = "GETID-007: Get pet with minimum valid id")
    @Story("Get pet by ID - boundary testing")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies boundary value testing with minimum valid ID")
    public void testGetPetWithMinimumValidId() {
        Long petId = 1L;
        Response<Pet> response = petService.getPetById(petId);
        
        if (response.isSuccessful()) {
            assertThat(response.code())
                    .as("Status code should be 200")
                    .isEqualTo(200);
            
            Pet pet = response.body();
            assertThat(pet)
                    .as("Pet should not be null")
                    .isNotNull();
        } else {
            assertThat(response.code())
                    .as("Should return 404 if pet not found")
                    .isEqualTo(404);
        }
    }
    
    @Test(description = "GETID-008: Get pet with maximum integer id")
    @Story("Get pet by ID - boundary testing")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies boundary value testing with maximum integer value")
    public void testGetPetWithMaximumIntegerId() {
        Long petId = 2147483647L;
        Response<Pet> response = petService.getPetById(petId);
        
        // Should handle large ID gracefully
        assertThat(response.code())
                .as("Should return valid response code")
                .isIn(200, 404);
    }
    
    @Test(description = "GETID-010: Verify response schema for single pet")
    @Story("Schema validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that response matches expected Pet schema")
    public void testGetPetByIdResponseSchema() {
        Long petId = 1L;
        Response<Pet> response = petService.getPetById(petId);
        
        if (response.isSuccessful()) {
            Pet pet = response.body();
            assertThat(pet)
                    .as("Pet should not be null")
                    .isNotNull();
            
            assertThat(pet.getId())
                    .as("Pet should have ID field")
                    .isNotNull();
            
            assertThat(pet.getName())
                    .as("Pet should have name field")
                    .isNotNull();
            
            // tag is optional, so we don't assert it
        }
    }
}
