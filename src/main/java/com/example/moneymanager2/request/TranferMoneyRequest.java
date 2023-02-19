package com.example.moneymanager2.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranferMoneyRequest {
    private String userId;
    private String sentBasketId;
    private String receiveBasketId;
    private double money;
    private Date createdDate;
    private String note;
}
