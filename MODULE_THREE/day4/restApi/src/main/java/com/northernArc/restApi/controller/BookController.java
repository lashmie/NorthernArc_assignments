package com.northernArc.restApi.controller;

import com.northernArc.restApi.RestApiApplication;
import com.northernArc.restApi.model.Book;
import com.northernArc.restApi.service.BookDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    public BookDaoService bookDaoService;

    @DeleteMapping("/{id}")
    public ResponseEntity Deletebyid(@PathVariable int id){
        bookDaoService.deleteBookById(id);
       return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity findByid(@PathVariable int id){
        return new ResponseEntity<>(bookDaoService.findBookById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id,@RequestBody Book book){
        bookDaoService.updateBookDetails(id,book);
    }
    @PostMapping
    public ResponseEntity<Book> addper(@RequestBody Book book){
        Book b=bookDaoService.addBook(book);
        return ResponseEntity.status(201).body(b);
    }
    @GetMapping
    public ResponseEntity<Map<Integer, Book>> getAll() {
        return ResponseEntity.ok(bookDaoService.showBook());
    }

}
