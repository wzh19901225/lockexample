package com.lk.redis;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class LkRedisApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(LkRedisApplication.class, args);
	}

	@Bean
	public Redisson redisson(){
		//此为单机模式
		Config config = new Config();
		config.useSingleServer().setAddress("120.76.242.182:16379").setDatabase(0);
		return (Redisson) Redisson.create(config);
	}
}
