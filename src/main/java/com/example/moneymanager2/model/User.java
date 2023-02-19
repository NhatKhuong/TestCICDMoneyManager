package com.example.moneymanager2.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String urlPic;
}
