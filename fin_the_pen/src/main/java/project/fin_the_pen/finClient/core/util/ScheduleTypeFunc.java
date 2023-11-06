package project.fin_the_pen.finClient.core.util;

import project.fin_the_pen.finClient.schedule.dto.ScheduleAllDTO;

@FunctionalInterface
public interface ScheduleTypeFunc {
    public void callbackMethod(ScheduleAllDTO dto);
}
