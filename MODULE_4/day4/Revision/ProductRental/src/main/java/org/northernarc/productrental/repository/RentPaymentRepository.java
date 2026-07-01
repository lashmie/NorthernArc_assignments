package org.northernarc.productrental.repository;

import org.northernarc.productrental.model.RentPayment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentPaymentRepository extends JpaRepository<RentPayment, Long> {

    List<RentPayment> findByRentalRecordRentalId(Long rentalId);

    @Query("SELECT rp FROM RentPayment rp ORDER BY rp.paymentDate DESC, rp.paymentId DESC")
    List<RentPayment> findLatestRentPayment(Pageable pageable);

    @Query("""
            SELECT SUM(rp.amount)
            FROM RentPayment rp
            """)
    Double findTotalRentCollected();
}

