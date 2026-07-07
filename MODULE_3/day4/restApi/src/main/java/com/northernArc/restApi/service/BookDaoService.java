package com.northernArc.restApi.service;

import com.northernArc.restApi.model.Book;

import java.util.Map;

public interface BookDaoService {
    Book addBook(Book book);
    Book findBookById(int id);
    void deleteBookById(int id);
    Book updateBookDetails(int id,Book book);
    Map<Integer,Book > showBook();
    void clearAllBook();
}
