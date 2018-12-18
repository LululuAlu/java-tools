package cn.lgwen.schedule;


import lombok.Setter;

/**
 * create schedule based this class
 */
public abstract class AbstractSchedule implements Schedule {

    @Setter
    private String name;


    @Override
    public String getScheduleName() {
        if (name == null) {
            return this.getClass().getName();
        }
        return name;
    }
}
