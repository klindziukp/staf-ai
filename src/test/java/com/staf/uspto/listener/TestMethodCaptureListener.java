package com.staf.uspto.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

/**
 * TestNG listener to capture and log test method execution details.
 * This listener is invoked before and after each test method execution.
 */
@Slf4j
public class TestMethodCaptureListener implements IInvokedMethodListener {
    
    private static final String TEST_METHOD_START = "========== Starting Test Method: {} ==========";
    private static final String TEST_METHOD_END = "========== Finished Test Method: {} - Status: {} ==========";
    private static final String TEST_METHOD_DURATION = "Test Method Duration: {} ms";
    
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            String methodName = method.getTestMethod().getMethodName();
            log.info(TEST_METHOD_START, methodName);
            testResult.setAttribute("startTime", System.currentTimeMillis());
        }
    }
    
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            String methodName = method.getTestMethod().getMethodName();
            String status = getTestStatus(testResult);
            
            Long startTime = (Long) testResult.getAttribute("startTime");
            if (startTime != null) {
                long duration = System.currentTimeMillis() - startTime;
                log.info(TEST_METHOD_DURATION, duration);
            }
            
            log.info(TEST_METHOD_END, methodName, status);
            
            if (testResult.getThrowable() != null) {
                log.error("Test Method Failed with exception:", testResult.getThrowable());
            }
        }
    }
    
    /**
     * Gets the test execution status as a string.
     *
     * @param testResult the test result
     * @return status string
     */
    private String getTestStatus(ITestResult testResult) {
        return switch (testResult.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP -> "SKIPPED";
            default -> "UNKNOWN";
        };
    }
}
