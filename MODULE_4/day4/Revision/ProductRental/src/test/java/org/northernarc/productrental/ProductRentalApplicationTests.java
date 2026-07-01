package org.northernarc.productrental;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.northernarc.productrental.model.*;
import org.northernarc.productrental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
public class ProductRentalApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RentalRecordRepository rentalRecordRepository;

    @Autowired
    private RentPaymentRepository rentPaymentRepository;

    private Customer testCustomer;
    private Product testProduct1;
    private Product testProduct2;

    @BeforeEach
    void setUpData() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        rentPaymentRepository.deleteAll();
        rentalRecordRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();

        // -------------------------------
        // Seed Customer
        // -------------------------------

        testCustomer = new Customer();
        testCustomer.setCustomerName("Rahul Sharma");
        testCustomer.setEmail("rahul@gmail.com");
        testCustomer.setPassword("$2a$10$X8x....");
        testCustomer.setCity("Chennai");

        testCustomer = customerRepository.save(testCustomer);

        // -------------------------------
        // Seed Product 1
        // -------------------------------

        testProduct1 = new Product();
        testProduct1.setProductName("Dell Latitude Laptop");
        testProduct1.setCategory("LAPTOP");
        testProduct1.setRentPerDay(800.0);
        testProduct1.setAvailable(true);

        testProduct1 = productRepository.save(testProduct1);

        // -------------------------------
        // Seed Product 2
        // -------------------------------

        testProduct2 = new Product();
        testProduct2.setProductName("Canon DSLR Camera");
        testProduct2.setCategory("CAMERA");
        testProduct2.setRentPerDay(500.0);
        testProduct2.setAvailable(true);

        testProduct2 = productRepository.save(testProduct2);
    }

    // ============================================================
    // TASK 1 : ENTITY MAPPING TESTS
    // ============================================================

    @Nested
    @DisplayName("Task 1: Entity Relationship & Cascade Validation")
    class EntityMappingTests {

        @Test
        @DisplayName("Should cascadingly delete RentalRecords and RentPayments when a Customer is deleted")
        void testCascadeDeleteCustomer() {

            RentalRecord rental = new RentalRecord();
            rental.setRentDate(LocalDate.now().minusDays(8));
            rental.setExpectedReturnDate(LocalDate.now().minusDays(2));
            rental.setActualReturnDate(null);
            rental.setStatus("OVERDUE");
            rental.setCustomer(testCustomer);
            rental.setProduct(testProduct1);

            rental = rentalRecordRepository.save(rental);

            RentPayment payment = new RentPayment();
            payment.setAmount(3200.0);
            payment.setPaymentMode(PaymentMode.ONLINE);
            payment.setPaymentDate(LocalDate.now());
            payment.setRentalRecord(rental);

            rentPaymentRepository.save(payment);

            customerRepository.delete(testCustomer);
            customerRepository.flush();

            assertThat(rentalRecordRepository.findById(rental.getRentalId())).isEmpty();
        }

    }

    // ============================================================
    // TASK 2 : VALIDATION TESTS
    // ============================================================

    @Nested
    @DisplayName("Task 2: Bean Validation & API Constraints")
    class ValidationTests {

        @Test
        @DisplayName("Should return 400 when Customer validation fails")
        void testCustomerValidationConstraints() throws Exception {

            Customer customer = new Customer();

            customer.setCustomerName("");
            customer.setEmail("wrong-email");
            customer.setPassword("123");

            mockMvc.perform(post("/api/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customer)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.errors",
                            hasSize(greaterThanOrEqualTo(1))));
        }

        @Test
        @DisplayName("Should return 400 when Product rentPerDay is negative")
        void testNegativeRentValidation() throws Exception {

            Product product = new Product();

            product.setProductName("Gaming Laptop");
            product.setCategory("LAPTOP");
            product.setRentPerDay(-500.0);
            product.setAvailable(true);

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(product)))
                    .andExpect(status().isBadRequest());
        }

    }

    // ============================================================
    // TASK 3 : DERIVED QUERY TESTS
    // ============================================================

    @Nested
    @DisplayName("Task 3: Spring Data JPA Derived Query Assertions")
    class DerivedQueryTests {

        @Test
        @DisplayName("Verify findByCategory works correctly")
        void testFindByCategory() {

            List<Product> laptops =
                    productRepository.findByCategory("LAPTOP");

            assertThat(laptops).hasSize(1);

            assertThat(laptops.get(0).getProductName())
                    .isEqualTo("Dell Latitude Laptop");
        }

        @Test
        @DisplayName("Verify findByCity works correctly")
        void testFindByCity() {

            List<Customer> customers =
                    customerRepository.findByCity("Chennai");

            assertThat(customers).hasSize(1);

            assertThat(customers.get(0).getCustomerName())
                    .isEqualTo("Rahul Sharma");
        }

        @Test
        @DisplayName("Verify findByRentPerDayGreaterThan works correctly")
        void testFindByRentPerDayGreaterThan() {

            List<Product> products =
                    productRepository.findByRentPerDayGreaterThan(600.0);

            assertThat(products).hasSize(1);

            assertThat(products.get(0).getProductName())
                    .isEqualTo("Dell Latitude Laptop");
        }

        @Test
        @DisplayName("Verify findByAvailable works correctly")
        void testFindByAvailable() {

            List<Product> products =
                    productRepository.findByAvailable(true);

            assertThat(products).hasSize(2);
        }

        @Test
        @DisplayName("Verify findByStatus works correctly")
        void testFindByStatus() {

            RentalRecord rental = new RentalRecord();

            rental.setRentDate(LocalDate.now());
            rental.setExpectedReturnDate(LocalDate.now().plusDays(5));
            rental.setActualReturnDate(null);
            rental.setStatus("ACTIVE");
            rental.setCustomer(testCustomer);
            rental.setProduct(testProduct1);

            rentalRecordRepository.save(rental);

            List<RentalRecord> activeRentals =
                    rentalRecordRepository.findByStatus(RentalStatus.ACTIVE);

            assertThat(activeRentals).hasSize(1);

            assertThat(activeRentals.get(0).getStatus())
                    .isEqualTo(RentalStatus.ACTIVE);
        }

    }

    // ============================================================
    // TASK 4 : JPQL CUSTOM QUERY TESTS
    // ============================================================

    @Nested
    @DisplayName("Task 4: Complex JPQL Query Operations")
    class JpqlQueryTests {

        @Test
        @DisplayName("JPQL : Find Customers having rentals more than a specified count")
        void testFindFrequentCustomers() {

            RentalRecord rental1 = new RentalRecord(
                    null,
                    LocalDate.now().minusDays(10),
                    LocalDate.now().minusDays(5),
                    null,
                    "RETURNED",
                    testCustomer,
                    testProduct1
            );

            RentalRecord rental2 = new RentalRecord(
                    null,
                    LocalDate.now().minusDays(3),
                    LocalDate.now().plusDays(2),
                    null,
                  "ACTIVE",
                    testCustomer,
                    testProduct2
            );

            rentalRecordRepository.saveAll(List.of(rental1, rental2));

            List<Customer> customers =
                    customerRepository.findFrequentCustomers(1L);

            assertThat(customers).hasSize(1);

            assertThat(customers.get(0).getCustomerName())
                    .isEqualTo("Rahul Sharma");
        }

        @Test
        @DisplayName("JPQL : Find Total Rent Collected Per City")
        void testFindTotalRentCollectedPerCity() {

            RentalRecord rental = new RentalRecord(
                    null,
                    LocalDate.now().minusDays(5),
                    LocalDate.now(),
                    LocalDate.now(),
                    "RETURNED",
                    testCustomer,
                    testProduct1
            );

            rental = rentalRecordRepository.save(rental);

            RentPayment payment = new RentPayment(
                    null,
                    4000.0,
                    PaymentMode.ONLINE,
                    LocalDate.now(),
                    rental
            );

            rentPaymentRepository.save(payment);

            List<Object[]> result =
                    customerRepository.findTotalRentCollectedPerCity();

            assertThat(result).isNotEmpty();

            Object[] row = result.get(0);

            assertThat(row[0]).isEqualTo("Chennai");

            assertThat((Double) row[1]).isEqualTo(4000.0);
        }

        @Test
        @DisplayName("JPQL : Find Customers Renting Multiple Categories")
        void testFindCustomersRentingMultipleCategories() {

            RentalRecord rental1 = new RentalRecord(
                    null,
                    LocalDate.now(),
                    LocalDate.now().plusDays(2),
                    null,
                    "ACTIVE",
                    testCustomer,
                    testProduct1
            );

            RentalRecord rental2 = new RentalRecord(
                    null,
                    LocalDate.now(),
                    LocalDate.now().plusDays(4),
                    null,
                    "ACTIVE",
                    testCustomer,
                    testProduct2
            );

            rentalRecordRepository.saveAll(
                    List.of(rental1, rental2));

            List<Customer> customers =
                    customerRepository
                            .findCustomersRentingMultipleCategories();

            assertThat(customers).hasSize(1);

            assertThat(customers.get(0).getCustomerName())
                    .isEqualTo("Rahul Sharma");
        }

        @Test
        @DisplayName("JPQL : Find Latest Rent Payment")
        void testFindLatestRentPayment() {

            RentalRecord rental = new RentalRecord(
                    null,
                    LocalDate.now(),
                    LocalDate.now().plusDays(2),
                    null,
                   "ACTIVE",
                    testCustomer,
                    testProduct1
            );

            rental = rentalRecordRepository.save(rental);

            RentPayment payment1 = new RentPayment(
                    null,
                    1000.0,
                    PaymentMode.CASH,
                    LocalDate.now().minusDays(3),
                    rental
            );

            RentPayment payment2 = new RentPayment(
                    null,
                    2500.0,
                    PaymentMode.ONLINE,
                    LocalDate.now(),
                    rental
            );

            rentPaymentRepository.save(payment1);
            rentPaymentRepository.save(payment2);

            List<RentPayment> latest =
                    rentPaymentRepository.findLatestRentPayment(
                            PageRequest.of(0,1));

            assertThat(latest).hasSize(1);

            assertThat(latest.get(0).getAmount())
                    .isEqualTo(2500.0);
        }

        @Test
        @DisplayName("JPQL : Find Products With No Overdue Rentals")
        void testFindProductsWithNoOverdueRentals() {

            List<Product> products =
                    productRepository.findProductsWithNoOverdueRentals();

            assertThat(products).hasSize(2);
        }

        @Test
        @DisplayName("JPQL : Find Products Currently Overdue")
        void testFindProductsCurrentlyOverdue() {

            RentalRecord rental = new RentalRecord();

            rental.setRentDate(LocalDate.now().minusDays(10));
            rental.setExpectedReturnDate(LocalDate.now().minusDays(3));
            rental.setActualReturnDate(null);
            rental.setStatus("OVERDUE");
            rental.setCustomer(testCustomer);
            rental.setProduct(testProduct1);

            rentalRecordRepository.save(rental);

            List<Product> overdueProducts =
                    productRepository.findProductsCurrentlyOverdue();

            assertThat(overdueProducts).hasSize(1);

            assertThat(overdueProducts.get(0).getProductName())
                    .isEqualTo("Dell Latitude Laptop");
        }

    }

    // ============================================================
    // TASK 5 : JPQL UPDATE TESTS
    // ============================================================

    @Nested
    @DisplayName("Task 5: Modifying JPQL Update Operations")
    class JpqlUpdateTests {

        @Test
        @DisplayName("Verify increaseRentPerDay() updates only selected category")
        void testIncreaseRentPerDay() {

            int updated =
                    productRepository.increaseRentPerDay(
                            "LAPTOP",
                            200.0);

            assertThat(updated).isEqualTo(1);

            productRepository.flush();

            Product updatedProduct =
                    productRepository.findById(
                                    testProduct1.getProductId())
                            .orElseThrow();

            assertThat(updatedProduct.getRentPerDay())
                    .isEqualTo(1000.0);

            Product untouchedProduct =
                    productRepository.findById(
                                    testProduct2.getProductId())
                            .orElseThrow();

            assertThat(untouchedProduct.getRentPerDay())
                    .isEqualTo(500.0);
        }

    }

    // ============================================================
    // TASK 6 : PAGINATION & SORTING
    // ============================================================

    @Nested
    @DisplayName("Task 6: Pagination & Sorting")
    class PaginationTests {

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("GET /products should return products sorted by rentPerDay DESC")
        void testGetProductsPaginatedAndSorted()
                throws Exception {

            mockMvc.perform(get("/api/products")
                            .param("page", "0")
                            .param("size", "10")
                            .contentType(MediaType.APPLICATION_JSON))

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.content",
                            hasSize(2)))

                    .andExpect(jsonPath(
                            "$.content[0].productName")
                            .value("Dell Latitude Laptop"))

                    .andExpect(jsonPath(
                            "$.content[1].productName")
                            .value("Canon DSLR Camera"));
        }

    }

    // ============================================================
    // TASK 7 : DTO PROJECTION TESTS
    // ============================================================

    @Nested
    @DisplayName("Task 7: DTO Projection Mapping")
    class DtoMappingTests {

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Verify CustomerRentalSummaryDTO Mapping")
        void testCustomerRentalSummaryDto()
                throws Exception {

            RentalRecord rental =
                    new RentalRecord(
                            null,
                            LocalDate.now(),
                            LocalDate.now().plusDays(5),
                            null,
                            "ACTIVE",
                            testCustomer,
                            testProduct1);

            rental = rentalRecordRepository.save(rental);

            RentPayment payment =
                    new RentPayment(
                            null,
                            4000.0,
                            PaymentMode.ONLINE,
                            LocalDate.now(),
                            rental);

            rentPaymentRepository.save(payment);

            mockMvc.perform(
                            get("/api/customers/"
                                    + testCustomer.getCustomerId()
                                    + "/summary")
                                    .contentType(MediaType.APPLICATION_JSON))

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.customerName")
                            .value("Rahul Sharma"))

                    .andExpect(jsonPath("$.city")
                            .value("Chennai"))

                    .andExpect(jsonPath("$.numberOfRentals")
                            .value(1))

                    .andExpect(jsonPath("$.totalRentPaid")
                            .value(4000.0));
        }

    }
    // ============================================================
    // TASK 8 : JWT AUTHENTICATION TESTS
    // ============================================================

    @Nested
    @DisplayName("Task 8: JWT Authentication Tests")
    class JwtAuthenticationTests {

        @Test
        @DisplayName("POST /login should return valid JWT token")
        void testSuccessfulLogin() throws Exception {

            Map<String, String> loginRequest = Map.of(
                    "email", "rahul@gmail.com",
                    "password", "password123"
            );

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists())
                    .andExpect(jsonPath("$.token",
                            startsWith("eyJ")));
        }

        @Test
        @DisplayName("Unauthenticated request should return 401")
        void testUnauthorizedAccess() throws Exception {

            mockMvc.perform(get("/api/products")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }


    // ============================================================
    // TASK 9 : ROLE BASED AUTHORIZATION
    // ============================================================

    @Nested
    @DisplayName("Task 9: Role Based Authorization")
    class AuthorizationTests {

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("USER should not delete Product")
        void testUserCannotDeleteProduct() throws Exception {

            mockMvc.perform(delete("/api/products/"
                            + testProduct1.getProductId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("ADMIN can delete Product")
        void testAdminCanDeleteProduct() throws Exception {

            mockMvc.perform(delete("/api/products/"
                            + testProduct1.getProductId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("MANAGER can update Rent Per Day")
        void testManagerCanUpdateRent() throws Exception {

            mockMvc.perform(
                            put("/api/products/"
                                    + testProduct1.getProductId()
                                    + "/rent")
                                    .param("amount", "100")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }


    // ============================================================
    // TASK 10 : GLOBAL EXCEPTION HANDLING
    // ============================================================

    @Nested
    @DisplayName("Task 10: Global Exception Handling")
    class ExceptionHandlingTests {

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("ProductNotFoundException should return 404")
        void testProductNotFoundException() throws Exception {

            mockMvc.perform(
                            get("/api/products/999999")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status")
                            .value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message")
                            .exists());
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("CustomerNotFoundException should return 404")
        void testCustomerNotFoundException() throws Exception {

            mockMvc.perform(
                            get("/api/customers/999999")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status")
                            .value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message")
                            .exists());
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("RentalRecordNotFoundException should return 404")
        void testRentalRecordNotFoundException() throws Exception {

            mockMvc.perform(
                            get("/api/rentals/999999")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status")
                            .value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message")
                            .exists());
        }

    }


    // ============================================================
    // FINAL CHALLENGE : DASHBOARD
    // ============================================================

    @Nested
    @DisplayName("Final Challenge: Dashboard Metrics")
    class DashboardTests {

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Dashboard should return all rental metrics")
        void testDashboardMetrics() throws Exception {

            RentalRecord rental =
                    new RentalRecord(
                            null,
                            LocalDate.now(),
                            LocalDate.now().plusDays(5),
                            null,
                            "ACTIVE",
                            testCustomer,
                            testProduct1);

            rental = rentalRecordRepository.save(rental);

            RentPayment payment =
                    new RentPayment(
                            null,
                            4000.0,
                            PaymentMode.ONLINE,
                            LocalDate.now(),
                            rental);

            rentPaymentRepository.save(payment);

            mockMvc.perform(get("/api/dashboard")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalCustomers")
                            .value(greaterThanOrEqualTo(1)))
                    .andExpect(jsonPath("$.totalProducts")
                            .value(greaterThanOrEqualTo(2)))
                    .andExpect(jsonPath("$.activeRentals")
                            .value(greaterThanOrEqualTo(1)))
                    .andExpect(jsonPath("$.totalRentCollected")
                            .value(4000.0))
                    .andExpect(jsonPath("$.topCategory")
                            .value("LAPTOP"))
                    .andExpect(jsonPath("$.highestPayingCustomer")
                            .value("Rahul Sharma"));
        }

    }

}







