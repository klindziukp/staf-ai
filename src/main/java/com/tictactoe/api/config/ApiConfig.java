package com.tictactoe.api.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager for API settings.
 * Loads configuration from properties files based on environment.
 */
@Slf4j
@Getter
public class ApiConfig {
    private static final String DEFAULT_ENVIRONMENT = "dev";
    private static ApiConfig instance;

    private final String baseUrl;
    private final String apiKey;
    private final String bearerToken;
    private final String oauth2ClientId;
    private final String oauth2ClientSecret;
    private final String oauth2TokenUrl;
    private final String oauth2AuthorizationUrl;
    private final int connectionTimeout;
    private final int readTimeout;
    private final int maxRetries;
    private final boolean enableRequestLogging;
    private final boolean enableResponseLogging;

    private ApiConfig(String environment) {
        Properties properties = loadProperties(environment);

        this.baseUrl = getProperty(properties, "api.base.url");
        this.apiKey = getProperty(properties, "api.key", "");
        this.bearerToken = getProperty(properties, "api.bearer.token", "");
        this.oauth2ClientId = getProperty(properties, "oauth2.client.id", "");
        this.oauth2ClientSecret = getProperty(properties, "oauth2.client.secret", "");
        this.oauth2TokenUrl = getProperty(properties, "oauth2.token.url", "");
        this.oauth2AuthorizationUrl = getProperty(properties, "oauth2.authorization.url", "");
        this.connectionTimeout = Integer.parseInt(getProperty(properties, "api.connection.timeout", "30000"));
        this.readTimeout = Integer.parseInt(getProperty(properties, "api.read.timeout", "30000"));
        this.maxRetries = Integer.parseInt(getProperty(properties, "api.max.retries", "3"));
        this.enableRequestLogging = Boolean.parseBoolean(getProperty(properties, "api.logging.request.enabled", "true"));
        this.enableResponseLogging = Boolean.parseBoolean(getProperty(properties, "api.logging.response.enabled", "true"));

        log.info("API Configuration loaded for environment: {}", environment);
        log.info("Base URL: {}", baseUrl);
    }

    /**
     * Gets the singleton instance of ApiConfig.
     *
     * @return the ApiConfig instance
     */
    public static synchronized ApiConfig getInstance() {
        if (instance == null) {
            String environment = System.getProperty("environment", DEFAULT_ENVIRONMENT);
            instance = new ApiConfig(environment);
        }
        return instance;
    }

    /**
     * Resets the configuration instance (useful for testing).
     */
    public static synchronized void reset() {
        instance = null;
    }

    private Properties loadProperties(String environment) {
        Properties properties = new Properties();
        String configFile = String.format("config-%s.properties", environment);

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                log.warn("Configuration file {} not found, trying default config.properties", configFile);
                return loadDefaultProperties();
            }
            properties.load(input);
            log.info("Loaded configuration from {}", configFile);
        } catch (IOException e) {
            log.error("Error loading configuration file: {}", configFile, e);
            throw new RuntimeException("Failed to load configuration", e);
        }

        return properties;
    }

    private Properties loadDefaultProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Default config.properties not found");
            }
            properties.load(input);
            log.info("Loaded default configuration");
        } catch (IOException e) {
            log.error("Error loading default configuration", e);
            throw new RuntimeException("Failed to load default configuration", e);
        }
        return properties;
    }

    private String getProperty(Properties properties, String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Required property not found: " + key);
        }
        return value.trim();
    }

    private String getProperty(Properties properties, String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue)).trim();
    }
}
