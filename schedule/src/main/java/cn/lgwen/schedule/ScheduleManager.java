package cn.lgwen.schedule;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

@Slf4j
@NoArgsConstructor
public class ScheduleManager {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private Map<String, Future> futureMap = new ConcurrentHashMap<>();

    private Map<String, Schedule> scheduleMap = new ConcurrentHashMap<>();

    /**
     * unique key schedule.name
     * add schedule and schedule will not start
     * you should call {@link #run(Schedule)}
     * @param schedule
     * @throws IllegalArgumentException when schedule.name conflict
     */
    public void add(Schedule schedule){
        if(scheduleMap.containsKey(schedule.getScheduleName())) {
            throw new IllegalArgumentException("DuplicateKey,the schedule has already contains " + schedule.getScheduleName());
        }
        scheduleMap.put(schedule.getScheduleName(), schedule);
    }

    public Schedule get(String scheduleName) {
        return scheduleMap.get(scheduleName);
    }

    public Collection<Schedule> getAll() {
        return scheduleMap.values();
    }
    /**
     * stop and delete specified schedule
     * @param schedule
     */
    public void remove(Schedule schedule){
        stop(schedule);
        scheduleMap.remove(schedule.getScheduleName());
    }

    /**
     * stop and delete all schedule
     */
    public void clear() {
        if (scheduleMap != null && !scheduleMap.isEmpty()) {
            for (Schedule schedule : scheduleMap.values()) {
                remove(schedule);
            }
        }
    }

    /**
     * stop specified schedule
     */
    public void stop(Schedule schedule) {
        if(scheduleMap.containsKey(schedule.getScheduleName())) {
            Future future = futureMap.remove(schedule.getScheduleName());
            if (future != null) {
                future.cancel(true);
            }
        }
    }
    /**
     * add & run schedule immediately
     * if ScheduleManager contains same name schedule stop remove first and create
     * @param schedule
     */
    public synchronized void run(Schedule schedule) {
        if (!scheduleMap.containsKey(schedule.getScheduleName())) {
            add(schedule);
        }
        remove(schedule);
        Future future = threadPoolTaskScheduler.schedule(schedule, schedule.getTrigger());
        futureMap.put(schedule.getScheduleName(),future);
    }

    /**
     * start all schedule
     */
    public synchronized void start() {
        if (scheduleMap != null && !scheduleMap.isEmpty()) {
            for (Schedule schedule : scheduleMap.values()) {
                if (futureMap.containsKey(schedule.getScheduleName())) {
                    continue;
                }
                Future future = threadPoolTaskScheduler.schedule(schedule, schedule.getTrigger());
                futureMap.put(schedule.getScheduleName(),future);
            }
        }
    }

    /**
     * stop all schedule
     */
    public void shutdown() {
        if (futureMap != null && !futureMap.isEmpty()) {
            for (Map.Entry<String, Future> schedule : futureMap.entrySet()) {
                if (!futureMap.containsKey(schedule.getKey())) {
                    continue;
                }
                if (schedule.getValue() != null) {
                    schedule.getValue().cancel(true);
                }
                futureMap.remove(schedule.getKey());
            }
        }
    }

}
