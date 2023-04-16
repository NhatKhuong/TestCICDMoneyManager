package com.example.moneymanager2;

import com.example.moneymanager2.model.Transaction;
import com.example.moneymanager2.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoneyManager2Application {
    public static void main(String[] args) {
        SpringApplication.run(MoneyManager2Application.class, args);
    }

}
