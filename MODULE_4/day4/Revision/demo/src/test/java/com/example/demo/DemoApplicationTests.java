package com.example.demo;


import com.example.demo.model.Book;
import com.example.demo.model.FineTransaction;
import com.example.demo.model.IssueRecord;
import com.example.demo.model.Member;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.FineTransactionRepository;
import com.example.demo.repository.IssueRecordRepository;
import com.example.demo.repository.MemberRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class DemoApplicationTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private IssueRecordRepository issueRecordRepository;
    @Autowired private FineTransactionRepository fineTransactionRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private Member testMember;
    private Book testBook1;
    private Book testBook2;

    @BeforeEach
    void setUpData() {
        fineTransactionRepository.deleteAll();
        issueRecordRepository.deleteAll();
        bookRepository.deleteAll();
        memberRepository.deleteAll();

        // Seed basic reference data for integration tests
        testMember = new Member();
        testMember.setMemberName("Amit Patel");
        testMember.setEmail("amit@library.com");
        testMember.setPassword(passwordEncoder.encode("password123"));
        testMember.setMembershipBranch("Central Library");
        testMember = memberRepository.save(testMember);

        testBook1 = new Book();
        testBook1.setIsbn("978-3-16-148410-0");
        testBook1.setTitle("Introduction to Algorithms");
        testBook1.setBookType("ACADEMIC");
        testBook1.setDailyFineRate(5.0);
        testBook1 = bookRepository.save(testBook1);

        testBook2 = new Book();
        testBook2.setIsbn("978-0-451-52493-5");
        testBook2.setTitle("1841");
        testBook2.setBookType("FICTION");
        testBook2.setDailyFineRate(1.5);
        testBook2 = bookRepository.save(testBook2);
    }

    // --- TASK 1: ENTITY MAPPING TESTS ---
    @Nested
    @DisplayName("Task 1: Entity Relationship & Cascade Validation")
    class EntityMappingTests {

        @Test
        @DisplayName("Should cascadingly delete IssueRecords and FineTransactions when a Member is deleted")
        void testCascadeDeleteMember() {
            IssueRecord record = new IssueRecord();
            record.setIssueDate(LocalDate.now().minusDays(5));
            record.setStatus("ISSUED");
            record.setMember(testMember);
            record.setBook(testBook1);
            record = issueRecordRepository.save(record);

            FineTransaction fine = new FineTransaction();
            fine.setAmount(15.0);
            fine.setPaymentType("CASH");
            fine.setPaymentDate(LocalDate.now());
            fine.setBook(testBook1);
            fineTransactionRepository.save(fine);

            memberRepository.delete(testMember);
//            memberRepository.flush();

            assertThat(issueRecordRepository.findById(record.getIssueId())).isEmpty();
        }
    }

    // --- TASK 2: BEAN VALIDATION TESTS ---
    @Nested
    @DisplayName("Task 2: Bean Validation Unit & API Constraints")
    class ValidationTests {

        @Test
        @DisplayName("Should return 400 Bad Request when member registrations break constraints")
        void testMemberValidationConstraints() throws Exception {
            Member invalidMember = new Member();
            invalidMember.setMemberName(""); // @NotBlank validation failure
            invalidMember.setEmail("not-an-email"); // @Email validation failure
            invalidMember.setPassword("123"); // @Size validation failure

            mockMvc.perform(post("/api/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidMember)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.errors", hasSize(greaterThanOrEqualTo(1))));
        }

        @Test
        @DisplayName("Should return 400 Bad Request if book daily fine rate is negative")
        void testNegativeFineRateValidation() throws Exception {
            Book invalidBook = new Book();
            invalidBook.setIsbn("978-0-123456-78-9");
            invalidBook.setTitle("Invalid Fine Book");
            invalidBook.setBookType("FICTION");
            invalidBook.setDailyFineRate(-2.5); // Broken @Positive/@PositiveOrZero validation

            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidBook)))
                    .andExpect(status().isBadRequest());
        }
    }

    // --- TASK 3: SPRING DATA JPA DERIVED QUERIES TESTS ---
    @Nested
    @DisplayName("Task 3: Spring Data JPA Derived Query Asserts")
    class DerivedQueryTests {

        @Test
        @DisplayName("Verify findByBookType derived mechanism works flawlessly")
        void testDerivedQueries() {
            List<Book> fictionBooks = bookRepository.findByBookType("FICTION");
            assertThat(fictionBooks).hasSize(1);
            assertThat(fictionBooks.get(0).getIsbn()).isEqualTo("978-0-451-52493-5");

            List<Member> centralMembers = memberRepository.findByMembershipBranch("Central Library");
            assertThat(centralMembers).hasSize(1);

            List<Book> highFineBooks = bookRepository.findByDailyFineRateGreaterThan(3.0);
            assertThat(highFineBooks).hasSize(1);
            assertThat(highFineBooks.get(0).getIsbn()).isEqualTo("978-3-16-148410-0");
        }
    }

    // --- TASK 4: JPQL CUSTOM COMPLEX QUERIES TESTS ---
    @Nested
    @DisplayName("Task 4: Complex JPQL Query Operations")
    class JpqlQueryTests {

        @Test
        @DisplayName("JPQL: Find Avid Readers exceeding a set structural circulation checkout threshold")
        void testFindAvidReaders() {
            IssueRecord record1 = new IssueRecord(null, LocalDate.now(), null, "ISSUED", testMember, testBook1);
            IssueRecord record2 = new IssueRecord(null, LocalDate.now(), null, "ISSUED", testMember, testBook2);
            issueRecordRepository.saveAll(List.of(record1, record2));

            List<Member> avidReaders = memberRepository.findAvidReaders(1L);
            assertThat(avidReaders).hasSize(1);
            assertThat(avidReaders.get(0).getMemberName()).isEqualTo("Amit Patel");
        }

        @Test
        @DisplayName("JPQL: Find aggregated total fines paid grouped per branch location")
        void testFindTotalFinesPaidPerBranch() {
            IssueRecord record = new IssueRecord(null, LocalDate.now(), null, "ISSUED", testMember, testBook1);
            issueRecordRepository.save(record);

            FineTransaction fine = new FineTransaction(null, 50.0, "ONLINE", LocalDate.now(), testBook1);
            fineTransactionRepository.save(fine);

            List<Object[]> finePerBranch = memberRepository.findTotalFinesPaidPerBranch();
            assertThat(finePerBranch).isNotEmpty();
            Object[] branchRow = finePerBranch.get(0);
            assertThat(branchRow[0]).isEqualTo("Central Library"); // Branch Name
            assertThat((Double) branchRow[1]).isEqualTo(50.0); // Accumulated fine total
        }

        @Test
        @DisplayName("JPQL: Find members borrowing multiple distinct book genres/categories")
        void testFindMembersHoldingMultiGenreBooks() {
            IssueRecord record1 = new IssueRecord(null, LocalDate.now(), null, "ISSUED", testMember, testBook1);
            IssueRecord record2 = new IssueRecord(null, LocalDate.now(), null, "ISSUED", testMember, testBook2);
            issueRecordRepository.saveAll(List.of(record1, record2));

            List<Member> multiGenreMembers = memberRepository.findMembersHoldingMultiGenreBooks();
            assertThat(multiGenreMembers).hasSize(1);
        }

        @Test
        @DisplayName("JPQL: Fetch the latest single fine transaction execution item logs")
        void testFindLatestFinePayment() {
            FineTransaction fine1 = new FineTransaction(null, 5.0, "CASH", LocalDate.now().minusDays(2), testBook1);
            fineTransactionRepository.save(fine1);

            FineTransaction fine2 = new FineTransaction(null, 25.0, "CARD", LocalDate.now(), testBook1);
            fineTransactionRepository.save(fine2);

            List<FineTransaction> latest = fineTransactionRepository.findLatestFinePayment(PageRequest.of(0, 1));
            assertThat(latest).hasSize(1);
            assertThat(latest.get(0).getAmount()).isEqualTo(25.0);
        }

        @Test
        @DisplayName("JPQL: Retrieve all system catalog books having zero fine history entries logged")
        void testFindBooksWithNoOverdueHistory() {
            List<Book> cleanBooks = bookRepository.findBooksWithNoOverdueHistory();
            // Neither seeded book has had fine transaction history attached yet in setup
            assertThat(cleanBooks).hasSize(2);
        }
    }

    // --- TASK 5: JPQL MODIFIER UPDATE TESTS ---
    @Nested
    @DisplayName("Task 5: Modifying In-place JPQL Target Update Invocations")
    class JpqlUpdateTests {

        @Test
        @DisplayName("Verify daily fine rate batch adjustments process fine via custom JPQL Modifiers")
        void testIncreaseDailyFineRates() {
            int itemsUpdated = bookRepository.increaseDailyFineRates("ACADEMIC", 2.0);
            assertThat(itemsUpdated).isEqualTo(1);

            bookRepository.flush();
            Book updatedBook = bookRepository.findById("978-3-16-148410-0").orElseThrow();
            assertThat(updatedBook.getDailyFineRate()).isEqualTo(7.0);

            // Re-verify that other items (like Book 2) were left entirely untouched
            Book untouchedBook = bookRepository.findById("978-0-451-52493-5").orElseThrow();
            assertThat(untouchedBook.getDailyFineRate()).isEqualTo(1.5);
        }
    }

    // --- TASK 6: PAGINATION & SORTING TESTS ---
    @Nested
    @DisplayName("Task 6: API Driven Pagination & Dynamic Sort Evaluations")
    class PaginationTests {

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("GET /books should yield sorted pages descending by raw daily fine rate rules")
        void testGetBooksPaginatedAndSorted() throws Exception {
            mockMvc.perform(get("/api/books")
                            .param("page", "0")
                            .param("size", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    // Daily Fine Rate DESC: Book 1 (5.0) must come before Book 2 (1.5)
                    .andExpect(jsonPath("$.content[0].isbn").value("978-3-16-148410-0"))
                    .andExpect(jsonPath("$.content[1].isbn").value("978-0-451-52493-5"));
        }
    }

    // --- TASK 7: DTO MAPPING CHECKS ---
    @Nested
    @DisplayName("Task 7: DTO Projection Mapping Layer Safeguards")
    class DtoMappingTests {

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Verify structural serialization matches MemberSummaryDTO expectations precisely")
        void testMemberSummaryDtoMapping() throws Exception {
            IssueRecord record1 = new IssueRecord(null, LocalDate.now(), null, "ISSUED", testMember, testBook1);
            issueRecordRepository.save(record1);

            FineTransaction fine = new FineTransaction(null, 50.0, "ONLINE", LocalDate.now(), testBook1);
            fineTransactionRepository.save(fine);

            mockMvc.perform(get("/api/members/" + testMember.getMemberId() + "/summary")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.memberName").value("Amit Patel"))
                    .andExpect(jsonPath("$.membershipBranch").value("Central Library"))
                    .andExpect(jsonPath("$.numberOfBorrowedBooks").value(1))
                    .andExpect(jsonPath("$.totalFinesPaid").value(50.0));
        }
    }

    // --- TASK 8: SECURITY & JWT PIECES ---
    @Nested
    @DisplayName("Task 8: End-to-End JWT Auth Engine Handshakes")
    class JwtAuthenticationTests {

        @Test
        @DisplayName("POST /login with valid details must serve back a valid JWT Bearer String")
        void testSuccessfulLogin() throws Exception {
            Map<String, String> loginCredentials = Map.of(
                    "email", "amit@library.com",
                    "password", "password123"
            );

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginCredentials)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists())
                    .andExpect(jsonPath("$.token").value(startsWith("eyJ")));
        }

        @Test
        @DisplayName("Accessing locked library actions with zero authentication headers must prompt a 401 Unauthorized status")
        void testUnauthenticatedAccessFails() throws Exception {
            mockMvc.perform(get("/api/books")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    // --- TASK 9: RBAC METHOD LEVEL AUTHORIZATION ---
    @Nested
    @DisplayName("Task 9: Method Level Role Based Access Validation Control")
    class AuthorizationTests {

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("USER Role must encounter a 403 Forbidden status when attempting to clear a book record")
        void testUserCannotDeleteBook() throws Exception {
            mockMvc.perform(delete("/api/books/978-3-16-148410-0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("ADMIN Role should be permitted to clear a book record successfully")
        void testAdminCanDeleteBook() throws Exception {
            mockMvc.perform(delete("/api/books/978-3-16-148410-0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("MANAGER Role should be permitted to perform structural configuration daily fine rate operations")
        void testManagerCanUpdateBook() throws Exception {
            mockMvc.perform(put("/api/books/978-0-451-52493-5/rate")
                            .param("amount", "2.0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    // --- TASK 10: ADVICE EXCEPTION CORES ---
    @Nested
    @DisplayName("Task 10: Global Exception Interception Handlers")
    class ExceptionHandlingTests {

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Should translate a missing member query into a normalized 404 Entity Not Found layout")
        void testMemberNotFoundExceptionHandling() throws Exception {
            mockMvc.perform(get("/api/members/999999/summary")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    // --- FINAL CHALLENGE: PERFORMANCE-OPTIMIZED METRICS DASHBOARD ---
    @Nested
    @DisplayName("Final Challenge: Highly Optimized Single Execution Operations Dashboard Metrics")
    class FinalChallengeTests {

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("GET /dashboard must correctly resolve complex aggregate metric fields without spawning sequential N+1 data queries")
        void testGetDashboardPerformanceMetrics() throws Exception {
            IssueRecord record = new IssueRecord(null, LocalDate.now(), null, "ISSUED", testMember, testBook1);
            issueRecordRepository.save(record);

            FineTransaction fine = new FineTransaction(null, 50.0, "ONLINE", LocalDate.now(), testBook1);
            fineTransactionRepository.save(fine);

            mockMvc.perform(get("/api/dashboard")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalMembers").value(greaterThanOrEqualTo(1)))
                    .andExpect(jsonPath("$.totalBooks").value(greaterThanOrEqualTo(2)))
                    .andExpect(jsonPath("$.totalFinesCollected").value(50.0))
                    .andExpect(jsonPath("$.topBranch").value("Central Library"))
                    .andExpect(jsonPath("$.highestFinePayingMember").value("Amit Patel"));
        }
    }
}
