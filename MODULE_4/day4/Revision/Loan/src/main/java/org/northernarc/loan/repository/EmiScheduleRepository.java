package org.northernarc.loan.repository;

import org.northernarc.loan.model.EmiSchedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EmiScheduleRepository extends JpaRepository<EmiSchedule, Long> {

    List<EmiSchedule> findByStatus(String status);

    List<EmiSchedule> findByDueDateBeforeAndStatusNot(LocalDate date, String status);

    List<EmiSchedule> findByDueDateBefore(LocalDate date);

    List<EmiSchedule> findByLoanLoanId(Long loanId);

    List<EmiSchedule> findByStatusInAndDueDateBefore(List<String> statuses, LocalDate date);

    long countByStatus(String status);

    @Query("""
            select es
            from EmiSchedule es
            where es.status = 'OVERDUE'
            order by es.amountDue desc, es.daysPastDue desc
            """)
    List<EmiSchedule> findHighestOverdueAmount(Pageable pageable);

    default EmiSchedule findHighestOverdueEmi() {
        List<EmiSchedule> list = findHighestOverdueAmount(Pageable.ofSize(1));
        return list.isEmpty() ? null : list.get(0);
    }
}

