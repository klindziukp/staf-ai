package com.petstore.automation.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG test listener for logging test method events.
 * 
 * @author Petstore Automation Team
 * @version 1.0
 */
@Slf4j
public class TestMethodCaptureListener implements ITestListener {
    
    @Override
    public void onTestStart(ITestResult result) {
        log.info("Test Started: {}.{}", 
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
        log.error("Failure reason: {}", result.getThrowable().getMessage());
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test SKIPPED: {}.{}", 
                result.getTestClass().getName(), 
                result.getMethod().getMethodName());
    }
    
    @Override
    public void onStart(ITestContext context) {
        log.info("Test Context Started: {}", context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        log.info("Test Context Finished: {}", context.getName());
        log.info("Tests Passed: {}", context.getPassedTests().size());
        log.info("Tests Failed: {}", context.getFailedTests().size());
        log.info("Tests Skipped: {}", context.getSkippedTests().size());
    }
}
