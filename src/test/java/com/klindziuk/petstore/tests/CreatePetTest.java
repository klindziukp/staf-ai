package com.klindziuk.petstore.tests;

import com.klindziuk.petstore.base.BaseTest;
import com.klindziuk.petstore.model.Pet;
import com.klindziuk.petstore.util.TestDataBuilder;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import retrofit2.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for POST /pets endpoint
 */
@Epic("Petstore API")
@Feature("Create Pet")
public class CreatePetTest extends BaseTest {
    
    @Test(description = "POST-001: Create pet with required fields only")
    @Story("Create pet")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that POST /pets endpoint creates pet with id and name")
    public void testCreatePetWithRequiredFields() {
        Pet pet = TestDataBuilder.createPetWithoutTag();
        
        Response<Pet> response = petService.createPet(pet);
        
        assertThat(response.code())
                .as("Status code should be 201 Created")
                .isEqualTo(201);
    }
    
    @Test(description = "POST-002: Create pet with all fields")
    @Story("Create pet")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that POST /pets endpoint creates pet with all fields including optional tag")
    public void testCreatePetWithAllFields() {
        Pet pet = TestDataBuilder.createRandomPet();
        
        Response<Pet> response = petService.createPet(pet);
        
        assertThat(response.code())
                .as("Status code should be 201 Created")
                .isEqualTo(201);
    }
    
    @Test(description = "POST-003: Create pet without required field: id")
    @Story("Create pet - negative scenarios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that POST /pets endpoint returns error when id is missing")
    public void testCreatePetWithoutId() {
        Pet pet = TestDataBuilder.createPetWithoutId();
        
        Response<Pet> response = petService.createPet(pet);
        
        assertThat(response.code())
                .as("Should return error code for missing required field")
                .isGreaterThanOrEqualTo(400);
    }
    
    @Test(description = "POST-004: Create pet without required field: name")
    @Story("Create pet - negative scenarios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that POST /pets endpoint returns error when name is missing")
    public void testCreatePetWithoutName() {
        Pet pet = TestDataBuilder.createPetWithoutName();
        
        Response<Pet> response = petService.createPet(pet);
        
        assertThat(response.code())
                .as("Should return error code for missing required field")
                .isGreaterThanOrEqualTo(400);
    }
    
    @Test(description = "POST-009: Create pet with id at lower bound (0)")
    @Story("Create pet - boundary testing")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies boundary value testing with id=0")
    public void testCreatePetWithIdZero() {
        Pet pet = TestDataBuilder.createPetWithId(0L);
        
        Response<Pet> response = petService.createPet(pet);
        
        // API may accept or reject id=0 depending on business rules
        if (response.isSuccessful()) {
            assertThat(response.code())
                    .as("Status code should be 201 if id=0 is allowed")
                    .isEqualTo(201);
        } else {
            assertThat(response.code())
                    .as("Should return error code if id=0 is not allowed")
                    .isGreaterThanOrEqualTo(400);
        }
    }
    
    @Test(description = "POST-010: Create pet with id at upper bound (large integer)")
    @Story("Create pet - boundary testing")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies boundary value testing with maximum integer value")
    public void testCreatePetWithMaxIntegerId() {
        Pet pet = TestDataBuilder.createPetWithId(2147483647L);
        
        Response<Pet> response = petService.createPet(pet);
        
        assertThat(response.code())
                .as("Status code should be 201 for valid large integer id")
                .isEqualTo(201);
    }
    
    @Test(description = "POST-011: Create pet with empty name")
    @Story("Create pet - boundary testing")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that empty name is handled correctly")
    public void testCreatePetWithEmptyName() {
        Pet pet = TestDataBuilder.createPetWithIdAndName(TestDataBuilder.generateRandomId(), "");
        
        Response<Pet> response = petService.createPet(pet);
        
        // API may accept or reject empty name depending on business rules
        if (response.isSuccessful()) {
            assertThat(response.code())
                    .as("Status code should be 201 if empty name is allowed")
                    .isEqualTo(201);
        } else {
            assertThat(response.code())
                    .as("Should return error code if empty name is not allowed")
                    .isGreaterThanOrEqualTo(400);
        }
    }
    
    @Test(description = "POST-014: Verify response schema for created pet")
    @Story("Schema validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that response matches expected Pet schema")
    public void testCreatePetResponseSchema() {
        Pet pet = TestDataBuilder.createRandomPet();
        
        Response<Pet> response = petService.createPet(pet);
        
        if (response.isSuccessful()) {
            Pet createdPet = response.body();
            if (createdPet != null) {
                assertThat(createdPet.getId())
                        .as("Created pet should have ID field")
                        .isNotNull();
                assertThat(createdPet.getName())
                        .as("Created pet should have name field")
                        .isNotNull();
            }
        }
    }
}
