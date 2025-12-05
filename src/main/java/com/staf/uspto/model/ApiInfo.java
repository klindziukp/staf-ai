package com.staf.uspto.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing API information for a dataset.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiInfo {
    
    @SerializedName("apiKey")
    private String apiKey;
    
    @SerializedName("apiVersionNumber")
    private String apiVersionNumber;
    
    @SerializedName("apiUrl")
    private String apiUrl;
    
    @SerializedName("apiDocumentationUrl")
    private String apiDocumentationUrl;
}
