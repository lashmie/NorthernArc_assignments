package com.example.demo.service;

import com.example.demo.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    Page<Book> getBooks(Pageable pageable);

    int increaseDailyFineRates(List<String> bookTypes, Double increaseAmount);

    void deleteBook(String isbn);

    Book updateDailyFineRate(String isbn, Double amount);
}
