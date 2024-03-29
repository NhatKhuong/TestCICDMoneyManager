package com.example.moneymanager2.request;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchTransactionFromDateToDate extends BasePageAndSortRequest{
    private Date fromDate;
    private Date toDate;
    private Integer type;
    private String userId;
    private int month;
    private int year;
    private String basketId;
    private Integer typeBasket;
    private Boolean isDay;
    private Boolean isWeek;
}
