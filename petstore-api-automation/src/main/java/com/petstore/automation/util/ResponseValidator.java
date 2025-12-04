package com.petstore.automation.util;

import com.petstore.automation.model.ApiError;
import com.petstore.automation.model.Pet;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;

import java.util.List;

/**
 * Utility class for validating API responses.
 * Provides methods for schema validation and data validation.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Slf4j
public final class ResponseValidator {
    
    /**
     * Private constructor to prevent instantiation.
     */
    private ResponseValidator() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Validates Pet object has all required fields.
     * 
     * @param pet the pet to validate
     * @param softly SoftAssertions instance
     */
    public static void validatePetSchema(Pet pet, SoftAssertions softly) {
        log.debug("Validating pet schema: {}", pet);
        
        softly.assertThat(pet)
                .as("Pet object should not be null")
                .isNotNull();
        
        if (pet != null) {
            softly.assertThat(pet.getId())
                    .as("Pet ID should not be null")
                    .isNotNull();
            
            softly.assertThat(pet.getName())
                    .as("Pet name should not be null or empty")
                    .isNotNull()
                    .isNotEmpty();
        }
    }
    
    /**
     * Validates list of pets.
     * 
     * @param pets list of pets to validate
     * @param softly SoftAssertions instance
     */
    public static void validatePetsList(List<Pet> pets, SoftAssertions softly) {
        log.debug("Validating pets list with size: {}", pets != null ? pets.size() : 0);
        
        softly.assertThat(pets)
                .as("Pets list should not be null")
                .isNotNull();
        
        if (pets != null && !pets.isEmpty()) {
            pets.forEach(pet -> validatePetSchema(pet, softly));
        }
    }
    
    /**
     * Validates Error response has all required fields.
     * 
     * @param error the error to validate
     * @param softly SoftAssertions instance
     */
    public static void validateErrorSchema(ApiError error, SoftAssertions softly) {
        log.debug("Validating error schema: {}", error);
        
        softly.assertThat(error)
                .as("Error object should not be null")
                .isNotNull();
        
        if (error != null) {
            softly.assertThat(error.getCode())
                    .as("Error code should not be null")
                    .isNotNull();
            
            softly.assertThat(error.getMessage())
                    .as("Error message should not be null or empty")
                    .isNotNull()
                    .isNotEmpty();
        }
    }
    
    /**
     * Validates response status code.
     * 
     * @param response the response to validate
     * @param expectedStatusCode expected status code
     */
    public static void validateStatusCode(Response response, int expectedStatusCode) {
        log.debug("Validating status code. Expected: {}, Actual: {}", 
                expectedStatusCode, response.getStatusCode());
        
        org.assertj.core.api.Assertions.assertThat(response.getStatusCode())
                .as("Response status code")
                .isEqualTo(expectedStatusCode);
    }
    
    /**
     * Validates response content type.
     * 
     * @param response the response to validate
     * @param expectedContentType expected content type
     */
    public static void validateContentType(Response response, String expectedContentType) {
        log.debug("Validating content type. Expected: {}, Actual: {}", 
                expectedContentType, response.getContentType());
        
        org.assertj.core.api.Assertions.assertThat(response.getContentType())
                .as("Response content type")
                .contains(expectedContentType);
    }
}
