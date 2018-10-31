package cn.lgwen.cache.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * create by wuxuyang on 2018/8/21
 * 拦截bean 动态代理bean
 */
@Component
@Slf4j
public class CacheBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private CacheProxy proxy;

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return CacheBeanFactory.createCacheProxyBean(o, proxy);
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }

}
