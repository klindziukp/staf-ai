package gov.uspto.api.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for searching dataset records
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchRequest {
    
    @JsonProperty("criteria")
    private String criteria;
    
    @JsonProperty("start")
    @Builder.Default
    private Integer start = 0;
    
    @JsonProperty("rows")
    @Builder.Default
    private Integer rows = 100;
}
