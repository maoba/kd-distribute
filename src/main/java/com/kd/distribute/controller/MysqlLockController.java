package com.kd.distribute.controller;

import com.kd.distribute.service.MySQLOrderService;
import com.kd.distribute.service.RedisLockService;
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
}
