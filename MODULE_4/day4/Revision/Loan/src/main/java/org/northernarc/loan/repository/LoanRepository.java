package org.northernarc.loan.repository;

import org.northernarc.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByLoanType(String loanType);

    List<Loan> findByCustomerCity(String city);

    List<Loan> findByLoanStatus(String loanStatus);

    @Query("select l from Loan l where l.loanStatus = 'ACTIVE'")
    List<Loan> findActiveLoans();

    List<Loan> findByPrincipalAmountGreaterThan(Double amount);

    @Query("""
            select c.city, coalesce(sum(es.amountPaid), 0)
            from Loan l
            join l.customer c
            join l.emiSchedules es
            group by c.city
            """)
    List<Object[]> findTotalEmiCollectionByCity();

    @Query("""
            select l
            from Loan l
            left join l.emiSchedules es
            group by l
            having sum(case when es.status = 'OVERDUE' then 1 else 0 end) = 0
            """)
    List<Loan> findLoansWithZeroOverdueEmis();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Loan l
            set l.annualInterestRate = :newRate
            where l.loanType = :loanType
            """)
    int reviseAnnualInterestRateByLoanType(@Param("loanType") String loanType,
                                           @Param("newRate") Double newRate);

    default int updateInterestRateByLoanType(String loanType, Double newRate) {
        return reviseAnnualInterestRateByLoanType(loanType, newRate);
    }

    @Query("select count(l) from Loan l")
    long countAllLoans();

    @Query("select count(l) from Loan l where l.loanStatus = 'ACTIVE'")
    long countActiveLoans();

    @Query("select count(l) from Loan l where l.loanStatus = 'CLOSED'")
    long countClosedLoans();

    @Query("select coalesce(sum(es.amountPaid), 0) from Loan l join l.emiSchedules es")
    Double findTotalEmiCollected();

    @Query("select coalesce(sum(es.penaltyAmount), 0) from Loan l join l.emiSchedules es")
    Double findTotalPenaltyCollected();

    @Query("select c.city, count(l) from Loan l join l.customer c group by c.city")
    List<Object[]> findLoanCountPerCity();

    @Query("select coalesce(avg(l.annualInterestRate), 0) from Loan l")
    Double findAverageInterestRate();

    @Query("select l from Loan l order by l.principalAmount desc")
    List<Loan> findHighestOutstandingLoanList();

    default Loan findHighestOutstandingLoan() {
        List<Loan> list = findHighestOutstandingLoanList();
        return list.isEmpty() ? null : list.get(0);
    }
}

