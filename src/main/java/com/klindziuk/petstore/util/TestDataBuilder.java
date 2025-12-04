package com.klindziuk.petstore.util;

import com.klindziuk.petstore.model.Pet;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * Utility class for building test data
 */
@Slf4j
public final class TestDataBuilder {
    
    private static final Random random = new Random();
    private static final String[] PET_NAMES = {"Buddy", "Max", "Charlie", "Lucy", "Daisy", "Rocky", "Luna", "Cooper", "Bailey", "Sadie"};
    private static final String[] TAGS = {"friendly", "playful", "calm", "energetic", "loyal", "smart", "cute", "brave"};
    
    private TestDataBuilder() {
        // Private constructor to prevent instantiation
    }
    
    public static Pet createRandomPet() {
        return Pet.builder()
                .id(generateRandomId())
                .name(getRandomPetName())
                .tag(getRandomTag())
                .build();
    }
    
    public static Pet createPetWithName(String name) {
        return Pet.builder()
                .id(generateRandomId())
                .name(name)
                .tag(getRandomTag())
                .build();
    }
    
    public static Pet createPetWithId(Long id) {
        return Pet.builder()
                .id(id)
                .name(getRandomPetName())
                .tag(getRandomTag())
                .build();
    }
    
    public static Pet createPetWithIdAndName(Long id, String name) {
        return Pet.builder()
                .id(id)
                .name(name)
                .tag(getRandomTag())
                .build();
    }
    
    public static Pet createPetWithoutTag() {
        return Pet.builder()
                .id(generateRandomId())
                .name(getRandomPetName())
                .build();
    }
    
    public static Pet createPetWithoutId() {
        return Pet.builder()
                .name(getRandomPetName())
                .tag(getRandomTag())
                .build();
    }
    
    public static Pet createPetWithoutName() {
        return Pet.builder()
                .id(generateRandomId())
                .tag(getRandomTag())
                .build();
    }
    
    public static Long generateRandomId() {
        return Math.abs(random.nextLong() % 1000000);
    }
    
    private static String getRandomPetName() {
        return PET_NAMES[random.nextInt(PET_NAMES.length)];
    }
    
    private static String getRandomTag() {
        return TAGS[random.nextInt(TAGS.length)];
    }
}
