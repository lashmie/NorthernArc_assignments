package org.northernarc.loan.serviceimpl;

import org.northernarc.loan.dto.EmiPaymentRequest;
import org.northernarc.loan.dto.LoanApprovalRequest;
import org.northernarc.loan.dto.LoanDashboardDTO;
import org.northernarc.loan.dto.LoanSummaryDTO;
import org.northernarc.loan.exception.BusinessRuleException;
import org.northernarc.loan.model.Customer;
import org.northernarc.loan.model.EmiPayment;
import org.northernarc.loan.model.EmiSchedule;
import org.northernarc.loan.model.Loan;
import org.northernarc.loan.repository.CustomerRepository;
import org.northernarc.loan.repository.EmiScheduleRepository;
import org.northernarc.loan.repository.LoanRepository;
import org.northernarc.loan.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    private static final double PENALTY_RATE = 0.02;
    private static final double DAILY_PENALTY = 50.0;

    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final EmiScheduleRepository emiScheduleRepository;

    public LoanServiceImpl(CustomerRepository customerRepository,
                           LoanRepository loanRepository,
                           EmiScheduleRepository emiScheduleRepository,
                           org.northernarc.loan.repository.EmiPaymentRepository ignoredEmiPaymentRepository) {
        this.customerRepository = customerRepository;
        this.loanRepository = loanRepository;
        this.emiScheduleRepository = emiScheduleRepository;
    }

    @Override
    public LoanSummaryDTO approveLoan(LoanApprovalRequest request) {
        if (request.getPrincipalAmount() <= 0.0) {
            throw new BusinessRuleException("Loan cannot be approved for principal amount less than or equal to zero");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new BusinessRuleException("Customer not found: " + request.getCustomerId()));

        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setLoanType(request.getLoanType());
        loan.setPrincipalAmount(round2(request.getPrincipalAmount()));
        loan.setAnnualInterestRate(round2(request.getAnnualInterestRate()));
        loan.setTenureMonths(request.getTenureMonths());
        loan.setDisbursementDate(request.getDisbursementDate());
        loan.setLoanStatus("ACTIVE");
        loan.setEmiAmount(calculateEmi(loan.getPrincipalAmount(), loan.getAnnualInterestRate(), loan.getTenureMonths()));

        List<EmiSchedule> schedules = generateEmiSchedules(loan);
        for (EmiSchedule schedule : schedules) {
            loan.addEmiSchedule(schedule);
        }

        Loan saved = loanRepository.save(loan);
        return toLoanSummary(saved);
    }

    @Override
    public LoanSummaryDTO payEmi(EmiPaymentRequest request) {
        EmiSchedule emi = emiScheduleRepository.findById(request.getEmiId())
                .orElseThrow(() -> new BusinessRuleException("EMI schedule not found: " + request.getEmiId()));

        Loan loan = emi.getLoan();
        if ("CLOSED".equalsIgnoreCase(loan.getLoanStatus())) {
            throw new BusinessRuleException("Closed loans cannot accept further EMI payments");
        }

        LocalDate paymentDate = request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now();
        recalculatePenalty(emi, paymentDate);

        double alreadyPaid = nvl(emi.getAmountPaid());
        double paymentAmount = round2(request.getAmount());
        double totalPayable = round2(nvl(emi.getAmountDue()) + nvl(emi.getPenaltyAmount()));

        if (round2(alreadyPaid + paymentAmount) > totalPayable) {
            throw new BusinessRuleException("Payment exceeds total amount payable for this EMI");
        }

        EmiPayment payment = new EmiPayment();
        payment.setAmount(paymentAmount);
        payment.setPaymentMode(request.getPaymentMode());
        payment.setPaymentDate(paymentDate);
        payment.setReferenceNumber(request.getReferenceNumber());
        emi.addEmiPayment(payment);

        emi.setAmountPaid(round2(alreadyPaid + paymentAmount));
        if (Math.abs(emi.getAmountPaid() - totalPayable) < 0.01) {
            emi.setStatus("PAID");
            emi.setPaymentDate(paymentDate);
        } else if (paymentDate.isAfter(emi.getDueDate())) {
            emi.setStatus("OVERDUE");
        } else {
            emi.setStatus("PENDING");
        }

        emiScheduleRepository.save(emi);
        updateLoanClosureStatus(loan);
        return toLoanSummary(loan);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanSummaryDTO> getLoans(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "principalAmount"));
        return loanRepository.findAll(pageable).map(this::toLoanSummary);
    }

    @Override
    public LoanDashboardDTO getDashboard() {
        refreshOverdueStatuses(LocalDate.now());
        return LoanDashboardDTO.builder()
                .totalLoans(loanRepository.countAllLoans())
                .activeLoans(loanRepository.countActiveLoans())
                .closedLoans(loanRepository.countClosedLoans())
                .overdueEmis(emiScheduleRepository.countByStatus("OVERDUE"))
                .totalEmiCollected(round2(nvl(loanRepository.findTotalEmiCollected())))
                .totalPenaltyCollected(round2(nvl(loanRepository.findTotalPenaltyCollected())))
                .build();
    }

    @Override
    public int reviseInterestRateByLoanType(String loanType, Double newRate) {
        if (newRate < 0.0) {
            throw new BusinessRuleException("Annual interest rate cannot be negative");
        }
        return loanRepository.reviseAnnualInterestRateByLoanType(loanType, round2(newRate));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanSummaryDTO> getLoansByLoanType(String loanType) {
        return loanRepository.findByLoanType(loanType).stream().map(this::toLoanSummary).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanSummaryDTO> getLoansByCity(String city) {
        return loanRepository.findByCustomerCity(city).stream().map(this::toLoanSummary).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanSummaryDTO> getLoansByStatus(String status) {
        return loanRepository.findByLoanStatus(status).stream().map(this::toLoanSummary).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanSummaryDTO> getLoansByPrincipalAmountGreaterThan(Double amount) {
        return loanRepository.findByPrincipalAmountGreaterThan(amount).stream().map(this::toLoanSummary).toList();
    }

    @Override
    public void refreshOverdueStatuses(LocalDate asOnDate) {
        List<String> pendingStates = List.of("PENDING", "OVERDUE");
        List<EmiSchedule> items = emiScheduleRepository.findByStatusInAndDueDateBefore(pendingStates, asOnDate);
        for (EmiSchedule emiSchedule : items) {
            recalculatePenalty(emiSchedule, asOnDate);
        }
        emiScheduleRepository.saveAll(items);
    }

    private List<EmiSchedule> generateEmiSchedules(Loan loan) {
        List<EmiSchedule> schedules = new ArrayList<>();
        double outstanding = nvl(loan.getPrincipalAmount());
        double monthlyRate = monthlyRate(loan.getAnnualInterestRate());

        for (int installment = 1; installment <= loan.getTenureMonths(); installment++) {
            double interest = round2(outstanding * monthlyRate);
            double principal = round2(loan.getEmiAmount() - interest);

            if (installment == loan.getTenureMonths()) {
                principal = round2(outstanding);
            }

            double amountDue = round2(principal + interest);

            EmiSchedule emi = new EmiSchedule();
            emi.setInstallmentNumber(installment);
            emi.setDueDate(loan.getDisbursementDate().plusMonths(installment));
            emi.setAmountDue(amountDue);
            emi.setPrincipalComponent(principal);
            emi.setInterestComponent(interest);
            emi.setAmountPaid(0.0);
            emi.setStatus("PENDING");
            emi.setDaysPastDue(0);
            emi.setPenaltyAmount(0.0);

            schedules.add(emi);
            outstanding = Math.max(0.0, round2(outstanding - principal));
        }

        return schedules;
    }

    private Double calculateEmi(Double principal, Double annualRate, int tenureMonths) {
        double monthlyRate = monthlyRate(annualRate);
        if (monthlyRate == 0.0) {
            return round2(principal / tenureMonths);
        }

        double emiValue = (principal * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths))
                / (Math.pow(1 + monthlyRate, tenureMonths) - 1);
        return round2(emiValue);
    }

    private double monthlyRate(Double annualRate) {
        return annualRate / (12.0 * 100.0);
    }

    private void recalculatePenalty(EmiSchedule emi, LocalDate currentDate) {
        if ("PAID".equalsIgnoreCase(emi.getStatus())) {
            return;
        }

        if (!currentDate.isAfter(emi.getDueDate())) {
            emi.setDaysPastDue(0);
            emi.setPenaltyAmount(0.0);
            if (nvl(emi.getAmountPaid()) < nvl(emi.getAmountDue())) {
                emi.setStatus("PENDING");
            }
            return;
        }

        long days = ChronoUnit.DAYS.between(emi.getDueDate(), currentDate);
        double penalty = round2(nvl(emi.getAmountDue()) * PENALTY_RATE + (days * DAILY_PENALTY));

        emi.setDaysPastDue((int) days);
        emi.setPenaltyAmount(penalty);

        double totalPayable = round2(nvl(emi.getAmountDue()) + penalty);
        if (nvl(emi.getAmountPaid()) + 0.0001 < totalPayable) {
            emi.setStatus("OVERDUE");
        }
    }

    private void updateLoanClosureStatus(Loan loan) {
        boolean allPaid = loan.getEmiSchedules().stream().allMatch(es -> "PAID".equalsIgnoreCase(es.getStatus()));
        loan.setLoanStatus(allPaid ? "CLOSED" : "ACTIVE");
        loanRepository.save(loan);
    }

    private LoanSummaryDTO toLoanSummary(Loan loan) {
        long overdueCount = loan.getEmiSchedules().stream()
                .filter(es -> "OVERDUE".equalsIgnoreCase(es.getStatus()))
                .count();

        double outstandingPrincipal = loan.getEmiSchedules().stream()
                .filter(es -> !"PAID".equalsIgnoreCase(es.getStatus()))
                .mapToDouble(es -> nvl(es.getPrincipalComponent()))
                .sum();

        return LoanSummaryDTO.builder()
                .loanId(loan.getLoanId())
                .customerId(loan.getCustomer().getCustomerId())
                .customerName(loan.getCustomer().getCustomerName())
                .loanType(loan.getLoanType())
                .principalAmount(loan.getPrincipalAmount())
                .annualInterestRate(loan.getAnnualInterestRate())
                .tenureMonths(loan.getTenureMonths())
                .emiAmount(loan.getEmiAmount())
                .disbursementDate(loan.getDisbursementDate())
                .loanStatus(loan.getLoanStatus())
                .overdueEmiCount(overdueCount)
                .outstandingPrincipal(round2(outstandingPrincipal))
                .build();
    }

    private double nvl(Double value) {
        return value == null ? 0.0 : value;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
