package com.example.moneymanager2.repository;

import com.example.moneymanager2.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {

}
