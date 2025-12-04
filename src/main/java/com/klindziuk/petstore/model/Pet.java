package com.klindziuk.petstore.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pet model representing a pet in the Petstore API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    
    @SerializedName("id")
    private Long id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("tag")
    private String tag;
}
