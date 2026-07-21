# verify() Usage Patterns - Complete Examples

## 📋 Pattern 1: Success Path (times(1))

### Example 1: Authentication - Valid Register
**File**: AuthenticationControllerTest.java, Line 127
```java
// Test: Valid registration with all required fields returns 201 CREATED
@Test
@DisplayName("should register customer successfully")
void shouldRegisterCustomerSuccessfully() throws Exception {
    // Arrange
    Map<String, Object> request = validRegisterRequest();
    org.mockito.Mockito.when(authenticationService.register(request))
        .thenReturn(response);

    // Act
    mockMvc.perform(post("/api/auth/register")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        // Assert
        .andExpect(status().isCreated());

    // Verify - Service called exactly once
    verify(authenticationService, org.mockito.Mockito.times(1)).register(request);
}
```

**What happens**:
1. ✅ Mock configured to return response
2. ✅ Request sent to controller
3. ✅ Controller calls service.register()
4. ✅ `verify()` confirms it was called **exactly 1 time**

---

### Example 2: Auth - Valid Login
**File**: AuthControllerTest.java, Line 232
```java
// Test: Valid credentials return 200 OK with JWT token
@Test
@DisplayName("should return 200 with JWT token when credentials are valid")
void login_whenValidCredentials_shouldReturnOk() throws Exception {
    AuthRequest request = new AuthRequest();
    
    when(authService.login(request)).thenReturn(response);

    mockMvc.perform(post("/auth/login")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    // Verify called exactly once + no other calls
    verify(authService, times(1)).login(request);
    verifyNoMoreInteractions(authService);
}
```

**What happens**:
1. ✅ login() configured
2. ✅ Request sent
3. ✅ `verify(times(1))` - called once ✓
4. ✅ `verifyNoMoreInteractions()` - no extra calls ✓

---

### Example 3: Account - Valid Deposit
**File**: AccountControllerTest.java, Line 282
```java
// Test: Valid amount returns 200 OK + updated balance
@Test
@DisplayName("should return 200 for valid deposit")
void deposit_whenValidAmount_shouldReturnOk() throws Exception {
    AccountRequest request = new AccountRequest();
    
    when(accountService.deposit("SB-1001", bd("50.00")))
        .thenReturn(accountResponse("SB-1001", bd("175.00")));

    mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-1001")
            .with(jwt())
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(accountService, times(1)).deposit("SB-1001", bd("50.00"));
}
```

**What happens**:
1. ✅ Service configured with deposit()
2. ✅ POST request made
3. ✅ `verify()` confirms deposit() called exactly once

---

## 📋 Pattern 2: Validation Failure (times(0))

### Example 1: Register - Blank Email
**File**: AuthenticationControllerTest.java, Line 158
```java
// Test: Invalid email format rejected at validation layer returns 400
@Test
@DisplayName("should return bad request for invalid email")
void shouldReturnBadRequestForInvalidEmail() throws Exception {
    Map<String, Object> request = validRegisterRequest();
    request.put("email", "invalid");  // Bad email format

    mockMvc.perform(post("/api/auth/register")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());  // 400

    // Verify - Service NEVER called
    verify(authenticationService, org.mockito.Mockito.never()).register(request);
}
```

**What happens**:
1. ❌ Email format invalid
2. ❌ Controller validation catches error
3. ❌ Service.register() never reached
4. ✅ `verify(never())` confirms service NOT called

---

### Example 2: Customer - Blank Name
**File**: CustomerControllerTest.java, Line ~165
```java
// Test: Blank name returns 400 BAD_REQUEST
@Test
@DisplayName("should return bad request for blank name")
void shouldReturnBadRequestForBlankName() throws Exception {
    Map<String, Object> req = validCustomerRequest();
    req.put("name", " ");  // Blank name

    mockMvc.perform(post("/api/customers")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest());

    verify(customerService, times(0)).createCustomer(req);  // times(0) = never
}
```

**What happens**:
1. ❌ Name is blank (whitespace only)
2. ❌ Validation fails in controller
3. ❌ Service never called
4. ✅ `verify(times(0))` confirms 0 invocations

---

### Example 3: Transaction - Missing Amount
**File**: TransactionControllerTest.java, Line ~200
```java
// Test: Missing amount field returns 400 BAD_REQUEST
@Test
@DisplayName("should return 400 when amount field is missing")
void transfer_whenAmountMissing_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(post("/transactions/transfer")
            .with(jwt())
            .contentType(APPLICATION_JSON)
            .content("{\"sourceAccountNumber\":\"SB-1001\",\"destinationAccountNumber\":\"SB-2002\"}"))
        .andExpect(status().isBadRequest());

    verify(transactionService, times(0)).transfer(null);  // Service NOT called
}
```

**What happens**:
1. ❌ Amount field missing from JSON
2. ❌ Spring binding fails validation
3. ❌ Service not reached
4. ✅ `verify(times(0))` - no invocation

---

## 📋 Pattern 3: Business Error (times(1) with exception)

### Example 1: Duplicate Email
**File**: AuthenticationControllerTest.java, Line 143
```java
// Test: Duplicate email in register request returns 409 CONFLICT
@Test
@DisplayName("should return conflict when duplicate email")
void shouldReturnConflictWhenDuplicateEmail() throws Exception {
    Map<String, Object> request = validRegisterRequest();
    
    // Mock throws exception
    org.mockito.Mockito.when(authenticationService.register(request))
        .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate email"));

    mockMvc.perform(post("/api/auth/register")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict());  // 409

    // Verify - Service WAS called (before throwing)
    verify(authenticationService, org.mockito.Mockito.times(1)).register(request);
}
```

**What happens**:
1. ✅ Valid data format passes validation
2. ✅ Service.register() called
3. ❌ Service detects duplicate email
4. ❌ Service throws exception
5. ✅ Controller catches, returns 409
6. ✅ `verify(times(1))` - service was called

---

### Example 2: Account Not Found
**File**: AccountControllerTest.java, Line ~365
```java
// Test: Account not found returns 404 NOT_FOUND
@Test
@DisplayName("should return 404 when account is missing")
void getAccount_whenMissing_shouldReturnNotFound() throws Exception {
    // Mock throws exception
    when(accountService.getAccountByNumber("SB-404"))
        .thenThrow(new AccountNotFoundException("not found"));

    mockMvc.perform(get("/accounts/{accountNumber}", "SB-404").with(jwt()))
        .andExpect(status().isNotFound());  // 404

    verify(accountService, times(1)).getAccountByNumber("SB-404");
}
```

**What happens**:
1. ✅ Valid account number format
2. ✅ Service.getAccountByNumber() called
3. ❌ Service doesn't find account
4. ❌ Service throws AccountNotFoundException
5. ✅ Controller catches, returns 404
6. ✅ `verify(times(1))` - service reached

---

## 📋 Pattern 4: Cleanup in @AfterEach

### Used in All 5 Test Classes
```java
@AfterEach
void afterEach() {
    // Verify no unexpected interactions with mocks after each test
    verifyNoMoreInteractions(mockService);
}
```

**What this does**:
- ✅ Runs after EVERY test
- ✅ Checks for any unexpected calls
- ✅ Fails if service called more than expected
- ✅ Ensures test isolation

**Example failure**:
```java
@Test
void someTest() {
    when(service.method1()).thenReturn(response);
    mockMvc.perform(post(...)).andExpect(status().isOk());
    verify(service, times(1)).method1();
    
    // But service also called method2() unexpectedly
}

@AfterEach
void afterEach() {
    verifyNoMoreInteractions(service);  // ❌ FAILS: method2 was called
}
```

---

## 🔄 Complete Test Flow with verify()

```
@BeforeEach
├─ reset(mockService)  ← Clear previous state
│
@Test Method
├─ Configure mock: when(service.method()).thenReturn(response)
├─ Make request: mockMvc.perform(post(...))
├─ Assert status: .andExpect(status().isOk())
├─ Assert response: .andExpect(jsonPath(...))
└─ Verify mock: verify(service, times(1)).method()
   
@AfterEach
└─ verifyNoMoreInteractions(service)  ← Check no extra calls
```

---

## 📊 Decision Tree: Which verify() to Use

```
                           ┌─ Did validation fail? ─ YES
                           │                          │
                           │                    verify(times(0))
                           │
Test starts ────────────────┤
                           │
                           │─ Did validation pass? ─ NO
                           │                        │
                           │            verify(times(0))
                           │
                           ├─ Did service get called? ─ YES
                           │                              │
                           │              ┌─ Did it succeed? ─ YES
                           │              │                    │
                           │              │            verify(times(1))
                           │              │
                           │              └─ Did it throw error? ─ YES
                           │                                       │
                           │                               verify(times(1))
                           │
                           └─ Always in @AfterEach:
                              verifyNoMoreInteractions()
```

---

## ✅ Summary Table

| Scenario | verify() Call | Expected Result |
|----------|--------------|-----------------|
| Valid request, service succeeds | `times(1)` | ✅ 2xx status |
| Invalid request, validation fails | `times(0)` | ✅ 400 status |
| Valid request, service throws error | `times(1)` | ✅ 4xx/5xx status |
| Every test ends | `verifyNoMoreInteractions()` | ✅ No extra calls |

---

**Last Updated**: July 8, 2026
**Total Examples**: 8 real test cases
**Coverage**: All 5 test classes

