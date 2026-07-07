package org.northernarc.applicationspringjdbc.controller;


import org.northernarc.applicationspringjdbc.model.Book;
import org.northernarc.applicationspringjdbc.service.BookDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    public BookDaoService bookDaoService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> Deletebyid(@PathVariable int id){
        bookDaoService.deleteBookById(id);
       return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> findByid(@PathVariable int id){
        return new ResponseEntity<>(bookDaoService.findBookById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable int id,@RequestBody Book booki){
        return ResponseEntity.ok(bookDaoService.updateBookDetails(id, booki));
    }
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book booki){
        bookDaoService.addBook(booki);
        return ResponseEntity.status(201).body(booki);
    }
    @GetMapping
    public ResponseEntity<Map<Integer, Book>> getAll() {
        return ResponseEntity.ok(bookDaoService.showBook());
    }

}
