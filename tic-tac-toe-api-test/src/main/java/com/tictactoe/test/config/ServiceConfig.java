/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.config;

/**
 * Configuration class for service endpoints and settings.
 */
public final class ServiceConfig {

    public static final String BASE_URL = System.getProperty("base.url", "https://learn.openapis.org");
    public static final String API_KEY = System.getProperty("api.key", "");
    public static final String BEARER_TOKEN = System.getProperty("bearer.token", "");

    private ServiceConfig() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
