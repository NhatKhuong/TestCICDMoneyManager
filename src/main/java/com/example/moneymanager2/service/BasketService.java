package com.example.moneymanager2.service;

import com.example.moneymanager2.model.Basket;
import com.example.moneymanager2.model.Transaction;
import com.example.moneymanager2.model.User;
import com.example.moneymanager2.repository.BasketRepository;
import com.example.moneymanager2.repository.TransactionRepositoty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Basket> findAllByTypeAndStatus(int type, int status){
        return basketRepository.findAllByTypeAndStatus(3,0);
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

    public List<Double> getListAsset(String userId){
        List<Double> lstResult = new ArrayList<>();
        List<Basket> lstAsset = basketRepository.findAllByUserIdAndType(userId,4);
        List<Basket> lstDream = basketRepository.findAllByUserIdAndType(userId,3);
        List<Basket> lstBasket = basketRepository.findAllByUserIdAndType(userId,1);
        List<Basket> lstDebt = basketRepository.findAllByUserIdAndType(userId,2);
        double asset = 0.0;
        double dream = 0.0;
        double basket = 0.0;
        double debt = 0.0;

        for (Basket item : lstAsset) {
            asset = asset + item.getAvailableBalances();
        }

        for (Basket item : lstDream) {
            dream = dream + item.getAvailableBalances();
        }

        for (Basket item : lstBasket) {
            basket = basket + item.getAvailableBalances();
        }

        for (Basket item : lstDebt) {
            debt = debt + item.getAvailableBalances();
        }
        lstResult.add(asset);
        lstResult.add(dream);
        lstResult.add(basket);
        lstResult.add(debt);
        return  lstResult;
    }

    public String updateStatus(String basketId, int status){
        try {
            Basket basket = basketRepository.findById(basketId).orElse(null);
            basket.setStatus(status);
            basketRepository.save(basket);
            return "updated status";
        } catch (Exception e){
            e.printStackTrace();
        }
        return "update fail";

    }
}
