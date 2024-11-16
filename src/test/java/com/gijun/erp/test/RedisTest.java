package com.gijun.erp.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private ValueOperations<String, String> valueOps;

    @BeforeEach
    public void setUp() {
        valueOps = redisTemplate.opsForValue();
    }

    @AfterEach
    public void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Test
    public void testRedisSetAndGet() {
        valueOps.set("testKey", "testValue");
        String value = valueOps.get("testKey");
        assertThat(value).isEqualTo("testValue");
    }
}
