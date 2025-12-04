package com.petstore.automation.listener;

import com.petstore.automation.config.ConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer for handling flaky tests.
 * Retries failed tests based on configuration.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {
    
    private int retryCount = 0;
    private final int maxRetryCount;
    
    public RetryAnalyzer() {
        this.maxRetryCount = ConfigManager.getInstance().getRetryCount();
    }
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            log.warn("Retrying test: {} - Attempt: {}/{}", 
                    result.getMethod().getMethodName(), 
                    retryCount, 
                    maxRetryCount);
            return true;
        }
        return false;
    }
}
