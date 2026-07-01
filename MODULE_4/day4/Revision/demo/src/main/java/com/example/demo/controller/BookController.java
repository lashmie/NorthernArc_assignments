package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/books", "/books"})
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public Page<Book> getBooks(@PageableDefault(size = 10) Pageable pageable) {
        return bookService.getBooks(pageable);
    }

    @PatchMapping("/fine-rates/increase")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Object>> increaseDailyFineRates(
            @RequestParam List<String> bookTypes,
            @RequestParam Double increaseAmount
    ) {
        int updatedCount = bookService.increaseDailyFineRates(bookTypes, increaseAmount);
        return ResponseEntity.ok(Map.of(
                "updatedCount", updatedCount,
                "bookTypes", bookTypes,
                "increaseAmount", increaseAmount
        ));
    }

    @PutMapping("/{isbn}/rate")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Book> updateDailyFineRate(
            @PathVariable String isbn,
            @RequestParam Double amount
    ) {
        return ResponseEntity.ok(bookService.updateDailyFineRate(isbn, amount));
    }

    @DeleteMapping("/{isbn}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }
}
