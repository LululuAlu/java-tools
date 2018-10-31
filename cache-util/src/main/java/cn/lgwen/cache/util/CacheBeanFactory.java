package cn.lgwen.cache.util;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * create by wuxuyang on 2018/10/31
 * cache proxy bean factory
 */
public class CacheBeanFactory {

    private CacheBeanFactory() {

    }

    public static Object createCacheProxyBean(Object o, CacheProxy proxy) {
        Class clazz = o.getClass();
        Cache cache = AnnotationUtils.findAnnotation(clazz, Cache.class);
        if (cache == null) {
            return o;
        }
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(o);
        proxyFactory.addAdvice(proxy);
        return proxyFactory.getProxy();
    }
}
