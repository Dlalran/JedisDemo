package priv.howard.jedisdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import priv.howard.jedisdemo.entity.User;
import priv.howard.jedisdemo.service.UserService;
import redis.clients.jedis.JedisPool;

@SpringBootTest
class JedisDemoApplicationTests {

    @Autowired
    private JedisPool jedisPool;
    @Test
    void contextLoads() {
//        测试是否成功注入JedisPool
        System.out.println(jedisPool);
    }

    @Autowired
    private UserService userService;

    @Test
    void testString() {
        User result = userService.testRedisString("1");
        System.out.println(result);
    }

    @Test
    void testHash() {
        User result = userService.testRedisHash("2");
        System.out.println(result);
    }
}
