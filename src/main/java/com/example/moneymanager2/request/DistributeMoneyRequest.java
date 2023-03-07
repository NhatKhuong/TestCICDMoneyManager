package com.example.moneymanager2.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributeMoneyRequest {
    private String userId;
    private double money;
    private Date createdDate;
    private String note;
}
