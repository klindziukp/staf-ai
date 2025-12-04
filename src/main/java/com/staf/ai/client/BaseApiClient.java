package com.staf.ai.client;

import com.staf.ai.config.ApiConfig;
import com.staf.ai.config.ConfigurationManager;
import com.staf.ai.utils.RequestSpecificationBuilder;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;

/**
 * Base API client providing common functionality for all API clients.
 * Implements template method pattern for API calls.
 */
@Slf4j
public abstract class BaseApiClient {
    
    protected final ApiConfig apiConfig;
    
    protected BaseApiClient() {
        this.apiConfig = ConfigurationManager.getInstance().getApiConfig();
    }
    
    /**
     * Creates a base request specification with default configuration.
     *
     * @return configured RequestSpecification
     */
    protected RequestSpecification createBaseRequest() {
        return RequestSpecificationBuilder.newBuilder().build();
    }
    
    /**
     * Executes a GET request.
     *
     * @param requestSpec the request specification
     * @param endpoint    the endpoint path
     * @return the Response object
     */
    @Step("Execute GET request to {endpoint}")
    protected Response executeGet(RequestSpecification requestSpec, String endpoint) {
        log.info("Executing GET request to: {}", endpoint);
        Response response = given()
                .spec(requestSpec)
                .when()
                .get(endpoint);
        logResponse(response);
        return response;
    }
    
    /**
     * Executes a POST request.
     *
     * @param requestSpec the request specification
     * @param endpoint    the endpoint path
     * @return the Response object
     */
    @Step("Execute POST request to {endpoint}")
    protected Response executePost(RequestSpecification requestSpec, String endpoint) {
        log.info("Executing POST request to: {}", endpoint);
        Response response = given()
                .spec(requestSpec)
                .when()
                .post(endpoint);
        logResponse(response);
        return response;
    }
    
    /**
     * Executes a PUT request.
     *
     * @param requestSpec the request specification
     * @param endpoint    the endpoint path
     * @return the Response object
     */
    @Step("Execute PUT request to {endpoint}")
    protected Response executePut(RequestSpecification requestSpec, String endpoint) {
        log.info("Executing PUT request to: {}", endpoint);
        Response response = given()
                .spec(requestSpec)
                .when()
                .put(endpoint);
        logResponse(response);
        return response;
    }
    
    /**
     * Executes a DELETE request.
     *
     * @param requestSpec the request specification
     * @param endpoint    the endpoint path
     * @return the Response object
     */
    @Step("Execute DELETE request to {endpoint}")
    protected Response executeDelete(RequestSpecification requestSpec, String endpoint) {
        log.info("Executing DELETE request to: {}", endpoint);
        Response response = given()
                .spec(requestSpec)
                .when()
                .delete(endpoint);
        logResponse(response);
        return response;
    }
    
    /**
     * Logs response details.
     *
     * @param response the Response object
     */
    private void logResponse(Response response) {
        log.info("Response status code: {}", response.getStatusCode());
        log.debug("Response time: {} ms", response.getTime());
    }
    
    /**
     * Validates response status code.
     *
     * @param response           the Response object
     * @param expectedStatusCode the expected status code
     */
    protected void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            log.error("Expected status code {} but got {}", expectedStatusCode, actualStatusCode);
            throw new AssertionError(
                    String.format("Expected status code %d but got %d", expectedStatusCode, actualStatusCode)
            );
        }
    }
}
