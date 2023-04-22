package com.example.moneymanager2.controller;

import com.example.moneymanager2.model.Basket;
import com.example.moneymanager2.model.Book;
import com.example.moneymanager2.model.Transaction;
import com.example.moneymanager2.request.DistributeMoneyRequest;
import com.example.moneymanager2.request.SearchBasketByTimeRequest;
import com.example.moneymanager2.request.TranferMoneyRequest;
import com.example.moneymanager2.service.BasketService;
import com.example.moneymanager2.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/basket")
public class BasketController {
    @Autowired
    private BasketService basketService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public boolean save(@RequestBody Basket basket){
//        Thu
//        basketService.save(basket);
//        return "save success";
        return basketService.save(basket);
    }

    @PostMapping("/create-list-basket")
    public List<Basket> createListBasket(@RequestBody List<Basket> lstBasket){
        return basketService.createListBasket(lstBasket);
//        try {
//            for (Basket basket : lstBasket) {
//                if(Objects.isNull(basket.getId())){
//                    basket.setId(UUID.randomUUID().toString());
//                }
//                save(basket);
//            }
//            return lstBasket;
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
    }

    @PostMapping("/transfer-money")
    public String transferMoney(@RequestBody TranferMoneyRequest request){
        return basketService.transferMoney(request) ? "success" : "false";
//        try {
//            Basket basketSent = basketService.findById(request.getSentBasketId());
//            Basket basketReceive = basketService.findById(request.getReceiveBasketId());
//
//            basketSent.setTotalSpending(basketSent.getTotalSpending() + request.getMoney());
//            basketSent.setAvailableBalances(basketSent.getTotalIncome() - basketSent.getTotalSpending());
//            basketService.save(basketSent);
//
//            basketReceive.setTotalIncome(basketReceive.getTotalIncome() + request.getMoney());
//            basketReceive.setAvailableBalances(basketReceive.getTotalIncome() - basketReceive.getTotalSpending());
//            basketService.save(basketReceive);
//
//            Transaction transactionSent = new Transaction();
//            transactionSent.setBasketId(request.getSentBasketId());
//            transactionSent.setCreateDate(request.getCreatedDate());
//            transactionSent.setMoneyTransaction(request.getMoney());
//            transactionSent.setType(-1);
//            transactionSent.setNote(request.getNote());
//            transactionSent.setUserId(request.getUserId());
//            transactionSent.setTypeBasket(basketSent.getType());
//            transactionSent.setNameBasket(basketSent.getName());
//
//            Transaction transactionReceive = new Transaction();
//            transactionReceive.setBasketId(request.getReceiveBasketId());
//            transactionReceive.setCreateDate(request.getCreatedDate());
//            transactionReceive.setMoneyTransaction(request.getMoney());
//            transactionReceive.setType(1);
//            transactionReceive.setNote(request.getNote());
//            transactionReceive.setUserId(request.getUserId());
//            transactionReceive.setTypeBasket(basketReceive.getType());
//            transactionReceive.setNameBasket(basketReceive.getName());
//
//            transactionService.save(transactionReceive);
//            transactionService.save(transactionSent);
//            return "success";
//        } catch (Exception e){
//            e.printStackTrace();
//
//        }
//        return "false";
    }


    @GetMapping
    public List<Basket> getAll(){
        return basketService.findAll();
    }

    @GetMapping("/{id}")
    public Basket findById(@PathVariable("id") String id){
        return  basketService.findById(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") String id){
        return basketService.delete(id) ? "delete success" : "delete fail";
//        Basket basket = basketService.findById(id);
//        List<Transaction> transactions = transactionService.findAllByBasketId(id);
//        try {
//            basketService.delete(basket);
//            for (Transaction transaction : transactions) {
//                transactionService.delete(transaction);
//            }
//            return  "delete success";
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return "delete fail";
    }

    @PutMapping("/{id}")
    public Basket update(@PathVariable("id") String id, @RequestBody Basket basket){
        return basketService.update(id,basket);
//        Basket basketEx = findById(id);
//        basketEx.setName(basket.getName());
//        basketEx.setAvailableBalances(basket.getAvailableBalances());
//        basketEx.setPrecent(basket.getPrecent());
//        basketEx.setTotalIncome(basket.getTotalIncome());
//        basketEx.setTotalSpending(basket.getTotalSpending());
//        basketService.update(basket);
//        return basket;
    }

    @GetMapping("/get-all-by-userId-and-type/{userId}/{type}")
    public List<Basket> getAllBasketByUserId(@PathVariable("userId") String userId, @PathVariable("type") int type){
        return basketService.findAllByUserIdAndTpe(userId,type);
    }

    @PostMapping("/get-all-by-userId-and-type-by-time/{userId}/{type}")
    public List<Basket> getAllBasketByUserIdAndTime(@PathVariable("userId") String userId, @PathVariable("type") int type, @RequestBody SearchBasketByTimeRequest request){
        return basketService.findAllByUserIdAndTypeAndMonthNumberAndYearNumber(userId,type,request);
    }

    @GetMapping("/get-all-by-userId-and-type/{userId}/{type}/{status}")
    public List<Basket> getAllBasketByUserIdAndTypeAndStatus(@PathVariable("userId") String userId, @PathVariable("type") int type, @PathVariable("status") int status){
        return basketService.findAllByUserIdAndTypeAndStatus(userId,type,status);
    }

    @PostMapping("/get-all-asset-by-userId/{userId}")
    public List<Double> getAllBasketByUserId(@PathVariable("userId") String userId, @RequestBody SearchBasketByTimeRequest request){
        return basketService.getListAsset(userId, request.getMonthNumber(),request.getYearNumber());
    }

    @PostMapping("/update-status/{basketId}/{status}")
    public String updateStatus(@PathVariable("basketId") String basketId, @PathVariable("status") int status){
        return basketService.updateStatus(basketId,status);
    }

    @PostMapping("/distribute-money")
    public boolean updateStatus(@RequestBody DistributeMoneyRequest distributeMoneyRequest){
        return basketService.distributeMoneyIntoBasket(distributeMoneyRequest);
    }

    @GetMapping("/get-cash-basket/{userId}/{type}")
    public Basket getCashBasket(@PathVariable("userId") String userId, @PathVariable("type") int type){
        return basketService.findAllByUserIdAndTypeAndIsCash(userId,type);
    }



}
