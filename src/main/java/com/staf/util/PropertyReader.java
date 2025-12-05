package com.staf.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for reading properties from application.properties file.
 */
@Slf4j
public final class PropertyReader {

    private static final Properties properties = new Properties();
    private static final String PROPERTIES_FILE = "application.properties";

    static {
        try (InputStream input = PropertyReader.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                log.error("Unable to find {}", PROPERTIES_FILE);
                throw new RuntimeException("Unable to find " + PROPERTIES_FILE);
            }
            properties.load(input);
            log.info("Properties loaded successfully from {}", PROPERTIES_FILE);
        } catch (IOException ex) {
            log.error("Error loading properties file", ex);
            throw new RuntimeException("Error loading properties file", ex);
        }
    }

    private PropertyReader() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Get property value by key.
     *
     * @param key property key
     * @return property value
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property value by key with default value.
     *
     * @param key          property key
     * @param defaultValue default value if property not found
     * @return property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get integer property value.
     *
     * @param key property key
     * @return integer property value
     */
    public static int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    /**
     * Get integer property value with default.
     *
     * @param key          property key
     * @param defaultValue default value if property not found
     * @return integer property value or default value
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }
}
