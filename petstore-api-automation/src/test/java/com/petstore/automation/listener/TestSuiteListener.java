package com.petstore.automation.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * TestNG suite listener for logging suite-level events.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Slf4j
public class TestSuiteListener implements ISuiteListener {
    
    @Override
    public void onStart(ISuite suite) {
        log.info("========================================");
        log.info("Starting Test Suite: {}", suite.getName());
        log.info("========================================");
    }
    
    @Override
    public void onFinish(ISuite suite) {
        log.info("========================================");
        log.info("Finished Test Suite: {}", suite.getName());
        log.info("========================================");
    }
}
