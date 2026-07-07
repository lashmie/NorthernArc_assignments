package org.northernarc.applicationspringjdbc.service;

import org.northernarc.applicationspringjdbc.model.Book;
import org.northernarc.applicationspringjdbc.repository.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookDaoServiceImpl implements BookDaoService {
    @Autowired
    public BookDao books;

    // we will validate all the stuffs later
    @Override
    public Book addBook(Book book) {
        books.save(book);
        return book;
    }

    @Override
    public Book findBookById(int id) {
        return books.findById(id).get();
    }

    @Override
    public void deleteBookById(int id) {
        books.deleteById(id);

    }
    @Override
    public Book updateBookDetails(int id, Book book) {
       Book book1 = books.findById(id).get();
       book1.setTitle(book.getTitle());
       book1.getAuthor();
       book1.setPublisher(book.getPublisher());
       books.save(book1);
        return book;
    }

    @Override
    public Map<Integer, Book> showBook() {
        return books.findAll().stream().collect(java.util.stream.Collectors.toMap(Book::getId, book -> book));
    }

    @Override
    public void clearAllBook() {
        books.deleteAll();
    }
}
