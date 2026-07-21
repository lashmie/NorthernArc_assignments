# Test Review Checklist

Use this checklist when writing or reviewing tests to ensure consistency and correctness.

---

## Before Writing a Test

- [ ] Test name clearly describes what is being tested (e.g., `shouldReturnBadRequestForMissingEmail`)
- [ ] Test placed in appropriate `@Nested` class by endpoint/scenario
- [ ] Test class uses `@WebMvcTest(controllers = SomeController.class)` for controller unit tests
- [ ] `@MockBean` used for service layer, not real beans
- [ ] Clear Arrange/Act/Assert comments or sections

---

## Happy Path Tests (Success 2xx)

### Arrange Phase
- [ ] Mock service configured with `.when(...).thenReturn(...)`
- [ ] Realistic test data used (email format valid, amount positive, etc.)

### Act Phase
- [ ] Request properly formatted (JSON, Content-Type)
- [ ] Security included if endpoint requires JWT (`.with(jwt())`)
- [ ] Correct HTTP method (POST, GET, PUT, DELETE)
- [ ] Correct endpoint path

### Assert Phase
- [ ] Correct HTTP status code (200, 201, 204)
- [ ] Response body checked with `jsonPath()`
- [ ] Key fields verified

### Verify Phase
- [ ] `verify(service).method(request)` - confirms service called exactly once
- [ ] Optional: `verifyNoMoreInteractions(service)` - confirms no other calls
- [ ] NO `verifyNoInteractions(service)` - that's for failures!

---

## Validation Failure Tests (400 BAD_REQUEST)

### Arrange Phase
- [ ] Invalid request data prepared (missing field, wrong type, etc.)
- [ ] NO mock setup needed (service shouldn't be reached)

### Act Phase
- [ ] Request made with invalid data
- [ ] Correct endpoint and HTTP method
- [ ] Content-Type set correctly

### Assert Phase
- [ ] Status is `.isBadRequest()` (400)
- [ ] No response body validation needed (validation fails before business logic)

### Verify Phase - **CRITICAL**
- [ ] `verifyNoInteractions(service)` - service MUST NOT be called
- [ ] **This is non-negotiable** - validation happens at Spring binding layer
- [ ] If service is called on 400, the controller is broken

---

## Business Error Tests (4xx/5xx excluding 400)

### Scenarios
- [ ] Duplicate resource (409 CONFLICT)
- [ ] Resource not found (404 NOT_FOUND)
- [ ] Insufficient funds (422 UNPROCESSABLE_ENTITY)
- [ ] Business rule violation (422)

### Arrange Phase
- [ ] Mock configured to throw business exception:
  ```java
  when(service.method(request))
      .thenThrow(new BusinessException("message"));
  ```
- [ ] Exception matches expected status code

### Act Phase
- [ ] Request with valid format but triggers business error
- [ ] Must have valid data types (not validation failure)

### Assert Phase
- [ ] Correct HTTP status for the error (409, 404, 422, etc.)
- [ ] Error message in response if applicable

### Verify Phase
- [ ] `verify(service).method(request)` - service WAS called
- [ ] Service reached business logic layer
- [ ] Never use `verifyNoInteractions()` here

---

## Security Tests (401/403)

### Missing JWT (401)
- [ ] Request made WITHOUT `.with(jwt())`
- [ ] Assert: `.andExpect(status().isUnauthorized())`
- [ ] Verify: `verifyNoInteractions(service)` - security filter blocks before service

### Invalid JWT (401)
- [ ] Request made with invalid/expired token
- [ ] Assert: `.andExpect(status().isUnauthorized())`
- [ ] Verify: `verifyNoInteractions(service)`

### Wrong Role (403)
- [ ] Request made with insufficient role:
  ```java
  .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_VIEWER")))
  ```
- [ ] Assert: `.andExpect(status().isForbidden())`
- [ ] Verify: `verifyNoInteractions(service)`

---

## Media Type & Format Tests (415/400)

### Unsupported Media Type (415)
- [ ] Request made WITHOUT `.contentType(APPLICATION_JSON)`
- [ ] Assert: `.andExpect(status().isUnsupportedMediaType())`
- [ ] Verify: `verifyNoInteractions(service)`

### Malformed JSON (400)
- [ ] Request with broken JSON:
  ```java
  .content("{\"name\":")  // Missing closing brace, unclosed quote
  ```
- [ ] Assert: `.andExpect(status().isBadRequest())`
- [ ] Verify: `verifyNoInteractions(service)`

---

## Test Data Validation Checklist

### Email Fields
- [ ] Valid format: `test@example.com` ✓
- [ ] Invalid format: `invalid`, `test@`, `@example.com` ✗
- [ ] Test both cases

### Phone Numbers
- [ ] Exact length requirement (e.g., 10 digits)
- [ ] Test: Too short (9 digits) ✗
- [ ] Test: Too long (11 digits) ✗
- [ ] Test: Valid (10 digits) ✓

### Passwords
- [ ] Minimum length requirement
- [ ] Test: Too short ✗
- [ ] Test: Valid length ✓
- [ ] Test: Empty/blank ✗

### Currency/Amounts
- [ ] Positive amounts only
- [ ] Test: Zero (0.00) ✗
- [ ] Test: Negative (-1.00) ✗
- [ ] Test: Positive (50.00) ✓
- [ ] Test: Decimal precision (0.01, 1.99) ✓

### Required Fields
- [ ] Test each missing one individually
- [ ] Test all missing together
- [ ] Test null values

---

## Response Verification Patterns

### Single Object Response
```java
.andExpect(jsonPath("$.id").value(1L))
.andExpect(jsonPath("$.name").value("Asha"))
.andExpect(jsonPath("$.email").value("asha@test.com"))
```

### Array Response
```java
.andExpect(jsonPath("$").isArray())
.andExpect(jsonPath("$[0].id").value(1L))
.andExpect(jsonPath("$[1].id").value(2L))
```

### Empty Array
```java
.andExpect(jsonPath("$").isArray())
.andExpect(jsonPath("$").isEmpty())
```

### Decimal/BigDecimal Fields
```java
.andExpect(jsonPath("$.balance").value(125.75))
// Don't use string comparison for decimals
```

---

## Mock Verification Checklist

### For Success (service called)
- [ ] Use `verify(service).method(arguments)`
- [ ] Match exact argument types (eq() for specific values, any() for flexible)
- [ ] Optional: Add `verifyNoMoreInteractions(service)` for clarity

### For Validation Failure (service NOT called)
- [ ] Use `verifyNoInteractions(service)` 
- [ ] NEVER use `verify(service, never()).method()` (less readable)
- [ ] NEVER forget this check

### For Business Error (service called then threw)
- [ ] Use `verify(service).method(arguments)`
- [ ] Ensure mock is configured to throw exception
- [ ] Status code matches exception type

---

## Content-Type Handling

### Correct (with Content-Type)
```java
.contentType(APPLICATION_JSON)
.content(objectMapper.writeValueAsString(request))
// Returns 201/200
```

### Missing (no Content-Type)
```java
.content(objectMapper.writeValueAsString(request))
// Returns 415 UNSUPPORTED_MEDIA_TYPE
// Should have: .contentType(APPLICATION_JSON)
```

---

## JWT Security Handling

### Endpoints WITHOUT Security
```java
mockMvc.perform(post("/auth/login")
    .contentType(APPLICATION_JSON)
    .content(...))
    // NO .with(jwt())
```

### Endpoints WITH Security (default ROLE_USER)
```java
mockMvc.perform(post("/accounts")
    .with(jwt())  // Uses default ROLE_USER
    .contentType(APPLICATION_JSON)
    .content(...))
```

### Endpoints WITH Security (specific role)
```java
mockMvc.perform(post("/accounts")
    .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
    .contentType(APPLICATION_JSON)
    .content(...))
```

### Testing missing JWT
```java
mockMvc.perform(post("/accounts")
    // Missing: .with(jwt())
    .contentType(APPLICATION_JSON)
    .content(...))
    .andExpect(status().isUnauthorized());
```

---

## Common Test Scenarios Quick Reference

| What to Test | Status | Verify |
|--------------|--------|--------|
| Valid input, no errors | 2xx | `verify(service)` |
| Missing required field | 400 | `verifyNoInteractions(service)` |
| Invalid field type | 400 | `verifyNoInteractions(service)` |
| Invalid format (email) | 400 | `verifyNoInteractions(service)` |
| Malformed JSON | 400 | `verifyNoInteractions(service)` |
| Missing Content-Type | 415 | `verifyNoInteractions(service)` |
| Missing JWT | 401 | `verifyNoInteractions(service)` |
| Wrong role | 403 | `verifyNoInteractions(service)` |
| Duplicate email | 409 | `verify(service)` |
| Not found | 404 | `verify(service)` |
| Insufficient funds | 422 | `verify(service)` |
| Unexpected error | 500 | `verify(service)` |

---

## Pre-Commit Checklist

Before committing test code:

- [ ] All tests pass locally: `mvn test`
- [ ] No hardcoded values without explanation
- [ ] No duplicate test code (DRY principle)
- [ ] Test names are descriptive and consistent
- [ ] Comments explain complex setup or assertions
- [ ] Mock verification present in all tests
- [ ] No `@Ignore` or skipped tests without reason
- [ ] Code follows team conventions
- [ ] Reviewed by another developer (if possible)

---

## Documentation Requirement

Every new test class must include:

```java
/**
 * TestClassName - Brief description
 *
 * TESTING COVERAGE:
 * ================
 * 1. ENDPOINT 1 (METHOD /path):
 *    - Happy path scenario
 *    - Error scenarios
 *    - Validation scenarios
 *
 * KEY VERIFICATION PATTERNS:
 * =========================
 * - verify() usage
 * - verifyNoInteractions() usage
 *
 * BAD REQUEST BEHAVIOR:
 * ====================
 * - How invalid requests are handled
 */
@WebMvcTest(...)
class TestClassName {
    // ...
}
```

---

## Review Questions for Maintainers

When reviewing test PRs:

1. **For Happy Path**: Does test call `verify()` on the mock?
2. **For 400 Status**: Does test call `verifyNoInteractions(service)`?
3. **For 4xx/5xx (not 400)**: Does test call `verify()` to confirm service was reached?
4. **For Security**: Are JWT and role tests included?
5. **For Validation**: Is each invalid case tested separately?
6. **For Data**: Is test data realistic and matches domain rules?
7. **For Response**: Are key response fields validated?
8. **For Names**: Is test name clear about what it's testing?

---

**Last Updated**: July 8, 2026

**Remember**: The golden rule is:
> ✅ **Success (2xx) → `verify(service)`**
> ✅ **Failure at validation (400) → `verifyNoInteractions(service)`**
> ✅ **Failure at business logic (4xx/5xx, not 400) → `verify(service)`**

