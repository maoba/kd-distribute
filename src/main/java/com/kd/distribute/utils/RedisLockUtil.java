package com.kd.distribute.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author kdaddy@163.com
 * @date 2021/1/7 22:36
 * 公众号 “程序员老猫”
 */
@Service
public class RedisLockUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    private String value = UUID.randomUUID().toString();

    public Boolean lock(String key){
        RedisCallback<Boolean> redisCallback = redisConnection -> {
            //表示set nx 存在key的话就不设置，不存在则设置
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
            //设置过期时间
            Expiration expiration = Expiration.seconds(30);
            byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
            byte[] redisValue = redisTemplate.getKeySerializer().serialize(value);
            Boolean result = redisConnection.set(redisKey,redisValue,expiration,setOption);
            return result;
        };

        //获取分布式锁
        Boolean lock = (Boolean)redisTemplate.execute(redisCallback);
        return lock;
    }

    public Boolean releaseLock(String key){
        String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";
        RedisScript<Boolean> redisScript = RedisScript.of(script,Boolean.class);
        List<String> keys = Arrays.asList(key);

        boolean result = (Boolean) redisTemplate.execute(redisScript,keys,value);
        return result;
    }
}
