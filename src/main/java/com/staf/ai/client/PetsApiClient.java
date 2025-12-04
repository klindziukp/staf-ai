package com.staf.ai.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staf.ai.model.Pet;
import com.staf.ai.utils.RequestSpecificationBuilder;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * API client for Pets endpoints.
 * Provides methods for all pet-related API operations.
 */
@Slf4j
public class PetsApiClient extends BaseApiClient {
    
    private static final String PETS_ENDPOINT = "/pets";
    private static final String PET_BY_ID_ENDPOINT = "/pets/{petId}";
    private static final int MAX_LIMIT = 100;
    private final ObjectMapper objectMapper;
    
    public PetsApiClient() {
        super();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Gets all pets without limit.
     *
     * @return Response object
     */
    @Step("Get all pets")
    public Response getAllPets() {
        log.info("Getting all pets");
        RequestSpecification requestSpec = createBaseRequest();
        return executeGet(requestSpec, PETS_ENDPOINT);
    }
    
    /**
     * Gets all pets with a specified limit.
     *
     * @param limit the maximum number of pets to return (max 100)
     * @return Response object
     */
    @Step("Get all pets with limit: {limit}")
    public Response getAllPetsWithLimit(int limit) {
        log.info("Getting pets with limit: {}", limit);
        RequestSpecification requestSpec = RequestSpecificationBuilder.newBuilder()
                .withQueryParam("limit", limit)
                .build();
        
        return executeGet(requestSpec, PETS_ENDPOINT);
    }
    
    /**
     * Gets all pets and deserializes to List of Pet objects.
     *
     * @return List of Pet objects
     */
    @Step("Get all pets as list")
    public List<Pet> getAllPetsAsList() {
        Response response = getAllPets();
        validateStatusCode(response, 200);
        return response.jsonPath().getList("", Pet.class);
    }
    
    /**
     * Gets all pets with limit and deserializes to List of Pet objects.
     *
     * @param limit the maximum number of pets to return
     * @return List of Pet objects
     */
    @Step("Get all pets with limit as list: {limit}")
    public List<Pet> getAllPetsWithLimitAsList(int limit) {
        Response response = getAllPetsWithLimit(limit);
        validateStatusCode(response, 200);
        return response.jsonPath().getList("", Pet.class);
    }
    
    /**
     * Creates a new pet.
     *
     * @param pet the Pet object to create
     * @return Response object
     */
    @Step("Create pet: {pet}")
    public Response createPet(Pet pet) {
        log.info("Creating pet: {}", pet);
        RequestSpecification requestSpec = RequestSpecificationBuilder.newBuilder()
                .withBody(pet)
                .build();
        
        return executePost(requestSpec, PETS_ENDPOINT);
    }
    
    /**
     * Creates a new pet and returns the created Pet object.
     *
     * @param pet the Pet object to create
     * @return the created Pet object
     */
    @Step("Create pet and return object: {pet}")
    public Pet createPetAndReturn(Pet pet) {
        Response response = createPet(pet);
        validateStatusCode(response, 201);
        return response.as(Pet.class);
    }
    
    /**
     * Gets a pet by its ID.
     *
     * @param petId the pet ID
     * @return Response object
     */
    @Step("Get pet by ID: {petId}")
    public Response getPetById(String petId) {
        log.info("Getting pet by ID: {}", petId);
        RequestSpecification requestSpec = RequestSpecificationBuilder.newBuilder()
                .withPathParam("petId", petId)
                .build();
        
        return executeGet(requestSpec, PET_BY_ID_ENDPOINT);
    }
    
    /**
     * Gets a pet by its ID and deserializes to Pet object.
     *
     * @param petId the pet ID
     * @return Pet object
     */
    @Step("Get pet by ID as object: {petId}")
    public Pet getPetByIdAsObject(String petId) {
        Response response = getPetById(petId);
        validateStatusCode(response, 200);
        return response.as(Pet.class);
    }
}
