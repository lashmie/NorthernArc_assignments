package org.northernarc.week5_assess.repository;

import java.util.List;

import org.northernarc.week5_assess.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction t where t.account.accountNumber = :accountNumber order by t.createdAt desc")
    List<Transaction> findByAccountNumberOrderByCreatedAtDesc(@Param("accountNumber") String accountNumber);

    List<Transaction> findByAccountIdOrderByTransactionDateDesc(Long accountId);
}

