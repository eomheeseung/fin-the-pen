package project.fin_the_pen.model.schedule.service.modify;

import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;

import java.util.List;

public interface ModifyXXXFunc {
    public void modifySchedule(ModifyScheduleDTO dto, List<Schedule> entities);
}
