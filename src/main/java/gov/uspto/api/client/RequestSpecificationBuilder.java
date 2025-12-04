package gov.uspto.api.client;

import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder for creating customized request specifications
 */
public class RequestSpecificationBuilder {
    private static final Logger log = LoggerFactory.getLogger(RequestSpecificationBuilder.class);
    private final RequestSpecification requestSpec;
    private final Map<String, Object> headers;
    private final Map<String, Object> queryParams;
    private final Map<String, Object> pathParams;

    public RequestSpecificationBuilder() {
        this.requestSpec = ApiClient.createNewRequestSpec();
        this.headers = new HashMap<>();
        this.queryParams = new HashMap<>();
        this.pathParams = new HashMap<>();
    }

    public RequestSpecificationBuilder addHeader(String key, String value) {
        headers.put(key, value);
        log.debug("Added header: {} = {}", key, value);
        return this;
    }

    public RequestSpecificationBuilder addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        log.debug("Added headers: {}", headers);
        return this;
    }

    public RequestSpecificationBuilder addQueryParam(String key, Object value) {
        queryParams.put(key, value);
        log.debug("Added query parameter: {} = {}", key, value);
        return this;
    }

    public RequestSpecificationBuilder addQueryParams(Map<String, Object> params) {
        this.queryParams.putAll(params);
        log.debug("Added query parameters: {}", params);
        return this;
    }

    public RequestSpecificationBuilder addPathParam(String key, Object value) {
        pathParams.put(key, value);
        log.debug("Added path parameter: {} = {}", key, value);
        return this;
    }

    public RequestSpecificationBuilder addPathParams(Map<String, Object> params) {
        this.pathParams.putAll(params);
        log.debug("Added path parameters: {}", params);
        return this;
    }

    public RequestSpecification build() {
        if (!headers.isEmpty()) {
            requestSpec.headers(headers);
        }
        if (!queryParams.isEmpty()) {
            requestSpec.queryParams(queryParams);
        }
        if (!pathParams.isEmpty()) {
            requestSpec.pathParams(pathParams);
        }
        return requestSpec;
    }
}
