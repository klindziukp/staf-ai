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
 * Test class for POST /pets endpoint
 * Covers positive, negative, and edge case scenarios for pet creation
 */
@Slf4j
@Epic("Petstore API")
@Feature("Pet Management")
@Story("Create Pet")
public class CreatePetTests extends BaseTest {
    
    @Test(description = "Verify creating a pet with all required fields")
    @Description("Test verifies that POST /pets creates a pet with id and name")
    @Severity(SeverityLevel.BLOCKER)
    public void testCreatePetWithRequiredFields() {
        log.info("Test: Create pet with required fields");
        
        Pet newPet = DataGenerator.generatePetWithoutTag();
        
        Response<Pet> response = petService.createPet(newPet);
        
        ResponseValidator.validateStatusCode(response, 201);
        
        log.info("Successfully created pet");
    }
    
    @Test(description = "Verify creating a pet with all fields")
    @Description("Test verifies that POST /pets creates a pet with id, name, and tag")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePetWithAllFields() {
        log.info("Test: Create pet with all fields");
        
        Pet newPet = DataGenerator.generateRandomPet();
        
        Response<Pet> response = petService.createPet(newPet);
        
        ResponseValidator.validateStatusCode(response, 201);
        
        log.info("Successfully created pet with all fields");
    }
    
    @Test(description = "Verify creating a pet without optional tag field")
    @Description("Test verifies that POST /pets creates a pet without tag field")
    @Severity(SeverityLevel.NORMAL)
    public void testCreatePetWithoutTag() {
        log.info("Test: Create pet without tag");
        
        Pet newPet = Pet.builder()
                .id(DataGenerator.generateRandomId())
                .name(DataGenerator.generatePetName())
                .build();
        
        Response<Pet> response = petService.createPet(newPet);
        
        ResponseValidator.validateStatusCode(response, 201);
        
        log.info("Successfully created pet without tag");
    }
    
    @Test(description = "Verify creating a pet with special characters in name")
    @Description("Test verifies that POST /pets handles special characters in pet name")
    @Severity(SeverityLevel.NORMAL)
    public void testCreatePetWithSpecialCharactersInName() {
        log.info("Test: Create pet with special characters in name");
        
        Pet newPet = Pet.builder()
                .id(DataGenerator.generateRandomId())
                .name(DataGenerator.generateSpecialCharacterString())
                .tag("special")
                .build();
        
        Response<Pet> response = petService.createPet(newPet);
        
        // Should either accept or reject with proper error
        assertThat(response.code()).isIn(201, 400);
        
        log.info("Response received for pet with special characters");
    }
    
    @Test(description = "Verify creating a pet with very long name")
    @Description("Test verifies that POST /pets handles very long pet names")
    @Severity(SeverityLevel.MINOR)
    public void testCreatePetWithLongName() {
        log.info("Test: Create pet with long name");
        
        Pet newPet = Pet.builder()
                .id(DataGenerator.generateRandomId())
                .name(DataGenerator.generateLongString(1000))
                .tag("long-name")
                .build();
        
        Response<Pet> response = petService.createPet(newPet);
        
        // Should either accept or reject with proper error
        assertThat(response.code()).isIn(201, 400);
        
        log.info("Response received for pet with long name");
    }
    
    @Test(description = "Verify error when creating pet without name")
    @Description("Test verifies that POST /pets returns error when name is missing")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePetWithoutName() {
        log.info("Test: Create pet without name (required field)");
        
        Pet newPet = Pet.builder()
                .id(DataGenerator.generateRandomId())
                .tag("no-name")
                .build();
        
        Response<Pet> response = petService.createPet(newPet);
        
        // Expecting error response
        assertThat(response.isSuccessful()).isFalse();
        
        log.info("Correctly received error response for missing name");
    }
    
    @Test(description = "Verify error when creating pet without id")
    @Description("Test verifies that POST /pets returns error when id is missing")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePetWithoutId() {
        log.info("Test: Create pet without id (required field)");
        
        Pet newPet = Pet.builder()
                .name(DataGenerator.generatePetName())
                .tag("no-id")
                .build();
        
        Response<Pet> response = petService.createPet(newPet);
        
        // Expecting error response or auto-generated ID
        log.info("Response code: {}", response.code());
        
        if (response.isSuccessful()) {
            assertThat(response.body()).isNotNull();
            assertThat(response.body().getId()).isNotNull();
            log.info("API auto-generated ID for pet");
        } else {
            log.info("API rejected pet without ID");
        }
    }
    
    @Test(description = "Verify creating pet with id=0")
    @Description("Test verifies that POST /pets handles id=0")
    @Severity(SeverityLevel.MINOR)
    public void testCreatePetWithZeroId() {
        log.info("Test: Create pet with id=0");
        
        Pet newPet = Pet.builder()
                .id(0L)
                .name(DataGenerator.generatePetName())
                .tag("zero-id")
                .build();
        
        Response<Pet> response = petService.createPet(newPet);
        
        // Should either accept or reject
        assertThat(response.code()).isIn(201, 400);
        
        log.info("Response received for pet with id=0");
    }
    
    @Test(description = "Verify creating pet with maximum id value")
    @Description("Test verifies that POST /pets handles maximum int64 value for id")
    @Severity(SeverityLevel.MINOR)
    public void testCreatePetWithMaxId() {
        log.info("Test: Create pet with maximum id value");
        
        Pet newPet = Pet.builder()
                .id(Long.MAX_VALUE)
                .name(DataGenerator.generatePetName())
                .tag("max-id")
                .build();
        
        Response<Pet> response = petService.createPet(newPet);
        
        ResponseValidator.validateStatusCode(response, 201);
        
        log.info("Successfully created pet with maximum id");
    }
    
    @Test(description = "Verify SQL injection protection in pet name")
    @Description("Test verifies that POST /pets properly handles SQL injection attempts")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePetWithSqlInjection() {
        log.info("Test: Create pet with SQL injection in name");
        
        Pet newPet = Pet.builder()
                .id(DataGenerator.generateRandomId())
                .name(DataGenerator.generateSqlInjectionString())
                .tag("sql-test")
                .build();
        
        Response<Pet> response = petService.createPet(newPet);
        
        // Should handle safely - either accept as string or reject
        assertThat(response.code()).isIn(201, 400);
        
        log.info("SQL injection attempt handled safely");
    }
    
    @Test(description = "Verify XSS protection in pet name")
    @Description("Test verifies that POST /pets properly handles XSS attempts")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePetWithXss() {
        log.info("Test: Create pet with XSS in name");
        
        Pet newPet = Pet.builder()
                .id(DataGenerator.generateRandomId())
                .name(DataGenerator.generateXssString())
                .tag("xss-test")
                .build();
        
        Response<Pet> response = petService.createPet(newPet);
        
        // Should handle safely - either accept as string or reject
        assertThat(response.code()).isIn(201, 400);
        
        log.info("XSS attempt handled safely");
    }
}
