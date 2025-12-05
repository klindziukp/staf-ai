package com.klindziuk.staf.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Model representing search response from dataset records.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    @JsonProperty("numFound")
    private Integer numFound;

    @JsonProperty("start")
    private Integer start;

    @JsonProperty("docs")
    private List<Map<String, Object>> docs;
}
