package com.klindziuk.petstore.base;

import com.klindziuk.petstore.config.ConfigurationManager;
import com.klindziuk.petstore.service.PetService;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

/**
 * Base test class providing common setup and utilities for all tests
 */
@Slf4j
public abstract class BaseTest {
    
    protected PetService petService;
    protected ConfigurationManager config;
    
    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        log.info("Setting up test class: {}", this.getClass().getSimpleName());
        config = ConfigurationManager.getInstance();
        petService = new PetService();
        
        Allure.addAttachment("Environment", "text/plain", config.getProperty("environment", "DEV"));
        Allure.addAttachment("Base URL", "text/plain", config.getBaseUrl());
    }
    
    @BeforeMethod(alwaysRun = true)
    public void setUpMethod(Method method) {
        log.info("Setting up test method: {}", method.getName());
        Allure.addAttachment("Test Method", "text/plain", method.getName());
    }
}
