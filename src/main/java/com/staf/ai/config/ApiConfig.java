package com.staf.ai.config;

import lombok.Builder;
import lombok.Data;

/**
 * API configuration holder.
 * Contains all necessary configuration for API testing.
 */
@Data
@Builder
public class ApiConfig {
    
    private String baseUrl;
    private String basePath;
    private Integer connectionTimeout;
    private Integer socketTimeout;
    private Boolean enableLogging;
    private String logLevel;
    
    /**
     * Gets the complete base URI (baseUrl + basePath).
     *
     * @return the complete base URI
     */
    public String getBaseUri() {
        return baseUrl + basePath;
    }
}
