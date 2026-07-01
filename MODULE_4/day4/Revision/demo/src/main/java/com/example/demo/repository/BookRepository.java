package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,String> {
    List<Book> findByBookType(String bookType);
    List<Book> findByDailyFineRateGreaterThan(double amount);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
    UPDATE Book b
    SET b.dailyFineRate = b.dailyFineRate + :increaseAmount
    WHERE b.bookType = :bookType
    """)
    int increaseDailyFineRates(@Param("bookType") String bookType, @Param("increaseAmount") Double increaseAmount);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
    UPDATE Book b
    SET b.dailyFineRate = b.dailyFineRate + :increaseAmount
    WHERE b.bookType IN :bookTypes
    """)
    int increaseDailyFineRates(@Param("bookTypes") List<String> bookTypes, @Param("increaseAmount") Double increaseAmount);

    @Query("""
    SELECT b
    FROM Book b
    LEFT JOIN b.fineTransactions ft
    WHERE ft.transactionId IS NULL
    """)
    List<Book> findBooksWithNoOverdueHistory();


}
