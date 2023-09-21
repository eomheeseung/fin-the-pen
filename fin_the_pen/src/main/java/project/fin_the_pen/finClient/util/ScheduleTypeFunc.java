package project.fin_the_pen.finClient.util;

import project.fin_the_pen.finClient.data.schedule.ScheduleRequestDTO;

@FunctionalInterface
public interface ScheduleTypeFunc {
    public void callbackMethod(ScheduleRequestDTO dto);
}
