package com.uspto.api.client;

import com.uspto.api.config.ConfigurationManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Generic REST client wrapper for REST Assured.
 * Provides common HTTP methods with logging and Allure integration.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Slf4j
public class RestClient {

    private final ConfigurationManager config;
    private RequestSpecification requestSpec;
    private ResponseSpecification responseSpec;

    /**
     * Constructor initializes REST client with configuration.
     */
    public RestClient() {
        this.config = ConfigurationManager.getInstance();
        setupRequestSpecification();
        setupResponseSpecification();
    }

    /**
     * Sets up the default request specification.
     */
    private void setupRequestSpecification() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(config.getBaseUrl());
        builder.setContentType(ContentType.JSON);
        builder.addFilter(new AllureRestAssured());
        
        if (config.isRequestLoggingEnabled()) {
            builder.log(LogDetail.ALL);
        }

        requestSpec = builder.build();
        log.debug("Request specification configured with base URL: {}", config.getBaseUrl());
    }

    /**
     * Sets up the default response specification.
     */
    private void setupResponseSpecification() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        
        if (config.isResponseLoggingEnabled()) {
            builder.log(LogDetail.ALL);
        }

        responseSpec = builder.build();
        log.debug("Response specification configured");
    }

    /**
     * Gets the request specification.
     *
     * @return RequestSpecification
     */
    public RequestSpecification getRequestSpec() {
        return RestAssured.given().spec(requestSpec);
    }

    /**
     * Performs GET request.
     *
     * @param endpoint API endpoint
     * @return Response
     */
    public Response get(String endpoint) {
        log.info("Executing GET request to endpoint: {}", endpoint);
        Response response = getRequestSpec()
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
        log.info("GET request completed with status code: {}", response.getStatusCode());
        return response;
    }

    /**
     * Performs GET request with path parameters.
     *
     * @param endpoint API endpoint
     * @param pathParams path parameters
     * @return Response
     */
    public Response get(String endpoint, Map<String, String> pathParams) {
        log.info("Executing GET request to endpoint: {} with path params: {}", endpoint, pathParams);
        Response response = getRequestSpec()
                .pathParams(pathParams)
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
        log.info("GET request completed with status code: {}", response.getStatusCode());
        return response;
    }

    /**
     * Performs GET request with query parameters.
     *
     * @param endpoint API endpoint
     * @param queryParams query parameters
     * @return Response
     */
    public Response getWithQueryParams(String endpoint, Map<String, String> queryParams) {
        log.info("Executing GET request to endpoint: {} with query params: {}", endpoint, queryParams);
        Response response = getRequestSpec()
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
        log.info("GET request completed with status code: {}", response.getStatusCode());
        return response;
    }

    /**
     * Performs POST request with JSON body.
     *
     * @param endpoint API endpoint
     * @param body request body
     * @return Response
     */
    public Response post(String endpoint, Object body) {
        log.info("Executing POST request to endpoint: {}", endpoint);
        Response response = getRequestSpec()
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
        log.info("POST request completed with status code: {}", response.getStatusCode());
        return response;
    }

    /**
     * Performs POST request with path parameters and JSON body.
     *
     * @param endpoint API endpoint
     * @param pathParams path parameters
     * @param body request body
     * @return Response
     */
    public Response post(String endpoint, Map<String, String> pathParams, Object body) {
        log.info("Executing POST request to endpoint: {} with path params: {}", endpoint, pathParams);
        Response response = getRequestSpec()
                .pathParams(pathParams)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
        log.info("POST request completed with status code: {}", response.getStatusCode());
        return response;
    }

    /**
     * Performs POST request with form parameters.
     *
     * @param endpoint API endpoint
     * @param formParams form parameters
     * @return Response
     */
    public Response postWithFormParams(String endpoint, Map<String, String> formParams) {
        log.info("Executing POST request with form params to endpoint: {}", endpoint);
        Response response = getRequestSpec()
                .contentType(ContentType.URLENC)
                .formParams(formParams)
                .when()
                .post(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
        log.info("POST request completed with status code: {}", response.getStatusCode());
        return response;
    }

    /**
     * Performs POST request with path parameters and form parameters.
     *
     * @param endpoint API endpoint
     * @param pathParams path parameters
     * @param formParams form parameters
     * @return Response
     */
    public Response postWithFormParams(String endpoint, Map<String, String> pathParams, Map<String, String> formParams) {
        log.info("Executing POST request with form params to endpoint: {} with path params: {}", endpoint, pathParams);
        Response response = getRequestSpec()
                .contentType(ContentType.URLENC)
                .pathParams(pathParams)
                .formParams(formParams)
                .when()
                .post(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
        log.info("POST request completed with status code: {}", response.getStatusCode());
        return response;
    }

    /**
     * Performs POST request with custom headers.
     *
     * @param endpoint API endpoint
     * @param headers custom headers
     * @param body request body
     * @return Response
     */
    public Response postWithHeaders(String endpoint, Map<String, String> headers, Object body) {
        log.info("Executing POST request to endpoint: {} with custom headers", endpoint);
        Response response = getRequestSpec()
                .headers(headers)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
        log.info("POST request completed with status code: {}", response.getStatusCode());
        return response;
    }

    /**
     * Performs PUT request.
     *
     * @param endpoint API endpoint
     * @param body request body
     * @return Response
     */
    public Response put(String endpoint, Object body) {
        log.info("Executing PUT request to endpoint: {}", endpoint);
        Response response = getRequestSpec()
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
        log.info("PUT request completed with status code: {}", response.getStatusCode());
        return response;
    }

    /**
     * Performs DELETE request.
     *
     * @param endpoint API endpoint
     * @return Response
     */
    public Response delete(String endpoint) {
        log.info("Executing DELETE request to endpoint: {}", endpoint);
        Response response = getRequestSpec()
                .when()
                .delete(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
        log.info("DELETE request completed with status code: {}", response.getStatusCode());
        return response;
    }

    /**
     * Performs PATCH request.
     *
     * @param endpoint API endpoint
     * @param body request body
     * @return Response
     */
    public Response patch(String endpoint, Object body) {
        log.info("Executing PATCH request to endpoint: {}", endpoint);
        Response response = getRequestSpec()
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
        log.info("PATCH request completed with status code: {}", response.getStatusCode());
        return response;
    }
}
