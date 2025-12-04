package com.petstore.automation.client;

import com.petstore.automation.config.ConfigManager;
import com.petstore.automation.model.Pet;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * API client for Pet-related endpoints in Petstore API.
 * Provides methods for CRUD operations on pets using REST Assured.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Slf4j
public class PetApiClient {
    
    private static final String PETS_ENDPOINT = "/pets";
    private static final String PET_BY_ID_ENDPOINT = "/pets/{petId}";
    
    private final ConfigManager config;
    
    /**
     * Constructor initializing the API client with configuration.
     */
    public PetApiClient() {
        this.config = ConfigManager.getInstance();
        RestAssured.baseURI = config.getBaseUrl();
        log.info("PetApiClient initialized with base URL: {}", config.getBaseUrl());
    }
    
    /**
     * Creates a base request specification with common settings.
     * 
     * @return configured RequestSpecification
     */
    private RequestSpecification getBaseRequest() {
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .filter(new AllureRestAssured());
        
        if (config.isRequestLoggingEnabled()) {
            request.log().all();
        }
        
        return request;
    }
    
    /**
     * Lists all pets with optional limit parameter.
     * 
     * @param limit maximum number of pets to return (max 100)
     * @return Response containing list of pets
     */
    @Step("List all pets with limit: {limit}")
    public Response listPets(Integer limit) {
        log.info("Listing pets with limit: {}", limit);
        
        RequestSpecification request = getBaseRequest();
        
        if (limit != null) {
            request.queryParam("limit", limit);
        }
        
        Response response = request
                .when()
                .get(PETS_ENDPOINT);
        
        if (config.isResponseLoggingEnabled()) {
            response.then().log().all();
        }
        
        log.info("List pets response - Status: {}", response.getStatusCode());
        return response;
    }
    
    /**
     * Creates a new pet in the store.
     * 
     * @param pet the pet to create
     * @return Response containing the created pet
     */
    @Step("Create pet with name: {pet.name}")
    public Response createPet(Pet pet) {
        log.info("Creating pet: {}", pet);
        
        Response response = getBaseRequest()
                .body(pet)
                .when()
                .post(PETS_ENDPOINT);
        
        if (config.isResponseLoggingEnabled()) {
            response.then().log().all();
        }
        
        log.info("Create pet response - Status: {}", response.getStatusCode());
        return response;
    }
    
    /**
     * Gets a pet by its ID.
     * 
     * @param petId the pet ID
     * @return Response containing the pet
     */
    @Step("Get pet by ID: {petId}")
    public Response getPetById(String petId) {
        log.info("Getting pet by ID: {}", petId);
        
        Response response = getBaseRequest()
                .pathParam("petId", petId)
                .when()
                .get(PET_BY_ID_ENDPOINT);
        
        if (config.isResponseLoggingEnabled()) {
            response.then().log().all();
        }
        
        log.info("Get pet response - Status: {}", response.getStatusCode());
        return response;
    }
    
    /**
     * Gets a pet by its ID (Long version).
     * 
     * @param petId the pet ID
     * @return Response containing the pet
     */
    @Step("Get pet by ID: {petId}")
    public Response getPetById(Long petId) {
        return getPetById(String.valueOf(petId));
    }
}
