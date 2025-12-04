package gov.uspto.api.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing a single dataset
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSet {
    
    @JsonProperty("apiKey")
    private String apiKey;
    
    @JsonProperty("apiVersionNumber")
    private String apiVersionNumber;
    
    @JsonProperty("apiUrl")
    private String apiUrl;
    
    @JsonProperty("apiDocumentationUrl")
    private String apiDocumentationUrl;
}
