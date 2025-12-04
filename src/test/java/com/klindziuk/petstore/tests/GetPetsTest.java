package com.klindziuk.petstore.tests;

import com.klindziuk.petstore.base.BaseTest;
import com.klindziuk.petstore.model.Pet;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import retrofit2.Response;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for GET /pets endpoint
 */
@Epic("Petstore API")
@Feature("Get Pets")
public class GetPetsTest extends BaseTest {
    
    @Test(description = "GET-001: Verify getting all pets without limit")
    @Story("Get all pets")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that GET /pets endpoint returns list of pets without limit parameter")
    public void testGetAllPetsWithoutLimit() {
        Response<List<Pet>> response = petService.getAllPets();
        
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();
        
        assertThat(response.code())
                .as("Status code should be 200")
                .isEqualTo(200);
        
        List<Pet> pets = response.body();
        assertThat(pets)
                .as("Pets list should not be null")
                .isNotNull();
        
        if (!pets.isEmpty()) {
            assertThat(pets.size())
                    .as("Pets array should not exceed 100 items")
                    .isLessThanOrEqualTo(100);
            
            pets.forEach(pet -> {
                assertThat(pet.getId())
                        .as("Pet ID should not be null")
                        .isNotNull();
                assertThat(pet.getName())
                        .as("Pet name should not be null or empty")
                        .isNotBlank();
            });
        }
    }
    
    @Test(description = "GET-002: Verify getting pets with valid limit=10")
    @Story("Get pets with limit")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that GET /pets endpoint returns limited number of pets")
    public void testGetPetsWithValidLimit() {
        int limit = 10;
        Response<List<Pet>> response = petService.getPets(limit);
        
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();
        
        assertThat(response.code())
                .as("Status code should be 200")
                .isEqualTo(200);
        
        List<Pet> pets = response.body();
        assertThat(pets)
                .as("Pets list should not be null")
                .isNotNull();
        
        assertThat(pets.size())
                .as("Number of pets should not exceed limit")
                .isLessThanOrEqualTo(limit);
    }
    
    @Test(description = "GET-003: Verify getting pets with maximum limit=100")
    @Story("Get pets with limit")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that GET /pets endpoint handles maximum limit of 100")
    public void testGetPetsWithMaximumLimit() {
        int limit = 100;
        Response<List<Pet>> response = petService.getPets(limit);
        
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();
        
        assertThat(response.code())
                .as("Status code should be 200")
                .isEqualTo(200);
        
        List<Pet> pets = response.body();
        assertThat(pets)
                .as("Pets list should not be null")
                .isNotNull();
        
        assertThat(pets.size())
                .as("Number of pets should not exceed maximum limit")
                .isLessThanOrEqualTo(limit);
    }
    
    @Test(description = "GET-004: Verify getting pets with limit=0 (lower bound)")
    @Story("Get pets with limit")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies boundary value testing with limit=0")
    public void testGetPetsWithZeroLimit() {
        int limit = 0;
        Response<List<Pet>> response = petService.getPets(limit);
        
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();
        
        List<Pet> pets = response.body();
        assertThat(pets)
                .as("Pets list should not be null")
                .isNotNull();
        
        assertThat(pets)
                .as("Pets list should be empty when limit is 0")
                .isEmpty();
    }
    
    @Test(description = "GET-005: Verify getting pets with limit=1 (just above lower bound)")
    @Story("Get pets with limit")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies boundary value testing with limit=1")
    public void testGetPetsWithLimitOne() {
        int limit = 1;
        Response<List<Pet>> response = petService.getPets(limit);
        
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();
        
        List<Pet> pets = response.body();
        assertThat(pets)
                .as("Pets list should not be null")
                .isNotNull();
        
        assertThat(pets.size())
                .as("Number of pets should not exceed 1")
                .isLessThanOrEqualTo(limit);
    }
    
    @Test(description = "GET-006: Verify getting pets with limit=101 (above upper bound)")
    @Story("Get pets with limit")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that limit above 100 returns error or is capped at 100")
    public void testGetPetsWithLimitAboveMaximum() {
        int limit = 101;
        Response<List<Pet>> response = petService.getPets(limit);
        
        // API may either return error or cap the limit at 100
        if (response.isSuccessful()) {
            List<Pet> pets = response.body();
            assertThat(pets)
                    .as("Pets list should not be null")
                    .isNotNull();
            
            assertThat(pets.size())
                    .as("Number of pets should not exceed 100 even if limit is 101")
                    .isLessThanOrEqualTo(100);
        } else {
            assertThat(response.code())
                    .as("Should return error code for invalid limit")
                    .isGreaterThanOrEqualTo(400);
        }
    }
    
    @Test(description = "GET-007: Verify getting pets with negative limit")
    @Story("Get pets with limit")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that negative limit returns error")
    public void testGetPetsWithNegativeLimit() {
        int limit = -1;
        Response<List<Pet>> response = petService.getPets(limit);
        
        // API should return error for negative limit
        assertThat(response.code())
                .as("Should return error code for negative limit")
                .isGreaterThanOrEqualTo(400);
    }
    
    @Test(description = "GET-011: Verify response schema for pets array")
    @Story("Schema validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that response matches expected Pet schema")
    public void testPetsResponseSchema() {
        Response<List<Pet>> response = petService.getAllPets();
        
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();
        
        List<Pet> pets = response.body();
        assertThat(pets)
                .as("Pets list should not be null")
                .isNotNull();
        
        if (!pets.isEmpty()) {
            Pet firstPet = pets.get(0);
            assertThat(firstPet.getId())
                    .as("Pet should have ID field")
                    .isNotNull();
            assertThat(firstPet.getName())
                    .as("Pet should have name field")
                    .isNotNull();
            // tag is optional, so we don't assert it
        }
    }
}
