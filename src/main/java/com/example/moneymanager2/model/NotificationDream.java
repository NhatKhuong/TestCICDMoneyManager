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
@Document(collection = "notification_dream")
public class NotificationDream {
    @Id
    private String id;

    private String basketId;
    private String userId;
    private Date createdDate;
    private String message;
    private int type;



}
