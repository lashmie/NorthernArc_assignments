package org.northernarc.minion.service;

import org.northernarc.minion.dto.LoanCreateRequest;
import org.northernarc.minion.dto.DashboardDTO;
import org.northernarc.minion.dto.LoanDashboardDTO;
import org.northernarc.minion.dto.LoanSummaryDTO;
import org.northernarc.minion.exception.ResourceNotFoundException;
import org.northernarc.minion.model.Customer;
import org.northernarc.minion.model.EmiPayment;
import org.northernarc.minion.model.Loan;
import org.northernarc.minion.repository.CustomerRepository;
import org.northernarc.minion.repository.EmiPaymentRepository;
import org.northernarc.minion.repository.EmiScheduleRepository;
import org.northernarc.minion.repository.LoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final EmiPaymentRepository emiPaymentRepository;
    private final EmiScheduleRepository emiScheduleRepository;

    public LoanService(LoanRepository loanRepository,
                       CustomerRepository customerRepository,
                       EmiPaymentRepository emiPaymentRepository,
                       EmiScheduleRepository emiScheduleRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.emiPaymentRepository = emiPaymentRepository;
        this.emiScheduleRepository = emiScheduleRepository;
    }

    public Loan createLoan(LoanCreateRequest request) {
        if (request.principalAmount() == null || request.principalAmount() <= 0) {
            throw new IllegalArgumentException("principalAmount must be greater than zero");
        }
        if (request.tenureMonths() == null || request.tenureMonths() <= 0) {
            throw new IllegalArgumentException("tenureMonths must be greater than zero");
        }

        Loan loan = new Loan();
        loan.setLoanType(request.loanType());
        loan.setPrincipalAmount(request.principalAmount());
        loan.setAnnualInterestRate(request.annualInterestRate());
        loan.setTenureMonths(request.tenureMonths());
        loan.setEmiAmount(calculateEmi(request.principalAmount(), request.annualInterestRate(), request.tenureMonths()));
        loan.setDisbursementDate(LocalDate.now());
        loan.setLoanStatus("ACTIVE");

        if (request.customerId() != null) {
            Customer customer = customerRepository.findById(request.customerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + request.customerId()));
            loan.setCustomer(customer);
        }

        return loanRepository.save(loan);
    }

    public Page<LoanSummaryDTO> getLoansPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("principalAmount").descending());
        return loanRepository.findAll(pageable).map(this::toSummary);
    }

    public List<Object[]> getTotalEmiCollectionByCity() {
        return loanRepository.findTotalEmiCollectionByCity();
    }

    public List<LoanSummaryDTO> getLoansWithZeroOverdueEmis() {
        return loanRepository.findLoansWithZeroOverdueEmis().stream().map(this::toSummary).toList();
    }

    @Transactional
    public int updateInterestRateByLoanType(String loanType, Double rate) {
        return loanRepository.updateInterestRateByLoanType(loanType, rate);
    }

    @Transactional
    public Loan updateLoanInterestById(Long loanId, Double rate) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found: " + loanId));
        loan.setAnnualInterestRate(rate);
        return loanRepository.save(loan);
    }

    @Transactional
    public void deleteLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found: " + loanId));
        loanRepository.delete(loan);
    }

    public LoanDashboardDTO getDashboard() {
        List<Loan> activeLoans = loanRepository.findActiveLoans();
        List<Loan> zeroOverdue = loanRepository.findLoansWithZeroOverdueEmis();

        Map<String, Double> collectionByCity = new LinkedHashMap<>();
        for (Object[] row : loanRepository.findTotalEmiCollectionByCity()) {
            collectionByCity.put((String) row[0], (Double) row[1]);
        }

        Loan highest = loanRepository.findHighestOutstandingLoan();
        LoanSummaryDTO highestSummary = highest == null ? null : toSummary(highest);

        return new LoanDashboardDTO(
                activeLoans.size(),
                zeroOverdue.size(),
                loanRepository.findAverageInterestRate(),
                loanRepository.findTotalPenaltyCollected(),
                loanRepository.findTotalEmiCollected(),
                highestSummary,
                collectionByCity
        );
    }

    public DashboardDTO getFinalDashboard() {
        long totalCustomers = customerRepository.count();
        long totalLoans = loanRepository.count();
        long activeLoans = loanRepository.countByLoanStatus("ACTIVE");
        long closedLoans = loanRepository.countByLoanStatus("CLOSED");
        long overdueEmis = emiScheduleRepository.countByStatus("OVERDUE");

        Double totalEmiCollected = loanRepository.findTotalEmiCollected();
        Double totalPenaltyCollected = loanRepository.findTotalPenaltyCollected();
        Double averageInterestRate = loanRepository.findAverageInterestRate();

        List<Loan> highestRows = loanRepository.findHighestOutstandingWithCustomer(PageRequest.of(0, 1));
        LoanSummaryDTO highestOutstandingLoan = highestRows.isEmpty() ? null : toSummary(highestRows.get(0));

        String highestPayingCustomer = null;
        try {
            Object[] highestPayerRow = customerRepository.findHighestPayingCustomer();
            if (highestPayerRow != null && highestPayerRow.length > 0) {
                Object firstElement = highestPayerRow[0];
                if (firstElement instanceof String) {
                    highestPayingCustomer = (String) firstElement;
                }
            }
        } catch (Exception e) {
            // Handle query errors gracefully
            highestPayingCustomer = null;
        }

        long npaAccounts = loanRepository.countNpaAccounts();

        return new DashboardDTO(
                totalCustomers,
                totalLoans,
                activeLoans,
                closedLoans,
                overdueEmis,
                totalEmiCollected,
                totalPenaltyCollected,
                averageInterestRate,
                highestOutstandingLoan,
                highestPayingCustomer,
                npaAccounts
        );
    }

    public List<EmiPayment> getLatestPayments() {
        return emiPaymentRepository.findLatestPayment(PageRequest.of(0, 1));
    }

    private LoanSummaryDTO toSummary(Loan loan) {
        String customerName = loan.getCustomer() != null ? loan.getCustomer().getCustomerName() : null;
        String city = loan.getCustomer() != null ? loan.getCustomer().getCity() : null;

        return new LoanSummaryDTO(
                loan.getLoanId(),
                loan.getLoanType(),
                loan.getPrincipalAmount(),
                loan.getAnnualInterestRate(),
                loan.getTenureMonths(),
                loan.getEmiAmount(),
                loan.getDisbursementDate(),
                loan.getLoanStatus(),
                customerName,
                city
        );
    }

    private double calculateEmi(double principal, double annualInterestRate, int tenureMonths) {
        double monthlyRate = annualInterestRate / (12 * 100);
        double factor = Math.pow(1 + monthlyRate, tenureMonths);
        return (principal * monthlyRate * factor) / (factor - 1);
    }
}

