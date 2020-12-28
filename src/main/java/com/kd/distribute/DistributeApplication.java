package com.kd.distribute;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kd.distribute.dao")
public class DistributeApplication {
    public static void main(String[] args) {
        SpringApplication.run(DistributeApplication.class, args);
    }

}
