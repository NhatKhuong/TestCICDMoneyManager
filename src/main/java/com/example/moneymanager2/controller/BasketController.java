package com.example.moneymanager2.controller;

import com.example.moneymanager2.model.Basket;
import com.example.moneymanager2.model.Book;
import com.example.moneymanager2.model.Transaction;
import com.example.moneymanager2.request.TranferMoneyRequest;
import com.example.moneymanager2.service.BasketService;
import com.example.moneymanager2.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/basket")
public class BasketController {
    @Autowired
    private BasketService basketService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public String save(@RequestBody Basket basket){
        basketService.save(basket);
        return "save success";
    }

    @PostMapping("/create-list-basket")
    public String createListBasket(@RequestBody List<Basket> lstBasket){
        for (Basket basket : lstBasket) {
            save(basket);
        }
        return "save success";
    }

    @PostMapping("/transfer-money")
    public String createListBasket(@RequestBody TranferMoneyRequest request){
        try {
            Basket basketSent = basketService.findById(request.getSentBasketId());
            Basket basketReceive = basketService.findById(request.getReceiveBasketId());

            basketSent.setTotalSpending(basketSent.getTotalSpending() + request.getMoney());
            basketSent.setAvailableBalances(basketSent.getTotalIncome() - basketSent.getTotalSpending());
            basketService.save(basketSent);

            basketReceive.setTotalIncome(basketReceive.getTotalIncome() + request.getMoney());
            basketReceive.setAvailableBalances(basketReceive.getTotalIncome() - basketReceive.getTotalSpending());
            basketService.save(basketReceive);

            Transaction transactionSent = new Transaction();
            transactionSent.setBasketId(request.getSentBasketId());
            transactionSent.setCreateDate(request.getCreatedDate());
            transactionSent.setMoneyTransaction(request.getMoney());
            transactionSent.setType(-1);
            transactionSent.setNote(request.getNote());
            transactionSent.setUserId(request.getUserId());

            Transaction transactionReceive = new Transaction();
            transactionReceive.setBasketId(request.getReceiveBasketId());
            transactionReceive.setCreateDate(request.getCreatedDate());
            transactionReceive.setMoneyTransaction(request.getMoney());
            transactionReceive.setType(1);
            transactionReceive.setNote(request.getNote());
            transactionReceive.setUserId(request.getUserId());

            transactionService.save(transactionReceive);
            transactionService.save(transactionSent);
            return "success";
        } catch (Exception e){
            e.printStackTrace();

        }
        return "false";
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
        Basket basket = basketService.findById(id);
        try {
            basketService.delete(basket);
            return  "delete sucess";
        } catch (Exception e){
            e.printStackTrace();
        }
        return "delete fail";
    }

    @PutMapping("/{id}")
    public Basket update(@PathVariable("id") String id, @RequestBody Basket basket){
        Basket basketEx = findById(id);
        basketEx.setName(basket.getName());
        basketEx.setAvailableBalances(basket.getAvailableBalances());
        basketEx.setPrecent(basket.getPrecent());
        basketEx.setTotalIncome(basket.getTotalIncome());
        basketEx.setTotalSpending(basket.getTotalSpending());
        basketService.update(basketEx);
        return basketEx;
    }

    @GetMapping("/get-all-by-userId-and-type/{userId}/{type}")
    public List<Basket> getAllBasketByUserId(@PathVariable("userId") String userId, @PathVariable("type") int type){
        return basketService.findAllByUserIdAndTpe(userId,type);
    }


}
