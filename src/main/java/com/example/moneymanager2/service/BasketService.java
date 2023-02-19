package com.example.moneymanager2.service;

import com.example.moneymanager2.model.Basket;
import com.example.moneymanager2.model.Transaction;
import com.example.moneymanager2.model.User;
import com.example.moneymanager2.repository.BasketRepository;
import com.example.moneymanager2.repository.TransactionRepositoty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BasketService {
    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private TransactionRepositoty transactionRepositoty;
    @Autowired
    private TransactionService transactionService;

    public boolean save(Basket basket){
        try{
            basketRepository.save(basket);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Basket basket){
        try{
            basketRepository.delete(basket);
            List<Transaction> lst = transactionService.findAllByBasketId(basket.getId());
            for (Transaction item : lst) {
                transactionRepositoty.delete(item);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Basket findById(String id){
        Basket basket = basketRepository.findById(id).orElse(null);
        return basket;
    }

    public boolean update(Basket basket){
        try{
            basketRepository.save(basket);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Basket> findAll(){
        try{
            List<Basket> users = basketRepository.findAll();
            return users;
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Basket> findAllByUserIdAndTpe(String userId, int type){
        return basketRepository.findAllByUserIdAndType(userId,type);
    }
}
