package annotion;

import com.lk.redis.LkRedisApplication;
import com.lk.redis.entity.RedissonProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LkRedisApplication.class)
public class PropertiesTest {
    @Autowired
    RedissonProperties redissonProperties;

    @Test
    public  void RedissonPropertiesTest(){
        System.out.println(redissonProperties.getPassword());
    }


}
