package com.example.moneymanager2.service;

import com.example.moneymanager2.model.Book;
import com.example.moneymanager2.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public String save(Book book){
        try {
            bookRepository.save(book);
            return "save done";
        } catch (Exception e){
            e.printStackTrace();
        }
        return "save fail";

    }


}
