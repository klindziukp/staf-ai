package com.tictactoe.api.tests.base;

import com.tictactoe.api.client.TicTacToeApiClient;
import com.tictactoe.api.config.ApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

/**
 * Base test class providing common setup and utilities for all tests.
 */
@Slf4j
public abstract class BaseTest {
    protected TicTacToeApiClient apiClient;
    protected ApiConfig config;

    @BeforeClass
    public void setUpClass() {
        log.info("Setting up test class: {}", this.getClass().getSimpleName());
        config = ApiConfig.getInstance();
        initializeApiClient();
    }

    @BeforeMethod
    public void setUpMethod(Method method) {
        log.info("Starting test: {}", method.getName());
    }

    /**
     * Initializes the API client with default authentication.
     * Override this method in subclasses to use different authentication.
     */
    protected void initializeApiClient() {
        apiClient = TicTacToeApiClient.withApiKey();
    }

    /**
     * Creates a new API client with Bearer token authentication.
     *
     * @return API client with Bearer auth
     */
    protected TicTacToeApiClient createBearerAuthClient() {
        return TicTacToeApiClient.withBearerToken();
    }

    /**
     * Creates a new API client without authentication.
     *
     * @return API client without auth
     */
    protected TicTacToeApiClient createUnauthenticatedClient() {
        return TicTacToeApiClient.withoutAuth();
    }

    /**
     * Creates a new API client with custom Bearer token.
     *
     * @param token the bearer token
     * @return API client with custom Bearer auth
     */
    protected TicTacToeApiClient createBearerAuthClient(String token) {
        return TicTacToeApiClient.withBearerToken(token);
    }
}
