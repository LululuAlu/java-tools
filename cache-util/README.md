## 缓存工具
```cn.lgwen.cache.util.cache``` 注解需要缓存的类用来生成代理类，
@cache注解注释方法用来代理方法为缓存方法。

### 例子：
* 注解类
```java
@Cache
public class Example {
    
    @Cache("say")
    public String sayHallo() {
        return "abc";
    }
    
}
```
* cache 注解有4个参数
  * String value 缓存的key
  * long timeOut 缓存超时时间
  * int timeUnit 超时时间的单位
  * String keyGenerator 定制的key生成器