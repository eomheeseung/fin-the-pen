package project.fin_the_pen.finClient.listener;

import lombok.extern.slf4j.Slf4j;
import project.fin_the_pen.finClient.data.schedule.Schedule;
import project.fin_the_pen.finClient.data.schedule.ScheduleManage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
//@Component
// TODO
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
