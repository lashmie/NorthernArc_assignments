r tellsomo# JUnit 5 Lifecycle Quick Reference Card

## ⚡ Quick Overview

| Annotation | When | Runs | Static | Usage |
|-----------|------|------|--------|-------|
| **@BeforeAll** | Before all tests | Once | YES ✅ | Expensive setup |
| **@BeforeEach** | Before each test | N times | NO | Test prep |
| **@AfterEach** | After each test | N times | NO | Cleanup |
| **@AfterAll** | After all tests | Once | YES ✅ | Resource release |

---

## 🔄 Execution Flow

```
START
  │
  ├─ @BeforeAll (once)
  │   │
  │   ├─ @BeforeEach
  │   │   └─ @Test 1
  │   │   └─ @AfterEach
  │   │
  │   ├─ @BeforeEach
  │   │   └─ @Test 2
  │   │   └─ @AfterEach
  │   │
  │   └─ ...repeat for each test...
  │
  ├─ @AfterAll (once)
  │
END
```

---

## 📝 Syntax & Examples

### @BeforeAll (Static Method)
```java
@BeforeAll
static void beforeAll() {
    // One-time setup
    System.out.println("Running once before ALL tests");
}
```

### @BeforeEach (Instance Method)
```java
@BeforeEach
void beforeEach() {
    // Setup before each test
    System.out.println("Running before EACH test");
}
```

### @AfterEach (Instance Method)
```java
@AfterEach
void afterEach() {
    // Cleanup after each test
    System.out.println("Running after EACH test");
}
```

### @AfterAll (Static Method)
```java
@AfterAll
static void afterAll() {
    // Final cleanup
    System.out.println("Running once after ALL tests");
}
```

---

## 💡 Common Use Cases

### 1. Mock Reset
```java
@BeforeEach
void beforeEach() {
    reset(mockService);  // Reset mocks before each test
}
```

### 2. Verify Mock Calls
```java
@AfterEach
void afterEach() {
    verify(mockService, times(1)).someMethod();  // Check after test
}
```

### 3. Resource Setup
```java
private TestResource resource;

@BeforeEach
void beforeEach() {
    resource = new TestResource();
    resource.initialize();
}

@AfterEach
void afterEach() {
    resource.cleanup();
}
```

### 4. Shared Data
```java
private static List<Data> sharedData;

@BeforeAll
static void beforeAll() {
    sharedData = new ArrayList<>();
    // Initialize expensive shared data
}

@AfterAll
static void afterAll() {
    sharedData.clear();
}
```

---

## ❌ Common Mistakes

| ❌ WRONG | ✅ CORRECT |
|---------|----------|
| `@BeforeAll void setup()` | `@BeforeAll static void setup()` |
| `@AfterAll void cleanup()` | `@AfterAll static void cleanup()` |
| Non-static `@BeforeAll` | Static `@BeforeAll` |
| Sharing state between tests | Reset state in `@BeforeEach` |
| Heavy logic in `@BeforeEach` | Move to `@BeforeAll` |
| Forgetting `@AfterEach` cleanup | Always cleanup resources |

---

## 🎯 Test Class Template

```java
@WebMvcTest(SomeController.class)
@DisplayName("SomeControllerTest")
class SomeControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private SomeService service;

    @BeforeAll
    static void beforeAll() {
        // One-time expensive setup
    }

    @BeforeEach
    void beforeEach() {
        // Setup before each test
    }

    @AfterEach
    void afterEach() {
        // Cleanup after each test
    }

    @AfterAll
    static void afterAll() {
        // Final cleanup after all tests
    }

    @Test
    @DisplayName("should test something")
    void testSomething() throws Exception {
        // Test implementation
    }
}
```

---

## 📊 Test Execution Example

```
Class Initialization
  ↓
@BeforeAll ← Runs ONCE
  ↓
Test 1:
  ├─ @BeforeEach ← Runs before test 1
  ├─ Test logic
  └─ @AfterEach ← Runs after test 1
  ↓
Test 2:
  ├─ @BeforeEach ← Runs before test 2
  ├─ Test logic
  └─ @AfterEach ← Runs after test 2
  ↓
Test 3:
  ├─ @BeforeEach ← Runs before test 3
  ├─ Test logic
  └─ @AfterEach ← Runs after test 3
  ↓
@AfterAll ← Runs ONCE
  ↓
Class Cleanup
```

---

## 🚀 Rules Summary

✅ **DO**:
- Make `@BeforeAll` and `@AfterAll` **STATIC**
- Make `@BeforeEach` and `@AfterEach` instance methods
- Reset mocks in `@BeforeEach`
- Clean up resources in `@AfterEach`
- Use `@BeforeAll` for expensive setup only
- Keep lifecycle methods focused

❌ **DON'T**:
- Forget `static` on `@BeforeAll`/@AfterAll`
- Share mutable state between tests
- Put complex logic in lifecycle methods
- Create test dependencies
- Leave resources uncleaned

---

## 📍 Where Lifecycle Annotations Are Used

All 5 test classes now have lifecycle methods:

1. ✅ **AuthenticationControllerTest.java**
   - Lines: `@BeforeAll`, `@BeforeEach`, `@AfterEach`, `@AfterAll`

2. ✅ **AuthControllerTest.java**
   - Lines: `@BeforeAll`, `@BeforeEach`, `@AfterEach`, `@AfterAll`

3. ✅ **CustomerControllerTest.java**
   - Lines: `@BeforeAll`, `@BeforeEach`, `@AfterEach`, `@AfterAll`

4. ✅ **AccountControllerTest.java**
   - Lines: `@BeforeAll`, `@BeforeEach`, `@AfterEach`, `@AfterAll`

5. ✅ **TransactionControllerTest.java**
   - Lines: `@BeforeAll`, `@BeforeEach`, `@AfterEach`, `@AfterAll`

---

## 🔗 Related Documentation

- See `JUNIT5_LIFECYCLE_GUIDE.md` for detailed explanation
- See `TEST_DOCUMENTATION.md` for test coverage details
- See `MOCKITO_VERIFICATION_GUIDE.md` for verification patterns
- See `COMPLETE_TEST_UPDATES.md` for full summary

---

**Quick Tip**: If you forget which methods should be `static`, remember:
- `@BeforeAll` = Before ALL tests = Static ✅
- `@AfterAll` = After ALL tests = Static ✅
- `@BeforeEach` = Before EACH test = Instance method ❌
- `@AfterEach` = After EACH test = Instance method ❌

---

**Last Updated**: July 8, 2026

