package com.uspto.api.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager for loading and managing application properties.
 * Implements Singleton pattern to ensure single instance across the framework.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Slf4j
public final class ConfigurationManager {

    private static ConfigurationManager instance;
    private final Properties properties;
    private static final String CONFIG_FILE = "config.properties";

    /**
     * Private constructor to prevent instantiation.
     * Loads properties from config.properties file.
     */
    private ConfigurationManager() {
        properties = new Properties();
        loadProperties();
    }

    /**
     * Gets the singleton instance of ConfigurationManager.
     *
     * @return ConfigurationManager instance
     */
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
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
     * Gets property value by key.
     *
     * @param key property key
     * @return property value
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            log.warn("Property not found for key: {}", key);
        }
        return value;
    }

    /**
     * Gets property value by key with default value.
     *
     * @param key property key
     * @param defaultValue default value if property not found
     * @return property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Gets base URL from configuration.
     *
     * @return base URL
     */
    public String getBaseUrl() {
        return getProperty("api.base.url");
    }

    /**
     * Gets request timeout from configuration.
     *
     * @return request timeout in milliseconds
     */
    public int getRequestTimeout() {
        return Integer.parseInt(getProperty("api.request.timeout", "30000"));
    }

    /**
     * Gets connection timeout from configuration.
     *
     * @return connection timeout in milliseconds
     */
    public int getConnectionTimeout() {
        return Integer.parseInt(getProperty("api.connection.timeout", "10000"));
    }

    /**
     * Gets socket timeout from configuration.
     *
     * @return socket timeout in milliseconds
     */
    public int getSocketTimeout() {
        return Integer.parseInt(getProperty("api.socket.timeout", "30000"));
    }

    /**
     * Gets default dataset name.
     *
     * @return default dataset name
     */
    public String getDefaultDataset() {
        return getProperty("api.default.dataset", "oa_citations");
    }

    /**
     * Gets default version.
     *
     * @return default version
     */
    public String getDefaultVersion() {
        return getProperty("api.default.version", "v1");
    }

    /**
     * Gets test data directory path.
     *
     * @return test data directory path
     */
    public String getTestDataDirectory() {
        return getProperty("testdata.directory", "src/test/resources/testdata");
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

    /**
     * Gets retry count for failed requests.
     *
     * @return retry count
     */
    public int getRetryCount() {
        return Integer.parseInt(getProperty("api.retry.count", "3"));
    }

    /**
     * Gets retry delay in milliseconds.
     *
     * @return retry delay
     */
    public int getRetryDelay() {
        return Integer.parseInt(getProperty("api.retry.delay", "1000"));
    }
}
