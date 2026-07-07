package com.northernArc.restApi.dao;

import com.northernArc.restApi.model.Book;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface BookDao {
     Book save(Book book);
    Book findById(int id);
    void deleteById(int id);
    Book update(int id,Book book);
    Map<Integer,Book > findAll();
    void deleteAll();
}
