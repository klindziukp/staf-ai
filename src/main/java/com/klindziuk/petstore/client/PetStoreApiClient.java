package com.klindziuk.petstore.client;

import com.klindziuk.petstore.model.Pet;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Retrofit API client interface for Petstore API endpoints
 */
public interface PetStoreApiClient {
    
    /**
     * Get list of all pets with optional limit
     * @param limit Maximum number of pets to return (max 100)
     * @return List of pets
     */
    @GET("pets")
    Call<List<Pet>> getPets(@Query("limit") Integer limit);
    
    /**
     * Create a new pet
     * @param pet Pet object to create
     * @return Created pet
     */
    @POST("pets")
    Call<Pet> createPet(@Body Pet pet);
    
    /**
     * Get a specific pet by ID
     * @param petId ID of the pet to retrieve
     * @return Pet object
     */
    @GET("pets/{petId}")
    Call<Pet> getPetById(@Path("petId") Long petId);
}
