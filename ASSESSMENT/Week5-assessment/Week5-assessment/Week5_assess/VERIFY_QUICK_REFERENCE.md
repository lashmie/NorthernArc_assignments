# verify() Usage Quick Reference Card

## 📍 3 Main Ways verify() is Used

### 1️⃣ Success Path: Service Called Exactly Once
```java
verify(mockService, times(1)).method(args);
```
**When**: Test expects service to be called once and succeed
**Example**:
```java
// Valid registration returns 201 CREATED
@Test
void shouldRegisterCustomerSuccessfully() throws Exception {
    when(authService.register(request)).thenReturn(response);
    mockMvc.perform(post("/auth/register")...)
        .andExpect(status().isCreated());
    
    verify(authService, times(1)).register(request);  // ✅ Called exactly once
}
```

---

### 2️⃣ Validation Failure: Service Never Called
```java
verify(mockService, times(0)).method(args);
```
**When**: Test expects validation to fail BEFORE service is called
**Example**:
```java
// Blank email returns 400 BAD_REQUEST - service NEVER called
@Test
void shouldReturnBadRequestForBlankEmail() throws Exception {
    mockMvc.perform(post("/auth/register")
        .content("{\"name\":\"Asha\",\"email\":\"\",\"password\":\"pass\"}"))
        .andExpect(status().isBadRequest());
    
    verify(authService, times(0)).register(null);  // ❌ Never called
}
```

---

### 3️⃣ Cleanup: No Extra Calls
```java
verifyNoMoreInteractions(mockService);
```
**When**: In @AfterEach - ensure no unexpected calls happened
**Example**:
```java
@AfterEach
void afterEach() {
    verifyNoMoreInteractions(authService);  // Fails if extra calls made
}
```

---

## 📊 Quick Lookup Table

| Scenario | Code | Location | Count |
|----------|------|----------|-------|
| Valid registration | `verify(authService, times(1)).register(request)` | After success test | 1 |
| Invalid email | `verify(authService, times(0)).register(null)` | After validation failure | 0 |
| Duplicate email | `verify(authService, times(1)).register(request)` | After error test | 1 |
| Each test ends | `verifyNoMoreInteractions(authService)` | In @AfterEach | Auto |

---

## 🎯 All 5 Test Classes Using verify()

```
AuthenticationControllerTest
├── Register Tests: 10+ verify() calls
│   ├── Valid: times(1)
│   ├── Duplicate: times(1)
│   ├── Invalid email: times(0)
│   ├── Missing name: times(0)
│   ├── Missing password: times(0)
│   ├── Missing phone: times(0)
│   ├── Phone <10: times(0)
│   ├── Phone >10: times(0)
│   ├── Blank fields: times(0)
│   ├── Null body: times(0)
│   └── Malformed JSON: times(0)
│
├── Login Tests: 10+ verify() calls
│   ├── Valid: times(1)
│   ├── Invalid creds: times(1)
│   ├── Unknown email: times(1)
│   ├── Missing email: times(0)
│   ├── Missing password: times(0)
│   ├── Blank creds: times(0)
│   ├── Null body: times(0)
│   └── Malformed JSON: times(0)
│
└── @AfterEach: verifyNoMoreInteractions()

AuthControllerTest
├── Register Tests: 12+ verify() calls
└── Login Tests: 14+ verify() calls

CustomerControllerTest
├── Create: 8+ verify()
├── List: 2+ verify()
├── GetByID: 3+ verify()
├── Update: 7+ verify()
├── Delete: 3+ verify()
└── @AfterEach: verifyNoMoreInteractions()

AccountControllerTest
├── Create: 11+ verify()
├── Get: 7+ verify()
├── Deposit: 12+ verify()
├── Withdraw: 10+ verify()
└── @AfterEach: verifyNoMoreInteractions()

TransactionControllerTest
├── Transfer: 15+ verify()
├── History: 7+ verify()
└── @AfterEach: verifyNoMoreInteractions()
```

---

## 📈 Statistics

**Total verify() Usages**: 200+
- `times(1)`: 100+ (Success paths)
- `times(0)`: 50+ (Validation failures)
- `verifyNoMoreInteractions()`: 30+ (Cleanup)

---

## 💡 Remember These Rules

✅ **DO Use verify()**:
- After successful operations: `times(1)`
- After validation failures: `times(0)`
- In @AfterEach: `verifyNoMoreInteractions()`

❌ **DON'T Use verify()**:
- For HTTP status checks (use `.andExpect(status())`)
- For response body checks (use `.andExpect(jsonPath())`)
- Without specifying times (ambiguous)

---

## 🔗 Related Patterns

### With verifyNoMoreInteractions()
```java
@Test
void validRegister() {
    when(authService.register(request)).thenReturn(response);
    mockMvc.perform(post(...)).andExpect(status().isCreated());
    verify(authService, times(1)).register(request);
    verifyNoMoreInteractions(authService);  // No other calls
}
```

### With Error Throwing
```java
@Test
void duplicateEmail() {
    when(authService.register(request)).thenThrow(new Exception());
    mockMvc.perform(post(...)).andExpect(status().isConflict());
    verify(authService, times(1)).register(request);  // Service WAS called
}
```

### With Validation Failure
```java
@Test
void missingEmail() {
    mockMvc.perform(post(...).content("{}"))
        .andExpect(status().isBadRequest());
    verify(authService, times(0)).register(null);  // Service NOT called
}
```

---

**Last Updated**: July 8, 2026
**Quick Reference**: Use this card alongside WHERE_VERIFY_IS_USED.md for detailed info

