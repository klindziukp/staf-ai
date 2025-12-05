package com.uspto.api.listeners;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG test listener for test method level events.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Slf4j
public class TestMethodListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Starting Test: {}.{}", 
                result.getTestClass().getName(), 
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test PASSED: {}.{}", 
                result.getTestClass().getName(), 
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test FAILED: {}.{}", 
                result.getTestClass().getName(), 
                result.getMethod().getMethodName());
        log.error("Failure Reason: {}", result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test SKIPPED: {}.{}", 
                result.getTestClass().getName(), 
                result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("Starting Test Context: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Finished Test Context: {}", context.getName());
        log.info("Tests Passed: {}", context.getPassedTests().size());
        log.info("Tests Failed: {}", context.getFailedTests().size());
        log.info("Tests Skipped: {}", context.getSkippedTests().size());
    }
}
