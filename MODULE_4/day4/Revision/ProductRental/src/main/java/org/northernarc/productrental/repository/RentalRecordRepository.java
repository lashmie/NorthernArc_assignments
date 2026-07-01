package org.northernarc.productrental.repository;

import java.time.LocalDate;
import java.util.List;
import org.northernarc.productrental.model.RentalRecord;
import org.northernarc.productrental.model.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRecordRepository extends JpaRepository<RentalRecord, Long> {

    List<RentalRecord> findByCustomerCustomerId(Long customerId);
    List<RentalRecord> findByProductProductId(Long productId);
    List<RentalRecord> findByStatus(RentalStatus status);
    List<RentalRecord> findByStatusAndExpectedReturnDateBefore(RentalStatus status, LocalDate date);
    long countByStatus(RentalStatus status);
}

