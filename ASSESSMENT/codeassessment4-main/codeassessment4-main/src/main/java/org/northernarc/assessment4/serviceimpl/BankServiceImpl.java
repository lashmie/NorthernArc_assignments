package org.northernarc.assessment4.serviceimpl;

import org.northernarc.assessment4.dto.CustomerSummaryDTO;
import org.northernarc.assessment4.dto.DashboardResponse;
import org.northernarc.assessment4.exception.AccountNotFoundException;
import org.northernarc.assessment4.exception.CustomerNotFoundException;
import org.northernarc.assessment4.model.Account;
import org.northernarc.assessment4.model.Customer;
import org.northernarc.assessment4.model.Transaction;
import org.northernarc.assessment4.repository.AccountRepository;
import org.northernarc.assessment4.repository.CustomerRepository;
import org.northernarc.assessment4.repository.TransactionRepository;
import org.northernarc.assessment4.service.BankService;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankServiceImpl implements BankService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // --- Core Entity Writing Persistence Methods ---
    @Override
    @Transactional
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Page<Account> getAllAccountsPaginated(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountNumber) {
        if (!accountRepository.existsById(accountNumber)) {
            throw new AccountNotFoundException(accountNumber);
        }
        accountRepository.deleteById(accountNumber);
    }

    @Override
    public List<Account> getAccountsByType(String accountType) {
        return accountRepository.findByAccountType(accountType);
    }

    @Override
    public List<Customer> getCustomersByBranch(String branch) {
        return customerRepository.findByBranch(branch);
    }

    @Override
    public List<Transaction> getTransactionsByType(String transactionType) {
        return transactionRepository.findByTransactionType(transactionType);
    }

    @Override
    public List<Account> getAccountsWithBalanceGreaterThan(double amount) {
        return accountRepository.findByBalanceGreaterThan(amount);
    }

    @Override
    public List<Customer> getRichCustomers(double threshold) {
        return customerRepository.findRichCustomers(threshold);
    }

    @Override
    public Map<String, Double> getTotalBalancePerBranch() {
        Map<String, Double> result = new LinkedHashMap<>();
        for (Object[] row : customerRepository.findTotalBalancePerBranch()) {
            result.put((String) row[0], ((Number) row[1]).doubleValue());
        }
        return result;
    }

    @Override
    public List<Customer> getCustomersWithMultipleAccounts() {
        return customerRepository.findCustomersWithMultipleAccounts();
    }

    @Override
    public Transaction getLatestTransaction() {
        return transactionRepository.findLatestTransaction(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Account> getAccountsWithNoTransactions() {
        return accountRepository.findAccountsWithNoTransactions();
    }

    @Override
    @Transactional
    public void increaseAccountBalance(String accountNumber, double amount) {
        int updated = accountRepository.increaseBalance(accountNumber, amount);
        if (updated == 0) {
            throw new AccountNotFoundException(accountNumber);
        }
    }

    @Override
    public CustomerSummaryDTO getCustomerSummary(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        long numberOfAccounts = customer.getAccounts().size();
        double totalBalance = customer.getAccounts().stream()
                .map(Account::getBalance)
                .filter(balance -> balance != null)
                .mapToDouble(Double::doubleValue)
                .sum();

        return new CustomerSummaryDTO(customer.getCustomerName(), customer.getBranch(), numberOfAccounts, totalBalance);
    }

    @Override
    public DashboardResponse getDashboardMetrics() {
        Object[] row = customerRepository.findDashboardMetrics().stream()
                .findFirst()
                .orElse(new Object[]{0L, 0L, 0D, "N/A", "N/A"});

        long totalCustomers = ((Number) row[0]).longValue();
        long totalAccounts = ((Number) row[1]).longValue();
        double totalBalance = ((Number) row[2]).doubleValue();
        String topBranch = (String) row[3];
        String highestBalanceCustomer = (String) row[4];

        return new DashboardResponse(totalCustomers, totalAccounts, totalBalance, topBranch, highestBalanceCustomer);
    }


}
