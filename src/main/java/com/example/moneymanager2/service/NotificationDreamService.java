package com.example.moneymanager2.service;

import com.example.moneymanager2.model.Basket;
import com.example.moneymanager2.model.NotificationDream;
import com.example.moneymanager2.model.Transaction;
import com.example.moneymanager2.repository.BasketRepository;
import com.example.moneymanager2.repository.NotificationDreamRepository;
import com.example.moneymanager2.repository.TransactionRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationDreamService {
    @Autowired
    private NotificationDreamRepository notificationDreamRepository;

    @Autowired
    private TransactionRepositoty transactionRepositoty;
    @Autowired
    private TransactionService transactionService;

    public boolean save(NotificationDream notificationDream){
        try{
            notificationDreamRepository.save(notificationDream);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public NotificationDream findById(String id){
        NotificationDream notificationDream = notificationDreamRepository.findById(id).orElse(null);
        return notificationDream;
    }

    public boolean update(NotificationDream notificationDream){
        try{
            notificationDreamRepository.save(notificationDream);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<NotificationDream> findAllByUserId(String userId){
        return notificationDreamRepository.findAllByUserId(userId);
    }
}
