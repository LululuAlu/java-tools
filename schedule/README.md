## schedule 定时任务管理

* 依赖 threadPoolTaskScheduler
```java
@Bean
public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
    return new ThreadPoolTaskScheduler();
}

@Bean
public ScheduleManage scheduleManage() {
     return new ScheduleManage();
}
```
### 例子
* 创建schedule 实现
```java
public class SayHelloSchedule extends AbstractSchedule {
    @Override
    public void run() {
        System.out.println("hello world");
    }

    @Override
    public Trigger getTrigger() {
        return new PeriodicTrigger(1000, TimeUnit.SECONDS);
    }
}
```
* 添加并启动定时任务
```java
@Autowired
private ScheduleManager scheduleManager;

public void start() {
    SayHelloSchedule sayHelloSchedule = new SayHelloSchedule()
    sayHelloSchedule.setName("sayHelloSchedule");
    scheduleManage.run(sayHelloSchedule);
}
```
