package priv.howard.jedisdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.howard.jedisdemo.entity.Address;
import priv.howard.jedisdemo.entity.User;
import priv.howard.jedisdemo.service.UserService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@Service
//通过Lombok的@Log注解可以自动创建日志类
@Log
public class UserServiceImpl implements UserService {
    /**
     * @Description 通过Redis进行缓存实现，
     * 查询时先在Redis查询，不存在则在数据库查询并放入Redis
     */

//    注入Jedis连接池
    @Autowired
    private JedisPool jedisPool;

    /**
     * 测试操作String类型数据，转为JSON格式后存储在String类型中
     */
    @Override
    public User testRedisString(String id) {
//        通过连接池获取Jedis对象
        Jedis jedis = jedisPool.getResource();
        String key = "user:" + id;

        User user = null;
//        判断key是否存在
        if (jedis.exists(key)) {
            String value = jedis.get(key);
//            反序列化为对象，如果对象类型包含泛型则需要使用TypeReference
            user = JSON.parseObject(value, User.class);
            log.info("Redis中存在key:" + key + "，value:" + value);
        } else {
            log.info("Redis中不存在该数据，将在数据库中查询");
//            模拟数据库查询
            user = new User("1", "Howard", 22, new Address("scau", "sz"));
//            通过Fastjson将对象转换为json格式字符串
            String value = JSON.toJSONString(user);
//            将该key-value放入Redis中
            jedis.set(key, value);
            log.info("将key:" + key + "，value:" + value + "放入Redis中");
        }

//        注意使用后关闭连接
        jedis.close();
        return user;
    }

    /**
     * 测试Hash类型数据的操作
     */
    @Override
    public User testRedisHash(String id) {
        /**
         * TODO 手动转换Map和对象
         */
        String key = "user:" + id;
        Jedis jedis = jedisPool.getResource();
        User user = null;

        if (jedis.exists(key)) {
            log.info("Redis中存在key为" + key + "的数据");
            Map<String, String> value = jedis.hgetAll(key);
//            Map -> JSON字符串 -> JSON对象
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(value));
//            JSON对象 -> 对象
            user =(User) jsonObject.toJavaObject(User.class);
        } else {
            log.info("Redis中不存在该Key，将在数据库中查询");
            user = new User("2", "John", 23, new Address("pku", "gz"));
//            将对象序列化为JSON对象(Object -> JSON)
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(user);
//            将JSON对象反序列化成Map(JSON -> Map)
//            注意：此处泛型对象的反序列化需要使用TypeReference
            Map<String, String> map = jsonObject.toJavaObject(new TypeReference<Map<String, String>>(){});
//            放入Redis Hash中(Map -> field-value)
            jedis.hmset(key, map);
            log.info("将key为" + key + "的Hash放入Redis中");
        }

        jedis.close();
        return user;
    }
}
