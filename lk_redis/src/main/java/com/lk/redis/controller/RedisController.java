package com.lk.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 非高并发时可用
 */
@RestController("/redis")
public class RedisController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/deStock")
    public  String deStock(){
        String lockKey = "item_001";//商品id
        String clientId = UUID.randomUUID().toString();
        try {

            Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey,clientId,10,TimeUnit.SECONDS);
            if(!result){
                return  "fail";
            }
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if(stock>0){
                int realStock = stock -1;
                stringRedisTemplate.opsForValue().set("stock",realStock+"");
                System.out.println("扣减成功，剩余库存："+realStock+"");
            }else {
                System.out.println("扣减失败，库存不足");
            }

        }finally {
            if(clientId.equals(stringRedisTemplate.opsForValue().get(lockKey))){
                //释放锁
                stringRedisTemplate.delete(lockKey);
            }
        }
        return "success";
    }
}
