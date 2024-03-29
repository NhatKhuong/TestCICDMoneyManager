package com.example.moneymanager2.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "Basket")
public class Basket {
    @Id
    private String id;
    private String userId;
    private String name;
    private double precent;
    private double availableBalances;
    private double totalSpending;
    private double totalIncome;

    private Integer type;
    private Integer status;
    private double moneyPurpose;
    private Date datedComplete;
    private Date createdDate;
    private int monthNumber;
    private int yearNumber;
    private int quantity;
    private String code;
    private Boolean isCash;

    public Basket(String userId, String name, double precent, double availableBalances, double totalSpending, double totalIncome) {
        this.userId = userId;
        this.name = name;
        this.precent = precent;
        this.availableBalances = availableBalances;
        this.totalSpending = totalSpending;
        this.totalIncome = totalIncome;
    }

}
