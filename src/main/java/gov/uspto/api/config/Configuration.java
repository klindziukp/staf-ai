package gov.uspto.api.config;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager for loading environment-specific properties
 */
@Getter
public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);
    private static Configuration instance;
    private final Properties properties;
    private final Environment environment;

    private Configuration() {
        this.environment = Environment.fromString(System.getProperty("env", "dev"));
        this.properties = loadProperties();
        log.info("Configuration loaded for environment: {}", environment);
    }

    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        String configFile = String.format("config/%s.properties", environment.getName());
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                log.error("Unable to find configuration file: {}", configFile);
                throw new RuntimeException("Configuration file not found: " + configFile);
            }
            props.load(input);
            log.info("Successfully loaded configuration from: {}", configFile);
        } catch (IOException e) {
            log.error("Error loading configuration file: {}", configFile, e);
            throw new RuntimeException("Failed to load configuration", e);
        }
        
        return props;
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

    public boolean isLoggingEnabled() {
        return Boolean.parseBoolean(getProperty("api.logging.enabled", "true"));
    }

    public int getMaxRetryAttempts() {
        return Integer.parseInt(getProperty("api.retry.max.attempts", "3"));
    }

    public long getRetryDelay() {
        return Long.parseLong(getProperty("api.retry.delay.ms", "1000"));
    }

    public int getExpectedResponseTime() {
        return Integer.parseInt(getProperty("api.expected.response.time.ms", "5000"));
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            log.warn("Property '{}' not found in configuration", key);
            throw new RuntimeException("Required property not found: " + key);
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
