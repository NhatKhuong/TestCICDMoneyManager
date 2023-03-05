package com.example.moneymanager2.repository;

import com.example.moneymanager2.model.Basket;
import com.example.moneymanager2.model.NotificationDream;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationDreamRepository extends MongoRepository<NotificationDream,String> {
    List<NotificationDream> findAllByUserId(String userId);
}
