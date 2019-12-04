package com.lk.redis.config;

import com.lk.redis.entity.RedissonLock;
import com.lk.redis.entity.RedissonManager;
import com.lk.redis.entity.RedissonProperties;
import org.redisson.Redisson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @Description: Redisson自动化配置
 * @ConditionalOnClass：该注解的参数对应的类必须存在，否则不解析该注解修饰的配置类；
 * @ConditionalOnMissingBean：该注解表示，如果存在它修饰的类的bean，则不需要再创建这个bean；可以给该注解传入参数例如@ConditionOnMissingBean(name = "example")
 * ，这个表示如果name为“example”的bean存在，这该注解修饰的代码块不执行
 * @author wangzh
 * @date 2019/6/19 下午11:55
 */
@Configuration
@ConditionalOnClass(Redisson.class)
@EnableConfigurationProperties(RedissonProperties.class)
@ComponentScan("com.lk.redis.annotation")
public class RedissonAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    @Order(value = 2)
    public RedissonLock redissonLock(RedissonManager redissonManager) {
        RedissonLock redissonLock = new RedissonLock(redissonManager);
        LOGGER.info("[RedissonLock]组装完毕");
        return redissonLock;
    }

    @Bean
    @ConditionalOnMissingBean
    @Order(value = 1)
    public RedissonManager redissonManager(RedissonProperties redissonProperties) {
        RedissonManager redissonManager =
                new RedissonManager(redissonProperties);
        LOGGER.info("[RedissonManager]组装完毕,当前连接方式:" + redissonProperties.getType() +
            ",连接地址:" + redissonProperties.getAddress());
        return redissonManager;
    }
}

