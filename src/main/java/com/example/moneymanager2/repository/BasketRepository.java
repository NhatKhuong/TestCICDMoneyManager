package com.example.moneymanager2.repository;

import com.example.moneymanager2.model.Basket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BasketRepository extends MongoRepository<Basket,String> {
    public List<Basket> findAllByUserIdAndType(String userId,int type);
    public List<Basket> findAllByUserIdAndTypeAndMonthNumberAndYearNumber(String userId,int type, int monthNumber, int yearNumber);
    public List<Basket> findAllByUserIdAndTypeAndStatus(String userId, int type, int status);
    public Basket findAllByUserIdAndTypeAndIsCash(String userId, int type, boolean isCash);
}
