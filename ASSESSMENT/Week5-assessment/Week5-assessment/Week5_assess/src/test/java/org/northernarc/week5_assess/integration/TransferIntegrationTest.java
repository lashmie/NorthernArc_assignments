package org.northernarc.week5_assess.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.repository.AccountRepository;
import org.northernarc.week5_assess.repository.CustomerRepository;
import org.northernarc.week5_assess.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("TransferIntegrationTest")
class TransferIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Account sourceAccount;
    private Account destinationAccount;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();

        Customer c1 = customerRepository.save(customer("Asha", "asha@northernarc.org", "9000000001"));
        Customer c2 = customerRepository.save(customer("Ravi", "ravi@northernarc.org", "9000000002"));

        sourceAccount = accountRepository.save(account(c1, "ACC-1001", "SAVINGS", "500.00"));
        destinationAccount = accountRepository.save(account(c2, "ACC-2001", "CURRENT", "100.00"));
    }

    @Nested
    @DisplayName("Deposit Flow")
    class DepositFlowTests {

        @Test
        @DisplayName("should deposit successfully and create transaction record")
        void shouldDepositSuccessfullyAndCreateTransactionRecord() throws Exception {
            // Arrange
            long beforeTx = transactionRepository.count();

            // Act + Assert
            mockMvc.perform(post("/api/accounts/deposit")
                    .with(jwt())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(depositRequest(sourceAccount.getId(), "50.00"))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sourceAccount.getId()));

            // Assert DB state
            Account refreshed = accountRepository.findById(sourceAccount.getId()).orElseThrow();
            assertThat(refreshed.getBalance()).isEqualByComparingTo("550.00");
            assertThat(transactionRepository.count()).isEqualTo(beforeTx + 1);
        }

        @Test
        @DisplayName("should reject zero and negative deposits")
        void shouldRejectZeroAndNegativeDeposits() throws Exception {
            mockMvc.perform(post("/api/accounts/deposit").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(depositRequest(sourceAccount.getId(), "0.00"))))
                .andExpect(status().isBadRequest());

            mockMvc.perform(post("/api/accounts/deposit").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(depositRequest(sourceAccount.getId(), "-10.00"))))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return not found for unknown account deposit")
        void shouldReturnNotFoundForUnknownDepositAccount() throws Exception {
            mockMvc.perform(post("/api/accounts/deposit").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(depositRequest(99999L, "10.00"))))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should return bad request for invalid deposit request body")
        void shouldReturnBadRequestForInvalidDepositRequestBody() throws Exception {
            mockMvc.perform(post("/api/accounts/deposit").with(jwt()).contentType(APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());

            mockMvc.perform(post("/api/accounts/deposit").with(jwt()).contentType(APPLICATION_JSON).content("{\"accountId\":"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(post("/api/accounts/deposit").with(jwt()).content("{}"))
                .andExpect(status().isUnsupportedMediaType());
        }
    }

    @Nested
    @DisplayName("Withdrawal Flow")
    class WithdrawalFlowTests {

        @Test
        @DisplayName("should withdraw successfully and create transaction")
        void shouldWithdrawSuccessfullyAndCreateTransaction() throws Exception {
            long beforeTx = transactionRepository.count();

            mockMvc.perform(post("/api/accounts/withdraw")
                    .with(jwt())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(withdrawRequest(sourceAccount.getId(), "40.00"))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

            Account refreshed = accountRepository.findById(sourceAccount.getId()).orElseThrow();
            assertThat(refreshed.getBalance()).isEqualByComparingTo("460.00");
            assertThat(transactionRepository.count()).isEqualTo(beforeTx + 1);
        }

        @Test
        @DisplayName("should handle withdraw edge cases")
        void shouldHandleWithdrawEdgeCases() throws Exception {
            mockMvc.perform(post("/api/accounts/withdraw").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(withdrawRequest(sourceAccount.getId(), "500.00"))))
                .andExpect(status().isOk());

            mockMvc.perform(post("/api/accounts/withdraw").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(withdrawRequest(sourceAccount.getId(), "1.00"))))
                .andExpect(status().isBadRequest());

            mockMvc.perform(post("/api/accounts/withdraw").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(withdrawRequest(sourceAccount.getId(), "0.00"))))
                .andExpect(status().isBadRequest());

            mockMvc.perform(post("/api/accounts/withdraw").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(withdrawRequest(sourceAccount.getId(), "-1.00"))))
                .andExpect(status().isBadRequest());

            mockMvc.perform(post("/api/accounts/withdraw").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(withdrawRequest(99999L, "1.00"))))
                .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Transfer Flow")
    class TransferFlowTests {

        @Test
        @DisplayName("should transfer successfully and create two transactions")
        void shouldTransferSuccessfullyAndCreateTwoTransactions() throws Exception {
            long beforeTx = transactionRepository.count();

            mockMvc.perform(post("/api/accounts/transfer")
                    .with(jwt())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transferRequest(sourceAccount.getId(), destinationAccount.getId(), "75.00"))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

            Account src = accountRepository.findById(sourceAccount.getId()).orElseThrow();
            Account dst = accountRepository.findById(destinationAccount.getId()).orElseThrow();
            assertThat(src.getBalance()).isEqualByComparingTo("425.00");
            assertThat(dst.getBalance()).isEqualByComparingTo("175.00");
            assertThat(transactionRepository.count()).isEqualTo(beforeTx + 2);
        }

        @Test
        @DisplayName("should handle transfer edge cases")
        void shouldHandleTransferEdgeCases() throws Exception {
            mockMvc.perform(post("/api/accounts/transfer").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transferRequest(99999L, destinationAccount.getId(), "10.00"))))
                .andExpect(status().isNotFound());

            mockMvc.perform(post("/api/accounts/transfer").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transferRequest(sourceAccount.getId(), 99999L, "10.00"))))
                .andExpect(status().isNotFound());

            mockMvc.perform(post("/api/accounts/transfer").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transferRequest(sourceAccount.getId(), sourceAccount.getId(), "10.00"))))
                .andExpect(status().isBadRequest());

            mockMvc.perform(post("/api/accounts/transfer").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transferRequest(sourceAccount.getId(), destinationAccount.getId(), "0.00"))))
                .andExpect(status().isBadRequest());

            mockMvc.perform(post("/api/accounts/transfer").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transferRequest(sourceAccount.getId(), destinationAccount.getId(), "-1.00"))))
                .andExpect(status().isBadRequest());

            mockMvc.perform(post("/api/accounts/transfer").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transferRequest(sourceAccount.getId(), destinationAccount.getId(), "999999.00"))))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should keep balances unchanged and no partial transactions when transfer fails")
        void shouldKeepBalancesUnchangedOnFailedTransfer() throws Exception {
            BigDecimal sourceBefore = accountRepository.findById(sourceAccount.getId()).orElseThrow().getBalance();
            BigDecimal destinationBefore = accountRepository.findById(destinationAccount.getId()).orElseThrow().getBalance();
            long txBefore = transactionRepository.count();

            mockMvc.perform(post("/api/accounts/transfer")
                    .with(jwt())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transferRequest(sourceAccount.getId(), destinationAccount.getId(), "999999.00"))))
                .andExpect(status().isBadRequest());

            BigDecimal sourceAfter = accountRepository.findById(sourceAccount.getId()).orElseThrow().getBalance();
            BigDecimal destinationAfter = accountRepository.findById(destinationAccount.getId()).orElseThrow().getBalance();
            long txAfter = transactionRepository.count();

            assertThat(sourceAfter).isEqualByComparingTo(sourceBefore);
            assertThat(destinationAfter).isEqualByComparingTo(destinationBefore);
            assertThat(txAfter).isEqualTo(txBefore);
        }
    }

    @Nested
    @DisplayName("Transaction History")
    class HistoryTests {

        @Test
        @DisplayName("should return transaction history in correct order")
        void shouldReturnTransactionHistoryInCorrectOrder() throws Exception {
            // Arrange
            mockMvc.perform(post("/api/accounts/deposit").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(depositRequest(sourceAccount.getId(), "10.00"))))
                .andExpect(status().isOk());
            mockMvc.perform(post("/api/accounts/withdraw").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(withdrawRequest(sourceAccount.getId(), "5.00"))))
                .andExpect(status().isOk());

            // Act + Assert
            mockMvc.perform(get("/api/accounts/{id}/transactions", sourceAccount.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
        }

        @Test
        @DisplayName("should return empty list when no transactions")
        void shouldReturnEmptyListWhenNoTransactions() throws Exception {
            mockMvc.perform(get("/api/accounts/{id}/transactions", destinationAccount.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("should return not found for unknown account history")
        void shouldReturnNotFoundForUnknownAccountHistory() throws Exception {
            mockMvc.perform(get("/api/accounts/{id}/transactions", 99999L).with(jwt()))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should return bad request for invalid account id history")
        void shouldReturnBadRequestForInvalidAccountIdHistory() throws Exception {
            mockMvc.perform(get("/api/accounts/{id}/transactions", "abc").with(jwt()))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Security and Media")
    class SecurityAndMediaTests {

        @Test
        @DisplayName("should return unauthorized when secured endpoint called without jwt")
        void shouldReturnUnauthorizedWithoutJwt() throws Exception {
            mockMvc.perform(post("/api/accounts/deposit")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(depositRequest(sourceAccount.getId(), "10.00"))))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("should return unsupported media type for transfer without json content type")
        void shouldReturnUnsupportedMediaTypeForTransferWithoutJson() throws Exception {
            mockMvc.perform(post("/api/accounts/transfer")
                    .with(jwt())
                    .content("{}"))
                .andExpect(status().isUnsupportedMediaType());
        }
    }

    private static Customer customer(String name, String email, String phone) {
        Customer c = new Customer();
        c.setName(name);
        c.setEmail(email);
        c.setPhoneNumber(phone);
        c.setPassword("encoded-pass");
        return c;
    }

    private static Account account(Customer customer, String accountNumber, String accountType, String balance) {
        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setBalance(new BigDecimal(balance));
        return account;
    }

    private static Map<String, Object> depositRequest(Long accountId, String amount) {
        Map<String, Object> req = new HashMap<>();
        req.put("accountId", accountId);
        req.put("amount", new BigDecimal(amount));
        return req;
    }

    private static Map<String, Object> withdrawRequest(Long accountId, String amount) {
        Map<String, Object> req = new HashMap<>();
        req.put("accountId", accountId);
        req.put("amount", new BigDecimal(amount));
        return req;
    }

    private static Map<String, Object> transferRequest(Long sourceId, Long destinationId, String amount) {
        Map<String, Object> req = new HashMap<>();
        req.put("sourceAccountId", sourceId);
        req.put("destinationAccountId", destinationId);
        req.put("amount", new BigDecimal(amount));
        return req;
    }
}
