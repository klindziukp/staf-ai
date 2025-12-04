package com.tictactoe.api.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager for API test framework.
 * Loads configuration from properties files based on environment.
 */
@Slf4j
@Getter
public class ApiConfig {
    private static final String DEFAULT_ENV = "dev";
    private static final String CONFIG_FILE_PATTERN = "config-%s.properties";
    private static final String DEFAULT_CONFIG_FILE = "config.properties";

    private static ApiConfig instance;

    private final String baseUrl;
    private final String environment;
    private final int connectionTimeout;
    private final int socketTimeout;
    private final boolean enableRequestLogging;
    private final boolean enableResponseLogging;
    private final int maxRetryAttempts;

    private ApiConfig() {
        this.environment = System.getProperty("env", DEFAULT_ENV);
        Properties properties = loadProperties();

        this.baseUrl = properties.getProperty("api.base.url", "https://api.tictactoe.example.com");
        this.connectionTimeout = Integer.parseInt(
                properties.getProperty("api.connection.timeout", "10000"));
        this.socketTimeout = Integer.parseInt(
                properties.getProperty("api.socket.timeout", "10000"));
        this.enableRequestLogging = Boolean.parseBoolean(
                properties.getProperty("api.logging.request.enabled", "true"));
        this.enableResponseLogging = Boolean.parseBoolean(
                properties.getProperty("api.logging.response.enabled", "true"));
        this.maxRetryAttempts = Integer.parseInt(
                properties.getProperty("api.retry.max.attempts", "3"));

        log.info("API Configuration loaded for environment: {}", environment);
        log.info("Base URL: {}", baseUrl);
    }

    /**
     * Gets singleton instance of ApiConfig.
     *
     * @return ApiConfig instance
     */
    public static synchronized ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }
        return instance;
    }

    private Properties loadProperties() {
        Properties properties = new Properties();

        // Load default properties
        loadPropertiesFromFile(DEFAULT_CONFIG_FILE, properties);

        // Load environment-specific properties
        String envConfigFile = String.format(CONFIG_FILE_PATTERN, environment);
        loadPropertiesFromFile(envConfigFile, properties);

        return properties;
    }

    private void loadPropertiesFromFile(String fileName, Properties properties) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input != null) {
                properties.load(input);
                log.debug("Loaded properties from: {}", fileName);
            } else {
                log.warn("Configuration file not found: {}", fileName);
            }
        } catch (IOException e) {
            log.error("Error loading properties from {}: {}", fileName, e.getMessage());
        }
    }
}
