package com.example.demo.serviceImpl;

import com.example.demo.exceptions.BookNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Page<Book> getBooks(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "dailyFineRate")
        );
        return bookRepository.findAll(sortedPageable);
    }

    @Override
    public int increaseDailyFineRates(List<String> bookTypes, Double increaseAmount) {
        if (bookTypes == null || bookTypes.isEmpty()) {
            return 0;
        }
        return bookRepository.increaseDailyFineRates(bookTypes, increaseAmount);
    }

    @Override
    public void deleteBook(String isbn) {
        if (!bookRepository.existsById(isbn)) {
            throw new BookNotFoundException("Book not found: " + isbn);
        }
        bookRepository.deleteById(isbn);
    }

    @Override
    public Book updateDailyFineRate(String isbn, Double amount) {
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + isbn));

        double currentRate = book.getDailyFineRate() == null ? 0.0 : book.getDailyFineRate();
        book.setDailyFineRate(currentRate + amount);
        return bookRepository.save(book);
    }
}
