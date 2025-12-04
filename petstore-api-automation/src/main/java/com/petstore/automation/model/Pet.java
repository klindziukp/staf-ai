package com.petstore.automation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pet model representing a pet in the Petstore API.
 * Based on OpenAPI specification for Swagger Petstore.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pet {
    
    /**
     * Unique identifier for the pet (required).
     */
    @JsonProperty("id")
    private Long id;
    
    /**
     * Name of the pet (required).
     */
    @JsonProperty("name")
    private String name;
    
    /**
     * Optional tag for the pet.
     */
    @JsonProperty("tag")
    private String tag;
}
