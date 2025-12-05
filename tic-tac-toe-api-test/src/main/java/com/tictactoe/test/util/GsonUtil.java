/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Utility class for Gson operations.
 */
public final class GsonUtil {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private GsonUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Converts an object to JsonObject.
     *
     * @param object the object to convert
     * @return JsonObject representation
     */
    public static JsonObject toJsonObject(Object object) {
        String json = GSON.toJson(object);
        return GSON.fromJson(json, JsonObject.class);
    }

    /**
     * Converts an object to JSON string.
     *
     * @param object the object to convert
     * @return JSON string representation
     */
    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    /**
     * Converts JSON string to object.
     *
     * @param json the JSON string
     * @param clazz the target class
     * @param <T> the type parameter
     * @return the deserialized object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }
}
