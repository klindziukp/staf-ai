package com.petstore.utils;

import com.github.javafaker.Faker;
import com.petstore.models.Pet;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility class for generating test data
 * Uses JavaFaker library for realistic data generation
 */
@Slf4j
public class DataGenerator {
    
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    
    private DataGenerator() {
    }
    
    public static Pet generateRandomPet() {
        return Pet.builder()
                .id(generateRandomId())
                .name(generatePetName())
                .tag(generatePetTag())
                .build();
    }
    
    public static Pet generatePetWithName(String name) {
        return Pet.builder()
                .id(generateRandomId())
                .name(name)
                .tag(generatePetTag())
                .build();
    }
    
    public static Pet generatePetWithoutId() {
        return Pet.builder()
                .name(generatePetName())
                .tag(generatePetTag())
                .build();
    }
    
    public static Pet generatePetWithoutTag() {
        return Pet.builder()
                .id(generateRandomId())
                .name(generatePetName())
                .build();
    }
    
    public static List<Pet> generateMultiplePets(int count) {
        List<Pet> pets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            pets.add(generateRandomPet());
        }
        return pets;
    }
    
    public static Long generateRandomId() {
        return Math.abs(random.nextLong());
    }
    
    public static String generatePetName() {
        return faker.animal().name();
    }
    
    public static String generatePetTag() {
        String[] tags = {"friendly", "aggressive", "playful", "lazy", "energetic", "calm"};
        return tags[random.nextInt(tags.length)];
    }
    
    public static String generateLongString(int length) {
        return faker.lorem().characters(length);
    }
    
    public static Integer generateRandomLimit(int max) {
        return random.nextInt(max) + 1;
    }
    
    public static String generateSpecialCharacterString() {
        return "!@#$%^&*()_+-=[]{}|;':\",./<>?";
    }
    
    public static String generateSqlInjectionString() {
        return "'; DROP TABLE pets;--";
    }
    
    public static String generateXssString() {
        return "<script>alert('XSS')</script>";
    }
}
