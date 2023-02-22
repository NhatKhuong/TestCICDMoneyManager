package com.example.moneymanager2.repository;


import com.example.moneymanager2.model.Basket;
import com.example.moneymanager2.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface TransactionRepositoty extends MongoRepository<Transaction,String> {
    public List<Transaction> findAllByUserIdAndTypeAndTypeBasket(String userId,int type, int typeBasket);
    public List<Transaction> findAllByUserIdAndBasketId(String userId, String basketId);
    public List<Transaction> findAllByUserIdAndTypeBasketAndCreateDateBetween(String userId,int typeBasket, Date from, Date to);
    public List<Transaction> findAllByUserIdAndTypeAndTypeBasketAndCreateDateBetween(String userId, int type,int typeBasket, Date from, Date to);
    public List<Transaction> findAllByUserIdAndTypeAndBasketIdAndCreateDateBetween(String userId, int type,String basketId, Date from, Date to);
    public List<Transaction> findAllByBasketId(String basketId);
}
