package com.kd.distribute;

import com.kd.distribute.service.OrderService;
import com.kd.distribute.service.OrderServiceOptimizeOne;
import com.kd.distribute.service.OrderServiceSync01;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class DistributeApplicationTests {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderServiceOptimizeOne orderServiceOptimizeOne;

    @Autowired
    private OrderServiceSync01 orderServiceSync01;

    @Autowired
    private OrderServiceSync01 orderServiceSync02;

    @Autowired
    private OrderServiceSync01 orderServiceSync03;

    @Test
    public void concurrentOrder() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i =0;i<5;i++){
            es.execute(()->{
                try {
                    cyclicBarrier.await();
                    Integer orderId = orderServiceSync03.createOrder();
                    System.out.println("订单id："+orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    cdl.countDown();
                }
            });
        }
        cdl.await();
        es.shutdown();
    }
}
