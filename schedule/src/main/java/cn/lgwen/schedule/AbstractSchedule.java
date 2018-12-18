package cn.lgwen.schedule;


import lombok.Setter;
import org.springframework.scheduling.Trigger;

/**
 * create schedule based this class
 */
public abstract class AbstractSchedule implements Schedule {

    private String name;

    @Setter
    private Trigger trigger;

    @Override
    public String getScheduleName() {
        return name;
    }
}
