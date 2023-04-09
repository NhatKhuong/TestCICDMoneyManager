package com.example.moneymanager2.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseSort {
    private String key;
    private Boolean asc = true;
}
