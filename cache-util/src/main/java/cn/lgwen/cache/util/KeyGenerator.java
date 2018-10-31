package cn.lgwen.cache.util;

import java.lang.reflect.Method;

/**
 * create by wuxuyang on 2018/10/31
 */
public interface KeyGenerator {

    Object generate(Object target, Method method, Object... params);
}
