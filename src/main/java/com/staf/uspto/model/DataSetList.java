package com.staf.uspto.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model class representing the list of available datasets from USPTO API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSetList {
    
    @SerializedName("total")
    private Integer total;
    
    @SerializedName("apis")
    private List<ApiInfo> apis;
}
