package com.tictactoe.api.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Authentication configuration manager.
 * Handles API keys, JWT tokens, and OAuth2 credentials.
 */
@Slf4j
@Getter
public class AuthConfig {
    private static AuthConfig instance;

    private final String apiKey;
    private final String jwtToken;
    private final String oauth2ClientId;
    private final String oauth2ClientSecret;
    private final String oauth2TokenUrl;

    private AuthConfig() {
        Properties properties = loadProperties();

        this.apiKey = properties.getProperty("auth.api.key", "test-api-key");
        this.jwtToken = properties.getProperty("auth.jwt.token", "");
        this.oauth2ClientId = properties.getProperty("auth.oauth2.client.id", "");
        this.oauth2ClientSecret = properties.getProperty("auth.oauth2.client.secret", "");
        this.oauth2TokenUrl = properties.getProperty("auth.oauth2.token.url", "");

        log.info("Authentication configuration loaded");
    }

    /**
     * Gets singleton instance of AuthConfig.
     *
     * @return AuthConfig instance
     */
    public static synchronized AuthConfig getInstance() {
        if (instance == null) {
            instance = new AuthConfig();
        }
        return instance;
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        String environment = System.getProperty("env", "dev");
        String configFile = String.format("config-%s.properties", environment);

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            log.error("Error loading auth properties: {}", e.getMessage());
        }

        return properties;
    }

    /**
     * Checks if API key authentication is configured.
     *
     * @return true if API key is present
     */
    public boolean hasApiKey() {
        return apiKey != null && !apiKey.isEmpty();
    }

    /**
     * Checks if JWT authentication is configured.
     *
     * @return true if JWT token is present
     */
    public boolean hasJwtToken() {
        return jwtToken != null && !jwtToken.isEmpty();
    }
}
