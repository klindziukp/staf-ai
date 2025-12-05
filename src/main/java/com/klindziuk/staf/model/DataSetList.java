package com.klindziuk.staf.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model representing the list of available data sets.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSetList {

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("apis")
    private List<ApiInfo> apis;
}
