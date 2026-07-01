package com.example.demo.repository;

import com.example.demo.model.FineTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FineTransactionRepository  extends JpaRepository<FineTransaction,Long> {

    List<FineTransaction> findByPaymentType(String paymentType);

    @Query("""
    SELECT ft
    FROM FineTransaction ft
    ORDER BY ft.paymentDate DESC, ft.transactionId DESC
    """)
    List<FineTransaction> findLatestFinePayment(Pageable pageable);

    default Optional<FineTransaction> findLatestFinePayment() {
        return findLatestFinePayment(PageRequest.of(0, 1)).stream().findFirst();
    }

    @Query("""
    SELECT COALESCE(SUM(ft.amount), 0)
    FROM FineTransaction ft
    """)
    Double findTotalFinesCollected();
}
