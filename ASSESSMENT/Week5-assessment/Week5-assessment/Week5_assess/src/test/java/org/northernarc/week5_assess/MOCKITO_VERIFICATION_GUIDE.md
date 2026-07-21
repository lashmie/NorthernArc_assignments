# Mockito Verification Quick Reference Guide

This guide explains the Mockito verification patterns used throughout the test suite.

---

## Pattern 1: Happy Path (Service Called Once)

```java
// Arrange: Mock returns a successful response
when(service.method(request)).thenReturn(response);

// Act: Make request
mockMvc.perform(post("/endpoint")
    .contentType(APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(request)))
    
// Assert: Check HTTP status
    .andExpect(status().isCreated());

// Verify: Service was called exactly once
verify(service).method(request);
verifyNoMoreInteractions(service);  // Optional: ensures no other calls
```

**When to use**: Success scenarios where service processes and returns data

**Example test**:
```java
@Test
void shouldRegisterCustomerSuccessfully() throws Exception {
    RegisterRequest request = new RegisterRequest();
    request.setName("Asha");
    request.setEmail("asha@test.com");
    
    when(authService.register(request)).thenReturn(new AuthResponse("token123"));
    
    mockMvc.perform(post("/auth/register")...)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.token").value("token123"));
    
    verify(authService).register(request);
    verifyNoMoreInteractions(authService);
}
```

---

## Pattern 2: Validation Failure (Service Never Called)

```java
// Arrange: Invalid request (e.g., missing field)
String invalidRequest = "{\"email\":\"\"}";  // Missing password

// Act: Make request
mockMvc.perform(post("/endpoint")
    .contentType(APPLICATION_JSON)
    .content(invalidRequest))
    
// Assert: Check for Bad Request status
    .andExpect(status().isBadRequest());

// Verify: Service MUST NOT be called
verifyNoInteractions(service);
```

**When to use**: Validation failures at binding/constraint layer
- Missing required fields
- Invalid field types (string instead of number)
- Constraint violations (blank, invalid email format)
- Malformed JSON

**Example test**:
```java
@Test
void shouldReturnBadRequestForMissingEmail() throws Exception {
    mockMvc.perform(post("/auth/login")
        .contentType(APPLICATION_JSON)
        .content("{\"password\":\"Password123\"}"))  // Missing email
        .andExpect(status().isBadRequest());
    
    verifyNoInteractions(authService);  // Service must NOT be invoked
}
```

**Key Point**: If a validation layer passes a bad request to the service, your controller is broken!

---

## Pattern 3: Business Error (Service Called, Then Threw)

```java
// Arrange: Mock throws business exception
when(service.method(request)).thenThrow(new BusinessException("error message"));

// Act: Make request
mockMvc.perform(post("/endpoint")
    .contentType(APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(request)))
    
// Assert: Check appropriate HTTP status
    .andExpect(status().isUnprocessableEntity());  // 422 or other error status

// Verify: Service WAS called (before throwing)
verify(service).method(request);
```

**When to use**: Service validates at business logic level and throws
- Insufficient funds
- Duplicate resource
- Resource not found
- Account already exists

**Example test**:
```java
@Test
void shouldReturnConflictWhenDuplicateEmail() throws Exception {
    RegisterRequest request = new RegisterRequest();
    request.setName("Asha");
    request.setEmail("asha@test.com");
    
    when(authService.register(request))
        .thenThrow(new DuplicateResourceException("Email exists"));
    
    mockMvc.perform(post("/auth/register")...)
        .andExpect(status().isConflict());
    
    verify(authService).register(request);  // Service WAS called
}
```

**Key Point**: The service reached business logic and detected an error condition

---

## Pattern 4: Security Failure (Service Not Called)

```java
// Arrange: Missing JWT or wrong role
// (No mock setup needed - endpoint won't reach service)

// Act: Make request without JWT
mockMvc.perform(post("/protected-endpoint")
    .contentType(APPLICATION_JSON)
    .content("{}"))
    // Missing: .with(jwt())

// Assert: Check security status
    .andExpect(status().isUnauthorized());  // 401

// Verify: Service never reached
verifyNoInteractions(service);
```

**When to use**: 
- Missing JWT → 401 UNAUTHORIZED
- Invalid JWT → 401 UNAUTHORIZED  
- Wrong role → 403 FORBIDDEN

**Example test**:
```java
@Test
void shouldReturnUnauthorizedWhenJwtMissing() throws Exception {
    mockMvc.perform(post("/accounts")
        .contentType(APPLICATION_JSON)
        .content("{\"customerId\":1,\"accountType\":\"SAVINGS\"}"))
        // NO .with(jwt())
        .andExpect(status().isUnauthorized());
    
    verifyNoInteractions(accountService);  // Security filter blocked it
}
```

---

## Verification Methods Reference

### `verify(mock).method(...)`
- Verifies method called **exactly once**
- Throws exception if called 0 times or 2+ times

```java
verify(service).register(request);  // Must be called exactly once
```

### `verifyNoInteractions(mock)`
- Verifies method **never called** (no interactions at all)
- Used for failed validations

```java
verifyNoInteractions(service);  // Service must not be invoked
```

### `verifyNoMoreInteractions(mock)` 
- Verifies method called as specified, **with no additional calls**
- Works with multiple verify() calls

```java
verify(service).register(request);
verify(service).logActivity("registered");
verifyNoMoreInteractions(service);  // No more calls beyond these two
```

### `times(n)` / `never()` / `atLeastOnce()` / `atMost(n)`
- Specifies invocation count explicitly

```java
verify(service, times(1)).register(request);      // Exactly once
verify(service, never()).register(request);       // Never called
verify(service, atLeastOnce()).register(request); // At least once
verify(service, atMost(2)).register(request);     // Max twice
```

---

## Mockito Matchers for Flexibility

### `any()` - Match Any Argument
```java
when(service.register(any())).thenReturn(response);
verify(service).register(any());
```

### `eq()` - Exact Match
```java
verify(service).updateCustomer(eq(1L), any());  // First arg must be 1L
```

### `ArgumentMatchers.any(ClassName.class)`
```java
when(service.processRequest(ArgumentMatchers.any(RegisterRequest.class)))
    .thenReturn(response);
```

---

## Complete Test Example

```java
@WebMvcTest(AuthController.class)
class AuthControllerTest {
    
    @MockBean
    private AuthService authService;
    
    @Autowired
    private MockMvc mockMvc;
    
    // Pattern 1: Success
    @Test
    void register_whenValid_shouldReturnCreated() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("test@test.com");
        
        when(authService.register(req))
            .thenReturn(new AuthResponse("jwt-token"));
        
        mockMvc.perform(post("/auth/register")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated());
        
        verify(authService).register(req);
        verifyNoMoreInteractions(authService);
    }
    
    // Pattern 2: Validation Failure (bad request)
    @Test
    void register_whenEmailMissing_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/auth/register")
            .contentType(APPLICATION_JSON)
            .content("{}"))
            .andExpect(status().isBadRequest());
        
        verifyNoInteractions(authService);  // CRITICAL: Service not called
    }
    
    // Pattern 3: Business Error
    @Test
    void register_whenDuplicate_shouldReturnConflict() throws Exception {
        RegisterRequest req = new RegisterRequest();
        
        when(authService.register(req))
            .thenThrow(new DuplicateResourceException("Email exists"));
        
        mockMvc.perform(post("/auth/register")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isConflict());
        
        verify(authService).register(req);  // Service WAS called
    }
    
    // Pattern 4: Security (no JWT)
    @Test
    void deposit_whenNoJwt_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/accounts/deposit")
            .contentType(APPLICATION_JSON)
            .content("{}"))
            // Missing: .with(jwt())
            .andExpect(status().isUnauthorized());
        
        verifyNoInteractions(authService);
    }
}
```

---

## Common Mistakes & Fixes

### ❌ MISTAKE: Not verifying service invocation
```java
// Wrong!
mockMvc.perform(post("/endpoint")...)
    .andExpect(status().isCreated());
// No verify() call!
```

**Fix**: Always verify
```java
// Right!
mockMvc.perform(post("/endpoint")...)
    .andExpect(status().isCreated());
verify(service).method(request);
```

---

### ❌ MISTAKE: Using `verify()` for bad request
```java
// Wrong!
mockMvc.perform(post("/endpoint")...)
    .andExpect(status().isBadRequest());
verify(service).method(request);  // Should NOT be here!
```

**Fix**: Use `verifyNoInteractions()` for bad request
```java
// Right!
mockMvc.perform(post("/endpoint")...)
    .andExpect(status().isBadRequest());
verifyNoInteractions(service);  // Service must not be called
```

---

### ❌ MISTAKE: Forgetting `when()` for exception cases
```java
// Wrong!
// when(service.method(...)).thenThrow(...);  // Forgot this!

mockMvc.perform(post("/endpoint")...)
    .andExpect(status().isConflict());
verify(service).method(request);
```

**Fix**: Always mock the exception
```java
// Right!
when(service.method(request))
    .thenThrow(new DuplicateResourceException("exists"));

mockMvc.perform(post("/endpoint")...)
    .andExpect(status().isConflict());
verify(service).method(request);
```

---

### ❌ MISTAKE: Calling `verify()` when not verifying service reached
```java
// Wrong!
mockMvc.perform(post("/endpoint")...)
    .andExpect(status().isBadRequest());
verify(service, never()).method(request);  // Overly complicated
```

**Fix**: Use `verifyNoInteractions()` when service shouldn't be reached
```java
// Right!
mockMvc.perform(post("/endpoint")...)
    .andExpect(status().isBadRequest());
verifyNoInteractions(service);  // Simpler, clearer intent
```

---

## Summary Table

| Scenario | Status | Service Called? | Verify Statement |
|----------|--------|-----------------|------------------|
| Happy Path | 2xx (200/201/204) | YES | `verify(service).method(...)` |
| Validation Failure | 400 BAD_REQUEST | NO | `verifyNoInteractions(service)` |
| Business Error | 4xx/5xx (409/422/404) | YES | `verify(service).method(...)` |
| Security Failure | 401/403 | NO | `verifyNoInteractions(service)` |
| Unsupported Media Type | 415 | NO | `verifyNoInteractions(service)` |
| Malformed JSON | 400 | NO | `verifyNoInteractions(service)` |

---

**Remember**: The most critical rule is:
> **If HTTP status is 400 (or any validation failure), the service layer must NEVER be invoked.**
> This is verified by `verifyNoInteractions(service)` and is essential for clean architecture.

---

**Last Updated**: July 8, 2026

