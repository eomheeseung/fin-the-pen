package project.fin_the_pen.repository;

import org.springframework.stereotype.Repository;
import project.fin_the_pen.data.schedule.Schedule;
import project.fin_the_pen.data.schedule.ScheduleRequestDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class ScheduleRepository {
    @PersistenceContext
    EntityManager entityManager;

    //TODO 일정을 가져올 수 있어야 함
    public void registerSchedule(ScheduleRequestDTO scheduleRequestDTO) {
        Schedule schedule = new Schedule();

        schedule.setCategories(scheduleRequestDTO.getCategories());
        schedule.setExclusion(scheduleRequestDTO.isExclusion());
        schedule.setPeriod(scheduleRequestDTO.getPeriod());
        schedule.setEndTime(scheduleRequestDTO.getEndTime());
        schedule.setStartDateTime(scheduleRequestDTO.getStartDateTime());
        schedule.setEventDate(scheduleRequestDTO.getEventDate());
        schedule.setExpected_spending(scheduleRequestDTO.getExpected_spending());
        schedule.setCategories(scheduleRequestDTO.getCategories());
        schedule.setType(scheduleRequestDTO.getType());
        schedule.setUserId(scheduleRequestDTO.getUserId());

        entityManager.persist(schedule);
        entityManager.flush();
    }
}
