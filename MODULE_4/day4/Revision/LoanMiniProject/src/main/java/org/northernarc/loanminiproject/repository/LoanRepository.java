package org.northernarc.loanminiproject.repository;

import jakarta.transaction.Transactional;
import org.northernarc.loanminiproject.dto.LoanDetailDto;
import org.northernarc.loanminiproject.dto.LoanSummaryDto;
import org.northernarc.loanminiproject.dto.PaymentSummaryDto;
import org.northernarc.loanminiproject.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

	List<Loan> findByLoanType(String loanType);

	List<Loan> findByCustomerCity(String city);

	List<Loan> findByLoanStatus(String loanStatus);

	List<Loan> findByPrincipalAmountGreaterThan(Double principalAmount);

	List<Loan> findByPrincipalAmountBetween(Double minPrincipalAmount, Double maxPrincipalAmount);

	@Query("""
			SELECT l
			FROM Loan l
			WHERE l.loanStatus = 'ACTIVE'
			""")
	List<Loan> findActiveLoans();

	@Query("""
			SELECT l
			FROM Loan l
			WHERE NOT EXISTS (
				SELECT e
				FROM EmiSchedule e
				WHERE e.loan = l
				  AND e.status = 'OVERDUE'
			)
			""")
	List<Loan> findLoansWithZeroOverdueEmis();

	@Modifying
	@Transactional
	@Query("UPDATE Loan l SET l.annualInterestRate = :newRate WHERE l.loanType = :loanType")
	int updateInterestRateByLoanType(@Param("loanType") String loanType,
	                                       @Param("newRate") Double newRate);

	@Query("""
			SELECT c.city, COUNT(l)
			FROM Loan l
			JOIN l.customer c
			GROUP BY c.city
			""")
	List<Object[]> findLoanCountPerCity();

	@Query("""
			SELECT AVG(l.annualInterestRate)
			FROM Loan l
			""")
	Double findAverageInterestRate();

	@Query("""
			SELECT COALESCE(SUM(ep.amount), 0.0)
			FROM EmiPayment ep
			""")
	Double findTotalEmiCollected();

	@Query("""
			SELECT COALESCE(SUM(e.penaltyAmount), 0.0)
			FROM EmiSchedule e
			""")
	Double findTotalPenaltyCollected();

	@Query("""
			SELECT c.city,
			       COALESCE(SUM(p.amount), 0.0)
			FROM EmiPayment p
			JOIN p.emiSchedule e
			JOIN e.loan l
			JOIN l.customer c
			GROUP BY c.city
			ORDER BY SUM(p.amount) DESC
			""")
	List<Object[]> findTotalEmiCollectionByCity();

	@Query("""
			SELECT NEW org.northernarc.loanminiproject.dto.LoanSummaryDto(
				l.loanId,
				l.loanType,
				l.principalAmount,
				l.annualInterestRate,
				l.tenureMonths,
				l.emiAmount,
				l.loanStatus,
				l.disbursementDate,
				c.customerName,
				c.email,
				c.phoneNumber
			)
			FROM Loan l
			JOIN l.customer c
			ORDER BY l.principalAmount DESC
			""")
	Page<LoanSummaryDto> findAllLoanSummaries(Pageable pageable);

	@Query("""
			SELECT NEW org.northernarc.loanminiproject.dto.LoanSummaryDto(
				l.loanId,
				l.loanType,
				l.principalAmount,
				l.annualInterestRate,
				l.tenureMonths,
				l.emiAmount,
				l.loanStatus,
				l.disbursementDate,
				c.customerName,
				c.email,
				c.phoneNumber
			)
			FROM Loan l
			JOIN l.customer c
			WHERE l.loanStatus = :status
			""")
	List<LoanSummaryDto> findLoanSummariesByStatus(@Param("status") String status);

	@Query("""
			SELECT NEW org.northernarc.loanminiproject.dto.LoanSummaryDto(
				l.loanId,
				l.loanType,
				l.principalAmount,
				l.annualInterestRate,
				l.tenureMonths,
				l.emiAmount,
				l.loanStatus,
				l.disbursementDate,
				c.customerName,
				c.email,
				c.phoneNumber
			)
			FROM Loan l
			JOIN l.customer c
			WHERE l.loanType = :loanType
			""")
	List<LoanSummaryDto> findLoanSummariesByType(@Param("loanType") String loanType);

	@Query("""
			SELECT COUNT(DISTINCT l)
			FROM Loan l
			""")
	Long countTotalLoans();

	@Query("""
			SELECT COUNT(l)
			FROM Loan l
			WHERE l.loanStatus = 'ACTIVE'
			""")
	Long countActiveLoans();

	@Query("""
			SELECT COUNT(l)
			FROM Loan l
			WHERE l.loanStatus = 'CLOSED'
			""")
	Long countClosedLoans();


	@Query("""
			SELECT COUNT(DISTINCT l)
			FROM Loan l
			JOIN l.emiSchedules e
			WHERE e.status = 'OVERDUE'
			""")
	Long countLoansWithOverdueEmis();

	@Query("""
			SELECT l
			FROM Loan l
			LEFT JOIN l.emiSchedules e
			LEFT JOIN e.payments ep
			GROUP BY l.loanId
			ORDER BY COALESCE(l.principalAmount - COALESCE(SUM(ep.amount), 0.0), l.principalAmount) DESC
			""")
	Loan findHighestOutstandingLoan();

	@Query("""
			SELECT NEW org.northernarc.loanminiproject.dto.LoanDetailDto(
				l.loanId,
				c.customerName,
				l.principalAmount,
				l.annualInterestRate,
				l.loanStatus,
				COALESCE(l.principalAmount - COALESCE(SUM(ep.amount), 0.0), l.principalAmount)
			)
			FROM Loan l
			JOIN l.customer c
			LEFT JOIN l.emiSchedules e
			LEFT JOIN e.payments ep
			GROUP BY l.loanId, c.customerName, l.principalAmount, l.annualInterestRate, l.loanStatus
			ORDER BY COALESCE(l.principalAmount - COALESCE(SUM(ep.amount), 0.0), l.principalAmount) DESC
			""")
	List<LoanDetailDto> findHighestOutstandingLoanDetails();

	@Query("""
			SELECT NEW org.northernarc.loanminiproject.dto.LoanDetailDto(
				l.loanId,
				c.customerName,
				l.principalAmount,
				l.annualInterestRate,
				l.loanStatus,
				COALESCE(l.principalAmount - COALESCE(SUM(ep.amount), 0.0), l.principalAmount)
			)
			FROM Loan l
			JOIN l.customer c
			LEFT JOIN l.emiSchedules e
			LEFT JOIN e.payments ep
			GROUP BY l.loanId, c.customerName, l.principalAmount, l.annualInterestRate, l.loanStatus
			ORDER BY COALESCE(l.principalAmount - COALESCE(SUM(ep.amount), 0.0), l.principalAmount) DESC
			""")
	List<LoanDetailDto> findHighestOutstandingLoanDetails(Pageable pageable);

	@Query("""
			SELECT COUNT(DISTINCT l)
			FROM Loan l
			WHERE l.loanStatus = 'DEFAULTED'
			""")
	Long countNPAAccounts();
}
