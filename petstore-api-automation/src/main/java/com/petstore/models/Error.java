package com.petstore.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error model representing error responses from the Petstore API
 * Based on OpenAPI specification for Swagger Petstore
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    
    @JsonProperty("code")
    private Integer code;
    
    @JsonProperty("message")
    private String message;
}
