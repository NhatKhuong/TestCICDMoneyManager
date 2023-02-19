package com.example.moneymanager2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "transaction")
public class Transaction {
    @Id
    private String id;
    private String userId;
    private String basketId;
    private Date createDate;
    private double moneyTransaction;
    private Integer type;
    private String note;

    public Transaction(String userId, String basketId, Date createDate, double moneyTransaction, Integer type, String note) {
        this.userId = userId;
        this.basketId = basketId;
        this.createDate = createDate;
        this.moneyTransaction = moneyTransaction;
        this.type = type;
        this.note = note;
    }
}
