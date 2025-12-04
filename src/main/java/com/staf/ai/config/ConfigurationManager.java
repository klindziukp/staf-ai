package com.staf.ai.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager for loading and managing application properties.
 * Implements Singleton pattern to ensure single instance.
 */
@Slf4j
public final class ConfigurationManager {
    
    private static final String CONFIG_FILE = "config.properties";
    private static ConfigurationManager instance;
    private final Properties properties;
    private final ApiConfig apiConfig;
    
    private ConfigurationManager() {
        this.properties = loadProperties();
        this.apiConfig = buildApiConfig();
    }
    
    /**
     * Gets the singleton instance of ConfigurationManager.
     *
     * @return the ConfigurationManager instance
     */
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }
    
    /**
     * Loads properties from the configuration file.
     *
     * @return loaded Properties object
     */
    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                log.error("Unable to find {}", CONFIG_FILE);
                throw new RuntimeException("Configuration file not found: " + CONFIG_FILE);
            }
            props.load(input);
            log.info("Configuration loaded successfully from {}", CONFIG_FILE);
        } catch (IOException e) {
            log.error("Error loading configuration file", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
        return props;
    }
    
    /**
     * Builds ApiConfig from loaded properties.
     *
     * @return configured ApiConfig instance
     */
    private ApiConfig buildApiConfig() {
        return ApiConfig.builder()
                .baseUrl(getProperty("api.base.url", "http://petstore.swagger.io"))
                .basePath(getProperty("api.base.path", "/v1"))
                .connectionTimeout(getIntProperty("api.connection.timeout", 10000))
                .socketTimeout(getIntProperty("api.socket.timeout", 10000))
                .enableLogging(getBooleanProperty("api.logging.enabled", true))
                .logLevel(getProperty("api.log.level", "INFO"))
                .build();
    }
    
    /**
     * Gets API configuration.
     *
     * @return the ApiConfig instance
     */
    public ApiConfig getApiConfig() {
        return apiConfig;
    }
    
    /**
     * Gets a property value with a default fallback.
     *
     * @param key          the property key
     * @param defaultValue the default value if key not found
     * @return the property value or default
     */
    public String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key, defaultValue);
        }
        return value;
    }
    
    /**
     * Gets a property value.
     *
     * @param key the property key
     * @return the property value
     */
    public String getProperty(String key) {
        return getProperty(key, null);
    }
    
    /**
     * Gets an integer property value.
     *
     * @param key          the property key
     * @param defaultValue the default value if key not found
     * @return the integer property value
     */
    public Integer getIntProperty(String key, Integer defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid integer value for key {}: {}", key, value);
            return defaultValue;
        }
    }
    
    /**
     * Gets a boolean property value.
     *
     * @param key          the property key
     * @param defaultValue the default value if key not found
     * @return the boolean property value
     */
    public Boolean getBooleanProperty(String key, Boolean defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
}
