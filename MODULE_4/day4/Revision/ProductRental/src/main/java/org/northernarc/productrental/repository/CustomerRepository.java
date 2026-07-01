package org.northernarc.productrental.repository;

import java.util.List;
import java.util.Optional;
import org.northernarc.productrental.dto.CustomerRentalSummaryDTO;
import org.northernarc.productrental.model.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);
    List<Customer> findByCity(String city);

    @Query("""
            select c
            from Customer c
            join c.rentalRecords r
            group by c
            having count(r) > :minRentals
            """)
    List<Customer> findFrequentCustomers(@Param("minRentals") Long minRentals);

    @Query("""
            select c.city, coalesce(sum(p.amount), 0.0)
            from Customer c
            join c.rentalRecords r
            join r.rentPayments p
            group by c.city
            """)
    List<Object[]> findTotalRentCollectedPerCity();

    @Query("""
            select c
            from Customer c
            join c.rentalRecords r
            join r.product p
            group by c
            having count(distinct p.category) > 1
            """)
    List<Customer> findCustomersRentingMultipleCategories();

    @Query("""
            select new org.northernarc.productrental.dto.CustomerRentalSummaryDTO(
                c.customerName,
                c.city,
                count(distinct r.rentalId),
                coalesce(sum(p.amount), 0.0)
            )
            from Customer c
            left join c.rentalRecords r
            left join r.rentPayments p
            where c.customerId = :customerId
            group by c.customerId, c.customerName, c.city
            """)
    Optional<CustomerRentalSummaryDTO> findCustomerRentalSummary(@Param("customerId") Long customerId);

    @Query("""
            select c.customerName
            from RentPayment rp
            join rp.rentalRecord rr
            join rr.customer c
            group by c.customerId, c.customerName
            order by sum(rp.amount) desc, c.customerName asc
            """)
    List<String> findHighestPayingCustomer(Pageable pageable);
}

