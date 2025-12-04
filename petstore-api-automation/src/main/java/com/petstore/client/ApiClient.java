package com.petstore.client;

import com.petstore.models.Pet;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Retrofit API Client interface for Petstore API endpoints
 * Defines all available API operations
 */
public interface ApiClient {
    
    /**
     * Get list of all pets with optional limit
     * @param limit Maximum number of pets to return (max 100)
     * @return Call with list of pets
     */
    @GET("pets")
    Call<List<Pet>> getPets(@Query("limit") Integer limit);
    
    /**
     * Create a new pet
     * @param pet Pet object to create
     * @return Call with created pet
     */
    @POST("pets")
    Call<Pet> createPet(@Body Pet pet);
    
    /**
     * Get pet by ID
     * @param petId Pet ID
     * @return Call with pet details
     */
    @GET("pets/{petId}")
    Call<Pet> getPetById(@Path("petId") String petId);
}
