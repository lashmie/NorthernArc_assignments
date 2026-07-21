# JUnit 5 Lifecycle Annotations Guide

## Overview
JUnit 5 provides four lifecycle annotations to manage test setup and cleanup. These have been added to all test controller classes.

---

## Lifecycle Annotations Added

### 1. @BeforeAll
```java
@BeforeAll
static void beforeAll() {
    // Runs ONCE before ALL tests in the class
}
```
**Purpose**: One-time initialization before any tests run
**Usage**: 
- Set up expensive resources (database connections, file I/O, etc.)
- Initialize static data used across all tests
- **IMPORTANT**: Must be `static` method

**When it runs**: Before the first test executes (only once per test class)

---

### 2. @BeforeEach
```java
@BeforeEach
void beforeEach() {
    // Runs BEFORE EACH individual test method
}
```
**Purpose**: Prepare state before each test
**Usage**:
- Reset mock objects
- Clear test data
- Initialize test fixtures
- Reset counters or state variables

**When it runs**: Before every `@Test` method (runs N times for N tests)

---

### 3. @AfterEach
```java
@AfterEach
void afterEach() {
    // Runs AFTER EACH individual test method
}
```
**Purpose**: Clean up after each test
**Usage**:
- Release resources acquired in @BeforeEach
- Verify mock interactions
- Clear temporary files
- Reset state

**When it runs**: After every `@Test` method (runs N times for N tests)

---

### 4. @AfterAll
```java
@AfterAll
static void afterAll() {
    // Runs ONCE after ALL tests in the class
}
```
**Purpose**: Final cleanup after all tests complete
**Usage**:
- Release expensive resources (database connections)
- Clean up shared test data
- Generate reports
- **IMPORTANT**: Must be `static` method

**When it runs**: After the last test executes (only once per test class)

---

## Lifecycle Execution Order

```
@BeforeAll (ONCE)
    │
    ├─→ @BeforeEach → @Test 1 → @AfterEach
    ├─→ @BeforeEach → @Test 2 → @AfterEach
    ├─→ @BeforeEach → @Test 3 → @AfterEach
    └─→ @BeforeEach → @Test N → @AfterEach
    │
@AfterAll (ONCE)
```

---

## Example Usage Patterns

### Mocking Cleanup
```java
@BeforeEach
void beforeEach() {
    // Reset any mock state if needed
    reset(mockService);
}

@AfterEach
void afterEach() {
    // Verify mock was called correct number of times
    verify(mockService, atLeastOnce()).someMethod();
}
```

### Resource Management
```java
private TestResource resource;

@BeforeEach
void beforeEach() {
    resource = new TestResource();
    resource.initialize();
}

@AfterEach
void afterEach() {
    if (resource != null) {
        resource.cleanup();
    }
}
```

### Test Data Setup
```java
@BeforeEach
void beforeEach() {
    testData = new ArrayList<>();
    testData.add(new TestEntity(1L, "Test"));
}

@AfterEach
void afterEach() {
    testData.clear();
}
```

---

## Current Implementation in Test Classes

All five controller test classes have been updated with:

```java
@BeforeAll
static void beforeAll() {
    // One-time initialization before any test runs
}

@BeforeEach
void beforeEach() {
    // Reset any state before each test
}

@AfterEach
void afterEach() {
    // Cleanup after each test
}

@AfterAll
static void afterAll() {
    // One-time cleanup after all tests complete
}
```

### Test Classes Updated:
1. ✅ `AuthenticationControllerTest.java`
2. ✅ `AuthControllerTest.java`
3. ✅ `CustomerControllerTest.java`
4. ✅ `AccountControllerTest.java`
5. ✅ `TransactionControllerTest.java`

---

## Best Practices

### ✅ DO:
- Use `@BeforeEach` for per-test setup/cleanup
- Use `@BeforeAll` for expensive one-time setup
- Make `@BeforeAll` and `@AfterAll` methods `static`
- Keep lifecycle methods focused and simple
- Use descriptive names in lifecycle methods

### ❌ DON'T:
- Make `@BeforeEach` and `@AfterEach` static
- Share mutable state between tests without resetting
- Perform complex logic in lifecycle methods
- Create dependencies between tests
- Leave resources open without cleanup

---

## Comparison with JUnit 4

| JUnit 4 | JUnit 5 |
|---------|---------|
| @Before | @BeforeEach |
| @After | @AfterEach |
| @BeforeClass | @BeforeAll |
| @AfterClass | @AfterAll |

---

## Common Scenarios

### Scenario 1: Mock Verification
```java
@AfterEach
void afterEach() {
    // Verify service was called expected number of times
    verify(authService, times(1)).login(any());
}
```

### Scenario 2: Reset Mock State
```java
@BeforeEach
void beforeEach() {
    // Clear all previous interactions
    reset(authService, customerService, accountService);
}
```

### Scenario 3: Shared Test Data
```java
private static List<TestData> sharedData;

@BeforeAll
static void beforeAll() {
    sharedData = new ArrayList<>();
    // Initialize expensive shared data once
}

@AfterAll
static void afterAll() {
    sharedData.clear();
}
```

---

## Execution Guarantee

JUnit 5 guarantees that:
- ✅ `@BeforeAll` runs EXACTLY ONCE before first test
- ✅ `@BeforeEach` runs BEFORE EVERY `@Test`
- ✅ `@AfterEach` runs AFTER EVERY `@Test`
- ✅ `@AfterAll` runs EXACTLY ONCE after last test
- ✅ Order is always: BeforeAll → (BeforeEach → Test → AfterEach)* → AfterAll

---

## Documentation Format

Each lifecycle method now includes a comment explaining its purpose:

```java
// Setup method called once before all tests in this class
@BeforeAll
static void beforeAll() {
    // One-time initialization before any test runs
}

// Setup method called before each test method
@BeforeEach
void beforeEach() {
    // Reset any state before each test
}

// Cleanup method called after each test method
@AfterEach
void afterEach() {
    // Cleanup after each test
}

// Cleanup method called once after all tests in this class
@AfterAll
static void afterAll() {
    // One-time cleanup after all tests complete
}
```

---

**Last Updated**: July 8, 2026
**Applied To**: All 5 Controller Test Classes
**Total Lifecycle Methods Added**: 20 (4 per class × 5 classes)

