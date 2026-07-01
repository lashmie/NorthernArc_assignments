package org.northernarc.productrental.repository;

import org.northernarc.productrental.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByAvailable(Boolean available);

    List<Product> findByRentPerDayGreaterThan(double amount);

    @Query("SELECT p FROM Product p ORDER BY p.rentPerDay DESC")
    Page<Product> findAllProducts(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.rentPerDay = p.rentPerDay + :amount WHERE p.category = :category")
    int increaseRentPerDay(@Param("category") String category, @Param("amount") Double amount);

    @Query("SELECT p FROM Product p LEFT JOIN p.rentalRecords r ON r.status = 'OVERDUE' WHERE r.rentalId IS NULL")
    List<Product> findProductsWithNoOverdueRentals();

    @Query("SELECT DISTINCT p FROM Product p JOIN p.rentalRecords r WHERE r.status = 'OVERDUE' AND r.actualReturnDate IS NULL")
    List<Product> findProductsCurrentlyOverdue();
}

