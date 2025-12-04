package com.petstore.listeners;

import com.petstore.config.ConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * TestNG Retry Analyzer for handling flaky tests
 * Automatically retries failed tests based on configuration
 */
@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {
    
    private int retryCount = 0;
    private final int maxRetryCount;
    
    public RetryAnalyzer() {
        this.maxRetryCount = ConfigManager.getInstance().getMaxRetryAttempts();
    }
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            log.warn("Retrying test '{}' - Attempt {} of {}",
                    result.getMethod().getMethodName(), retryCount, maxRetryCount);
            return true;
        }
        return false;
    }
}
