package com.kd.distribute.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author kdaddy@163.com
 * @date 2021/1/16 22:49
 * @公众号 程序员老猫
 */
@Service
@Slf4j
public class CuratorLockService {

    private String ORDER_KEY = "order_kd";

    @Autowired
    private CuratorFramework client;

    public  Integer createOrder() throws Exception{
        log.info("进入了方法");
        InterProcessMutex lock = new InterProcessMutex(client, "/"+ORDER_KEY);
        try {
            if (lock.acquire(30, TimeUnit.SECONDS)) {
                log.info("拿到了锁");
                //此处为了手动演示并发，所以我们暂时在这里休眠6s
                Thread.sleep(6000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                log.info("我释放了锁！！");
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("方法执行完毕");
        return 1;
    }
}
