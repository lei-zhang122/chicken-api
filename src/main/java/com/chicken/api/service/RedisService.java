package com.chicken.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @description: redis包装类
 * @author: zhanglei
 * @create: 2019-09-02 19:11
 **/
@Service(value="redisService")
public class RedisService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key,value);
    }

    
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    
    public void setByTime(String key, Object value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key,value,time,timeUnit);
    }

    
    public void increment(String key, long value) {
        redisTemplate.opsForValue().increment(key,value);
    }

    
    public void increment(String key, long value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().increment(key,value);
        redisTemplate.expire(key,time,timeUnit);
    }
}
