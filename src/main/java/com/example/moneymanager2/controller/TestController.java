package com.example.moneymanager2.controller;

import com.example.moneymanager2.request.SearchTransactionFromDateToDate;
import com.example.moneymanager2.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.List;

@RestController
public class TestController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/test")
    public String test(){
        LocalDate date = LocalDate.of(2023, 3, 28);
        int weekOfYear = date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        System.out.println("==================="+weekOfYear);
//        List<Transaction> lst = transactionService.findAll(); 1 8 15 22
//        Transaction transaction = lst.get(0);
//        LocalDate currentDate = convertToLocalDateViaInstant(transaction.getCreateDate());
//        return "date is"+ currentDate.getDayOfMonth() + "mounth is"+ currentDate.getMonthValue() + "year is"+currentDate.getYear();
//        return transactionService.getTotalTransactionIncomeByDateOfMonth(searchTransactionFromDateToDate);
        return "success";
    }

}
