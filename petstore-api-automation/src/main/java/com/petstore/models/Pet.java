package com.petstore.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pet model representing a pet in the Petstore API
 * Based on OpenAPI specification for Swagger Petstore
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pet {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("tag")
    private String tag;
    
    /**
     * Creates a Pet with only required fields
     * @param name Pet name (required)
     * @return Pet instance with minimal required fields
     */
    public static Pet createMinimal(String name) {
        return Pet.builder()
                .name(name)
                .build();
    }
    
    /**
     * Creates a complete Pet with all fields
     * @param id Pet ID
     * @param name Pet name (required)
     * @param tag Pet tag (optional)
     * @return Complete Pet instance
     */
    public static Pet createComplete(Long id, String name, String tag) {
        return Pet.builder()
                .id(id)
                .name(name)
                .tag(tag)
                .build();
    }
}
