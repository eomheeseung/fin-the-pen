package project.fin_the_pen.model.schedule.listener;

import lombok.extern.slf4j.Slf4j;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.ScheduleManage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
public class ScheduleListener {
    @PersistenceContext
    EntityManager entityManager;

//    @PrePersist
//    @Transactional
    public void scheduleListen(Schedule entity) {
        if (entity.getScheduleManage() == null) {
            ScheduleManage manage = new ScheduleManage();
//            entityManager.persist(manage);

            manage.setDeleteFlag(false);
            entity.setScheduleManage(manage);
            log.info("test");
        }
    }
}
