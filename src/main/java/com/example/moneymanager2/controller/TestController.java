package com.example.moneymanager2.controller;

import com.example.moneymanager2.request.SearchTransactionFromDateToDate;
import com.example.moneymanager2.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/test")
    public List<Double> test(@RequestBody SearchTransactionFromDateToDate searchTransactionFromDateToDate){
//        List<Transaction> lst = transactionService.findAll();
//        Transaction transaction = lst.get(0);
//        LocalDate currentDate = convertToLocalDateViaInstant(transaction.getCreateDate());
//        return "date is"+ currentDate.getDayOfMonth() + "mounth is"+ currentDate.getMonthValue() + "year is"+currentDate.getYear();
        return transactionService.getTotalTransactionIncomeByDateOfMonth(searchTransactionFromDateToDate);
    }

}
