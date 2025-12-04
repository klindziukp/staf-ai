package com.staf.ai.base;

import com.staf.ai.client.PetsApiClient;
import com.staf.ai.config.ConfigurationManager;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

/**
 * Base test class providing common setup and teardown functionality.
 * All test classes should extend this class.
 */
@Slf4j
public abstract class BaseTest {
    
    protected PetsApiClient petsApiClient;
    protected ConfigurationManager configManager;
    
    /**
     * Suite-level setup executed once before all tests.
     */
    @BeforeSuite(alwaysRun = true)
    @Step("Initialize test suite")
    public void suiteSetup() {
        log.info("=== Test Suite Setup Started ===");
        configManager = ConfigurationManager.getInstance();
        log.info("Base URL: {}", configManager.getApiConfig().getBaseUri());
        log.info("=== Test Suite Setup Completed ===");
    }
    
    /**
     * Method-level setup executed before each test method.
     *
     * @param method the test method about to be executed
     */
    @BeforeMethod(alwaysRun = true)
    @Step("Setup test: {method.name}")
    public void setUp(Method method) {
        log.info("=== Starting Test: {} ===", method.getName());
        petsApiClient = new PetsApiClient();
    }
    
    /**
     * Method-level teardown executed after each test method.
     *
     * @param method the test method that was executed
     */
    @AfterMethod(alwaysRun = true)
    @Step("Teardown test: {method.name}")
    public void tearDown(Method method) {
        log.info("=== Completed Test: {} ===", method.getName());
    }
}
