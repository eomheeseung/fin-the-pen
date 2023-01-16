package project.fin_the_pen.repository;

import org.springframework.stereotype.Repository;
import project.fin_the_pen.data.schedule.Schedule;
import project.fin_the_pen.data.schedule.ScheduleDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class ScheduleRepository {
    @PersistenceContext
    EntityManager entityManager;

    //TODO 일정을 가져올 수 있어야 함
    public void registerSchedule(ScheduleDAO scheduleDAO) {
        Schedule schedule = new Schedule();

        schedule.setCategories(scheduleDAO.getCategories());
        schedule.setExclusion(scheduleDAO.isExclusion());
        schedule.setPeriod(scheduleDAO.getPeriod());
        schedule.setEndTime(scheduleDAO.getEndTime());
        schedule.setStartDateTime(scheduleDAO.getStartDateTime());
        schedule.setEventDate(scheduleDAO.getEventDate());
        schedule.setExpected_spending(scheduleDAO.getExpected_spending());
        schedule.setCategories(scheduleDAO.getCategories());
        schedule.setType(scheduleDAO.getType());
        schedule.setUserId(scheduleDAO.getUserId());

        entityManager.persist(schedule);
    }
}
