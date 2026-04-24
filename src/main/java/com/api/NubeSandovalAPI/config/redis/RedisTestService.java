package com.api.NubeSandovalAPI.config.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTestService {

    private final StringRedisTemplate redisTemplate;

    public RedisTestService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void testRedis() {
        redisTemplate.opsForValue().set("test:key", "Hola Redis 🚀");
        String value = redisTemplate.opsForValue().get("test:key");
        System.out.println("Valor desde Redis: " + value);
    }
}
