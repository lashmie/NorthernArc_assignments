package org.northernarc.loanminiproject.repository;

import org.northernarc.loanminiproject.dto.CustomerPaymentSummaryDto;
import org.northernarc.loanminiproject.dto.PaymentSummaryDto;
import org.northernarc.loanminiproject.model.EmiPayment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmiPaymentRepository extends JpaRepository<EmiPayment, Long> {

    Optional<EmiPayment> findTopByOrderByPaymentDateDesc();

    Optional<EmiPayment> findByReferenceNumberOptional(String referenceNumber);

    EmiPayment findByReferenceNumber(String referenceNumber);

    @Query("""
            SELECT p
            FROM EmiPayment p
            ORDER BY p.paymentDate DESC
            """)
    List<EmiPayment> findLatestPayment(Pageable pageable);

    @Query("""
            SELECT NEW org.northernarc.loanminiproject.dto.PaymentSummaryDto(
                c.city,
                COALESCE(SUM(p.amount), 0.0)
            )
            FROM EmiPayment p
            JOIN p.emiSchedule e
            JOIN e.loan l
            JOIN l.customer c
            GROUP BY c.city
            ORDER BY SUM(p.amount) DESC
            """)
    List<PaymentSummaryDto> findTotalEmiCollectionByCity();

    @Query("""
            SELECT NEW org.northernarc.loanminiproject.dto.CustomerPaymentSummaryDto(
                c.customerId,
                c.customerName,
                MAX(p.paymentDate)
            )
            FROM EmiPayment p
            JOIN p.emiSchedule e
            JOIN e.loan l
            JOIN l.customer c
            GROUP BY c.customerId, c.customerName
            ORDER BY MAX(p.paymentDate) DESC
            """)
    List<CustomerPaymentSummaryDto> findLatestPaymentByCustomer();

	@Query("""
			SELECT COALESCE(SUM(p.amount), 0.0)
			FROM EmiPayment p
			""")
	Double getTotalEmiCollected();

	@Query("""
			SELECT COALESCE(SUM(e.penaltyAmount), 0.0)
			FROM EmiSchedule e
			""")
	Double getTotalPenaltyCollected();
}
