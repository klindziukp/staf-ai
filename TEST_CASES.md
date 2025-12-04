# USPTO Data Set API - Test Cases Documentation

## Test Case Summary

| Test Suite | Total Tests | Priority | Status |
|------------|-------------|----------|--------|
| DataSetTests | 6 | HIGH | ✅ |
| FieldsTests | 5 | HIGH | ✅ |
| RecordsTests | 10 | HIGH | ✅ |
| NegativeDataSetTests | 8 | MEDIUM | ✅ |
| NegativeFieldsTests | 12 | MEDIUM | ✅ |
| NegativeRecordsTests | 15 | MEDIUM | ✅ |
| IntegrationTests | 11 | HIGH | ✅ |
| PerformanceTests | 10 | MEDIUM | ✅ |
| **TOTAL** | **77** | - | **✅** |

---

## 1. DataSetTests - Positive Scenarios

### TC-DS-001: Verify list of available datasets returns 200 OK
- **Priority**: CRITICAL
- **Description**: Test to verify that GET / endpoint returns 200 status code and valid response
- **Preconditions**: API is accessible
- **Test Steps**:
  1. Send GET request to `/`
  2. Verify status code is 200
  3. Verify content type is application/json
  4. Verify response contains 'total' field
- **Expected Result**: Status code 200, valid JSON response with total field
- **Test Data**: None

### TC-DS-002: Verify datasets response contains required fields
- **Priority**: CRITICAL
- **Description**: Validate that response structure contains all required fields
- **Preconditions**: API is accessible
- **Test Steps**:
  1. Send GET request to `/`
  2. Parse response as DataSetListResponse object
  3. Verify total is not null and >= 0
  4. Verify apis list is not empty
  5. For each dataset verify: apiKey, apiVersionNumber, apiUrl are present
- **Expected Result**: All required fields present and valid
- **Test Data**: None

### TC-DS-003: Verify response time is within acceptable limits
- **Priority**: NORMAL
- **Description**: Ensure API responds within expected time frame
- **Preconditions**: API is accessible
- **Test Steps**:
  1. Send GET request to `/`
  2. Measure response time
  3. Compare with expected response time from config
- **Expected Result**: Response time < 5000ms (configurable)
- **Test Data**: None

### TC-DS-004: Verify specific dataset exists in the list
- **Priority**: NORMAL
- **Description**: Confirm that known datasets are present in the response
- **Preconditions**: API is accessible
- **Test Steps**:
  1. Send GET request to `/`
  2. Parse response
  3. Search for specific dataset (e.g., "oa_citations")
- **Expected Result**: Dataset "oa_citations" found in the list
- **Test Data**: expectedDataset = "oa_citations"

### TC-DS-005: Verify datasets list is not empty
- **Priority**: CRITICAL
- **Description**: Ensure at least one dataset is available
- **Preconditions**: API is accessible
- **Test Steps**:
  1. Send GET request to `/`
  2. Verify total > 0
  3. Verify apis list size matches total
- **Expected Result**: Total > 0, apis list not empty
- **Test Data**: None

### TC-DS-006: Verify each dataset has valid URL format
- **Priority**: NORMAL
- **Description**: Validate URL format for all datasets
- **Preconditions**: API is accessible
- **Test Steps**:
  1. Send GET request to `/`
  2. For each dataset verify:
     - apiUrl starts with "http"
     - apiUrl contains "developer.uspto.gov"
     - apiDocumentationUrl (if present) starts with "http"
- **Expected Result**: All URLs are valid and properly formatted
- **Test Data**: None

---

## 2. FieldsTests - Positive Scenarios

### TC-FLD-001: Verify get fields returns 200 OK for valid dataset
- **Priority**: CRITICAL
- **Description**: Test that fields endpoint returns success for valid dataset/version
- **Preconditions**: Dataset "oa_citations" version "v1" exists
- **Test Steps**:
  1. Send GET request to `/oa_citations/v1/fields`
  2. Verify status code is 200
  3. Verify content type is application/json
- **Expected Result**: Status code 200, valid JSON response
- **Test Data**: dataset = "oa_citations", version = "v1"

### TC-FLD-002: Verify fields response is not empty
- **Priority**: CRITICAL
- **Description**: Ensure fields list contains data
- **Preconditions**: Dataset "oa_citations" version "v1" exists
- **Test Steps**:
  1. Send GET request to `/oa_citations/v1/fields`
  2. Verify response contains fields
  3. Verify fields list is not empty
- **Expected Result**: Fields list contains at least one field
- **Test Data**: dataset = "oa_citations", version = "v1"

### TC-FLD-003: Verify fields response time
- **Priority**: NORMAL
- **Description**: Ensure fields endpoint responds quickly
- **Preconditions**: Dataset "oa_citations" version "v1" exists
- **Test Steps**:
  1. Send GET request to `/oa_citations/v1/fields`
  2. Measure response time
  3. Compare with expected time
- **Expected Result**: Response time < 5000ms
- **Test Data**: dataset = "oa_citations", version = "v1"

### TC-FLD-004: Verify fields for different datasets
- **Priority**: NORMAL
- **Description**: Test fields endpoint for multiple datasets
- **Preconditions**: Multiple datasets exist
- **Test Steps**:
  1. Get list of available datasets
  2. For each dataset, request fields
  3. Verify each returns 200 OK
- **Expected Result**: All datasets return valid fields
- **Test Data**: All available datasets

### TC-FLD-005: Verify field metadata structure
- **Priority**: NORMAL
- **Description**: Validate structure of field information
- **Preconditions**: Dataset "oa_citations" version "v1" exists
- **Test Steps**:
  1. Send GET request to `/oa_citations/v1/fields`
  2. Verify each field has required properties
  3. Verify field types are valid
- **Expected Result**: All fields have valid metadata
- **Test Data**: dataset = "oa_citations", version = "v1"

---

## 3. RecordsTests - Positive Scenarios

### TC-REC-001: Verify search with default criteria returns 200 OK
- **Priority**: CRITICAL
- **Description**: Test basic search functionality
- **Preconditions**: Dataset "oa_citations" version "v1" exists
- **Test Steps**:
  1. Send POST request to `/oa_citations/v1/records`
  2. Body: {criteria: "*:*", start: 0, rows: 10}
  3. Verify status code is 200
- **Expected Result**: Status code 200, valid search results
- **Test Data**: criteria = "*:*", start = 0, rows = 10

### TC-REC-002: Verify pagination with different start positions
- **Priority**: HIGH
- **Description**: Test pagination functionality
- **Preconditions**: Dataset has sufficient records
- **Test Steps**:
  1. Search with start=0, rows=10
  2. Search with start=10, rows=10
  3. Search with start=20, rows=10
  4. Verify each returns different results
- **Expected Result**: Different pages return different records
- **Test Data**: Multiple start positions (0, 10, 20)

### TC-REC-003: Verify different rows parameter values
- **Priority**: NORMAL
- **Description**: Test different page sizes
- **Preconditions**: Dataset has sufficient records
- **Test Steps**:
  1. Search with rows=1
  2. Search with rows=10
  3. Search with rows=50
  4. Search with rows=100
  5. Verify appropriate number of results returned
- **Expected Result**: Results match requested rows
- **Test Data**: rows = [1, 10, 50, 100]

### TC-REC-004: Verify wildcard search
- **Priority**: HIGH
- **Description**: Test wildcard query functionality
- **Preconditions**: Dataset exists
- **Test Steps**:
  1. Send search with criteria "*:*"
  2. Verify results returned
  3. Verify numFound > 0
- **Expected Result**: All records returned (up to rows limit)
- **Test Data**: criteria = "*:*"

### TC-REC-005: Verify specific field search
- **Priority**: HIGH
- **Description**: Test field-specific queries
- **Preconditions**: Dataset exists with known field
- **Test Steps**:
  1. Send search with criteria "fieldName:value"
  2. Verify results match criteria
- **Expected Result**: Only matching records returned
- **Test Data**: criteria = "fieldName:specificValue"

### TC-REC-006: Verify range query
- **Priority**: NORMAL
- **Description**: Test range query syntax
- **Preconditions**: Dataset exists with numeric field
- **Test Steps**:
  1. Send search with criteria "field:[100 TO 200]"
  2. Verify results within range
- **Expected Result**: Results match range criteria
- **Test Data**: criteria = "numericField:[100 TO 200]"

### TC-REC-007: Verify date range query
- **Priority**: NORMAL
- **Description**: Test date range queries
- **Preconditions**: Dataset exists with date field
- **Test Steps**:
  1. Send search with criteria "dateField:[20200101 TO 20201231]"
  2. Verify results within date range
- **Expected Result**: Results match date range
- **Test Data**: criteria = "dateField:[20200101 TO 20201231]"

### TC-REC-008: Verify boolean operators (AND, OR, NOT)
- **Priority**: HIGH
- **Description**: Test Lucene boolean operators
- **Preconditions**: Dataset exists
- **Test Steps**:
  1. Test with AND operator
  2. Test with OR operator
  3. Test with NOT operator
  4. Verify results match logic
- **Expected Result**: Boolean logic applied correctly
- **Test Data**: Various boolean queries

### TC-REC-009: Verify response structure
- **Priority**: CRITICAL
- **Description**: Validate search response structure
- **Preconditions**: Dataset exists
- **Test Steps**:
  1. Send search request
  2. Verify response contains: numFound, docs, start
  3. Verify docs is array
- **Expected Result**: Response structure matches schema
- **Test Data**: criteria = "*:*"

### TC-REC-010: Verify empty results handling
- **Priority**: NORMAL
- **Description**: Test behavior when no results found
- **Preconditions**: Dataset exists
- **Test Steps**:
  1. Send search with criteria that returns no results
  2. Verify status code is 200
  3. Verify numFound = 0
  4. Verify docs is empty array
- **Expected Result**: Empty results handled gracefully
- **Test Data**: criteria = "nonExistentField:impossibleValue"

---

## 4. NegativeDataSetTests - Negative Scenarios

### TC-DS-NEG-001: Verify POST method is not allowed
- **Priority**: NORMAL
- **Description**: Test that POST method returns error
- **Test Steps**:
  1. Send POST request to `/`
  2. Verify status code is 405 or 404
- **Expected Result**: Method not allowed error
- **Test Data**: None

### TC-DS-NEG-002: Verify PUT method is not allowed
- **Priority**: NORMAL
- **Description**: Test that PUT method returns error
- **Test Steps**:
  1. Send PUT request to `/`
  2. Verify status code is 405 or 404
- **Expected Result**: Method not allowed error
- **Test Data**: None

### TC-DS-NEG-003: Verify DELETE method is not allowed
- **Priority**: NORMAL
- **Description**: Test that DELETE method returns error
- **Test Steps**:
  1. Send DELETE request to `/`
  2. Verify status code is 405 or 404
- **Expected Result**: Method not allowed error
- **Test Data**: None

### TC-DS-NEG-004: Verify invalid Accept header handling
- **Priority**: MINOR
- **Description**: Test behavior with invalid Accept header
- **Test Steps**:
  1. Send GET request with Accept: application/xml
  2. Verify response (200 or 406)
- **Expected Result**: Handled gracefully
- **Test Data**: Accept = "application/xml"

### TC-DS-NEG-005: Verify malformed URL handling
- **Priority**: NORMAL
- **Description**: Test malformed URL paths
- **Test Steps**:
  1. Send GET request to "//invalid//path"
  2. Verify error response (400 or 404)
- **Expected Result**: Error returned
- **Test Data**: path = "//invalid//path"

### TC-DS-NEG-006: Verify special characters in URL
- **Priority**: MINOR
- **Description**: Test XSS attempt in URL
- **Test Steps**:
  1. Send GET request with special characters
  2. Verify safe handling
- **Expected Result**: No XSS vulnerability
- **Test Data**: path = "/<script>alert('xss')</script>"

### TC-DS-NEG-007: Verify extremely long URL
- **Priority**: MINOR
- **Description**: Test URL length limits
- **Test Steps**:
  1. Send GET request with 10000 character path
  2. Verify error (400, 404, or 414)
- **Expected Result**: Request rejected
- **Test Data**: path = "/" + "a" * 10000

### TC-DS-NEG-008: Verify SQL injection attempt
- **Priority**: CRITICAL
- **Description**: Test SQL injection protection
- **Test Steps**:
  1. Send GET request with SQL injection attempt
  2. Verify safe handling
- **Expected Result**: No SQL injection vulnerability
- **Test Data**: path = "/' OR '1'='1"

---

## 5. NegativeFieldsTests - Negative Scenarios

### TC-FLD-NEG-001: Verify 404 for non-existent dataset
- **Priority**: CRITICAL
- **Description**: Test error handling for invalid dataset
- **Test Steps**:
  1. Send GET request to `/invalid_dataset/v1/fields`
  2. Verify status code is 404
- **Expected Result**: 404 Not Found
- **Test Data**: dataset = "non_existent_dataset_12345"

### TC-FLD-NEG-002: Verify 404 for non-existent version
- **Priority**: CRITICAL
- **Description**: Test error handling for invalid version
- **Test Steps**:
  1. Send GET request to `/oa_citations/v999/fields`
  2. Verify status code is 404
- **Expected Result**: 404 Not Found
- **Test Data**: version = "v999"

### TC-FLD-NEG-003 to TC-FLD-NEG-012: Various Invalid Input Tests
- Empty dataset name
- Dataset with spaces
- Dataset with special characters
- Dataset with slashes
- XSS attempts
- SQL injection attempts
- Path traversal attempts
- Null byte injection
- Extremely long dataset names
- Unicode characters

---

## 6. NegativeRecordsTests - Negative Scenarios

### TC-REC-NEG-001: Verify 404 for non-existent dataset
- **Priority**: CRITICAL
- **Description**: Test search on invalid dataset
- **Test Steps**:
  1. Send POST request to `/invalid_dataset/v1/records`
  2. Verify status code is 404
- **Expected Result**: 404 Not Found
- **Test Data**: dataset = "non_existent_dataset"

### TC-REC-NEG-002: Verify 404 for non-existent version
- **Priority**: CRITICAL
- **Description**: Test search on invalid version
- **Test Steps**:
  1. Send POST request to `/oa_citations/v999/records`
  2. Verify status code is 404
- **Expected Result**: 404 Not Found
- **Test Data**: version = "v999"

### TC-REC-NEG-003 to TC-REC-NEG-015: Invalid Query Tests
- Invalid Lucene syntax (unclosed parentheses, brackets, etc.)
- Negative pagination values
- Zero rows
- Extremely large pagination values
- Missing required criteria
- Empty criteria
- SQL injection in criteria
- XSS in criteria
- Extremely long criteria
- Special characters
- Unicode characters
- Null byte injection
- Maximum rows limit testing
- Invalid Content-Type header

---

## 7. IntegrationTests - End-to-End Scenarios

### TC-INT-001: Complete workflow test
- **Priority**: CRITICAL
- **Description**: Test complete API workflow
- **Test Steps**:
  1. List datasets
  2. Get fields for first dataset
  3. Search records in that dataset
  4. Verify all steps succeed
- **Expected Result**: Complete workflow succeeds
- **Test Data**: Dynamic (uses first available dataset)

### TC-INT-002: Verify all datasets have accessible fields
- **Priority**: CRITICAL
- **Description**: Validate all datasets support fields endpoint
- **Test Steps**:
  1. Get list of all datasets
  2. For each dataset, request fields
  3. Verify all return 200 OK
- **Expected Result**: All datasets accessible
- **Test Data**: All available datasets

### TC-INT-003: Verify all datasets support search
- **Priority**: CRITICAL
- **Description**: Validate all datasets support search
- **Test Steps**:
  1. Get list of all datasets
  2. For each dataset, perform search
  3. Verify all return 200 OK
- **Expected Result**: All datasets searchable
- **Test Data**: All available datasets

### TC-INT-004 to TC-INT-011: Additional Integration Tests
- Pagination integration
- Metadata consistency
- Response structure validation
- Different search criteria
- Concurrent requests
- API version consistency
- Empty search results handling

---

## 8. PerformanceTests - Performance Scenarios

### TC-PERF-001: Dataset list response time
- **Priority**: NORMAL
- **Description**: Measure dataset list endpoint performance
- **Test Steps**:
  1. Send GET request to `/`
  2. Measure response time
  3. Verify < 5000ms
- **Expected Result**: Response time acceptable
- **Test Data**: None

### TC-PERF-002: Fields endpoint response time
- **Priority**: NORMAL
- **Description**: Measure fields endpoint performance
- **Test Steps**:
  1. Send GET request to fields endpoint
  2. Measure response time
  3. Verify < 5000ms
- **Expected Result**: Response time acceptable
- **Test Data**: dataset = "oa_citations", version = "v1"

### TC-PERF-003: Search small result set response time
- **Priority**: NORMAL
- **Description**: Measure search performance with 10 rows
- **Test Steps**:
  1. Search with rows=10
  2. Measure response time
  3. Verify < 5000ms
- **Expected Result**: Response time acceptable
- **Test Data**: rows = 10

### TC-PERF-004: Search large result set response time
- **Priority**: NORMAL
- **Description**: Measure search performance with 100 rows
- **Test Steps**:
  1. Search with rows=100
  2. Measure response time
  3. Verify < 10000ms
- **Expected Result**: Response time acceptable
- **Test Data**: rows = 100

### TC-PERF-005 to TC-PERF-010: Additional Performance Tests
- Consecutive requests performance
- Repeated searches stability
- Pagination performance
- Complex query performance
- Wildcard search performance
- Response time consistency

---

## Test Execution Summary

### Coverage by Priority
- **CRITICAL**: 25 tests
- **HIGH**: 15 tests
- **NORMAL**: 30 tests
- **MINOR**: 7 tests

### Coverage by Type
- **Functional**: 21 tests (27%)
- **Negative**: 35 tests (45%)
- **Integration**: 11 tests (14%)
- **Performance**: 10 tests (13%)

### Expected Pass Rate
- **Target**: 100%
- **Minimum Acceptable**: 95%

---

## Test Data Requirements

### Datasets Required
- `oa_citations` (v1) - Primary test dataset
- `cancer_moonshot` (v1) - Secondary test dataset

### Test Environment
- **Base URL**: https://developer.uspto.gov/ds-api
- **Network**: Internet access required
- **Authentication**: None (public API)

---

## Defect Tracking

| Defect ID | Test Case | Severity | Status | Description |
|-----------|-----------|----------|--------|-------------|
| - | - | - | - | No defects currently tracked |

---

**Document Version**: 1.0  
**Last Updated**: 2025-01-XX  
**Author**: Test Automation Team
