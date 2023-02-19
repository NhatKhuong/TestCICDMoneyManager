package com.example.moneymanager2.repository;

import com.example.moneymanager2.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book,Integer> {
}
