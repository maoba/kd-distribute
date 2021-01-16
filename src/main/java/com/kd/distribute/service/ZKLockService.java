package com.kd.distribute.service;

import com.kd.distribute.utils.ZKLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author kdaddy@163.com
 * @date 2021/1/16 19:28
 */
@Service
@Slf4j
public class ZKLockService {

    @Autowired
    private ZKLockUtil zkLockUtil;

    private String ORDER_KEY = "order_kd";

    public  Integer createOrder() throws Exception{
        log.info("进入了方法");
        try {
            if (zkLockUtil.getLock(ORDER_KEY)) {
                log.info("拿到了锁");
                //此处为了手动演示并发，所以我们暂时在这里休眠6s
                Thread.sleep(6000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            zkLockUtil.close();
        }
        log.info("方法执行完毕");

        return 1;
    }
}
