package com.petstore.automation.util;

import com.petstore.automation.model.Pet;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Builder class for generating test data for Pet entities.
 * Provides methods to create valid and invalid test data.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Slf4j
public final class TestDataBuilder {
    
    private static final Random RANDOM = new Random();
    private static final String[] PET_NAMES = {
        "Buddy", "Max", "Charlie", "Cooper", "Rocky", "Bear", "Duke", "Zeus",
        "Luna", "Bella", "Daisy", "Lucy", "Sadie", "Molly", "Maggie", "Bailey"
    };
    
    private static final String[] TAG_NAMES = {
        "Friendly", "Playful", "Energetic", "Calm", "Trained", "Young", "Adult", "Senior"
    };
    
    /**
     * Private constructor to prevent instantiation.
     */
    private TestDataBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Generates a random pet ID.
     * 
     * @return random pet ID
     */
    public static Long generatePetId() {
        return ThreadLocalRandom.current().nextLong(1, 1000000);
    }
    
    /**
     * Generates a random pet name.
     * 
     * @return random pet name
     */
    public static String generatePetName() {
        return PET_NAMES[RANDOM.nextInt(PET_NAMES.length)];
    }
    
    /**
     * Generates a random tag name.
     * 
     * @return random tag name
     */
    public static String generateTagName() {
        return TAG_NAMES[RANDOM.nextInt(TAG_NAMES.length)];
    }
    
    /**
     * Creates a valid pet with all required fields.
     * 
     * @return Pet with valid data
     */
    public static Pet createValidPet() {
        return Pet.builder()
                .id(generatePetId())
                .name(generatePetName())
                .tag(generateTagName())
                .build();
    }
    
    /**
     * Creates a valid pet with only required fields.
     * 
     * @return Pet with minimal valid data
     */
    public static Pet createMinimalValidPet() {
        return Pet.builder()
                .id(generatePetId())
                .name(generatePetName())
                .build();
    }
    
    /**
     * Creates a pet with specific ID.
     * 
     * @param id the pet ID
     * @return Pet with specified ID
     */
    public static Pet createPetWithId(Long id) {
        return Pet.builder()
                .id(id)
                .name(generatePetName())
                .tag(generateTagName())
                .build();
    }
    
    /**
     * Creates a pet with specific name.
     * 
     * @param name the pet name
     * @return Pet with specified name
     */
    public static Pet createPetWithName(String name) {
        return Pet.builder()
                .id(generatePetId())
                .name(name)
                .tag(generateTagName())
                .build();
    }
    
    /**
     * Creates a pet without ID (for negative testing).
     * 
     * @return Pet without ID
     */
    public static Pet createPetWithoutId() {
        return Pet.builder()
                .name(generatePetName())
                .tag(generateTagName())
                .build();
    }
    
    /**
     * Creates a pet without name (for negative testing).
     * 
     * @return Pet without name
     */
    public static Pet createPetWithoutName() {
        return Pet.builder()
                .id(generatePetId())
                .tag(generateTagName())
                .build();
    }
}
