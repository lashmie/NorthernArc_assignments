package org.northernarc.minion.repository;

import org.northernarc.minion.model.EmiSchedule;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.time.LocalDate;
import java.util.List;

public interface EmiScheduleRepository extends JpaRepository<EmiSchedule, Long> {

    List<EmiSchedule> findByStatus(String status);

    List<EmiSchedule> findByDueDateBefore(LocalDate dueDate);

    List<EmiSchedule> findByLoanLoanId(Long loanId);

    long countByStatus(String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EmiSchedule e WHERE e.emiId = :emiId")
    Optional<EmiSchedule> findByIdForUpdate(@Param("emiId") Long emiId);

    @Query(value = "SELECT * FROM projectemischedule e WHERE e.status = 'OVERDUE' ORDER BY e.penalty_amount DESC LIMIT 1", nativeQuery = true)
    EmiSchedule findHighestOverdueEmi();
}

