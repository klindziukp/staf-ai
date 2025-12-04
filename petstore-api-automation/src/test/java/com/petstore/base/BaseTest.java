package com.petstore.base;

import com.petstore.config.ConfigManager;
import com.petstore.services.PetService;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

/**
 * Base test class providing common setup and teardown functionality
 * All test classes should extend this class
 */
@Slf4j
public abstract class BaseTest {
    
    protected PetService petService;
    protected ConfigManager config;
    
    @BeforeSuite(alwaysRun = true)
    @Step("Initialize test suite configuration")
    public void suiteSetup() {
        log.info("Initializing test suite configuration");
        config = ConfigManager.getInstance();
        log.info("Base URL: {}", config.getBaseUrl());
    }
    
    @BeforeMethod(alwaysRun = true)
    @Step("Setup test method: {method.name}")
    public void setUp(Method method) {
        log.info("Setting up test method: {}", method.getName());
        petService = new PetService();
    }
    
    @AfterMethod(alwaysRun = true)
    @Step("Teardown test method: {method.name}")
    public void tearDown(Method method) {
        log.info("Tearing down test method: {}", method.getName());
        // Add cleanup logic here if needed
    }
}
