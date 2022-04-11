package com.webshop.cron;


import com.webshop.service.OrderCartService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class OrderJob {
    private final OrderCartService orderCartService;

    public OrderJob(OrderCartService orderCartService) {
        this.orderCartService = orderCartService;
    }
    //every 2 days
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24 * 2)
    private void removeOldOrders(){
        this.orderCartService.removeOrderCartsOlderThen(2);  //2 days
    }
}
