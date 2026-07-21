package org.northernarc.minion.repository;

import jakarta.transaction.Transactional;
import org.northernarc.minion.model.Loan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByLoanType(String loanType);

    List<Loan> findByCustomerCity(String city);

    List<Loan> findByLoanStatus(String loanStatus);

    List<Loan> findByPrincipalAmountGreaterThan(Double principalAmount);

    long countByLoanStatus(String loanStatus);

    @Query("SELECT l.customer.city, COALESCE(SUM(e.amountPaid), 0) FROM Loan l JOIN l.emiSchedules e GROUP BY l.customer.city")
    List<Object[]> findTotalEmiCollectionByCity();

    @Query("SELECT l FROM Loan l WHERE NOT EXISTS (SELECT e FROM EmiSchedule e WHERE e.loan = l AND e.status = 'OVERDUE')")
    List<Loan> findLoansWithZeroOverdueEmis();

    @Query("SELECT l FROM Loan l WHERE l.loanStatus = 'ACTIVE'")
    List<Loan> findActiveLoans();

    @Query("SELECT l.customer.city, COUNT(l) FROM Loan l GROUP BY l.customer.city")
    List<Object[]> findLoanCountPerCity();

    @Query("SELECT AVG(l.annualInterestRate) FROM Loan l")
    Double findAverageInterestRate();

    Loan findTopByOrderByPrincipalAmountDesc();

    @Query("SELECT l FROM Loan l LEFT JOIN FETCH l.customer ORDER BY l.principalAmount DESC")
    List<Loan> findHighestOutstandingWithCustomer(Pageable pageable);

    default Loan findHighestOutstandingLoan() {
        return findTopByOrderByPrincipalAmountDesc();
    }

    @Query("SELECT COALESCE(SUM(e.penaltyAmount), 0) FROM Loan l JOIN l.emiSchedules e")
    Double findTotalPenaltyCollected();

    @Query("SELECT COALESCE(SUM(e.amountPaid), 0) FROM Loan l JOIN l.emiSchedules e")
    Double findTotalEmiCollected();

    @Query("SELECT COUNT(DISTINCT l.loanId) FROM Loan l JOIN l.emiSchedules e WHERE e.status = 'OVERDUE'")
    long countNpaAccounts();

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Loan l SET l.annualInterestRate = :rate WHERE l.loanType = :loanType")
    int updateInterestRateByLoanType(@Param("loanType") String loanType, @Param("rate") Double rate);
}

