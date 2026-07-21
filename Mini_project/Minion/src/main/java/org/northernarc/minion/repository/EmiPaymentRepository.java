package org.northernarc.minion.repository;

import org.northernarc.minion.model.EmiPayment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmiPaymentRepository extends JpaRepository<EmiPayment, Long> {

    EmiPayment findByReferenceNumber(String referenceNumber);

    @Query("SELECT p FROM EmiPayment p ORDER BY p.paymentDate DESC")
    List<EmiPayment> findLatestPayment(Pageable pageable);
}

