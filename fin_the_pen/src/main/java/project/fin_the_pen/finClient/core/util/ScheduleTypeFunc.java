package project.fin_the_pen.finClient.core.util;

import project.fin_the_pen.finClient.schedule.dto.ScheduleDTO;

@FunctionalInterface
public interface ScheduleTypeFunc {
    public void callbackMethod(ScheduleDTO dto);
}
