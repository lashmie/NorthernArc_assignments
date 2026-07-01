package org.northernarc.loanminiproject.service;

import org.northernarc.loanminiproject.dto.LoanDashboardDto;
import org.northernarc.loanminiproject.dto.LoanSummaryDto;
import org.northernarc.loanminiproject.model.Loan;
import org.northernarc.loanminiproject.model.LoanStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LoanService {

    int reviseInterestRate(String loanType, Double newRate);

    Page<Loan> getAllLoansOrderedByPrincipalDesc(int page, int size);

    Page<Loan> getAllLoans(int page, int size);

    List<Loan> findLoansByStatus(LoanStatus status);

    List<Loan> findLoansByType(String loanType);

    List<Loan> findLoansByCity(String city);

    List<Loan> findLoansWithZeroOverdueEmis();

    Page<LoanSummaryDto> getAllLoanSummaries(int page, int size);

    List<LoanSummaryDto> getLoanSummariesByStatus(LoanStatus status);

    List<LoanSummaryDto> getLoanSummariesByType(String loanType);

    LoanDashboardDto getLoanDashboard();
}
