package com.lk.redis.controller;

import com.lk.redis.annotation.DistributedLock;
import com.lk.redis.entity.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangzh
 * @Description: 基于注解的方式 加锁
 * @date 2019/6/19 下午11:01
 */
@RestController("/annotationRedisson")
@Slf4j
public class AnnotatinLockController {

    @Autowired
    RedissonLock redissonLock;

    /**
     * 模拟这个是商品库存
     */
    public static volatile Integer TOTAL = 10;

    @GetMapping("/annotatin-lock-decrease-stock")
    @DistributedLock(value="goods", leaseTime=5)
    public String lockDecreaseStock() throws InterruptedException {
        if (TOTAL > 0) {
            TOTAL--;
        }
        log.info("===注解模式=== 减完库存后,当前库存===" + TOTAL);
        return "=================================";
    }
}
