package org.northernarc.loan.service;

import org.northernarc.loan.dto.EmiPaymentRequest;
import org.northernarc.loan.dto.LoanApprovalRequest;
import org.northernarc.loan.dto.LoanDashboardDTO;
import org.northernarc.loan.dto.LoanSummaryDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface LoanService {

    LoanSummaryDTO approveLoan(LoanApprovalRequest request);

    LoanSummaryDTO payEmi(EmiPaymentRequest request);

    Page<LoanSummaryDTO> getLoans(int page, int size);

    LoanDashboardDTO getDashboard();

    int reviseInterestRateByLoanType(String loanType, Double newRate);

    List<LoanSummaryDTO> getLoansByLoanType(String loanType);

    List<LoanSummaryDTO> getLoansByCity(String city);

    List<LoanSummaryDTO> getLoansByStatus(String status);

    List<LoanSummaryDTO> getLoansByPrincipalAmountGreaterThan(Double amount);

    void refreshOverdueStatuses(LocalDate asOnDate);
}

