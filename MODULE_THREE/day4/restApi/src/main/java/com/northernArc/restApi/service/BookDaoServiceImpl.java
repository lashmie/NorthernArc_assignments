package com.northernArc.restApi.service;

import com.northernArc.restApi.dao.BookDao;
import com.northernArc.restApi.dao.BookDaoImpl;
import com.northernArc.restApi.model.Book;
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
        return books.findById(id);
    }

    @Override
    public void deleteBookById(int id) {
        books.deleteById(id);

    }


    @Override
    public Book updateBookDetails(int id, Book book) {
        books.update(id, book);
        return book;
    }

    @Override
    public Map<Integer, Book> showBook() {
        return books.findAll();
    }

    @Override
    public void clearAllBook() {
        books.deleteAll();
    }
}
