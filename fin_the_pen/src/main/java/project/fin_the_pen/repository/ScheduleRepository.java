package project.fin_the_pen.repository;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.data.schedule.Schedule;
import project.fin_the_pen.data.schedule.ScheduleRequestDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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
        } catch (Exception e) {
            return null;
        }
        return true;
    }

    // TODO 1. 바로 해야 함
    public JSONArray findAllSchedule(String id) {
        List<Schedule> scheduleList = entityManager.createQuery("select s from Schedule s where s.userId =: id", Schedule.class)
                .setParameter("id", id)
                .getResultList();

        JSONArray jsonArray = new JSONArray(scheduleList);//배열이 필요할때

        // JSON 으로 변환
        /*try {

            for (Schedule schedule : scheduleList) {
                JSONObject jsonObject = new JSONObject();//배열 내에 들어갈 json
                jsonObject.put("id", schedule.getId());
                jsonObject.put("event_name", schedule.getEventName());
                jsonObject.put("alarm", schedule.isAlarm());
                jsonArray.put(jsonObject);
                log.info(jsonObject.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        return jsonArray;
    }
}
