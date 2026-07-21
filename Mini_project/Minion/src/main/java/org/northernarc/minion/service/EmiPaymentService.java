package org.northernarc.minion.service;

import org.northernarc.minion.dto.EmiPaymentRequest;
import org.northernarc.minion.exception.BusinessRuleViolationException;
import org.northernarc.minion.exception.ResourceNotFoundException;
import org.northernarc.minion.model.EmiPayment;
import org.northernarc.minion.model.EmiSchedule;
import org.northernarc.minion.model.Loan;
import org.northernarc.minion.repository.EmiPaymentRepository;
import org.northernarc.minion.repository.EmiScheduleRepository;
import org.northernarc.minion.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class EmiPaymentService {

    private final EmiScheduleRepository emiScheduleRepository;
    private final EmiPaymentRepository emiPaymentRepository;
    private final LoanRepository loanRepository;

    public EmiPaymentService(EmiScheduleRepository emiScheduleRepository,
                             EmiPaymentRepository emiPaymentRepository,
                             LoanRepository loanRepository) {
        this.emiScheduleRepository = emiScheduleRepository;
        this.emiPaymentRepository = emiPaymentRepository;
        this.loanRepository = loanRepository;
    }

    @Transactional
    public EmiSchedule payEmi(Long emiId, EmiPaymentRequest request) {
        EmiSchedule emi = emiScheduleRepository.findByIdForUpdate(emiId)
                .orElseThrow(() -> new ResourceNotFoundException("EMI not found: " + emiId));

        Loan loan = emi.getLoan();
        if (loan != null && "CLOSED".equalsIgnoreCase(loan.getLoanStatus())) {
            throw new BusinessRuleViolationException("Closed loans cannot accept further EMI payments");
        }

        if (request.amount() == null || request.amount() <= 0) {
            throw new BusinessRuleViolationException("Payment amount must be greater than zero");
        }

        refreshPenaltyIfOverdue(emi);

        double totalPayable = emi.getAmountDue() + emi.getPenaltyAmount();
        double pendingAmount = totalPayable - emi.getAmountPaid();
        if (request.amount() > pendingAmount + 0.0001) {
            throw new BusinessRuleViolationException("Over-payment is not allowed");
        }

        EmiPayment payment = new EmiPayment();
        payment.setAmount(request.amount());
        payment.setPaymentMode(request.paymentMode());
        payment.setPaymentDate(LocalDate.now());
        payment.setReferenceNumber(request.referenceNumber() == null || request.referenceNumber().isBlank()
                ? "PAY-" + UUID.randomUUID()
                : request.referenceNumber());
        payment.setEmiSchedule(emi);
        emiPaymentRepository.save(payment);

        emi.setAmountPaid(emi.getAmountPaid() + request.amount());

        if (emi.getAmountPaid() + 0.0001 >= totalPayable) {
            emi.setStatus("PAID");
            emi.setPaymentDate(LocalDate.now());
            emi.setDaysPastDue(Math.max(0, emi.getDaysPastDue() == null ? 0 : emi.getDaysPastDue()));
        } else {
            emi.setStatus(LocalDate.now().isAfter(emi.getDueDate()) ? "OVERDUE" : "PENDING");
        }

        EmiSchedule saved = emiScheduleRepository.save(emi);
        closeLoanIfFullyPaid(saved.getLoan());
        return saved;
    }

    @Transactional
    public EmiSchedule recalculatePenalty(Long emiId) {
        EmiSchedule emi = emiScheduleRepository.findByIdForUpdate(emiId)
                .orElseThrow(() -> new ResourceNotFoundException("EMI not found: " + emiId));
        refreshPenaltyIfOverdue(emi);
        return emiScheduleRepository.save(emi);
    }

    private void refreshPenaltyIfOverdue(EmiSchedule emi) {
        if (emi.getAmountPaid() >= emi.getAmountDue()) {
            if (emi.getPaymentDate() != null && !emi.getPaymentDate().isAfter(emi.getDueDate())) {
                emi.setPenaltyAmount(0);
            }
            return;
        }

        LocalDate today = LocalDate.now();
        if (today.isAfter(emi.getDueDate())) {
            int dpd = (int) ChronoUnit.DAYS.between(emi.getDueDate(), today);
            double penalty = (emi.getAmountDue() * 0.02) + (dpd * 50);
            emi.setDaysPastDue(dpd);
            emi.setPenaltyAmount(penalty);
            emi.setStatus("OVERDUE");
        } else {
            emi.setDaysPastDue(0);
            emi.setPenaltyAmount(0);
            emi.setStatus("PENDING");
        }
    }

    private void closeLoanIfFullyPaid(Loan loan) {
        if (loan == null) {
            return;
        }
        boolean allPaid = loan.getEmiSchedules().stream().allMatch(e -> "PAID".equalsIgnoreCase(e.getStatus()));
        if (allPaid) {
            loan.setLoanStatus("CLOSED");
            loanRepository.save(loan);
        }
    }
}

