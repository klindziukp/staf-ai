package com.petstore.automation.base;

import com.petstore.automation.client.PetApiClient;
import com.petstore.automation.config.ConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

/**
 * Base test class providing common setup and utilities for all test classes.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Slf4j
public abstract class BaseTest {
    
    protected PetApiClient petApiClient;
    protected ConfigManager config;
    
    /**
     * Setup method executed before test class.
     * Initializes API client and configuration.
     */
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        log.info("=== Setting up test class: {} ===", this.getClass().getSimpleName());
        config = ConfigManager.getInstance();
        petApiClient = new PetApiClient();
        log.info("Base URL: {}", config.getBaseUrl());
    }
    
    /**
     * Setup method executed before each test method.
     * Logs test method information.
     * 
     * @param method the test method
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        log.info(">>> Starting test: {}", method.getName());
    }
}
