package project.fin_the_pen.finClient.listener;

import lombok.extern.slf4j.Slf4j;
import project.fin_the_pen.finClient.data.schedule.Schedule;
import project.fin_the_pen.finClient.data.schedule.ScheduleManage;

import javax.persistence.EntityListeners;
import javax.persistence.PrePersist;
import javax.transaction.Transactional;

@Slf4j
@EntityListeners(value = ScheduleListener.class)
// TODO
public class ScheduleListener {

    @PrePersist
    @Transactional
    public void scheduleListen(Schedule entity) {
        if (entity.getScheduleManage() == null) {
            ScheduleManage manage = new ScheduleManage();
            manage.setDeleteFlag(false);
            entity.setScheduleManage(manage);
            log.info("test");
        }
    }
}
