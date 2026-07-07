package org.northernarc.loanminiproject.repository;

import org.northernarc.loanminiproject.dto.CustomerDetailDto;
import org.northernarc.loanminiproject.model.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


	Optional<Customer> findByEmailOptional(String email);
	Optional<Customer> findByPhoneNumberOptional(String phoneNumber);

	Customer findByEmail(String email);

	Customer findByPhoneNumber(String phoneNumber);

	List<Customer> findByCity(String city);

	List<Customer> findByCreditScoreGreaterThan(Integer creditScore);

	@Query("""
SELECT DISTINCT c
FROM Customer c
JOIN c.loans l
JOIN l.emiSchedules e
WHERE e.status = 'OVERDUE'
""")
    List<Customer> findCustomersWithOverdueEmis();

    @Query("""
            SELECT DISTINCT c
            FROM Customer c
            JOIN c.loans l
            JOIN l.emiSchedules e
            WHERE e.daysPastDue > 0
            ORDER BY e.daysPastDue DESC
            """)
    List<Customer> findTopDefaulters();

	@Query("""
			SELECT COUNT(c)
			FROM Customer c
			""")
	Long countTotalCustomers();

	@Query("""
			SELECT NEW org.northernarc.loanminiproject.dto.CustomerDetailDto(
				c.customerId,
				c.customerName,
				c.email,
				c.phoneNumber,
				c.city,
				c.creditScore,
				COALESCE(SUM(ep.amount), 0.0)
			)
			FROM Customer c
			LEFT JOIN c.loans l
			LEFT JOIN l.emiSchedules e
			LEFT JOIN e.payments ep
			GROUP BY c.customerId, c.customerName, c.email, c.phoneNumber, c.city, c.creditScore
			ORDER BY SUM(ep.amount) DESC
			""")
	List<CustomerDetailDto> findHighestPayingCustomer(Pageable pageable);
}
