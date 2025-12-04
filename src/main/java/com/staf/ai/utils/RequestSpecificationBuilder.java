package com.staf.ai.utils;

import com.staf.ai.config.ApiConfig;
import com.staf.ai.config.ConfigurationManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

/**
 * Builder for creating REST Assured RequestSpecification with common configurations.
 * Implements fluent interface pattern for easy configuration.
 */
@Slf4j
public class RequestSpecificationBuilder {
    
    private final RequestSpecBuilder builder;
    private final ApiConfig apiConfig;
    
    public RequestSpecificationBuilder() {
        this.apiConfig = ConfigurationManager.getInstance().getApiConfig();
        this.builder = new RequestSpecBuilder();
        applyDefaultConfiguration();
    }
    
    /**
     * Applies default configuration to the request specification.
     */
    private void applyDefaultConfiguration() {
        builder.setBaseUri(apiConfig.getBaseUri())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured());
        
        if (apiConfig.getEnableLogging()) {
            builder.addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL));
        }
        
        log.debug("Default request specification configured with base URI: {}", apiConfig.getBaseUri());
    }
    
    /**
     * Adds a path parameter to the request.
     *
     * @param key   the parameter key
     * @param value the parameter value
     * @return this builder instance
     */
    public RequestSpecificationBuilder withPathParam(String key, Object value) {
        builder.addPathParam(key, value);
        return this;
    }
    
    /**
     * Adds a query parameter to the request.
     *
     * @param key   the parameter key
     * @param value the parameter value
     * @return this builder instance
     */
    public RequestSpecificationBuilder withQueryParam(String key, Object value) {
        builder.addQueryParam(key, value);
        return this;
    }
    
    /**
     * Adds a header to the request.
     *
     * @param key   the header key
     * @param value the header value
     * @return this builder instance
     */
    public RequestSpecificationBuilder withHeader(String key, String value) {
        builder.addHeader(key, value);
        return this;
    }
    
    /**
     * Sets the request body.
     *
     * @param body the request body object
     * @return this builder instance
     */
    public RequestSpecificationBuilder withBody(Object body) {
        builder.setBody(body);
        return this;
    }
    
    /**
     * Sets the base path for the request.
     *
     * @param basePath the base path
     * @return this builder instance
     */
    public RequestSpecificationBuilder withBasePath(String basePath) {
        builder.setBasePath(basePath);
        return this;
    }
    
    /**
     * Builds the RequestSpecification.
     *
     * @return the configured RequestSpecification
     */
    public RequestSpecification build() {
        return builder.build();
    }
    
    /**
     * Creates a new builder instance.
     *
     * @return a new RequestSpecificationBuilder
     */
    public static RequestSpecificationBuilder newBuilder() {
        return new RequestSpecificationBuilder();
    }
}
