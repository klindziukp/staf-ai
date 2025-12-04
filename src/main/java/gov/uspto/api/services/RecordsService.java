package gov.uspto.api.services;

import gov.uspto.api.client.RequestSpecificationBuilder;
import gov.uspto.api.models.request.SearchRequest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

/**
 * Service class for records search API operations
 */
public class RecordsService {
    private static final Logger log = LoggerFactory.getLogger(RecordsService.class);
    private static final String RECORDS_ENDPOINT = "/{dataset}/{version}/records";

    @Step("Search records in dataset: {dataset}, version: {version}")
    public Response searchRecords(String dataset, String version, SearchRequest searchRequest) {
        log.info("Searching records in dataset: {}, version: {} with criteria: {}", 
                dataset, version, searchRequest.getCriteria());
        
        return given()
                .spec(new RequestSpecificationBuilder()
                        .addPathParam("dataset", dataset)
                        .addPathParam("version", version)
                        .build())
                .contentType(ContentType.URLENC)
                .formParam("criteria", searchRequest.getCriteria())
                .formParam("start", searchRequest.getStart())
                .formParam("rows", searchRequest.getRows())
                .when()
                .post(RECORDS_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    @Step("Search with Lucene query: {luceneQuery}")
    public Response searchWithLuceneQuery(String dataset, String version, String luceneQuery) {
        SearchRequest request = SearchRequest.builder()
                .criteria(luceneQuery)
                .start(0)
                .rows(100)
                .build();
        
        return searchRecords(dataset, version, request);
    }

    @Step("Search with pagination - start: {start}, rows: {rows}")
    public Response searchWithPagination(String dataset, String version, String criteria, 
                                        int start, int rows) {
        SearchRequest request = SearchRequest.builder()
                .criteria(criteria)
                .start(start)
                .rows(rows)
                .build();
        
        return searchRecords(dataset, version, request);
    }
}
