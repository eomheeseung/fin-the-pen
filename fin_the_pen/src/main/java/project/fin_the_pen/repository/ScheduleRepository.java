package project.fin_the_pen.repository;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.data.schedule.Schedule;
import project.fin_the_pen.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.data.schedule.ScheduleResponseDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
@Slf4j
public class ScheduleRepository {
    @PersistenceContext
    EntityManager entityManager;

    //TODO 일정을 가져올 수 있어야 함
    public Boolean registerSchedule(ScheduleRequestDTO scheduleRequestDTO) {
        try {
            Schedule schedule = new Schedule(scheduleRequestDTO.getId(), scheduleRequestDTO.getUserId(),
                    scheduleRequestDTO.getEventName(), scheduleRequestDTO.isAlarm(), scheduleRequestDTO.getDate(), scheduleRequestDTO.getStartTime(),
                    scheduleRequestDTO.getEndTime(), scheduleRequestDTO.getCategory(), scheduleRequestDTO.getType(),
                    scheduleRequestDTO.getExpectedSpending(), scheduleRequestDTO.getRepeatingCycle(),
                    scheduleRequestDTO.getRepeatDeadLine(), scheduleRequestDTO.getRepeatEndDate(),
                    scheduleRequestDTO.isExclusion(), scheduleRequestDTO.getImportance());
            entityManager.persist(schedule);
            log.info(schedule.getUserId());
        } catch (Exception e) {
            return null;
        }
        return true;
    }

    // TODO 1. 바로 해야 함
    public JSONArray findAllSchedule(String id) {
        List<Schedule> scheduleList = entityManager.createQuery("select s from Schedule s where s.userId = :id", Schedule.class)
                .setParameter("id", id)
                .getResultList();

        List<ScheduleResponseDTO> schduleResponseList = new ArrayList<>();

        log.info(String.valueOf(scheduleList.size()));

        for (Schedule schedule : scheduleList) {
            ScheduleResponseDTO responseDTO = ScheduleResponseDTO.builder().id(schedule.getId()).alarm(schedule.isAlarm())
                    .endTime(schedule.getEndTime()).startTime(schedule.getStartTime()).category(schedule.getCategory())
                    .eventName(schedule.getEventName()).exclusion(schedule.isExclusion())
                    .expectedSpending(schedule.getExpectedSpending()).type(schedule.getType())
                    .importance(schedule.getImportance()).repeatDeadline(schedule.getRepeatDeadline())
                    .repeatEndDate(schedule.getRepeatEndDate()).repeatingCycle(schedule.getRepeatingCycle())
                    .date(schedule.getDate()).build();
            schduleResponseList.add(responseDTO);
        }

        JSONArray jsonArray = new JSONArray(schduleResponseList);
        return jsonArray;
    }
}
