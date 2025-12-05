package com.staf.config;

import com.staf.util.PropertyReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for API settings.
 */
@Slf4j
@Getter
public class ApiConfig {

    private static ApiConfig instance;

    private final String baseUrl;
    private final String scheme;
    private final int connectionTimeout;
    private final int readTimeout;
    private final String defaultDataset;
    private final String defaultVersion;

    private ApiConfig() {
        this.baseUrl = PropertyReader.getProperty("api.base.url");
        this.scheme = PropertyReader.getProperty("api.scheme", "https");
        this.connectionTimeout = PropertyReader.getIntProperty("api.timeout.connection", 30000);
        this.readTimeout = PropertyReader.getIntProperty("api.timeout.read", 30000);
        this.defaultDataset = PropertyReader.getProperty("test.dataset.name", "oa_citations");
        this.defaultVersion = PropertyReader.getProperty("test.dataset.version", "v1");
        
        log.info("API Configuration initialized: baseUrl={}, scheme={}", baseUrl, scheme);
    }

    /**
     * Get singleton instance of ApiConfig.
     *
     * @return ApiConfig instance
     */
    public static synchronized ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }
        return instance;
    }

    /**
     * Get full base URL with scheme.
     *
     * @return full base URL
     */
    public String getFullBaseUrl() {
        return baseUrl;
    }
}
