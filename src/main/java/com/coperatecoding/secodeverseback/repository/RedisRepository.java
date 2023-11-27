package com.coperatecoding.secodeverseback.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.refreshtoken-validity-in-seconds}")
    public long expirationTimeMillis; // 만료시간 2주


    @Autowired
    public RedisRepository(RedisTemplate<String, Object> redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set(userId, refreshToken, expirationTimeMillis);
        redisTemplate.expire(userId, expirationTimeMillis, TimeUnit.SECONDS);
    }

    public void deleteRefreshToken(String userId){
        redisTemplate.delete(userId);
    }

    public String getRefreshToken(String userId) {
        return (String) redisTemplate.opsForValue().get(userId);
    }


}