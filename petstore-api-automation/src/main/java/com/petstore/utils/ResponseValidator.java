package com.petstore.utils;

import com.petstore.models.Pet;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import retrofit2.Response;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Utility class for validating API responses
 * Provides reusable validation methods with Allure step annotations
 */
@Slf4j
public class ResponseValidator {
    
    private ResponseValidator() {
    }
    
    @Step("Validate response status code is {expectedStatusCode}")
    public static void validateStatusCode(Response<?> response, int expectedStatusCode) {
        log.info("Validating status code. Expected: {}, Actual: {}", expectedStatusCode, response.code());
        assertThat(response.code())
                .as("Response status code")
                .isEqualTo(expectedStatusCode);
    }
    
    @Step("Validate response is successful")
    public static void validateSuccessResponse(Response<?> response) {
        log.info("Validating successful response");
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();
        assertThat(response.body())
                .as("Response body should not be null")
                .isNotNull();
    }
    
    @Step("Validate pet details")
    public static void validatePet(Pet actualPet, Pet expectedPet) {
        log.info("Validating pet details");
        SoftAssertions softly = new SoftAssertions();
        
        if (expectedPet.getId() != null) {
            softly.assertThat(actualPet.getId())
                    .as("Pet ID")
                    .isEqualTo(expectedPet.getId());
        }
        
        softly.assertThat(actualPet.getName())
                .as("Pet name")
                .isEqualTo(expectedPet.getName());
        
        if (expectedPet.getTag() != null) {
            softly.assertThat(actualPet.getTag())
                    .as("Pet tag")
                    .isEqualTo(expectedPet.getTag());
        }
        
        softly.assertAll();
    }
    
    @Step("Validate pets list size")
    public static void validatePetsListSize(List<Pet> pets, int expectedSize) {
        log.info("Validating pets list size. Expected: {}, Actual: {}", expectedSize, pets.size());
        assertThat(pets)
                .as("Pets list size")
                .hasSize(expectedSize);
    }
    
    @Step("Validate pets list is not empty")
    public static void validatePetsListNotEmpty(List<Pet> pets) {
        log.info("Validating pets list is not empty");
        assertThat(pets)
                .as("Pets list should not be empty")
                .isNotEmpty();
    }
    
    @Step("Validate pets list size does not exceed limit")
    public static void validatePetsListLimit(List<Pet> pets, int limit) {
        log.info("Validating pets list size does not exceed limit: {}", limit);
        assertThat(pets.size())
                .as("Pets list size should not exceed limit")
                .isLessThanOrEqualTo(limit);
    }
    
    @Step("Validate pet has required fields")
    public static void validatePetRequiredFields(Pet pet) {
        log.info("Validating pet has required fields");
        SoftAssertions softly = new SoftAssertions();
        
        softly.assertThat(pet.getId())
                .as("Pet ID should not be null")
                .isNotNull();
        
        softly.assertThat(pet.getName())
                .as("Pet name should not be null or empty")
                .isNotBlank();
        
        softly.assertAll();
    }
    
    @Step("Validate error response")
    public static void validateErrorResponse(Response<?> response, int expectedStatusCode) {
        log.info("Validating error response");
        assertThat(response.isSuccessful())
                .as("Response should not be successful")
                .isFalse();
        validateStatusCode(response, expectedStatusCode);
    }
    
    @Step("Validate response header exists: {headerName}")
    public static void validateHeaderExists(Response<?> response, String headerName) {
        log.info("Validating header exists: {}", headerName);
        assertThat(response.headers().get(headerName))
                .as("Header should exist: " + headerName)
                .isNotNull();
    }
}
