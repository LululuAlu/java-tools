package cn.lgwen.cache.util;

import java.util.concurrent.TimeUnit;

/**
 * create by wuxuyang on 2018/10/31
 * cache proxy use this template save or get object to cache such eCache or redis
 */
public interface CacheTemplate {

    /**
     * get object from cache
     * @param key
     * @return if key is not found from cache return null
     */
    Object get(String key);

    /**
     * put object to cache with key and set expire time
     * @param key
     * @param object
     * @param expire if null cache don't expire
     * @param timeUnit
     */
    void put(String key, Object object, Long expire, TimeUnit timeUnit);

    /**
     * put object to cache with key and don't expire
     * @param key
     * @param object
     */
    void put(String key, Object object);

    /**
     * set expire witch key called 'key'
     * @param expire if null cache don't expire
     * @param timeUnit
     */
    void expire(String key, Long expire, TimeUnit timeUnit);

    /**
     * remove object from cache witch key is 'key'
     * @param key
     */
    void remove(String key);
}
