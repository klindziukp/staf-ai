package com.staf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing a search request for dataset records.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    
    @Builder.Default
    private String criteria = "*:*";
    
    @Builder.Default
    private Integer start = 0;
    
    @Builder.Default
    private Integer rows = 100;
}
