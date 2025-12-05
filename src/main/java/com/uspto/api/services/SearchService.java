package com.uspto.api.services;

import com.uspto.api.client.RestClient;
import com.uspto.api.constants.ApiEndpoints;
import com.uspto.api.models.SearchRequest;
import com.uspto.api.models.SearchResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class for Search-related API operations.
 * Provides methods to search records in datasets.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Slf4j
public class SearchService {

    private final RestClient restClient;

    /**
     * Constructor initializes the service with REST client.
     */
    public SearchService() {
        this.restClient = new RestClient();
    }

    /**
     * Searches records with criteria using JSON body.
     *
     * @param dataset dataset name
     * @param version version number
     * @param searchRequest search request body
     * @return Response containing search results
     */
    @Step("Search records in dataset: {dataset}, version: {version} with criteria: {searchRequest.criteria}")
    public Response searchRecords(String dataset, String version, SearchRequest searchRequest) {
        log.info("Searching records in dataset: {}, version: {} with criteria: {}", 
                dataset, version, searchRequest.getCriteria());
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("dataset", dataset);
        pathParams.put("version", version);
        
        Response response = restClient.post(ApiEndpoints.SEARCH_RECORDS, pathParams, searchRequest);
        log.info("Search completed. Found {} records", getNumFound(response));
        return response;
    }

    /**
     * Searches records with form parameters.
     *
     * @param dataset dataset name
     * @param version version number
     * @param criteria search criteria
     * @param start starting record number
     * @param rows number of rows to return
     * @return Response containing search results
     */
    @Step("Search records with form params - dataset: {dataset}, version: {version}, criteria: {criteria}")
    public Response searchRecordsWithFormParams(String dataset, String version, 
                                                String criteria, Integer start, Integer rows) {
        log.info("Searching records with form params in dataset: {}, version: {}", dataset, version);
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("dataset", dataset);
        pathParams.put("version", version);
        
        Map<String, String> formParams = new HashMap<>();
        formParams.put("criteria", criteria);
        formParams.put("start", String.valueOf(start));
        formParams.put("rows", String.valueOf(rows));
        
        Response response = restClient.postWithFormParams(ApiEndpoints.SEARCH_RECORDS, pathParams, formParams);
        log.info("Search completed with form params. Found {} records", getNumFound(response));
        return response;
    }

    /**
     * Searches all records (wildcard search).
     *
     * @param dataset dataset name
     * @param version version number
     * @return Response containing all records
     */
    @Step("Search all records in dataset: {dataset}, version: {version}")
    public Response searchAllRecords(String dataset, String version) {
        log.info("Searching all records in dataset: {}, version: {}", dataset, version);
        
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(100)
                .build();
        
        return searchRecords(dataset, version, searchRequest);
    }

    /**
     * Searches records with specific criteria.
     *
     * @param dataset dataset name
     * @param version version number
     * @param criteria search criteria
     * @return Response containing search results
     */
    @Step("Search records with criteria: {criteria}")
    public Response searchWithCriteria(String dataset, String version, String criteria) {
        log.info("Searching with criteria: {}", criteria);
        
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria(criteria)
                .start(0)
                .rows(100)
                .build();
        
        return searchRecords(dataset, version, searchRequest);
    }

    /**
     * Searches records with pagination.
     *
     * @param dataset dataset name
     * @param version version number
     * @param criteria search criteria
     * @param start starting record number
     * @param rows number of rows
     * @return Response containing paginated results
     */
    @Step("Search records with pagination - start: {start}, rows: {rows}")
    public Response searchWithPagination(String dataset, String version, 
                                        String criteria, int start, int rows) {
        log.info("Searching with pagination - start: {}, rows: {}", start, rows);
        
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria(criteria)
                .start(start)
                .rows(rows)
                .build();
        
        return searchRecords(dataset, version, searchRequest);
    }

    /**
     * Parses the search response into POJO.
     *
     * @param response API response
     * @return SearchResponse object
     */
    public SearchResponse parseSearchResponse(Response response) {
        return response.as(SearchResponse.class);
    }

    /**
     * Gets the number of records found.
     *
     * @param response API response
     * @return number of records found
     */
    public int getNumFound(Response response) {
        try {
            return response.jsonPath().getInt("response.numFound");
        } catch (Exception e) {
            log.warn("Unable to extract numFound from response", e);
            return 0;
        }
    }

    /**
     * Gets the starting position of results.
     *
     * @param response API response
     * @return starting position
     */
    public int getStart(Response response) {
        return response.jsonPath().getInt("response.start");
    }

    /**
     * Gets the number of documents returned.
     *
     * @param response API response
     * @return number of documents
     */
    public int getDocsCount(Response response) {
        return response.jsonPath().getList("response.docs").size();
    }

    /**
     * Checks if search returned any results.
     *
     * @param response API response
     * @return true if results found, false otherwise
     */
    public boolean hasResults(Response response) {
        return getNumFound(response) > 0;
    }
}
