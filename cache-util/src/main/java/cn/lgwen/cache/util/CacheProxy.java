package cn.lgwen.cache.util;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * create by wuxuyang on 2018/8/22
 */
@Slf4j
public class CacheProxy implements MethodInterceptor{

    @Setter
    private CacheTemplate cacheTemplate;

    @Setter
    private KeyGenerator generator;


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        if (cacheTemplate == null) {
            throw new RuntimeException("cacheTemplate cannot be null. Try to user cache method after bean init");
        }
        Method method = methodInvocation.getMethod();
        Cache cache = AnnotationUtils.findAnnotation(method, Cache.class);
        Object[] args = methodInvocation.getArguments();// 获取目标方法参数
        if (cache == null) {
            return method.invoke(methodInvocation.getThis(), args);
        }
        String key = "";
        String keyGenerator = cache.keyGenerator();
        if(!"".equals(keyGenerator) && generator != null) {
            Object generateKey = generator.generate(methodInvocation.getThis(), method, args);
            if (generateKey == null || !(generateKey instanceof String)) {
                log.warn("generator:{}.{} must be string", keyGenerator, "generator" );
            } else {
                key = generateKey.toString();
            }
        } else {
            // generate key is is null
            key = generatorKey(cache.value(), args);
        }

        if (key != null && !"".equals(key)) {
            //if key is empty doesn't cache
            return method.invoke(methodInvocation.getThis(), args);
        }

        // get value from redis
        Object cacheObj = cacheTemplate.get(key);
        if (cacheObj != null) {
            log.debug("从缓存中获取结果:{}", method.getName());
            return cacheObj;
        }
        // cannot hit cache from redis
        // execute invoke
        Object result = method.invoke(methodInvocation.getThis(), args);
        // put to redis
        doPut(key, result, cache);
        return result;
    }

    private TimeUnit timeUnit(int timeUnit) {
        switch (timeUnit) {
            case CacheUnit.MILLISECONDS:
                return TimeUnit.MILLISECONDS;
            case CacheUnit.SECONDS:
                return TimeUnit.SECONDS;
            case CacheUnit.MINUTES:
                return TimeUnit.MINUTES;
            case CacheUnit.HOURS:
                return TimeUnit.HOURS;
            case CacheUnit.DAYS:
                return TimeUnit.DAYS;
            default:
                return TimeUnit.MILLISECONDS;
        }
    }

    private void assertAnnotation(Cache cache) {

    }

    private void doPut(String key, Object object, Cache cache) {
        cacheTemplate.put(key, object);
        assertAnnotation(cache);
        long timeOut = cache.timeOut();
        if (timeOut == 0) {
            // no over-time
            return;
        }
        int unit = cache.timeUnit();
        TimeUnit timeUnit = timeUnit(unit);
        cacheTemplate.expire(key, timeOut, timeUnit);
    }
    /**
     *
     * @param key
     * @param args 方法的参数
     * @return
     */
    private static String generatorKey(String key, Object[] args) throws Throwable{
        if (key.contains("+#p")) {
            //contain placeholder
            String[] placeholders = key.split("\\+");
            Iterator<String> iterable =Arrays.asList(placeholders).iterator();
            while (iterable.hasNext()) {
                String str = iterable.next();
                if (str.contains("#p")) {
                    //contain placeholder
                    String order = str.substring(str.indexOf("p") + 1, str.indexOf("p") + 2);
                    try {
                        Integer num = Integer.valueOf(order);
                        key = key.replace("+" + str, findParamValue(str, num, args));
                        //转换成功 从参数列表中获取参数
                    } catch (NumberFormatException e) {
                        log.warn("conn't parse string to number");
                    }
                }
            }
        }
        return key;
    }

    private static String findParamValue(String placeholder, Integer order, Object[] args) throws Throwable{
        if (order > args.length) {
            throw new IllegalArgumentException("placeholder index cannot greater then args number");
        }
        Object arg = args[order];
        if (placeholder.contains(".")) {
            String argument = placeholder.substring(placeholder.lastIndexOf(".") + 1, placeholder.length());
            String firstLetter = argument.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + argument.substring(1);
            Method method = arg.getClass().getMethod(getter);
            Object value = method.invoke(arg);
            return value.toString();
        }
        return args[order].toString();
    }

}
