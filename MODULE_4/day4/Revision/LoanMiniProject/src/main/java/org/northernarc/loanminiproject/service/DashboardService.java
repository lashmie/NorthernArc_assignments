package org.northernarc.loanminiproject.service;

import org.northernarc.loanminiproject.dto.DashboardDto;
import org.northernarc.loanminiproject.dto.CustomerDetailDto;
import org.northernarc.loanminiproject.dto.LoanDetailDto;
import org.northernarc.loanminiproject.repository.CustomerRepository;
import org.northernarc.loanminiproject.repository.EmiPaymentRepository;
import org.northernarc.loanminiproject.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmiPaymentRepository emiPaymentRepository;

    public DashboardDto getDashboardMetrics() {
        DashboardDto dashboard = new DashboardDto();

        // Total Customers (single query)
        dashboard.setTotalCustomers(customerRepository.countTotalCustomers());

        // Total Loans (single query)
        dashboard.setTotalLoans(loanRepository.countTotalLoans());

        // Active Loans (single query)
        dashboard.setActiveLoans(loanRepository.countActiveLoans());

        // Closed Loans (single query)
        dashboard.setClosedLoans(loanRepository.countClosedLoans());

        // Defaulted Loans (single query)
        dashboard.setDefaultedLoans(loanRepository.countNPAAccounts());

        // Overdue EMIs (single query)
        dashboard.setOverdueEmis(loanRepository.countLoansWithOverdueEmis());

        // Total EMI Collected (single query)
        Double totalEmiCollected = emiPaymentRepository.getTotalEmiCollected();
        dashboard.setTotalEmiCollected(totalEmiCollected != null ? totalEmiCollected : 0.0);

        // Total Penalty Collected (single query)
        Double totalPenaltyCollected = emiPaymentRepository.getTotalPenaltyCollected();
        dashboard.setTotalPenaltyCollected(totalPenaltyCollected != null ? totalPenaltyCollected : 0.0);

        // Average Interest Rate (single query)
        Double avgInterestRate = loanRepository.findAverageInterestRate();
        dashboard.setAverageInterestRate(avgInterestRate != null ? avgInterestRate : 0.0);

        // Highest Outstanding Loan (single query with JOIN - no N+1)
        LoanDetailDto highestLoan = loanRepository.findHighestOutstandingLoanDetails(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);
        dashboard.setHighestOutstandingLoan(highestLoan);

        // Highest Paying Customer (single query with LEFT JOINs - no N+1)
        CustomerDetailDto highestCustomer = customerRepository.findHighestPayingCustomer(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);
        dashboard.setHighestPayingCustomer(highestCustomer);

        // NPA Accounts (Non-Performing Assets)
        dashboard.setNpaAccounts(loanRepository.countNPAAccounts());

        return dashboard;
    }
}

