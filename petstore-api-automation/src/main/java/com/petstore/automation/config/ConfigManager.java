package com.petstore.automation.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager for loading and accessing application properties.
 * Implements singleton pattern to ensure single instance across the framework.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Slf4j
public final class ConfigManager {
    
    private static final String CONFIG_FILE = "config.properties";
    private static ConfigManager instance;
    private final Properties properties;
    
    /**
     * Private constructor to prevent instantiation.
     * Loads properties from config.properties file.
     */
    private ConfigManager() {
        properties = new Properties();
        loadProperties();
    }
    
    /**
     * Gets the singleton instance of ConfigManager.
     * 
     * @return ConfigManager instance
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * Loads properties from the configuration file.
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                log.error("Unable to find {}", CONFIG_FILE);
                throw new RuntimeException("Configuration file not found: " + CONFIG_FILE);
            }
            properties.load(input);
            log.info("Configuration loaded successfully from {}", CONFIG_FILE);
        } catch (IOException e) {
            log.error("Error loading configuration file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
    
    /**
     * Gets a property value by key.
     * 
     * @param key the property key
     * @return the property value, or null if not found
     */
    public String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        return value;
    }
    
    /**
     * Gets a property value with a default fallback.
     * 
     * @param key the property key
     * @param defaultValue the default value if property not found
     * @return the property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Gets the API base URL.
     * 
     * @return the base URL
     */
    public String getBaseUrl() {
        return getProperty("api.base.url");
    }
    
    /**
     * Gets the connection timeout in seconds.
     * 
     * @return connection timeout
     */
    public int getConnectTimeout() {
        return Integer.parseInt(getProperty("api.timeout.connect", "30"));
    }
    
    /**
     * Gets the read timeout in seconds.
     * 
     * @return read timeout
     */
    public int getReadTimeout() {
        return Integer.parseInt(getProperty("api.timeout.read", "30"));
    }
    
    /**
     * Gets the write timeout in seconds.
     * 
     * @return write timeout
     */
    public int getWriteTimeout() {
        return Integer.parseInt(getProperty("api.timeout.write", "30"));
    }
    
    /**
     * Gets the retry count for failed tests.
     * 
     * @return retry count
     */
    public int getRetryCount() {
        return Integer.parseInt(getProperty("test.retry.count", "2"));
    }
    
    /**
     * Gets the number of parallel threads for test execution.
     * 
     * @return thread count
     */
    public int getParallelThreads() {
        return Integer.parseInt(getProperty("test.parallel.threads", "3"));
    }
    
    /**
     * Checks if request logging is enabled.
     * 
     * @return true if enabled, false otherwise
     */
    public boolean isRequestLoggingEnabled() {
        return Boolean.parseBoolean(getProperty("logging.request.enabled", "true"));
    }
    
    /**
     * Checks if response logging is enabled.
     * 
     * @return true if enabled, false otherwise
     */
    public boolean isResponseLoggingEnabled() {
        return Boolean.parseBoolean(getProperty("logging.response.enabled", "true"));
    }
}
