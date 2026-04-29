package com.api.NubeSandovalAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration.class
})
public class NubeSandovalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NubeSandovalApiApplication.class, args);
	}

}
