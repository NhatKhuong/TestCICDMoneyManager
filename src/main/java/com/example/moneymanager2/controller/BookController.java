package com.example.moneymanager2.controller;

import com.example.moneymanager2.model.Book;
import com.example.moneymanager2.repository.BookRepository;
import com.example.moneymanager2.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @PostMapping
    public String save(@RequestBody Book book){
        bookRepository.save(book);
        return "save success";
    }

    @GetMapping
    public List<Book> getAll(){
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable("id") int id){
        return  bookRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable("id") int id, @RequestBody Book book){
        Book bookEx = findById(id);
        bookEx.setAuthName(book.getAuthName());
        bookEx.setName(book.getName());
        bookRepository.save(bookEx);
        return bookEx;
    }

}
