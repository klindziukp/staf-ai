package com.tictactoe.api.utils;

import com.tictactoe.api.config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

/**
 * Builder for creating REST Assured RequestSpecification with common settings.
 */
@Slf4j
public class RequestSpecificationBuilder {
    private final ApiConfig config;
    private final io.restassured.builder.RequestSpecBuilder builder;

    public RequestSpecificationBuilder() {
        this.config = ApiConfig.getInstance();
        this.builder = new io.restassured.builder.RequestSpecBuilder();
        initializeDefaults();
    }

    private void initializeDefaults() {
        builder.setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured());

        if (config.isEnableRequestLogging()) {
            builder.addFilter(new RequestLoggingFilter(LogDetail.ALL));
        }

        if (config.isEnableResponseLogging()) {
            builder.addFilter(new ResponseLoggingFilter(LogDetail.ALL));
        }
    }

    /**
     * Adds API Key authentication header.
     *
     * @return this builder
     */
    public RequestSpecificationBuilder withApiKeyAuth() {
        builder.addHeader("api-key", config.getApiKey());
        log.debug("Added API Key authentication");
        return this;
    }

    /**
     * Adds Bearer token authentication header.
     *
     * @return this builder
     */
    public RequestSpecificationBuilder withBearerAuth() {
        builder.addHeader("Authorization", "Bearer " + config.getBearerToken());
        log.debug("Added Bearer token authentication");
        return this;
    }

    /**
     * Adds custom Bearer token authentication header.
     *
     * @param token the bearer token
     * @return this builder
     */
    public RequestSpecificationBuilder withBearerAuth(String token) {
        builder.addHeader("Authorization", "Bearer " + token);
        log.debug("Added custom Bearer token authentication");
        return this;
    }

    /**
     * Adds OAuth2 authentication header.
     *
     * @param accessToken the OAuth2 access token
     * @return this builder
     */
    public RequestSpecificationBuilder withOAuth2(String accessToken) {
        builder.addHeader("Authorization", "Bearer " + accessToken);
        log.debug("Added OAuth2 authentication");
        return this;
    }

    /**
     * Adds a custom header.
     *
     * @param name  the header name
     * @param value the header value
     * @return this builder
     */
    public RequestSpecificationBuilder withHeader(String name, String value) {
        builder.addHeader(name, value);
        log.debug("Added custom header: {} = {}", name, value);
        return this;
    }

    /**
     * Sets the base path for the request.
     *
     * @param basePath the base path
     * @return this builder
     */
    public RequestSpecificationBuilder withBasePath(String basePath) {
        builder.setBasePath(basePath);
        log.debug("Set base path: {}", basePath);
        return this;
    }

    /**
     * Adds a query parameter.
     *
     * @param name  the parameter name
     * @param value the parameter value
     * @return this builder
     */
    public RequestSpecificationBuilder withQueryParam(String name, Object value) {
        builder.addQueryParam(name, value);
        log.debug("Added query parameter: {} = {}", name, value);
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
}
