# Test Documentation - Week5 Assessment

## Overview
This document provides comprehensive coverage of all test classes in the Week5 Assessment project, organized by controller and test scenario.

---

## Test Class Summary

### 1. AuthenticationControllerTest (`controller/AuthenticationControllerTest.java`)
**Purpose**: Verify authentication endpoints (Register & Login) handle all scenarios correctly.

**Register Endpoint (POST /api/auth/register)**
- ✅ Happy Path: Valid payload → 201 CREATED + customer response
  - `verify(authenticationService).register(any())` called exactly once
- ❌ Duplicate Email: → 409 CONFLICT
  - `verify(authenticationService).register(any())` called once
- ❌ Bad Request Cases: → 400 BAD_REQUEST with `verifyNoInteractions(authenticationService)`
  - Missing: name, password, phoneNumber
  - Invalid: email format
  - Blank: fields (spaces only)
  - Phone validation: must be exactly 10 digits
  - Malformed JSON, null body, unsupported media type

**Login Endpoint (POST /api/auth/login)**
- ✅ Happy Path: Valid credentials → 200 OK + JWT token
  - `verify(authenticationService).login(any())` called exactly once
- ❌ Invalid Credentials: → 401 UNAUTHORIZED
  - `verify(authenticationService).login(any())` called once
- ❌ Bad Request Cases: → 400 BAD_REQUEST with `verifyNoInteractions(authenticationService)`
  - Missing: email, password
  - Blank: credentials (spaces only)
  - Malformed JSON, null body, unsupported media type

---

### 2. AuthControllerTest (`controller/AuthControllerTest.java`)
**Purpose**: Verify Auth Controller register and login with DTO-based requests.

**Register Endpoint (POST /auth/register)**
- ✅ Happy Path: Valid request → 201 CREATED + JWT token
  - `verify(authService).register(request)` + `verifyNoMoreInteractions(authService)`
- ❌ Duplicate Email: → 409 CONFLICT
  - `verify(authService).register(request)` called once
- ❌ Bad Request Cases: → 400 BAD_REQUEST with `verifyNoInteractions(authService)`
  - Blank: name, password
  - Invalid: email format
  - Short: password (< minimum length)
  - Missing: mandatory fields
  - Malformed JSON, whitespace-only values, null body

**Login Endpoint (POST /auth/login)**
- ✅ Happy Path: Valid credentials → 200 OK + JWT token
  - `verify(authService).login(request)` + `verifyNoMoreInteractions(authService)`
- ❌ Bad Credentials: → 401 UNAUTHORIZED
  - `verify(authService).login(request)` called once
- ❌ Bad Request Cases: → 400 BAD_REQUEST with `verifyNoInteractions(authService)`
  - Blank: email, password
  - Invalid: email format
  - Missing: fields
  - Malformed JSON, null body, trailing comma JSON

---

### 3. CustomerControllerTest (`controller/CustomerControllerTest.java`)
**Purpose**: Verify CRUD operations on Customer resource with proper validation and error handling.

**CREATE (POST /api/customers)**
- ✅ Happy Path: Valid payload → 201 CREATED + customer details
  - `verify(customerService).createCustomer(any())`
- ❌ Duplicate Email: → 409 CONFLICT
- ❌ Bad Request Cases: → 400 BAD_REQUEST with `verifyNoInteractions(customerService)`
  - Invalid: email format
  - Blank: name
  - Invalid: phone number length
  - Missing: request body
  - Malformed JSON, unsupported media type

**LIST (GET /api/customers)**
- ✅ Retrieve all: → 200 OK + list (or empty array)
  - `verify(customerService).getAllCustomers()`

**GET BY ID (GET /api/customers/{id})**
- ✅ Found: → 200 OK + customer details
  - `verify(customerService).getCustomerById(1L)`
- ❌ Not Found (404L): → 404 NOT_FOUND
- ❌ Invalid Path Variable (non-numeric): → 400 BAD_REQUEST with `verifyNoInteractions(customerService)`

**UPDATE (PUT /api/customers/{id})**
- ✅ Valid request: → 200 OK + updated customer
  - `verify(customerService).updateCustomer(eq(1L), any())`
- ❌ Not Found (404): → 404 NOT_FOUND
- ❌ Duplicate Email: → 409 CONFLICT
- ❌ Bad Request Cases: → 400 BAD_REQUEST with `verifyNoInteractions(customerService)`
  - Invalid: email format
  - Invalid: path variable (non-numeric)
  - Malformed JSON, unsupported media type

**DELETE (DELETE /api/customers/{id})**
- ✅ Successful delete: → 204 NO_CONTENT
  - `verify(customerService).deleteCustomer(1L)`
- ❌ Not Found (404): → 404 NOT_FOUND
- ❌ Invalid Path Variable: → 400 BAD_REQUEST with `verifyNoInteractions(customerService)`

---

### 4. AccountControllerTest (`controller/AccountControllerTest.java`)
**Purpose**: Verify Account management endpoints with JWT security and transaction handling.

**CREATE (POST /accounts) [REQUIRES JWT]**
- ✅ Valid request + JWT: → 201 CREATED + account details
  - `verify(accountService).createAccount(request)`
- ❌ Bad Request Cases: → 400 BAD_REQUEST with `verifyNoInteractions(accountService)`
  - Null: customerId, accountType
  - Blank: accountType
  - Invalid type: customerId (string instead of number)
  - Malformed JSON
- ❌ Customer Not Found: → 404 NOT_FOUND
- ❌ Unsupported Account Type: → 400 BAD_REQUEST
- ❌ Security: 401 (missing JWT), 403 (wrong role)

**GET (GET /accounts/{accountNumber}) [REQUIRES JWT]**
- ✅ Valid account + JWT: → 200 OK + account + balance (decimal precision)
  - `verify(accountService).getAccountByNumber("SB-1001")`
- ❌ Not Found: → 404 NOT_FOUND
- ❌ Blank Account Number: → 400 BAD_REQUEST
- ❌ Security: 401 (missing JWT), 403 (wrong role)

**DEPOSIT (POST /accounts/{accountNumber}/deposit) [REQUIRES JWT]**
- ✅ Valid amount + JWT: → 200 OK + updated balance
  - `verify(accountService).deposit("SB-1001", bd("50.00"))`
- ❌ Bad Request Cases: → 400 BAD_REQUEST with `verifyNoInteractions(accountService)`
  - Zero: amount
  - Negative: amount
  - Missing: amount field
  - Null: amount
  - Invalid type: amount (string)
  - Malformed JSON
- ❌ Account Not Found: → 404 NOT_FOUND
- ❌ Security: 401 (missing JWT), 403 (wrong role)

**WITHDRAW (POST /accounts/{accountNumber}/withdraw) [REQUIRES JWT]**
- ✅ Valid amount + JWT: → 200 OK + updated balance
  - `verify(accountService).withdraw("SB-1001", bd("20.00"))`
- ❌ Insufficient Funds: → 422 UNPROCESSABLE_ENTITY
- ❌ Bad Request Cases: → 400 BAD_REQUEST with `verifyNoInteractions(accountService)`
  - Zero: amount
  - Negative: amount
  - Missing: amount field
  - Null: amount
  - Invalid type: amount (string)
  - Malformed JSON
- ❌ Account Not Found: → 404 NOT_FOUND
- ❌ Security: 401 (missing JWT), 403 (wrong role)

---

### 5. TransactionControllerTest (`controller/TransactionControllerTest.java`)
**Purpose**: Verify transaction endpoints (transfers, history) with security and business logic validation.

**TRANSFER (POST /transactions/transfer) [REQUIRES JWT]**
- ✅ Happy Path: Valid transfer + JWT: → 200 OK + reference ID
  - `verify(transactionService).transfer(request)` + `verifyNoMoreInteractions(transactionService)`
- ✅ Edge Cases: Minimum (0.01), Large (999999999.99)
  - `verify(transactionService).transfer(request)`
- ❌ Insufficient Funds: → 422 UNPROCESSABLE_ENTITY
  - `verify(transactionService).transfer(request)` called
- ❌ Destination/Source Account Not Found: → 404 NOT_FOUND
- ❌ Bad Request Cases: → 400 BAD_REQUEST with `verifyNoInteractions(transactionService)`
  - Same source & destination
  - Zero: amount
  - Negative: amount
  - Missing: amount field
  - Null: amount
  - Blank: source/destination accounts
  - Invalid type: amount (string)
  - Malformed JSON
- ❌ Unexpected Error: → 500 INTERNAL_SERVER_ERROR
- ❌ Security: 401 (missing JWT), 403 (wrong role: ROLE_VIEWER, ROLE_GUEST)

**HISTORY (GET /transactions/{accountNumber}) [REQUIRES JWT]**
- ✅ Valid account + JWT: → 200 OK + transaction list
  - `verify(transactionService).getTransactionsForAccount("SB-1001")` + `verifyNoMoreInteractions(transactionService)`
- ✅ Empty History: → 200 OK + empty array
- ✅ Multiple Records: Returned in correct order (newest to oldest)
  - `verify(transactionService).getTransactionsForAccount("SB-1001")`
- ❌ Account Not Found: → 404 NOT_FOUND
- ❌ Blank Account Number: → 400 BAD_REQUEST
- ❌ Unexpected Error: → 500 INTERNAL_SERVER_ERROR
- ❌ Security: 401 (missing JWT), 403 (wrong role)

---

## Key Testing Patterns

### 1. Mock Verification Strategy

**On Success (service called):**
```java
verify(service).method(argument);              // Called exactly once
// OR
verify(service).method(argument);
verifyNoMoreInteractions(service);             // Called once, no other calls
```

**On Validation Failure (service NEVER called):**
```java
mockMvc.perform(...)
    .andExpect(status().isBadRequest());
verifyNoInteractions(service);                 // Service not invoked at all
```

**On Business Error (service called, then threw exception):**
```java
when(service.method(...)).thenThrow(exception);
mockMvc.perform(...)
    .andExpect(status().isUnprocessableEntity());
verify(service).method(...);                   // Service was called
```

### 2. Bad Request Behavior

**Missing or Invalid Values MUST:**
1. Return **400 BAD_REQUEST** at validation layer (Spring binding/validation)
2. **NEVER** invoke the service layer
3. **Assert** `verifyNoInteractions(service)` to enforce

**Examples:**
- Missing required field → 400 + verifyNoInteractions
- Invalid email format → 400 + verifyNoInteractions
- Malformed JSON → 400 + verifyNoInteractions
- Wrong field type (string instead of number) → 400 + verifyNoInteractions

### 3. Status Codes Used

| Code | Scenario |
|------|----------|
| 200 OK | Request successful |
| 201 CREATED | Resource created successfully |
| 204 NO_CONTENT | Deletion successful, no response body |
| 400 BAD_REQUEST | Validation failure (bad input from client) |
| 401 UNAUTHORIZED | Missing/invalid JWT |
| 403 FORBIDDEN | Authenticated but lacks required role |
| 404 NOT_FOUND | Resource doesn't exist |
| 409 CONFLICT | Duplicate resource (e.g., email) |
| 415 UNSUPPORTED_MEDIA_TYPE | Missing/incorrect Content-Type header |
| 422 UNPROCESSABLE_ENTITY | Business logic error (insufficient funds) |
| 500 INTERNAL_SERVER_ERROR | Unexpected service exception |

### 4. Security Testing

**JWT Requirement:**
- Missing JWT → 401 UNAUTHORIZED
- Invalid JWT → 401 UNAUTHORIZED

**Role-Based Authorization:**
- Insufficient role → 403 FORBIDDEN
- Required roles enforced per endpoint (e.g., ROLE_ADMIN, ROLE_USER)

---

## Best Practices Implemented

✅ **Descriptive Test Names**: Each test method clearly states what is being tested  
✅ **Nested Classes**: `@Nested` groups related tests by endpoint/scenario  
✅ **Mock Verification**: Every mock invocation verified with `verify()` or `verifyNoInteractions()`  
✅ **Input Validation**: Comprehensive testing of all required and optional fields  
✅ **Error Scenarios**: Success paths, validation failures, business errors, and security  
✅ **HTTP Status Codes**: Correct status code for each scenario  
✅ **JWT Security**: All protected endpoints tested with/without JWT and roles  
✅ **Decimal Precision**: Financial amounts tested with proper BigDecimal handling  
✅ **Edge Cases**: Minimum/maximum values, empty collections, null inputs  

---

## Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AuthenticationControllerTest

# Run specific test method
mvn test -Dtest=AuthenticationControllerTest#shouldRegisterCustomerSuccessfully

# Run with coverage report
mvn test jacoco:report
```

---

## Key Assertions Summary

| Assertion | Purpose |
|-----------|---------|
| `status().isOk()` | 200 OK response |
| `status().isCreated()` | 201 CREATED response |
| `status().isNoContent()` | 204 NO_CONTENT response |
| `status().isBadRequest()` | 400 BAD_REQUEST response |
| `status().isUnauthorized()` | 401 UNAUTHORIZED response |
| `status().isForbidden()` | 403 FORBIDDEN response |
| `status().isNotFound()` | 404 NOT_FOUND response |
| `status().isConflict()` | 409 CONFLICT response |
| `status().isUnsupportedMediaType()` | 415 UNSUPPORTED_MEDIA_TYPE response |
| `status().isUnprocessableEntity()` | 422 UNPROCESSABLE_ENTITY response |
| `status().isInternalServerError()` | 500 INTERNAL_SERVER_ERROR response |
| `jsonPath("$.field").value(expected)` | Verify JSON response field value |
| `jsonPath("$").isArray()` | Verify response is an array |
| `jsonPath("$").isEmpty()` | Verify array is empty |
| `verify(mock).method(args)` | Service method called exactly once |
| `verifyNoInteractions(mock)` | Service never called |
| `verifyNoMoreInteractions(mock)` | Service called once, no other calls |

---

## Integration with CI/CD

All tests are configured to:
- ✅ Run on each commit
- ✅ Report coverage metrics
- ✅ Block merge on test failure
- ✅ Generate HTML test reports

---

**Last Updated**: July 8, 2026

