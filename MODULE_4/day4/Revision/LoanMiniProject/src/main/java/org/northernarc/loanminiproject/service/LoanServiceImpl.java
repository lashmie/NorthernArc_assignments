package org.northernarc.loanminiproject.service;

import org.northernarc.loanminiproject.dto.LoanDashboardDto;
import org.northernarc.loanminiproject.dto.LoanSummaryDto;
import org.northernarc.loanminiproject.model.EmiStatus;
import org.northernarc.loanminiproject.model.Loan;
import org.northernarc.loanminiproject.model.LoanStatus;
import org.northernarc.loanminiproject.repository.EmiScheduleRepository;
import org.northernarc.loanminiproject.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private EmiScheduleRepository emiScheduleRepository;

    @Override
    public int reviseInterestRate(String loanType, Double newRate) {
        return loanRepository.updateInterestRateByLoanType(loanType, newRate);
    }

    @Override
    public Page<Loan> getAllLoansOrderedByPrincipalDesc(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("principalAmount").descending()
        );
        return loanRepository.findAll(pageable);
    }

    @Override
    public Page<Loan> getAllLoans(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("principalAmount").descending()
        );
        return loanRepository.findAll(pageable);
    }

    @Override
    public List<Loan> findLoansByStatus(LoanStatus status) {
        return loanRepository.findByLoanStatus(status.name());
    }

    @Override
    public List<Loan> findLoansByType(String loanType) {
        return loanRepository.findByLoanType(loanType);
    }

    @Override
    public List<Loan> findLoansByCity(String city) {
        return loanRepository.findByCustomerCity(city);
    }

    @Override
    public List<Loan> findLoansWithZeroOverdueEmis() {
        return loanRepository.findLoansWithZeroOverdueEmis();
    }

    @Override
    public Page<LoanSummaryDto> getAllLoanSummaries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("principalAmount").descending());
        return loanRepository.findAllLoanSummaries(pageable);
    }

    @Override
    public List<LoanSummaryDto> getLoanSummariesByStatus(LoanStatus status) {
        return loanRepository.findLoanSummariesByStatus(status.name());
    }

    @Override
    public List<LoanSummaryDto> getLoanSummariesByType(String loanType) {
        return loanRepository.findLoanSummariesByType(loanType);
    }

    @Override
    public LoanDashboardDto getLoanDashboard() {
        List<Loan> allLoans = loanRepository.findAll();

        long totalLoans = allLoans.size();
        Double totalPrincipal = allLoans.stream()
                .mapToDouble(Loan::getPrincipalAmount)
                .sum();
        Double avgInterestRate = allLoans.stream()
                .mapToDouble(Loan::getAnnualInterestRate)
                .average()
                .orElse(0.0);

        long activeLoans = allLoans.stream()
                .filter(l -> LoanStatus.ACTIVE.name().equalsIgnoreCase(l.getLoanStatus()))
                .count();
        long closedLoans = allLoans.stream()
                .filter(l -> LoanStatus.CLOSED.name().equalsIgnoreCase(l.getLoanStatus()))
                .count();
        long defaultedLoans = allLoans.stream()
                .filter(l -> LoanStatus.DEFAULTED.name().equalsIgnoreCase(l.getLoanStatus()))
                .count();

        long pendingEmis = emiScheduleRepository.findByStatus(EmiStatus.PENDING.name()).size();
        long overdueEmis = emiScheduleRepository.findByStatus(EmiStatus.OVERDUE.name()).size();

        return new LoanDashboardDto(
                totalLoans,
                totalPrincipal,
                avgInterestRate,
                activeLoans,
                closedLoans,
                defaultedLoans,
                0.0,
                pendingEmis,
                overdueEmis
        );
    }
}