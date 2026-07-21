# Where verify() is Used - Complete Guide

## 📍 verify() Locations Summary

### Pattern 1: Success Tests (Service Called Exactly Once)
```java
verify(service, times(1)).method(args);
```
**Used in**: Tests where service SHOULD be called once
**Line**: After the test assertion, to confirm service was called

---

### Pattern 2: Failure Tests (Service Never Called)
```java
verify(service, never()).method(args);
```
**Used in**: Tests where service SHOULD NOT be called (validation failures)
**Line**: After the test assertion

---

### Pattern 3: Cleanup (Check No Extra Calls)
```java
verifyNoMoreInteractions(service);
```
**Used in**: @AfterEach method
**Line**: Automatically called after every test

---

## 🔍 All verify() Usages by File

### 1. AuthenticationControllerTest.java

#### Register Tests:

**Line 127**: ✅ Valid registration called once
```java
// Test: Valid registration with all required fields returns 201 CREATED
verify(authenticationService, org.mockito.Mockito.times(1)).register(request);
```

**Line 143**: ✅ Duplicate email - service called once (to throw error)
```java
// Test: Duplicate email in register request returns 409 CONFLICT
verify(authenticationService, org.mockito.Mockito.times(1)).register(request);
```

**Line 158**: ❌ Invalid email - service NEVER called
```java
// Test: Invalid email format rejected at validation layer returns 400
verify(authenticationService, org.mockito.Mockito.never()).register(request);
```

**Line 173**: ❌ Missing name - service NEVER called
```java
// Test: Missing name field returns 400 BAD_REQUEST
verify(authenticationService, org.mockito.Mockito.never()).register(request);
```

**Line 188**: ❌ Missing password - service NEVER called
```java
// Test: Missing password field returns 400 BAD_REQUEST
verify(authenticationService, org.mockito.Mockito.never()).register(request);
```

**Line 203**: ❌ Missing phone - service NEVER called
```java
// Test: Missing phone number field returns 400 BAD_REQUEST
verify(authenticationService, org.mockito.Mockito.never()).register(request);
```

**Line 218**: ❌ Phone too short - service NEVER called
```java
// Test: Phone number less than 10 digits returns 400 BAD_REQUEST
verify(authenticationService, org.mockito.Mockito.never()).register(request);
```

**More similar verify() calls for...**
- Phone too long
- Blank fields
- Null body
- Malformed JSON
- Unsupported media type

#### Login Tests:

**Line ~310**: ✅ Valid login - called once
```java
verify(authenticationService, org.mockito.Mockito.times(1)).login(request);
```

**Line ~330**: ✅ Invalid credentials - called once (to throw error)
```java
verify(authenticationService, org.mockito.Mockito.times(1)).login(request);
```

**Line ~345**: ❌ Unknown email - called once
```java
verify(authenticationService, org.mockito.Mockito.times(1)).login(request);
```

**Line ~365**: ❌ Missing email - NEVER called
```java
verify(authenticationService, org.mockito.Mockito.never()).login(request);
```

**Plus more verify() calls for...**
- Missing password
- Blank credentials
- Null body
- Malformed JSON
- Unsupported media type

---

### 2. AuthControllerTest.java

#### Register Tests (Lines 147-190):

**Line 147**: ✅ Valid register - times(1)
```java
verify(authService, times(1)).register(request);
verifyNoMoreInteractions(authService);
```

**Line 166**: ✅ Duplicate email - times(1)
```java
verify(authService, times(1)).register(request);
```

**Line 178**: ❌ Blank name - times(0)
```java
verify(authService, times(0)).register(null);
```

**Line 190**: ❌ Invalid email - times(0)
```java
verify(authService, times(0)).register(null);
```

**Plus more verify() for...**
- Blank password
- Short password
- Missing fields
- Malformed JSON
- Content type missing
- Whitespace-only name
- Mixed-case email (success)
- Null body

#### Login Tests (Lines ~215-365):

**Line ~232**: ✅ Valid credentials - times(1)
```java
verify(authService, times(1)).login(request);
verifyNoMoreInteractions(authService);
```

**Line ~250**: ✅ Invalid credentials - times(1)
```java
verify(authService, times(1)).login(request);
```

**Line ~265**: ❌ Malformed JSON - times(0)
```java
verify(authService, times(0)).login(null);
```

**Plus more verify() for...**
- Blank email - times(0)
- Invalid email - times(0)
- Blank password - times(0)
- Missing fields - times(0)
- Null literal - times(0)
- Content type missing - times(0)
- Unexpected error - times(1)
- Mixed-case email - times(1)
- Trailing comma JSON - times(0)

---

### 3. CustomerControllerTest.java

#### Create Tests:

**Line ~165**: ✅ Valid customer - times(1)
```java
verify(customerService, times(1)).createCustomer(request);
```

**Line ~180**: ✅ Duplicate email - times(1)
```java
verify(customerService, times(1)).createCustomer(request);
```

**Line ~195**: ❌ Invalid email - times(0)
```java
verify(customerService, times(0)).createCustomer(req);
```

**Plus verify() for...**
- Blank name - times(0)
- Invalid phone - times(0)
- Missing body - times(0)
- Malformed JSON - times(0)
- Unsupported media type - times(0)

#### List Tests:

**Line ~228**: ✅ All customers - times(1)
```java
verify(customerService, times(1)).getAllCustomers();
```

**Line ~242**: ✅ Empty list - times(1)
```java
verify(customerService, times(1)).getAllCustomers();
```

#### Get By ID Tests:

**Line ~258**: ✅ Existing customer - times(1)
```java
verify(customerService, times(1)).getCustomerById(1L);
```

**Line ~273**: ✅ Not found - times(1)
```java
verify(customerService, times(1)).getCustomerById(404L);
```

**Line ~287**: ❌ Invalid path variable - times(0)
```java
verify(customerService, times(0)).getCustomerById(0L);
```

#### Update Tests:

**Line ~307**: ✅ Valid update - times(1)
```java
verify(customerService, times(1)).updateCustomer(eq(1L), eq(request));
```

**Line ~326**: ✅ Not found - times(1)
```java
verify(customerService, times(1)).updateCustomer(eq(404L), eq(request));
```

**Line ~344**: ✅ Duplicate email - times(1)
```java
verify(customerService, times(1)).updateCustomer(eq(1L), eq(request));
```

**Plus verify() for...**
- Invalid email - times(0)
- Invalid path - times(0)
- Malformed JSON - times(0)
- Unsupported media type - times(0)

#### Delete Tests:

**Line ~360**: ✅ Delete success - times(1)
```java
verify(customerService, times(1)).deleteCustomer(1L);
```

**Line ~373**: ✅ Not found - times(1)
```java
verify(customerService, times(1)).deleteCustomer(404L);
```

**Line ~384**: ❌ Invalid ID - times(0)
```java
verify(customerService, times(0)).deleteCustomer(0L);
```

---

### 4. AccountControllerTest.java

#### Create Account Tests:

**Line ~150**: ✅ Valid account - times(1)
```java
verify(accountService, times(1)).createAccount(request);
```

**Line ~171**: ❌ Null customer ID - times(0)
```java
verify(accountService, times(0)).createAccount(null);
```

**Plus verify() for...**
- Null account type - times(0)
- Blank account type - times(0)
- Unsupported type - times(1)
- Customer missing - times(1)
- Malformed JSON - times(0)
- Invalid ID type - times(0)
- Content type missing - times(0)
- JWT missing - times(0)
- Forbidden role - times(0)

#### Get Account Tests:

**Line ~195**: ✅ Account exists - times(1)
```java
verify(accountService, times(1)).getAccountByNumber("SB-1001");
```

**Line ~208**: ✅ Account missing - times(1)
```java
verify(accountService, times(1)).getAccountByNumber("SB-404");
```

**Line ~218**: ✅ Blank account - times(1)
```java
verify(accountService, times(1)).getAccountByNumber(" ");
```

**Plus verify() for...**
- Unexpected error - times(1)
- JWT missing - times(0)
- Forbidden role - times(0)
- Decimal balance - times(1)
- Service once - times(1)

#### Deposit Tests:

**Line ~282**: ✅ Valid deposit - times(1)
```java
verify(accountService, times(1)).deposit("SB-1001", bd("50.00"));
```

**Plus verify() for...**
- Two decimal amount - times(1)
- Zero amount - times(1)
- Negative amount - times(1)
- Missing amount - times(0)
- Null amount - times(0)
- Invalid type - times(0)
- Account missing - times(1)
- JWT missing - times(0)
- Forbidden role - times(0)
- Malformed JSON - times(0)

#### Withdraw Tests:

**Line ~428**: ✅ Valid withdraw - times(1)
```java
verify(accountService, times(1)).withdraw("SB-1001", bd("20.00"));
```

**Plus verify() for...**
- Insufficient funds - times(1)
- Account missing - times(1)
- Zero amount - times(1)
- Negative amount - times(1)
- Null amount - times(0)
- Missing amount - times(0)
- Invalid type - times(0)
- Malformed JSON - times(0)
- JWT missing - times(0)
- Forbidden role - times(0)

---

### 5. TransactionControllerTest.java

#### Transfer Tests:

**Line ~76**: ✅ Valid transfer - times(1)
```java
verify(transactionService, times(1)).transfer(request);
verifyNoMoreInteractions(transactionService);
```

**Line ~94**: ✅ Minimum amount - times(1)
```java
verify(transactionService, times(1)).transfer(request);
```

**Line ~115**: ✅ Insufficient funds - times(1)
```java
verify(transactionService, times(1)).transfer(request);
```

**Plus verify() for...**
- Destination missing - times(1)
- Source missing - times(1) (not explicitly verified)
- Same accounts - times(1) (when service is called)
- Zero amount - times(1) (when service is called)
- Negative amount - times(1) (when service is called)
- Missing amount - times(0)
- Null amount - times(0)
- Blank source - times(0)
- Blank destination - times(0)
- Invalid type - times(0)
- Unexpected error - times(1)
- Malformed JSON - times(0)
- Content type missing - times(0)
- JWT missing - times(0)
- Forbidden role - times(0)
- Large amount - times(1)
- Successful once - times(1)

#### History Tests:

**Line ~352**: ✅ Valid account - times(1)
```java
verify(transactionService, times(1)).getTransactionsForAccount("SB-1001");
verifyNoMoreInteractions(transactionService);
```

**Line ~359**: ✅ Empty history - times(1)
```java
verify(transactionService, times(1)).getTransactionsForAccount("SB-1001");
```

**Line ~370**: ✅ Account missing - times(1)
```java
verify(transactionService, times(1)).getTransactionsForAccount("SB-404");
```

**Plus verify() for...**
- Blank account - times(1) (when service is called)
- Unexpected error - times(1)
- JWT missing - times(0)
- Forbidden role - times(0)
- Multiple records - times(1)

---

## 📊 verify() Usage Statistics

### Total verify() Calls: 200+

### Breakdown:
| Type | Count | Usage |
|------|-------|-------|
| `times(1)` | ~100+ | Success paths - service called once |
| `times(0)` | ~50+ | Validation failures - service never called |
| `never()` | ~20+ | Validation failures (older pattern) |
| `verifyNoMoreInteractions()` | 10 | In @AfterEach methods |
| `verifyNoMoreInteractions()` | ~20 | In success tests |

---

## 🎯 Key Patterns

### Pattern 1: Successful Operation
```java
when(service.method(args)).thenReturn(response);
mockMvc.perform(...).andExpect(status().is2xx());
verify(service, times(1)).method(args);
verifyNoMoreInteractions(service);  // Ensure no extra calls
```

### Pattern 2: Validation Failure (Bad Request)
```java
mockMvc.perform(post(...).content("{}"))  // Invalid data
    .andExpect(status().isBadRequest());
verify(service, times(0)).method(null);  // Service NOT called
```

### Pattern 3: Business Error (Service Called, Then Threw)
```java
when(service.method(args)).thenThrow(new Exception(...));
mockMvc.perform(...).andExpect(status().is4xx());
verify(service, times(1)).method(args);  // Service WAS called
```

### Pattern 4: After Each Test
```java
@AfterEach
void afterEach() {
    verifyNoMoreInteractions(service);  // Global check
}
```

---

## ✅ Where NOT to Use verify()

❌ **Don't use verify() for**:
- Verifying HTTP status codes (use `.andExpect(status())`)
- Checking response body content (use `.andExpect(jsonPath())`)
- Testing database state (use assertions)
- Testing service logic (unit test the service)

✅ **Use verify() for**:
- Confirming mocks were called
- Checking invocation counts
- Ensuring test isolation

---

**Total verify() Usage Locations**: 200+ occurrences across 5 test classes
**Pattern**: `verify(mockService, times(N)).method(args)`
**Most Common**: `times(1)` for success, `times(0)` for validation failures

