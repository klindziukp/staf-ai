# USPTO Data Set API - Test Cases Documentation

## Test Case Summary

This document provides a comprehensive overview of all test cases implemented in the USPTO Data Set API Test Automation Framework.

---

## 📊 Test Statistics

- **Total Test Cases:** 30+
- **Smoke Tests:** 5
- **Functional Tests:** 15
- **Negative Tests:** 10
- **Schema Validation Tests:** 6

---

## 🔥 Smoke Tests

### TC-SMOKE-001: List Datasets - Success
- **Priority:** BLOCKER
- **Description:** Verify list datasets endpoint returns successful response
- **Endpoint:** GET /
- **Expected Result:** Status 200, valid JSON response with datasets list

### TC-SMOKE-002: List Datasets - Response Structure
- **Priority:** CRITICAL
- **Description:** Verify list datasets response contains required fields
- **Endpoint:** GET /
- **Expected Result:** Response contains 'total' and 'apis' fields with proper structure

### TC-SMOKE-003: List Datasets - Response Time
- **Priority:** NORMAL
- **Description:** Verify list datasets response time is acceptable
- **Endpoint:** GET /
- **Expected Result:** Response time < 5000ms

### TC-SMOKE-004: List Datasets - Default Dataset Exists
- **Priority:** CRITICAL
- **Description:** Verify specific dataset exists in the list
- **Endpoint:** GET /
- **Expected Result:** Default dataset 'oa_citations' exists in response

### TC-SMOKE-005: List Datasets - Valid JSON
- **Priority:** CRITICAL
- **Description:** Verify list datasets returns valid JSON
- **Endpoint:** GET /
- **Expected Result:** Response is valid JSON format

---

## ⚙️ Functional Tests

### List Fields Tests

#### TC-FUNC-001: List Fields - Valid Dataset and Version
- **Priority:** CRITICAL
- **Description:** Verify list fields endpoint with valid dataset and version
- **Endpoint:** GET /{dataset}/{version}/fields
- **Test Data:** dataset=oa_citations, version=v1
- **Expected Result:** Status 200, valid JSON with searchable fields

#### TC-FUNC-002: List Fields - OA Citations Dataset
- **Priority:** CRITICAL
- **Description:** Verify fields can be retrieved for oa_citations dataset
- **Endpoint:** GET /oa_citations/v1/fields
- **Expected Result:** Status 200, valid response with fields list

#### TC-FUNC-003: List Fields - Response Time
- **Priority:** NORMAL
- **Description:** Verify list fields response time is acceptable
- **Endpoint:** GET /{dataset}/{version}/fields
- **Expected Result:** Response time < 5000ms

#### TC-FUNC-004: List Fields - Invalid Dataset
- **Priority:** NORMAL
- **Description:** Verify invalid dataset returns 404
- **Endpoint:** GET /invalid_dataset/v1/fields
- **Expected Result:** Status 404 Not Found

#### TC-FUNC-005: List Fields - Invalid Version
- **Priority:** NORMAL
- **Description:** Verify invalid version returns 404
- **Endpoint:** GET /{dataset}/v999/fields
- **Expected Result:** Status 404 Not Found

#### TC-FUNC-006: List Fields - Special Characters
- **Priority:** MINOR
- **Description:** Verify API handles special characters in dataset name
- **Endpoint:** GET /test@#$%/v1/fields
- **Expected Result:** Status 404 or 400

#### TC-FUNC-007: List Fields - Valid JSON Format
- **Priority:** CRITICAL
- **Description:** Verify response is in valid JSON format
- **Endpoint:** GET /{dataset}/{version}/fields
- **Expected Result:** Valid JSON response

### Search Records Tests

#### TC-FUNC-008: Search Records - Wildcard Search
- **Priority:** CRITICAL
- **Description:** Verify search records with wildcard criteria
- **Endpoint:** POST /{dataset}/{version}/records
- **Request Body:** {"criteria": "*:*", "start": 0, "rows": 10}
- **Expected Result:** Status 200, results returned

#### TC-FUNC-009: Search Records - Pagination
- **Priority:** CRITICAL
- **Description:** Verify pagination works correctly
- **Endpoint:** POST /{dataset}/{version}/records
- **Request Body:** {"criteria": "*:*", "start": 0, "rows": 5}
- **Expected Result:** Status 200, max 5 records returned

#### TC-FUNC-010: Search Records - Form Parameters
- **Priority:** CRITICAL
- **Description:** Verify search works with form parameters
- **Endpoint:** POST /{dataset}/{version}/records
- **Content-Type:** application/x-www-form-urlencoded
- **Expected Result:** Status 200, results returned

#### TC-FUNC-011: Search Records - Zero Rows
- **Priority:** NORMAL
- **Description:** Verify rows=0 returns metadata but no documents
- **Endpoint:** POST /{dataset}/{version}/records
- **Request Body:** {"criteria": "*:*", "start": 0, "rows": 0}
- **Expected Result:** Status 200, 0 documents returned

#### TC-FUNC-012: Search Records - Large Rows Value
- **Priority:** NORMAL
- **Description:** Verify API handles large rows parameter
- **Endpoint:** POST /{dataset}/{version}/records
- **Request Body:** {"criteria": "*:*", "start": 0, "rows": 1000}
- **Expected Result:** Status 200, results returned

#### TC-FUNC-013: Search Records - Invalid Dataset
- **Priority:** NORMAL
- **Description:** Verify invalid dataset returns 404
- **Endpoint:** POST /invalid_dataset/v1/records
- **Expected Result:** Status 404 Not Found

#### TC-FUNC-014: Search Records - Response Time
- **Priority:** NORMAL
- **Description:** Verify search response time is acceptable
- **Endpoint:** POST /{dataset}/{version}/records
- **Expected Result:** Response time < 10000ms

#### TC-FUNC-015: Search Records - Valid JSON Format
- **Priority:** CRITICAL
- **Description:** Verify response is in valid JSON format
- **Endpoint:** POST /{dataset}/{version}/records
- **Expected Result:** Valid JSON response

---

## ❌ Negative Tests

### TC-NEG-001: List Fields - Empty Dataset Name
- **Priority:** NORMAL
- **Description:** Verify API handles empty dataset name
- **Endpoint:** GET //v1/fields
- **Expected Result:** Status 404 or 400

### TC-NEG-002: List Fields - Empty Version
- **Priority:** NORMAL
- **Description:** Verify API handles empty version
- **Endpoint:** GET /{dataset}//fields
- **Expected Result:** Status 404 or 400

### TC-NEG-003: Search Records - Null Criteria
- **Priority:** NORMAL
- **Description:** Verify API handles null search criteria
- **Request Body:** {"criteria": null, "start": 0, "rows": 10}
- **Expected Result:** Status 200 or 400

### TC-NEG-004: Search Records - Negative Start Value
- **Priority:** NORMAL
- **Description:** Verify API handles negative start value
- **Request Body:** {"criteria": "*:*", "start": -1, "rows": 10}
- **Expected Result:** Status 200 or 400

### TC-NEG-005: Search Records - Negative Rows Value
- **Priority:** NORMAL
- **Description:** Verify API handles negative rows value
- **Request Body:** {"criteria": "*:*", "start": 0, "rows": -10}
- **Expected Result:** Status 200 or 400

### TC-NEG-006: Search Records - Extremely Large Start
- **Priority:** MINOR
- **Description:** Verify API handles extremely large start value
- **Request Body:** {"criteria": "*:*", "start": 2147483647, "rows": 10}
- **Expected Result:** Status 200 or 400

### TC-NEG-007: List Fields - SQL Injection Attempt
- **Priority:** CRITICAL
- **Description:** Verify API is protected against SQL injection
- **Endpoint:** GET /test' OR '1'='1/v1/fields
- **Expected Result:** Status 404 or 400 (not 500)

### TC-NEG-008: Search Records - Malformed Criteria
- **Priority:** NORMAL
- **Description:** Verify API handles malformed search criteria
- **Request Body:** {"criteria": "}{invalid json}{", "start": 0, "rows": 10}
- **Expected Result:** Status 200 or 400

### TC-NEG-009: List Fields - Very Long Dataset Name
- **Priority:** MINOR
- **Description:** Verify API handles very long dataset name
- **Endpoint:** GET /{1000_character_string}/v1/fields
- **Expected Result:** Status 404 or 400

### TC-NEG-010: Search Records - Invalid Combination
- **Priority:** NORMAL
- **Description:** Verify API handles invalid dataset/version combination
- **Endpoint:** POST /invalid_dataset/invalid_version/records
- **Expected Result:** Status 404

---

## 📋 Schema Validation Tests

### TC-SCHEMA-001: List Datasets - Schema Validation
- **Priority:** CRITICAL
- **Description:** Verify list datasets response matches expected schema
- **Validation:** total (integer), apis (array)

### TC-SCHEMA-002: Search Records - Schema Validation
- **Priority:** CRITICAL
- **Description:** Verify search response matches expected schema
- **Validation:** response.numFound, response.start, response.docs

### TC-SCHEMA-003: List Datasets - API Info Schema
- **Priority:** CRITICAL
- **Description:** Verify each API info object contains required fields
- **Validation:** apiKey, apiVersionNumber, apiUrl, apiDocumentationUrl

### TC-SCHEMA-004: Search Records - Response Header Schema
- **Priority:** NORMAL
- **Description:** Verify response header contains expected fields
- **Validation:** responseHeader.status, responseHeader.QTime

### TC-SCHEMA-005: List Datasets - Total Matches Count
- **Priority:** NORMAL
- **Description:** Verify total field matches actual count of APIs
- **Validation:** total == apis.length

### TC-SCHEMA-006: Search Records - Docs Count Consistency
- **Priority:** NORMAL
- **Description:** Verify docs count matches returned count
- **Validation:** docs.length <= requested rows

---

## 🎯 Test Execution Priority

### Priority Levels

1. **BLOCKER** - Must pass for release
   - TC-SMOKE-001

2. **CRITICAL** - Core functionality
   - TC-SMOKE-002, TC-SMOKE-004, TC-SMOKE-005
   - TC-FUNC-001, TC-FUNC-002, TC-FUNC-007
   - TC-FUNC-008, TC-FUNC-009, TC-FUNC-010, TC-FUNC-015
   - TC-NEG-007
   - TC-SCHEMA-001, TC-SCHEMA-002, TC-SCHEMA-003

3. **NORMAL** - Important functionality
   - TC-SMOKE-003
   - TC-FUNC-003, TC-FUNC-004, TC-FUNC-005
   - TC-FUNC-011, TC-FUNC-012, TC-FUNC-013, TC-FUNC-014
   - TC-NEG-001 through TC-NEG-006, TC-NEG-008, TC-NEG-010
   - TC-SCHEMA-004, TC-SCHEMA-005, TC-SCHEMA-006

4. **MINOR** - Edge cases
   - TC-FUNC-006
   - TC-NEG-006, TC-NEG-009

---

## 📈 Test Coverage Matrix

| Endpoint | Positive Tests | Negative Tests | Schema Tests | Total |
|----------|---------------|----------------|--------------|-------|
| GET / | 5 | 0 | 3 | 8 |
| GET /{dataset}/{version}/fields | 4 | 5 | 0 | 9 |
| POST /{dataset}/{version}/records | 8 | 5 | 3 | 16 |
| **Total** | **17** | **10** | **6** | **33** |

---

## 🔄 Test Execution Flow

1. **Smoke Tests** - Quick validation of critical paths
2. **Functional Tests** - Detailed feature testing
3. **Negative Tests** - Error handling validation
4. **Schema Validation Tests** - Response structure validation

---

## 📝 Notes

- All tests include response time validation
- All tests validate JSON format
- All tests include comprehensive logging
- All tests are integrated with Allure reporting
- Tests can be executed in parallel
- Tests are data-driven where applicable

---

**Last Updated:** 2024
**Framework Version:** 1.0.0
