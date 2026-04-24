package com.api.NubeSandovalAPI.config.redis;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisTestRunner {

    @Bean
    public CommandLineRunner run(RedisTestService redisTestService) {
        return args -> {
            redisTestService.testRedis();
        };
    }
}
