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

	/**
	 * 0323
	 * @return
	 */
	@Bean
	public Redisson redisson(){
<<<<<<< HEAD
		//此为 单机模式
		// 0323
=======
		//此为单机模式 0325
>>>>>>> 0323
		Config config = new Config();
		config.useSingleServer().setAddress("http://120.76.242.182:16379").setPassword("123456");
		return (Redisson) Redisson.create(config);
	}
}
