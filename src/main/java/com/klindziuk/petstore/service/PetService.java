package com.klindziuk.petstore.service;

import com.klindziuk.petstore.client.PetStoreApiClient;
import com.klindziuk.petstore.client.RetrofitClientFactory;
import com.klindziuk.petstore.model.Pet;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

/**
 * Service class for Pet API operations
 */
@Slf4j
public class PetService {
    
    private final PetStoreApiClient apiClient;
    
    public PetService() {
        this.apiClient = RetrofitClientFactory.createService(PetStoreApiClient.class);
    }
    
    @Step("Get all pets with limit: {limit}")
    public Response<List<Pet>> getPets(Integer limit) {
        log.info("Getting pets with limit: {}", limit);
        try {
            Response<List<Pet>> response = apiClient.getPets(limit).execute();
            log.info("Response code: {}", response.code());
            return response;
        } catch (IOException e) {
            log.error("Error getting pets", e);
            throw new RuntimeException("Failed to get pets", e);
        }
    }
    
    @Step("Get all pets without limit")
    public Response<List<Pet>> getAllPets() {
        return getPets(null);
    }
    
    @Step("Create pet: {pet}")
    public Response<Pet> createPet(Pet pet) {
        log.info("Creating pet: {}", pet);
        try {
            Response<Pet> response = apiClient.createPet(pet).execute();
            log.info("Response code: {}", response.code());
            return response;
        } catch (IOException e) {
            log.error("Error creating pet", e);
            throw new RuntimeException("Failed to create pet", e);
        }
    }
    
    @Step("Get pet by ID: {petId}")
    public Response<Pet> getPetById(Long petId) {
        log.info("Getting pet by ID: {}", petId);
        try {
            Response<Pet> response = apiClient.getPetById(petId).execute();
            log.info("Response code: {}", response.code());
            return response;
        } catch (IOException e) {
            log.error("Error getting pet by ID", e);
            throw new RuntimeException("Failed to get pet by ID", e);
        }
    }
}
