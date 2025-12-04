package com.petstore.automation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error model representing API error responses.
 * Based on OpenAPI specification for Swagger Petstore.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    
    /**
     * Error code (required).
     */
    @JsonProperty("code")
    private Integer code;
    
    /**
     * Error message describing the issue (required).
     */
    @JsonProperty("message")
    private String message;
}
