package cn.lgwen.cache.util;

import java.lang.annotation.*;

/**
 * create by wuxuyang on 2018/8/21
 * 缓存注解
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {

    /**
     * cache name
     * 当使用了keyGenerator
     * @return
     */
    String value() default "";

    /**
     * 超时时间
     * @return
     */
    long timeOut() default 0;

    /**
     * 单位
     * @return
     */
    int timeUnit() default 0;

    /**
     * 定制的key
     * @return
     */
    String keyGenerator() default "";

    /**
     * 库
     * @return
     */
    String database() default "";
}
