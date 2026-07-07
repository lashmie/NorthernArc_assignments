package com.northernArc.restApi.dao;

import com.northernArc.restApi.model.Book;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookDaoImpl implements BookDao {

    Map<Integer, Book> books;

    @PostConstruct
    public void init() {
        books=new HashMap<>();
        books.put(1, new Book(1, "morning miracle", "lavanya", "mc ", 120));
    }

    @Override
    public Book save(Book book) {
        books.put(book.getId(), book);
        return book;
    }

    @Override
    public Book findById(int id) {
        return books.get(id);
    }

    @Override
    public void deleteById(int id) {
        books.remove(id);
    }

    @Override
    public Book update(int id,Book book) {
books.put(id,book);
return book;
    }

    @Override
    public Map<Integer, Book> findAll() {
        return books;
    }

    @Override
    public void deleteAll() {
books.clear();
    }
    @PreDestroy
    public void delte(){
        books.clear();
    }
}
