package com.francis.ktv;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KtvApplicationTests {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    public void contextLoads() {
        redisTemplate.opsForValue().set("test","我是测试内容");
        redisTemplate.opsForValue().getOperations().delete("test");
        System.out.println(redisTemplate.opsForValue().get("test"));
    }
}
