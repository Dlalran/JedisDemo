package priv.howard.jedisdemo.service;

import priv.howard.jedisdemo.entity.User;

public interface UserService {
    /**
     * @Description 通过Redis进行缓存实现，
     * 查询时先在Redis查询，不存在则在MySQL查询并放入Redis
     * 转为JSON格式后存储在String类型中
     */
    User testRedisString(String key);

    User testRedisHash(String id);
}
