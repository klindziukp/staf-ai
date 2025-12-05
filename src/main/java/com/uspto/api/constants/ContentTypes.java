package com.uspto.api.constants;

/**
 * Constants class for HTTP content types.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
public final class ContentTypes {

    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_XML = "application/xml";
    public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_HTML = "text/html";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * Private constructor to prevent instantiation.
     */
    private ContentTypes() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }
}
