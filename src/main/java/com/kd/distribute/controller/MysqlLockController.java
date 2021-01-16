package com.kd.distribute.controller;

import com.kd.distribute.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author kdaddy@163.com
 * @desc 公众号-“程序员老猫”
 * @date 2021/1/3 10:51
 */
@RestController
@RequestMapping(value = "lock", method = RequestMethod.POST)
public class MysqlLockController {

    @Autowired
    private MySQLOrderService mySQLOrderService;

    @Autowired
    private RedisLockService redisLockService;

    @Autowired
    private RedissonService redissonService;

    @Autowired
    private ZKLockService zkLockService;

    @Autowired
    private CuratorLockService curatorLockService;


    @RequestMapping(value = "/mysqlLock/createOrder")
    public String createOrder() throws Exception {
        mySQLOrderService.createOrder();
        return "success";
    }

    @RequestMapping(value = "/redisLock/redisLockCreateOrder")
    public String redisLockCreateOrder() throws Exception {
        redisLockService.createOrder();
        return "success";
    }

    @RequestMapping(value = "/redisLock/redissonCreateOrder")
    public String redissonCreateOrder() throws Exception {
        redissonService.createOrder();
        return "success";
    }

    @RequestMapping(value = "/zk/zkCreateOrder")
    public String zkCreateOrder() throws Exception {
        zkLockService.createOrder();
        return "success";
    }

    @RequestMapping(value = "/zk/curatorLockService")
    public String curatorLockService() throws Exception {
        curatorLockService.createOrder();
        return "success";
    }
}
