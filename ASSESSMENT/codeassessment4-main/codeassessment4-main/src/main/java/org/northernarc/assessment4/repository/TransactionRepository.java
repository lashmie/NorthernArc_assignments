package org.northernarc.assessment4.repository;

import org.northernarc.assessment4.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Task 3: Derived Query Method
    List<Transaction> findByTransactionType(String transactionType);

    @Query("""
            SELECT t
            FROM Transaction t
            ORDER BY t.transactionDate DESC
            """)
    List<Transaction> findLatestTransaction(Pageable pageable);

}
