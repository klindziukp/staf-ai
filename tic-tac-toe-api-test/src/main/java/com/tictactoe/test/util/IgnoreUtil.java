/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.util;

import com.google.gson.JsonObject;

/**
 * Utility class for adding ignore markers to JSON objects for comparison.
 */
public final class IgnoreUtil {

    private IgnoreUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Adds an ignore marker to a JSON object field for flexible comparison.
     *
     * @param jsonObject the JSON object to modify
     * @param fieldName the field name to ignore
     */
    public static void addIgnore(JsonObject jsonObject, String fieldName) {
        if (jsonObject.has(fieldName)) {
            jsonObject.addProperty(fieldName, "${json-unit.ignore}");
        }
    }

    /**
     * Adds ignore markers to multiple fields in a JSON object.
     *
     * @param jsonObject the JSON object to modify
     * @param fieldNames the field names to ignore
     */
    public static void addIgnores(JsonObject jsonObject, String... fieldNames) {
        for (String fieldName : fieldNames) {
            addIgnore(jsonObject, fieldName);
        }
    }
}
