package com.staf.uspto.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Model class representing search response from USPTO API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    
    @SerializedName("numFound")
    private Integer numFound;
    
    @SerializedName("start")
    private Integer start;
    
    @SerializedName("docs")
    private List<Map<String, Object>> docs;
}
