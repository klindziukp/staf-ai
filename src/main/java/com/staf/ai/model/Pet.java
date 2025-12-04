package com.staf.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Pet model representing a pet entity in the Petstore API.
 * This class follows the OpenAPI specification for the Pet object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pet implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Unique identifier for the pet.
     * Required field.
     */
    @JsonProperty("id")
    private Long id;
    
    /**
     * Name of the pet.
     * Required field.
     */
    @JsonProperty("name")
    private String name;
    
    /**
     * Tag/category for the pet.
     * Optional field.
     */
    @JsonProperty("tag")
    private String tag;
    
    /**
     * Creates a Pet with only required fields.
     *
     * @param id   the pet's unique identifier
     * @param name the pet's name
     * @return a new Pet instance
     */
    public static Pet createMinimal(Long id, String name) {
        return Pet.builder()
                .id(id)
                .name(name)
                .build();
    }
    
    /**
     * Creates a complete Pet with all fields.
     *
     * @param id   the pet's unique identifier
     * @param name the pet's name
     * @param tag  the pet's tag/category
     * @return a new Pet instance
     */
    public static Pet createComplete(Long id, String name, String tag) {
        return Pet.builder()
                .id(id)
                .name(name)
                .tag(tag)
                .build();
    }
}
