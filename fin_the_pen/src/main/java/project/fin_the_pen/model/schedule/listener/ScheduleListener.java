package project.fin_the_pen.model.schedule.listener;

/*@Slf4j
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
}*/
