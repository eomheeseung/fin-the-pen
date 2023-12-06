package project.fin_the_pen.finClient.core.util;

import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;

@FunctionalInterface
public interface ScheduleTypeFunc {
    public void callbackMethod(ScheduleRequestDTO dto);
}
