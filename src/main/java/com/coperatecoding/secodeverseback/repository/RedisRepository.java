package com.coperatecoding.secodeverseback.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(String userId, String refreshToken, long expirationTimeMillis) {
        redisTemplate.opsForValue().set(userId, refreshToken, expirationTimeMillis / 1000);
        redisTemplate.expire(userId, expirationTimeMillis, TimeUnit.SECONDS);
    }

    public void deleteRefreshToken(String userId){
        redisTemplate.delete(userId);
    }
}