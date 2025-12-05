package com.klindziuk.staf.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

/**
 * TestNG listener to capture test method execution details.
 * Logs test method start and completion with execution time.
 */
@Slf4j
public class TestMethodCaptureListener implements IInvokedMethodListener {

    private static final String TEST_METHOD_START = "Starting test method: {}";
    private static final String TEST_METHOD_SUCCESS = "Test method {} completed successfully in {} ms";
    private static final String TEST_METHOD_FAILURE = "Test method {} failed in {} ms";
    private static final String TEST_METHOD_SKIP = "Test method {} was skipped";

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            String methodName = getMethodName(method);
            log.info(TEST_METHOD_START, methodName);
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            String methodName = getMethodName(method);
            long executionTime = testResult.getEndMillis() - testResult.getStartMillis();

            switch (testResult.getStatus()) {
                case ITestResult.SUCCESS:
                    log.info(TEST_METHOD_SUCCESS, methodName, executionTime);
                    break;
                case ITestResult.FAILURE:
                    log.error(TEST_METHOD_FAILURE, methodName, executionTime);
                    log.error("Failure reason: ", testResult.getThrowable());
                    break;
                case ITestResult.SKIP:
                    log.warn(TEST_METHOD_SKIP, methodName);
                    break;
                default:
                    log.warn("Test method {} finished with unknown status: {}", methodName, testResult.getStatus());
            }
        }
    }

    /**
     * Extracts the full method name including class name.
     *
     * @param method the invoked method
     * @return formatted method name
     */
    private String getMethodName(IInvokedMethod method) {
        return method.getTestMethod().getTestClass().getName() + "." + 
               method.getTestMethod().getMethodName();
    }
}
