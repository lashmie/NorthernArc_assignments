package org.northernarc.loan.repository;

import org.northernarc.loan.model.Customer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

    default Optional<Customer> findOptionalByEmail(String email) {
        return Optional.ofNullable(findByEmail(email));
    }

    Customer findByPhoneNumber(String phoneNumber);

    List<Customer> findByCreditScoreGreaterThan(int creditScore);

    List<Customer> findByCity(String city);

    @Query("""
            select distinct c
            from Customer c
            join c.loans l
            join l.emiSchedules es
            where es.status = 'OVERDUE'
            """)
    List<Customer> findCustomersWithOverdueEmis();

    @Query("""
            select c
            from Customer c
            join c.loans l
            join l.emiSchedules es
            where es.status = 'OVERDUE'
            group by c
            order by sum(es.penaltyAmount) desc
            """)
    List<Customer> findTopDefaulters(Pageable pageable);

    default List<Customer> findTopDefaulters() {
        return findTopDefaulters(PageRequest.of(0, 10));
    }
}

