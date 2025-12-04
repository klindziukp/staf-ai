package com.tictactoe.api.utils;

import com.tictactoe.api.config.AuthConfig;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

/**
 * Helper class for handling API authentication.
 * Supports API Key, JWT, and OAuth2 authentication methods.
 */
@Slf4j
public final class AuthenticationHelper {
    private static final AuthConfig AUTH_CONFIG = AuthConfig.getInstance();
    private static final String API_KEY_HEADER = "api-key";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private AuthenticationHelper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Adds API Key authentication to request specification.
     *
     * @param spec RequestSpecification to modify
     * @return modified RequestSpecification
     */
    public static RequestSpecification withApiKey(RequestSpecification spec) {
        if (!AUTH_CONFIG.hasApiKey()) {
            log.warn("API Key not configured");
            return spec;
        }
        log.debug("Adding API Key authentication");
        return spec.header(API_KEY_HEADER, AUTH_CONFIG.getApiKey());
    }

    /**
     * Adds JWT Bearer token authentication to request specification.
     *
     * @param spec RequestSpecification to modify
     * @return modified RequestSpecification
     */
    public static RequestSpecification withJwtToken(RequestSpecification spec) {
        if (!AUTH_CONFIG.hasJwtToken()) {
            log.warn("JWT Token not configured");
            return spec;
        }
        log.debug("Adding JWT Bearer token authentication");
        return spec.header(AUTHORIZATION_HEADER, BEARER_PREFIX + AUTH_CONFIG.getJwtToken());
    }

    /**
     * Creates request specification with default authentication.
     * Uses API Key if available, otherwise JWT token.
     *
     * @return authenticated RequestSpecification
     */
    public static RequestSpecification getAuthenticatedSpec() {
        RequestSpecification spec = RestAssuredConfig.getDefaultRequestSpec();

        if (AUTH_CONFIG.hasApiKey()) {
            return withApiKey(spec);
        } else if (AUTH_CONFIG.hasJwtToken()) {
            return withJwtToken(spec);
        }

        log.warn("No authentication configured");
        return spec;
    }
}
