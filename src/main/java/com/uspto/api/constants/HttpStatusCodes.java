package com.uspto.api.constants;

/**
 * Constants class for HTTP status codes used in API testing.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
public final class HttpStatusCodes {

    // Success Codes
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NO_CONTENT = 204;

    // Client Error Codes
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int CONFLICT = 409;
    public static final int UNPROCESSABLE_ENTITY = 422;
    public static final int TOO_MANY_REQUESTS = 429;

    // Server Error Codes
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    /**
     * Private constructor to prevent instantiation.
     */
    private HttpStatusCodes() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }
}
