package org.northernarc.loanminiproject.repository;

import org.northernarc.loanminiproject.model.EmiSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmiScheduleRepository extends JpaRepository<EmiSchedule, Long> {

    List<EmiSchedule> findByStatus(String status);

    List<EmiSchedule> findByDaysPastDueGreaterThan(Integer daysPastDue);

    List<EmiSchedule> findByLoanLoanId(Long loanId);

    List<EmiSchedule> findByDueDateBefore(LocalDate dueDate);

    @Query("""
            SELECT e
            FROM EmiSchedule e
            WHERE e.penaltyAmount = (
                SELECT MAX(es.penaltyAmount)
                FROM EmiSchedule es
            )
            """)
    List<EmiSchedule> findHighestOverdueAmount();

    @Query("""
            SELECT e
            FROM EmiSchedule e
            WHERE e.status = 'OVERDUE'
              AND e.penaltyAmount = (
                  SELECT MAX(es.penaltyAmount)
                  FROM EmiSchedule es
                  WHERE es.status = 'OVERDUE'
              )
            """)
    EmiSchedule findHighestOverdueEmi();
}
