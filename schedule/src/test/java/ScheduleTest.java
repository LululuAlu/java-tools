import cn.lgwen.schedule.AbstractSchedule;
import cn.lgwen.schedule.ScheduleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.concurrent.TimeUnit;

public class ScheduleTest extends AbstractSchedule {
    @Override
    public void run() {
        System.out.println("123");
    }

    @Override
    public Trigger getTrigger() {
        return new PeriodicTrigger(1000, TimeUnit.SECONDS);
    }

    @Autowired
    private ScheduleManager scheduleManage;

    public void start() {
        ScheduleTest sayHelloSchedule = new ScheduleTest();
        scheduleManage.run(sayHelloSchedule);
    }
}
