package com.example.moneymanager2.schedule;

import com.example.moneymanager2.model.Basket;
import com.example.moneymanager2.model.NotificationDream;
import com.example.moneymanager2.service.BasketService;
import com.example.moneymanager2.service.NotificationDreamService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ScheduleNotification {
    @Autowired
    private BasketService basketService;
    private NotificationDreamService notificationDreamService;
    private Date now = new Date();
//    Timer timer = new Timer();
//    timer.schedule(new TimerTask() {
//        @Override
//        public void run() {
//            System.out.println("Run schedule job");
//        }
//    }, 0,1000);
    public void runScheduleNotification(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("run schedule job");
                List<Basket> lstBasket = basketService.findAllByTypeAndStatus(3,0);
                for (Basket basket : lstBasket) {
                    long nowTime = now.getTime();
                    long createTime = basket.getCreatedDate().getTime();
                    long spaceTime = Math.abs(nowTime - createTime);
                    long spaceDate = TimeUnit.DAYS.convert(spaceTime,TimeUnit.MILLISECONDS);
                    int deltaOfSpaceDateAndNumberDateComplete = (int) (spaceDate - basket.getNumberDateComplete());
                    if(deltaOfSpaceDateAndNumberDateComplete != 0 && deltaOfSpaceDateAndNumberDateComplete > basket.getNumberDateMix()){
                        NotificationDream notificationDream = new NotificationDream();
                        notificationDream.setBasketId(basket.getId());
                        notificationDream.setUserId(basket.getUserId());
                        notificationDream.setCreatedDate(new Date());
                        notificationDream.setMessage("Bạn đã quên tích lũy cho ước mơ "+basket.getName());
                        notificationDreamService.save(notificationDream);
                    }
                }


            }
        },0,1000);
    }
}
