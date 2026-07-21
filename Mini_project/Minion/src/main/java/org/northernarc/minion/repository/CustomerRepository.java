package org.northernarc.minion.repository;

import org.northernarc.minion.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

    Customer findByPhoneNumber(String phoneNumber);

    List<Customer> findByCity(String city);

    List<Customer> findByCreditScoreGreaterThan(Integer creditScore);

    @Query("SELECT DISTINCT c FROM Customer c JOIN c.loans l JOIN l.emiSchedules e WHERE e.status = 'OVERDUE'")
    List<Customer> findCustomersWithOverdueEmis();

    @Query("SELECT c FROM Customer c JOIN c.loans l JOIN l.emiSchedules e WHERE e.status = 'OVERDUE' GROUP BY c ORDER BY SUM(e.penaltyAmount) DESC")
    List<Customer> findTopDefaulters();

    @Query(value = "SELECT c.customer_name, COALESCE(SUM(e.amount_paid), 0) AS total_paid FROM projectcustomer c JOIN projectloan l ON l.customer_id = c.customer_id JOIN projectemischedule e ON e.loan_id = l.loan_id GROUP BY c.customer_name ORDER BY total_paid DESC LIMIT 1", nativeQuery = true)
    Object[] findHighestPayingCustomer();
}

