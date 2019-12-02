package com.lk.redis.controller;


import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController("/redisson")
public class RedissonController {
    @Autowired
    private Redisson redisson;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/deductStock")
    public  String deductStock(){
        String lockKey = "item_001";//商品id
        String clientId = UUID.randomUUID().toString();
        RLock rLock = redisson.getLock(lockKey);
        try {
            //加锁
            rLock.lock(30,TimeUnit.SECONDS);//设置锁时间 (redis单例，多个请求并发时 只有一个请求可以继续走下面代码)

            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if(stock>0){
                int realStock = stock -1;
                stringRedisTemplate.opsForValue().set("stock",realStock+"");
                System.out.println("扣减成功，剩余库存："+realStock+"");
            }else {
                System.out.println("扣减失败，库存不足");
            }
        }finally {
            rLock.unlock();
        }
        return "success";
    }

}
