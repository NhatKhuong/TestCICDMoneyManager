package com.example.moneymanager2;

import com.example.moneymanager2.schedule.ScheduleNotification;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class MoneyManager2Application {

    public static void main(String[] args) {
        SpringApplication.run(MoneyManager2Application.class, args);
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("Run schedule job");
//            }
//        }, 0,1000);
        ScheduleNotification scheduleNotification = new ScheduleNotification();
        scheduleNotification.runScheduleNotification();
    }

}
