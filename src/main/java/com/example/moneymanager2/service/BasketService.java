package com.example.moneymanager2.service;

import com.example.moneymanager2.model.Basket;
import com.example.moneymanager2.model.Transaction;
import com.example.moneymanager2.model.User;
import com.example.moneymanager2.repository.BasketRepository;
import com.example.moneymanager2.repository.TransactionRepositoty;
import com.example.moneymanager2.request.DistributeMoneyRequest;
import com.example.moneymanager2.request.SearchBasketByTimeRequest;
import com.example.moneymanager2.request.TranferMoneyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;

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

//    public String createListBasket(List<Basket> lstBasket){
//        try {
//            for (Basket basket : lstBasket) {
//                save(basket);
//            }
//            return "save success";
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return "fail";
//    }

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

    public List<Basket> findAllByUserIdAndTypeAndMonthNumberAndYearNumber(String userId, int type, SearchBasketByTimeRequest request){
        return basketRepository.findAllByUserIdAndTypeAndMonthNumberAndYearNumber(userId,type,request.getMonthNumber(),request.getYearNumber());
    }


    public List<Basket> findAllByUserIdAndTypeAndStatus(String userId, int type, int status){
        return basketRepository.findAllByUserIdAndTypeAndStatus(userId,type,status);
    }

    public Basket findAllByUserIdAndTypeAndIsCash(String userId, int type){
        return basketRepository.findAllByUserIdAndTypeAndIsCash(userId,type,true);
    }

    public List<Double> getListAsset(String userId, int month, int year){
        List<Double> lstResult = new ArrayList<>();
        List<Basket> lstAsset = basketRepository.findAllByUserIdAndType(userId,4);
        List<Basket> lstDream = basketRepository.findAllByUserIdAndTypeAndStatus(userId,3,0);
        List<Basket> lstBasket = basketRepository.findAllByUserIdAndTypeAndMonthNumberAndYearNumber(userId,1,month,year);
        List<Basket> lstDebt = basketRepository.findAllByUserIdAndTypeAndStatus(userId,2,0);
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
        lstResult.add(0.0);
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

    public Basket update(String id, Basket basket){
        Basket basketEx = findById(id);
        basketEx.setName(basket.getName());
        basketEx.setAvailableBalances(basket.getAvailableBalances());
        basketEx.setPrecent(basket.getPrecent());
        basketEx.setTotalIncome(basket.getTotalIncome());
        basketEx.setTotalSpending(basket.getTotalSpending());
        update(basket);
        return basket;
    }

    public boolean delete(String id){
        Basket basket = findById(id);
        List<Transaction> transactions = transactionService.findAllByBasketId(id);
        try {
            delete(basket);
            if(!CollectionUtils.isEmpty(transactions)){
                for (Transaction transaction : transactions) {
                    transactionService.delete(transaction);
                }
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public List<Basket> createListBasket(List<Basket> lstBasket){
        try {
            for (Basket basket : lstBasket) {
                if(Objects.isNull(basket.getId())){
                    basket.setId(UUID.randomUUID().toString());
                }
                save(basket);
            }
            return lstBasket;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean transferMoney(TranferMoneyRequest request){
        try {
            Basket basketSent = findById(request.getSentBasketId());
            Basket basketReceive = findById(request.getReceiveBasketId());

            basketSent.setTotalSpending(basketSent.getTotalSpending() + request.getMoney());
            basketSent.setAvailableBalances(basketSent.getTotalIncome() - basketSent.getTotalSpending());
            save(basketSent);

            basketReceive.setTotalIncome(basketReceive.getTotalIncome() + request.getMoney());
            basketReceive.setAvailableBalances(basketReceive.getTotalIncome() - basketReceive.getTotalSpending());
            save(basketReceive);

            Transaction transactionSent = new Transaction();
            transactionSent.setBasketId(request.getSentBasketId());
            transactionSent.setCreateDate(request.getCreatedDate());
            transactionSent.setMoneyTransaction(request.getMoney());
            transactionSent.setType(-1);
            transactionSent.setNote(request.getNote());
            transactionSent.setUserId(request.getUserId());
            transactionSent.setTypeBasket(basketSent.getType());
            transactionSent.setNameBasket(basketSent.getName());
            transactionSent.setIsTransfer(true);

            Transaction transactionReceive = new Transaction();
            transactionReceive.setBasketId(request.getReceiveBasketId());
            transactionReceive.setCreateDate(request.getCreatedDate());
            transactionReceive.setMoneyTransaction(request.getMoney());
            transactionReceive.setType(1);
            transactionReceive.setNote(request.getNote());
            transactionReceive.setUserId(request.getUserId());
            transactionReceive.setTypeBasket(basketReceive.getType());
            transactionReceive.setNameBasket(basketReceive.getName());
            transactionReceive.setIsTransfer(true);

            transactionService.save(transactionReceive);
            transactionService.save(transactionSent);
            return true;
        } catch (Exception e){
            e.printStackTrace();

        }
        return false;
    }

    public boolean distributeMoneyIntoBasket(DistributeMoneyRequest request){
        try {
//            List<Basket> lstBasket = basketRepository.findAllByUserIdAndType(request.getUserId(),1);
            List<Basket> lstBasket = basketRepository.findAllByUserIdAndTypeAndMonthNumberAndYearNumber(request.getUserId(),1,request.getMonthNumber(),request.getYearNumber());
            for (Basket basket : lstBasket) {

                basket.setAvailableBalances(basket.getAvailableBalances() + (basket.getPrecent()*0.01*request.getMoney()));
                basket.setTotalIncome(basket.getTotalIncome() + (request.getMoney()*0.01*basket.getPrecent()));
                basketRepository.save(basket);
                Transaction transaction = new Transaction();
                transaction.setTypeBasket(1);
                transaction.setMoneyTransaction(request.getMoney()*0.01*basket.getPrecent());
                transaction.setBasketId(basket.getId());
                transaction.setNote(request.getNote());
                transaction.setCreateDate(request.getCreatedDate());
                transaction.setType(1);
                transaction.setUserId(request.getUserId());
                transaction.setNameBasket(basket.getName());
                transactionRepositoty.save(transaction);
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
