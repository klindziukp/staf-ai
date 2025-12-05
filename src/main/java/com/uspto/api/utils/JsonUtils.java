package com.uspto.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for JSON operations.
 * Provides methods for JSON serialization, deserialization, and formatting.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Slf4j
public final class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private JsonUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Converts object to JSON string.
     *
     * @param object object to convert
     * @return JSON string
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

    /**
     * Converts object to pretty JSON string.
     *
     * @param object object to convert
     * @return pretty JSON string
     */
    public static String toPrettyJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * Converts JSON string to object.
     *
     * @param json JSON string
     * @param clazz target class
     * @param <T> type parameter
     * @return deserialized object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to object: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }

    /**
     * Reads JSON from file and converts to object.
     *
     * @param file JSON file
     * @param clazz target class
     * @param <T> type parameter
     * @return deserialized object
     */
    public static <T> T fromJsonFile(File file, Class<T> clazz) {
        try {
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            log.error("Error reading JSON from file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read JSON from file", e);
        }
    }

    /**
     * Writes object to JSON file.
     *
     * @param object object to write
     * @param file target file
     */
    public static void toJsonFile(Object object, File file) {
        try {
            objectMapper.writeValue(file, object);
            log.info("Successfully wrote JSON to file: {}", file.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error writing JSON to file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to write JSON to file", e);
        }
    }

    /**
     * Formats JSON string with proper indentation.
     *
     * @param json JSON string
     * @return formatted JSON string
     */
    public static String formatJson(String json) {
        try {
            return gson.toJson(JsonParser.parseString(json));
        } catch (Exception e) {
            log.error("Error formatting JSON: {}", e.getMessage(), e);
            return json;
        }
    }

    /**
     * Validates if string is valid JSON.
     *
     * @param json string to validate
     * @return true if valid JSON, false otherwise
     */
    public static boolean isValidJson(String json) {
        try {
            JsonParser.parseString(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets ObjectMapper instance.
     *
     * @return ObjectMapper instance
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
