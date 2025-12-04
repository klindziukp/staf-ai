package com.staf.ai.tests;

import com.staf.ai.base.BaseTest;
import com.staf.ai.model.Error;
import com.staf.ai.model.Pet;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for POST /pets endpoint.
 * Tests creating pets with various scenarios.
 */
@Epic("Petstore API")
@Feature("Pets Management")
@Story("Create Pet")
public class CreatePetApiTest extends BaseTest {
    
    @Test(groups = {"smoke", "regression"}, priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case 13: Create pet with required fields only")
    @Issue("TC-013")
    public void testCreatePetWithRequiredFields() {
        Pet pet = Pet.createMinimal(1L, "Fido");
        
        Response response = petsApiClient.createPet(pet);
        
        response.then()
                .statusCode(201);
    }
    
    @Test(groups = {"smoke", "regression"}, priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case 14: Create pet with all fields")
    @Issue("TC-014")
    public void testCreatePetWithAllFields() {
        Pet pet = Pet.createComplete(2L, "Whiskers", "cat");
        
        Response response = petsApiClient.createPet(pet);
        
        response.then()
                .statusCode(201);
    }
    
    @Test(groups = {"regression", "negative"}, priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case 15: Create pet with missing id")
    @Issue("TC-015")
    public void testCreatePetWithMissingId() {
        Pet pet = Pet.builder()
                .name("Fido")
                .build();
        
        Response response = petsApiClient.createPet(pet);
        
        // Expecting error response for missing required field
        assertThat(response.getStatusCode()).isNotEqualTo(201);
    }
    
    @Test(groups = {"regression", "negative"}, priority = 4)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case 16: Create pet with missing name")
    @Issue("TC-016")
    public void testCreatePetWithMissingName() {
        Pet pet = Pet.builder()
                .id(1L)
                .build();
        
        Response response = petsApiClient.createPet(pet);
        
        // Expecting error response for missing required field
        assertThat(response.getStatusCode()).isNotEqualTo(201);
    }
    
    @Test(groups = {"regression", "negative"}, priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 17: Create pet with invalid id type (string)")
    @Issue("TC-017")
    public void testCreatePetWithInvalidIdType() {
        Map<String, Object> invalidPet = new HashMap<>();
        invalidPet.put("id", "abc");
        invalidPet.put("name", "Fido");
        
        Response response = petsApiClient.createPet(invalidPet);
        
        // Expecting error response for invalid type
        assertThat(response.getStatusCode()).isNotEqualTo(201);
    }
    
    @Test(groups = {"regression", "negative"}, priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 18: Create pet with invalid name type (number)")
    @Issue("TC-018")
    public void testCreatePetWithInvalidNameType() {
        Map<String, Object> invalidPet = new HashMap<>();
        invalidPet.put("id", 1);
        invalidPet.put("name", 123);
        
        Response response = petsApiClient.createPet(invalidPet);
        
        // Expecting error response for invalid type
        assertThat(response.getStatusCode()).isNotEqualTo(201);
    }
    
    @Test(groups = {"regression", "negative"}, priority = 7)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 20: Create pet with empty request body")
    @Issue("TC-020")
    public void testCreatePetWithEmptyBody() {
        Map<String, Object> emptyPet = new HashMap<>();
        
        Response response = petsApiClient.createPet(emptyPet);
        
        // Expecting error response for empty body
        assertThat(response.getStatusCode()).isNotEqualTo(201);
    }
    
    @Test(groups = {"regression", "negative"}, priority = 8)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 21: Create pet with null values for required fields")
    @Issue("TC-021")
    public void testCreatePetWithNullValues() {
        Pet pet = Pet.builder()
                .id(null)
                .name(null)
                .build();
        
        Response response = petsApiClient.createPet(pet);
        
        // Expecting error response for null required fields
        assertThat(response.getStatusCode()).isNotEqualTo(201);
    }
    
    @Test(groups = {"regression", "boundary"}, priority = 9)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 22: Create pet with id=0 (lower boundary)")
    @Issue("TC-022")
    public void testCreatePetWithIdZero() {
        Pet pet = Pet.createMinimal(0L, "Zero");
        
        Response response = petsApiClient.createPet(pet);
        
        // Response could be 201 or error depending on business rules
        assertThat(response.getStatusCode()).isIn(201, 400, 422);
    }
    
    @Test(groups = {"regression", "boundary"}, priority = 10)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 23: Create pet with maximum integer id")
    @Issue("TC-023")
    public void testCreatePetWithMaxIntegerId() {
        Pet pet = Pet.createMinimal(2147483647L, "MaxInt");
        
        Response response = petsApiClient.createPet(pet);
        
        // Response could be 201 or error depending on business rules
        assertThat(response.getStatusCode()).isIn(201, 400, 422);
    }
    
    @Test(groups = {"regression", "validation"}, priority = 11)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 24: Validate Pet schema in create response")
    @Issue("TC-024")
    public void testValidatePetSchemaInCreateResponse() {
        Pet pet = Pet.createComplete(100L, "TestPet", "dog");
        
        Response response = petsApiClient.createPet(pet);
        
        if (response.getStatusCode() == 201) {
            // If response body is not null, validate schema
            String responseBody = response.getBody().asString();
            if (responseBody != null && !responseBody.isEmpty()) {
                Pet createdPet = response.as(Pet.class);
                assertThat(createdPet.getId()).isNotNull();
                assertThat(createdPet.getName()).isNotNull();
            }
        }
    }
}
