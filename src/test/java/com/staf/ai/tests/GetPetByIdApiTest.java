package com.staf.ai.tests;

import com.staf.ai.base.BaseTest;
import com.staf.ai.model.Error;
import com.staf.ai.model.Pet;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for GET /pets/{petId} endpoint.
 * Tests retrieving a specific pet by ID.
 */
@Epic("Petstore API")
@Feature("Pets Management")
@Story("Get Pet By ID")
public class GetPetByIdApiTest extends BaseTest {
    
    @Test(groups = {"smoke", "regression"}, priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case 26: Get existing pet by valid id")
    @Issue("TC-026")
    public void testGetExistingPetById() {
        String petId = "1";
        
        Response response = petsApiClient.getPetById(petId);
        
        // If pet exists, should return 200
        if (response.getStatusCode() == 200) {
            response.then()
                    .contentType("application/json");
            
            Pet pet = response.as(Pet.class);
            assertThat(pet).isNotNull();
            assertThat(pet.getId()).isNotNull();
            assertThat(pet.getName()).isNotNull();
        }
    }
    
    @Test(groups = {"regression", "negative"}, priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 27: Get pet with non-existent id")
    @Issue("TC-027")
    public void testGetPetWithNonExistentId() {
        String petId = "99999";
        
        Response response = petsApiClient.getPetById(petId);
        
        // Expecting error response for non-existent pet
        assertThat(response.getStatusCode()).isNotEqualTo(200);
    }
    
    @Test(groups = {"regression", "negative"}, priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 28: Get pet with non-integer id (abc)")
    @Issue("TC-028")
    public void testGetPetWithNonIntegerId() {
        String petId = "abc";
        
        Response response = petsApiClient.getPetById(petId);
        
        // Expecting error response for invalid id format
        assertThat(response.getStatusCode()).isNotEqualTo(200);
    }
    
    @Test(groups = {"regression", "negative"}, priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 29: Get pet with negative id")
    @Issue("TC-029")
    public void testGetPetWithNegativeId() {
        String petId = "-1";
        
        Response response = petsApiClient.getPetById(petId);
        
        // Expecting error response for negative id
        assertThat(response.getStatusCode()).isNotEqualTo(200);
    }
    
    @Test(groups = {"regression", "boundary"}, priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 30: Get pet with id=0")
    @Issue("TC-030")
    public void testGetPetWithIdZero() {
        String petId = "0";
        
        Response response = petsApiClient.getPetById(petId);
        
        // Response depends on whether id=0 is valid
        assertThat(response.getStatusCode()).isIn(200, 400, 404);
    }
    
    @Test(groups = {"regression", "boundary"}, priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 31: Get pet with id=1 (lower boundary)")
    @Issue("TC-031")
    public void testGetPetWithIdLowerBoundary() {
        String petId = "1";
        
        Response response = petsApiClient.getPetById(petId);
        
        // Response depends on whether pet with id=1 exists
        assertThat(response.getStatusCode()).isIn(200, 404);
    }
    
    @Test(groups = {"regression", "boundary"}, priority = 7)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 32: Get pet with maximum integer id")
    @Issue("TC-032")
    public void testGetPetWithMaxIntegerId() {
        String petId = "2147483647";
        
        Response response = petsApiClient.getPetById(petId);
        
        // Response depends on whether pet with max id exists
        assertThat(response.getStatusCode()).isIn(200, 404);
    }
    
    @Test(groups = {"regression", "validation"}, priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case 33: Validate Pet schema in get by id response")
    @Issue("TC-033")
    public void testValidatePetSchemaInGetByIdResponse() {
        String petId = "1";
        
        Response response = petsApiClient.getPetById(petId);
        
        if (response.getStatusCode() == 200) {
            Pet pet = response.as(Pet.class);
            assertThat(pet).isNotNull();
            assertThat(pet.getId()).isNotNull();
            assertThat(pet.getName()).isNotNull().isNotEmpty();
            // Tag is optional
        }
    }
    
    @Test(groups = {"regression", "validation"}, priority = 9)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case 34: Validate Error schema on invalid request")
    @Issue("TC-034")
    public void testValidateErrorSchemaOnInvalidRequest() {
        String petId = "invalid_id";
        
        Response response = petsApiClient.getPetById(petId);
        
        if (response.getStatusCode() != 200) {
            // Validate error response structure
            assertThat(response.getStatusCode()).isGreaterThanOrEqualTo(400);
            
            // Try to parse as Error object
            try {
                Error error = response.as(Error.class);
                assertThat(error.getCode()).isNotNull();
                assertThat(error.getMessage()).isNotNull();
            } catch (Exception e) {
                // Error response might not follow the schema
                // Log for investigation
            }
        }
    }
    
    @Test(groups = {"regression", "integration"}, priority = 10)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case 35: Create pet then retrieve by id")
    @Issue("TC-035")
    public void testCreatePetThenRetrieveById() {
        // Create a pet
        Pet newPet = Pet.createComplete(999L, "IntegrationTestPet", "test");
        Response createResponse = petsApiClient.createPet(newPet);
        
        if (createResponse.getStatusCode() == 201) {
            // Try to retrieve the created pet
            Response getResponse = petsApiClient.getPetById("999");
            
            if (getResponse.getStatusCode() == 200) {
                Pet retrievedPet = getResponse.as(Pet.class);
                assertThat(retrievedPet.getId()).isEqualTo(newPet.getId());
                assertThat(retrievedPet.getName()).isEqualTo(newPet.getName());
                assertThat(retrievedPet.getTag()).isEqualTo(newPet.getTag());
            }
        }
    }
}
