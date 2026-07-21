package org.northernarc.week5_assess.repository;

import java.util.List;
import java.util.Optional;

import org.northernarc.week5_assess.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByCustomerId(Long customerId);
}

