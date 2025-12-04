package com.klindziuk.petstore.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager for loading and accessing application properties
 */
@Slf4j
public final class ConfigurationManager {
    
    private static final String CONFIG_FILE = "config/application.properties";
    private static final Properties properties = new Properties();
    private static ConfigurationManager instance;
    
    private ConfigurationManager() {
        loadProperties();
    }
    
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }
    
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                log.error("Unable to find {}", CONFIG_FILE);
                throw new RuntimeException("Configuration file not found: " + CONFIG_FILE);
            }
            properties.load(input);
            log.info("Configuration loaded successfully from {}", CONFIG_FILE);
        } catch (IOException e) {
            log.error("Error loading configuration file", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
    
    public String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        return value;
    }
    
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }
    
    public String getBaseUrl() {
        String envName = getProperty("environment", "DEV");
        Environment environment = Environment.valueOf(envName.toUpperCase());
        return environment.getBaseUrl();
    }
    
    public int getDefaultTimeout() {
        return Integer.parseInt(getProperty("api.timeout", "30"));
    }
    
    public int getMaxRetries() {
        return Integer.parseInt(getProperty("api.max.retries", "3"));
    }
    
    public boolean isLoggingEnabled() {
        return Boolean.parseBoolean(getProperty("api.logging.enabled", "true"));
    }
}
