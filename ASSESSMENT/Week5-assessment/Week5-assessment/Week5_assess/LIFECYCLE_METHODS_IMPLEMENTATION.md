# Lifecycle Methods Implementation Details

## 📋 What's Inside Each Lifecycle Method

### @BeforeEach - Reset Mock State
```java
@BeforeEach
void beforeEach() {
    // Reset mock state and clear any previous interactions with [Service]
    reset(mockService);
}
```

**Why**: 
- Ensures each test starts with a clean slate
- Removes any previous stub configurations
- Clears interaction history from previous tests
- Prevents test pollution and flaky tests

**When it runs**: 
- Before EVERY test method
- Guaranteed to reset between each test

**What it does**:
- Clears all stubbing (`.when()` configurations)
- Resets invocation count to zero
- Prepares mock for fresh test scenario

---

### @AfterEach - Verify Mock Interactions
```java
@AfterEach
void afterEach() {
    // Verify no unexpected interactions with mocks after each test
    verifyNoMoreInteractions(mockService);
}
```

**Why**:
- Catches unexpected method calls
- Ensures test isolation
- Detects side effects
- Fails test if mock was called unexpectedly

**When it runs**:
- After EVERY test method
- Even if test fails

**What it does**:
- Verifies mock was called exactly as expected
- Throws exception if there are extra calls
- Ensures clean mock interaction pattern

---

## 🔄 Full Test Execution with Lifecycle

```
Test Class Initialization
│
@BeforeAll (once)
│   └─ One-time setup (rarely used)
│
├─ Test 1:
│  ├─ @BeforeEach
│  │  └─ reset(mockService)           ← Clean state for test 1
│  ├─ Test method executes
│  │  └─ when(...).thenReturn(...)    ← Configure mock
│  │  └─ mockMvc.perform(...)         ← Make request
│  │  └─ verify(...).method(...)      ← Verify call
│  └─ @AfterEach
│     └─ verifyNoMoreInteractions()   ← Check no extra calls
│
├─ Test 2:
│  ├─ @BeforeEach
│  │  └─ reset(mockService)           ← Clean state for test 2
│  ├─ Test method executes
│  └─ @AfterEach
│     └─ verifyNoMoreInteractions()
│
├─ Test 3:
│  ├─ @BeforeEach
│  │  └─ reset(mockService)
│  ├─ Test method executes
│  └─ @AfterEach
│     └─ verifyNoMoreInteractions()
│
@AfterAll (once)
│   └─ One-time cleanup (rarely used)
│
Test Class Shutdown
```

---

## 📊 Before & After Comparison

### ❌ WITHOUT Lifecycle Methods

```java
@Test
void test1() throws Exception {
    // Mock might have state from other tests
    // Hard to reset between tests
    when(service.method()).thenReturn(response);
    mockMvc.perform(...).andExpect(...);
    // Didn't verify mock interactions
}

@Test
void test2() throws Exception {
    // Previous test's mock config might interfere
    // No guarantee of clean state
    mockMvc.perform(...).andExpect(...);
}
```

**Problems**:
- ❌ Tests might interfere with each other
- ❌ Mock state carries over
- ❌ No verification of mock interactions
- ❌ Hard to debug test failures
- ❌ Flaky tests

---

### ✅ WITH Lifecycle Methods

```java
@BeforeEach
void beforeEach() {
    reset(service);  // Always start clean
}

@AfterEach
void afterEach() {
    verifyNoMoreInteractions(service);  // Verify clean interaction
}

@Test
void test1() throws Exception {
    // Clean state guaranteed by @BeforeEach
    when(service.method()).thenReturn(response);
    mockMvc.perform(...).andExpect(...);
    // Verified clean by @AfterEach
}

@Test
void test2() throws Exception {
    // Always starts fresh - previous test cleared
    mockMvc.perform(...).andExpect(...);
    // Extra interactions would fail here
}
```

**Benefits**:
- ✅ Guaranteed clean state for each test
- ✅ No test pollution
- ✅ Explicit verification of mock behavior
- ✅ Easy to debug
- ✅ Reliable, non-flaky tests

---

## 🎯 Implementation in Each Test Class

### AuthenticationControllerTest
```java
@BeforeEach
void beforeEach() {
    // Reset authenticationService before each test
    reset(authenticationService);
}

@AfterEach
void afterEach() {
    // Verify no unexpected calls to authenticationService
    verifyNoMoreInteractions(authenticationService);
}
```

### AuthControllerTest
```java
@BeforeEach
void beforeEach() {
    // Reset authService before each test
    reset(authService);
}

@AfterEach
void afterEach() {
    // Verify no unexpected calls to authService
    verifyNoMoreInteractions(authService);
}
```

### CustomerControllerTest
```java
@BeforeEach
void beforeEach() {
    // Reset customerService before each test
    reset(customerService);
}

@AfterEach
void afterEach() {
    // Verify no unexpected calls to customerService
    verifyNoMoreInteractions(customerService);
}
```

### AccountControllerTest
```java
@BeforeEach
void beforeEach() {
    // Reset accountService before each test
    reset(accountService);
}

@AfterEach
void afterEach() {
    // Verify no unexpected calls to accountService
    verifyNoMoreInteractions(accountService);
}
```

### TransactionControllerTest
```java
@BeforeEach
void beforeEach() {
    // Reset transactionService before each test
    reset(transactionService);
}

@AfterEach
void afterEach() {
    // Verify no unexpected calls to transactionService
    verifyNoMoreInteractions(transactionService);
}
```

---

## 🚀 How It Works In Practice

### Example: AuthControllerTest - Register Tests

```java
@Nested
class RegisterTests {

    // ✅ Test 1: Valid Register
    @Test
    void register_whenValidRequest_shouldReturnCreated() throws Exception {
        // @BeforeEach already ran:
        //   reset(authService);  ← authService is clean
        
        RegisterRequest request = new RegisterRequest();
        request.setName("Asha Raman");
        
        // Configure mock for THIS test ONLY
        when(authService.register(request)).thenReturn(new AuthResponse("token"));
        
        mockMvc.perform(post("/auth/register")...)
            .andExpect(status().isCreated());
        
        verify(authService, times(1)).register(request);
        
        // @AfterEach now runs:
        //   verifyNoMoreInteractions(authService);  ← Check no extra calls
        //   ✅ PASS if only register() was called once
        //   ❌ FAIL if register() called more than once
    }

    // ✅ Test 2: Duplicate Email
    @Test
    void register_whenDuplicateEmail_shouldReturnConflict() throws Exception {
        // @BeforeEach already ran:
        //   reset(authService);  ← authService is clean (not affected by Test 1)
        
        RegisterRequest request = new RegisterRequest();
        
        // Configure mock for THIS test (different from Test 1)
        when(authService.register(request))
            .thenThrow(new DuplicateResourceException("exists"));
        
        mockMvc.perform(post("/auth/register")...)
            .andExpect(status().isConflict());
        
        verify(authService, times(1)).register(request);
        
        // @AfterEach now runs:
        //   verifyNoMoreInteractions(authService);
        //   ✅ PASS if only register() was called once
    }

    // ✅ Test 3: Invalid Email
    @Test
    void register_whenEmailInvalid_shouldReturnBadRequest() throws Exception {
        // @BeforeEach already ran:
        //   reset(authService);  ← Fresh clean state again
        
        mockMvc.perform(post("/auth/register")
            .content("{\"name\":\"Asha\",\"email\":\"bad\",\"password\":\"pass\"}"))
            .andExpect(status().isBadRequest());
        
        // No when() configured - service shouldn't be called for bad request
        verify(authService, times(0)).register(null);
        
        // @AfterEach now runs:
        //   verifyNoMoreInteractions(authService);
        //   ✅ PASS if register() was never called
    }
}
```

---

## 💡 What Each Method Prevents

### `reset(mockService)` Prevents:

✅ **Test Pollution**: Previous test's configurations affecting next test
✅ **Stubbing Carryover**: Old `.when()` configurations remaining
✅ **Invocation Counting**: Previous test's call counts affecting next test
✅ **Flaky Tests**: Tests passing/failing randomly due to state

### `verifyNoMoreInteractions(mockService)` Prevents:

✅ **Unexpected Calls**: Catching accidental method calls
✅ **Test Isolation**: Ensuring test only tests what it intends
✅ **Side Effects**: Detecting if code calls service unexpectedly
✅ **Logic Errors**: Catching bugs where service called more than needed

---

## 📚 Key Imports Required

```java
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;
```

---

## ✅ Verification

All 5 test classes now have:
- ✅ `reset(mockService)` in `@BeforeEach`
- ✅ `verifyNoMoreInteractions(mockService)` in `@AfterEach`
- ✅ Required imports added
- ✅ Documentation comments

---

**Last Updated**: July 8, 2026
**Status**: ✅ COMPLETE - Lifecycle methods fully implemented with meaningful code

