package com.staf.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Error model representing an error response from the Petstore API.
 * This class follows the OpenAPI specification for the Error object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Error code.
     * Required field.
     */
    @JsonProperty("code")
    private Integer code;
    
    /**
     * Error message.
     * Required field.
     */
    @JsonProperty("message")
    private String message;
}
