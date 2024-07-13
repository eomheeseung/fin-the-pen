package project.fin_the_pen.model.schedule.service.modify;

import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.template.Template;

public interface ModifyXXXFunc {
    public void modifySchedule(ModifyScheduleDTO dto);

    void notTemplateAfterModify(ModifyScheduleDTO dto);

    void templateAfterModify(ModifyScheduleDTO dto, Template template);

}
