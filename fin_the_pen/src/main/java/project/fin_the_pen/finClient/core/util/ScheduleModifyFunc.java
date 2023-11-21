package project.fin_the_pen.finClient.core.util;

import project.fin_the_pen.model.schedule.dto.ScheduleDTO;

@FunctionalInterface
public interface ScheduleModifyFunc {
    public boolean modifyCallBack(ScheduleDTO dto);
}
