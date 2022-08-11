package com.jiaozx.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jiaozx.configuration.CustomObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @ClassName RedisTemplate
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/7/30 17:00
 * @Version 1.0
 */

@Component
@Slf4j
public class RedisTemplate {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private CustomObjectMapper objectMapper;

    /**
     * 存储普通字符串
     *
     * @param key
     * @param value
     * @param expire
     */
    public String set(String key, String value, Long expire) {
        Jedis jedis = jedisPool.getResource();
        String returnValue = null;
        try {
            // 如果操作成功会返回“ok”字符串，
            returnValue = jedis.setex(key, expire, value);
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            log.error("Redis execution error !", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return returnValue;
    }

    /**
     * 根据key获得一个字符串数据
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String returnValue = null;
        try {
            returnValue = jedis.get(key);
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            log.error("Redis execution error !", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return returnValue;
    }

    /**
     * 存储对象类型的数据
     *
     * @param key
     * @param object
     * @param expire
     * @return
     */
    public String setObject(String key, Object object, Long expire) {
        Jedis jedis = jedisPool.getResource();
        String returnValue = null;
        try {
            if (0 >= expire || null == expire) {
                // 讲object进行序列化
                String objValue = objectMapper.writeValueAsString(object);
                // 如果操作成功会返回“ok”字符串，
                returnValue = jedis.set(key, objValue);
            } else {
                String objValue = objectMapper.writeValueAsString(object);
                // 如果操作成功会返回“ok”字符串，
                returnValue = jedis.set(key, objValue);
                jedis.expire(key, expire);
            }
        } catch (JedisException | JsonProcessingException e) {
            jedisPool.returnBrokenResource(jedis);
            log.error("Redis execution error !", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return returnValue;
    }

    /**
     * 获取对象类型的数据
     *
     * @param key
     * @param valueType
     * @param <T>
     * @return
     */
    public <T> T getObject(String key, Class<T> valueType) {


        Jedis jedis = jedisPool.getResource();
        T object = null;
        try {
            // 如果操作成功会返回“ok”字符串，
            String returnValue = jedis.get(key);
            object = objectMapper.readValue(returnValue, valueType);
        } catch (IOException e) {
            jedisPool.returnBrokenResource(jedis);
            log.error("Redis execution error !", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return object;
    }

    public <T> T getObject(String key, TypeReference<T> typeReference) {

        Jedis jedis = jedisPool.getResource();
        T object = null;
        try {
            // 如果操作成功会返回“ok”字符串，
            String objectValue = jedis.get(key);
            object = objectMapper.readValue(objectValue, typeReference);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
            log.error("Redis execution error !", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return object;
    }


    /**
     * 删除多个key
     *
     * @param key
     * @return
     */
    public Long remove(String... key) {
        Jedis jedis = jedisPool.getResource();
        try {
            // 如果操作成功会返回“ok”字符串，
            return jedis.del(key);
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            log.error("Redis execution error !", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return -1L;
    }

    public Long lpush(String key, String... value) {
        Jedis jedis = jedisPool.getResource();
        long pos = -1;
        try {
            // 如果操作成功会返回“ok”字符串，
            pos = jedis.lpush(key, value);
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            log.error("Redis execution error !", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return pos;
    }

    public Optional<String> rpop(String key) {
        Jedis jedis = jedisPool.getResource();
        Optional<String> returnValue = Optional.empty();
        try {
            // 如果操作成功会返回“ok”字符串，
            String value = jedis.rpop(key);
            System.out.println("---------" + value);
            returnValue = value == null ? Optional.empty() : Optional.of(value);
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            log.error("Redis execution error !", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return returnValue;
    }

    public long llen(String key) {
        Jedis jedis = jedisPool.getResource();
        long llen = -1;
        try {
            // 如果操作成功会返回“ok”字符串，
            llen = jedis.llen(key);
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            log.error("Redis execution error !", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return llen;
    }

    public Set<String> keys(String token) {
        Jedis jedis = jedisPool.getResource();
        Set<String> key = null;
        try {
            key = jedis.keys(token);
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            log.error("Redis execution error !", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return key;
    }

    public void expire(String tokenKey, Long expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.expire(tokenKey, expire);
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            log.error("Redis execution error !", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
}

