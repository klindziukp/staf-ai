package com.petstore.tests;

import com.petstore.base.BaseTest;
import com.petstore.models.Pet;
import com.petstore.utils.DataGenerator;
import com.petstore.utils.ResponseValidator;
import io.qameta.allure.*;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import retrofit2.Response;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for GET /pets endpoint
 * Covers positive, negative, and edge case scenarios
 */
@Slf4j
@Epic("Petstore API")
@Feature("Pet Management")
@Story("Get Pets List")
public class GetPetsTests extends BaseTest {
    
    @Test(description = "Verify getting all pets without limit parameter")
    @Description("Test verifies that GET /pets returns a list of pets when no limit is specified")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetAllPetsWithoutLimit() {
        log.info("Test: Get all pets without limit");
        
        Response<List<Pet>> response = petService.getAllPets();
        
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateSuccessResponse(response);
        
        List<Pet> pets = response.body();
        assertThat(pets).isNotNull();
        assertThat(pets.size()).isLessThanOrEqualTo(100);
        
        log.info("Successfully retrieved {} pets", pets.size());
    }
    
    @Test(description = "Verify getting pets with valid limit parameter")
    @Description("Test verifies that GET /pets?limit=10 returns maximum 10 pets")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPetsWithValidLimit() {
        log.info("Test: Get pets with limit=10");
        
        int limit = 10;
        Response<List<Pet>> response = petService.getPets(limit);
        
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateSuccessResponse(response);
        
        List<Pet> pets = response.body();
        ResponseValidator.validatePetsListLimit(pets, limit);
        
        log.info("Successfully retrieved {} pets with limit {}", pets.size(), limit);
    }
    
    @Test(description = "Verify getting pets with maximum limit (100)")
    @Description("Test verifies that GET /pets?limit=100 returns maximum 100 pets")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetsWithMaxLimit() {
        log.info("Test: Get pets with limit=100 (maximum)");
        
        int limit = 100;
        Response<List<Pet>> response = petService.getPets(limit);
        
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateSuccessResponse(response);
        
        List<Pet> pets = response.body();
        ResponseValidator.validatePetsListLimit(pets, limit);
        
        log.info("Successfully retrieved {} pets with max limit", pets.size());
    }
    
    @Test(description = "Verify getting pets with limit=1")
    @Description("Test verifies that GET /pets?limit=1 returns exactly 1 pet")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetsWithLimitOne() {
        log.info("Test: Get pets with limit=1");
        
        int limit = 1;
        Response<List<Pet>> response = petService.getPets(limit);
        
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateSuccessResponse(response);
        
        List<Pet> pets = response.body();
        ResponseValidator.validatePetsListLimit(pets, limit);
        
        log.info("Successfully retrieved {} pet(s)", pets.size());
    }
    
    @Test(description = "Verify error when limit exceeds maximum (101)")
    @Description("Test verifies that GET /pets?limit=101 returns error response")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetsWithLimitExceedingMax() {
        log.info("Test: Get pets with limit=101 (exceeds maximum)");
        
        int limit = 101;
        Response<List<Pet>> response = petService.getPets(limit);
        
        // Expecting error response (400 or default error)
        assertThat(response.isSuccessful()).isFalse();
        
        log.info("Correctly received error response for limit exceeding maximum");
    }
    
    @Test(description = "Verify error when limit is negative")
    @Description("Test verifies that GET /pets?limit=-1 returns error response")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetsWithNegativeLimit() {
        log.info("Test: Get pets with negative limit");
        
        int limit = -1;
        Response<List<Pet>> response = petService.getPets(limit);
        
        // Expecting error response
        assertThat(response.isSuccessful()).isFalse();
        
        log.info("Correctly received error response for negative limit");
    }
    
    @Test(description = "Verify error when limit is zero")
    @Description("Test verifies that GET /pets?limit=0 returns appropriate response")
    @Severity(SeverityLevel.MINOR)
    public void testGetPetsWithZeroLimit() {
        log.info("Test: Get pets with limit=0");
        
        int limit = 0;
        Response<List<Pet>> response = petService.getPets(limit);
        
        ResponseValidator.validateStatusCode(response, 200);
        
        log.info("Response received for limit=0");
    }
    
    @Test(description = "Verify all pets have required fields")
    @Description("Test verifies that all returned pets have required fields (id, name)")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPetsValidateRequiredFields() {
        log.info("Test: Validate all pets have required fields");
        
        Response<List<Pet>> response = petService.getPets(10);
        
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateSuccessResponse(response);
        
        List<Pet> pets = response.body();
        
        for (Pet pet : pets) {
            ResponseValidator.validatePetRequiredFields(pet);
        }
        
        log.info("All {} pets have required fields", pets.size());
    }
}
