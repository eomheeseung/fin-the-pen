package project.fin_the_pen.finClient.util;

import project.fin_the_pen.finClient.data.schedule.ScheduleAllDTO;

@FunctionalInterface
public interface ScheduleTypeFunc {
    public void callbackMethod(ScheduleAllDTO dto);
}
