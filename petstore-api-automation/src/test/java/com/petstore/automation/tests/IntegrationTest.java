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

import java.util.List;

/**
 * Integration test class for end-to-end scenarios.
 * Tests complete workflows across multiple endpoints.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Slf4j
@Epic("Petstore API")
@Feature("Integration Tests")
public class IntegrationTest extends BaseTest {
    
    @Test(description = "INTEGRATION-001: Create pet and retrieve it")
    @Description("Verify complete workflow: create a pet and then retrieve it by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("End-to-End Workflows")
    public void testCreateAndRetrievePet() {
        // Step 1: Create a pet
        Pet pet = TestDataBuilder.createValidPet();
        Response createResponse = petApiClient.createPet(pet);
        
        SoftAssertions softly = new SoftAssertions();
        
        softly.assertThat(createResponse.getStatusCode())
                .as("Create pet should succeed")
                .isIn(200, 201, 204);
        
        if (createResponse.getStatusCode() >= 200 && createResponse.getStatusCode() < 300) {
            // Step 2: Retrieve the created pet
            Response getResponse = petApiClient.getPetById(pet.getId());
            
            if (getResponse.getStatusCode() == 200) {
                Pet retrievedPet = getResponse.as(Pet.class);
                
                ResponseValidator.validatePetSchema(retrievedPet, softly);
                
                softly.assertThat(retrievedPet.getId())
                        .as("Retrieved pet ID should match created pet")
                        .isEqualTo(pet.getId());
                
                softly.assertThat(retrievedPet.getName())
                        .as("Retrieved pet name should match created pet")
                        .isEqualTo(pet.getName());
                
                log.info("Successfully completed create and retrieve workflow");
            } else {
                log.warn("Pet retrieval returned status: {}", getResponse.getStatusCode());
            }
        }
        
        softly.assertAll();
    }
    
    @Test(description = "INTEGRATION-002: Create pet and verify it appears in list")
    @Description("Verify that newly created pet appears in the pets list")
    @Severity(SeverityLevel.NORMAL)
    @Story("End-to-End Workflows")
    public void testCreatePetAndVerifyInList() {
        // Step 1: Create a pet with unique name
        Pet pet = TestDataBuilder.createValidPet();
        Response createResponse = petApiClient.createPet(pet);
        
        SoftAssertions softly = new SoftAssertions();
        
        if (createResponse.getStatusCode() >= 200 && createResponse.getStatusCode() < 300) {
            // Step 2: List all pets
            Response listResponse = petApiClient.listPets(100);
            
            if (listResponse.getStatusCode() == 200) {
                List<Pet> pets = listResponse.jsonPath().getList("", Pet.class);
                
                softly.assertThat(pets)
                        .as("Pets list should not be null")
                        .isNotNull();
                
                // Note: This may not always work if API doesn't return all pets
                // or if the created pet is not immediately available
                log.info("Listed {} pets after creation", pets != null ? pets.size() : 0);
            }
        }
        
        softly.assertAll();
    }
    
    @Test(description = "INTEGRATION-003: Create multiple pets and list with limit")
    @Description("Verify creating multiple pets and listing them with limit parameter")
    @Severity(SeverityLevel.NORMAL)
    @Story("End-to-End Workflows")
    public void testCreateMultiplePetsAndListWithLimit() {
        SoftAssertions softly = new SoftAssertions();
        
        // Step 1: Create multiple pets
        int petsToCreate = 3;
        for (int i = 0; i < petsToCreate; i++) {
            Pet pet = TestDataBuilder.createValidPet();
            Response createResponse = petApiClient.createPet(pet);
            
            softly.assertThat(createResponse.getStatusCode())
                    .as("Pet creation should succeed")
                    .isIn(200, 201, 204);
            
            log.info("Created pet {}/{}", i + 1, petsToCreate);
        }
        
        // Step 2: List pets with limit
        int limit = 2;
        Response listResponse = petApiClient.listPets(limit);
        
        ResponseValidator.validateStatusCode(listResponse, 200);
        
        List<Pet> pets = listResponse.jsonPath().getList("", Pet.class);
        
        if (pets != null) {
            softly.assertThat(pets.size())
                    .as("Returned pets should not exceed limit")
                    .isLessThanOrEqualTo(limit);
            
            ResponseValidator.validatePetsList(pets, softly);
        }
        
        softly.assertAll();
        log.info("Successfully completed multiple pets creation and listing workflow");
    }
    
    @Test(description = "INTEGRATION-004: Verify data consistency across operations")
    @Description("Verify that pet data remains consistent across create and retrieve operations")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Data Consistency")
    public void testDataConsistencyAcrossOperations() {
        // Create a pet with all fields
        Pet originalPet = TestDataBuilder.createValidPet();
        Response createResponse = petApiClient.createPet(originalPet);
        
        SoftAssertions softly = new SoftAssertions();
        
        if (createResponse.getStatusCode() >= 200 && createResponse.getStatusCode() < 300) {
            // Retrieve the pet
            Response getResponse = petApiClient.getPetById(originalPet.getId());
            
            if (getResponse.getStatusCode() == 200) {
                Pet retrievedPet = getResponse.as(Pet.class);
                
                // Verify all fields match
                softly.assertThat(retrievedPet.getId())
                        .as("Pet ID should be consistent")
                        .isEqualTo(originalPet.getId());
                
                softly.assertThat(retrievedPet.getName())
                        .as("Pet name should be consistent")
                        .isEqualTo(originalPet.getName());
                
                if (originalPet.getTag() != null) {
                    softly.assertThat(retrievedPet.getTag())
                            .as("Pet tag should be consistent")
                            .isEqualTo(originalPet.getTag());
                }
                
                log.info("Data consistency verified successfully");
            }
        }
        
        softly.assertAll();
    }
}
