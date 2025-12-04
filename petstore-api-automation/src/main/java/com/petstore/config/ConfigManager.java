package com.petstore.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager for loading and managing application properties
 * Singleton pattern implementation
 */
@Slf4j
public class ConfigManager {
    
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();
    private static ConfigManager instance;
    
    private ConfigManager() {
        loadProperties();
    }
    
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
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
        return getProperty("api.base.url");
    }
    
    public int getConnectionTimeout() {
        return Integer.parseInt(getProperty("api.connection.timeout", "30000"));
    }
    
    public int getReadTimeout() {
        return Integer.parseInt(getProperty("api.read.timeout", "30000"));
    }
    
    public int getWriteTimeout() {
        return Integer.parseInt(getProperty("api.write.timeout", "30000"));
    }
    
    public boolean isLoggingEnabled() {
        return Boolean.parseBoolean(getProperty("api.logging.enabled", "true"));
    }
    
    public int getMaxRetryAttempts() {
        return Integer.parseInt(getProperty("test.retry.max.attempts", "2"));
    }
}
