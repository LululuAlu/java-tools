package cn.lgwen.schedule;


import org.springframework.scheduling.Trigger;

public interface Schedule extends Runnable {
    /**
     * each schedule have their unique name
     * @return schedule name
     */
    String getScheduleName();

    /**
     * two trigger
     * @see org.springframework.scheduling.support.CronTrigger
     * @see org.springframework.scheduling.support.PeriodicTrigger
     * @return trigger
     */
    Trigger getTrigger();

}
