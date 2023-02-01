package project.fin_the_pen.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import project.fin_the_pen.data.schedule.Schedule;
import project.fin_the_pen.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.data.schedule.ScheduleResponseDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

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
                    scheduleRequestDTO.getEventName(), scheduleRequestDTO.isAlarm(), scheduleRequestDTO.getDate(),
                    scheduleRequestDTO.getStartTime(), scheduleRequestDTO.getEndTime(), scheduleRequestDTO.getCategory(),
                    scheduleRequestDTO.getType(), scheduleRequestDTO.getExpectedSpending(), scheduleRequestDTO.getRepeatingCycle(),
                    scheduleRequestDTO.getRepeatDeadLine(), scheduleRequestDTO.getRepeatEndDate(),
                    scheduleRequestDTO.isExclusion(), scheduleRequestDTO.getImportance());
            entityManager.persist(schedule);
            log.info(schedule.getUserId());
        } catch (Exception e) {
            return null;
        }
        return true;
    }

    // 전체 일정을 jsonArray로 리턴
    public JsonObject findAllSchedule(String id) {
        List<Schedule> scheduleList = entityManager.createQuery("select s from Schedule s where s.userId = :id", Schedule.class)
                .setParameter("id", id)
                .getResultList();

//        List<ScheduleResponseDTO> schduleResponseList = new ArrayList<>();

        log.info(String.valueOf(scheduleList.size()));

        JsonObject jsonObj = new JsonObject();

        if (CollectionUtils.isEmpty(scheduleList) == false) {
            JsonArray jsonArr = new Gson().toJsonTree(scheduleList).getAsJsonArray();
            jsonObj.add("schedule", jsonArr);
        }

        return jsonObj;

        /*JSONObject obj = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (Schedule schedule : scheduleList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", String.valueOf(schedule.getId()));
            jsonObject.put("alarm", schedule.isAlarm());
            jsonObject.put("start_time", schedule.getStartTime());
            jsonObject.put("end_time", schedule.getEndTime());
            jsonObject.put("category", schedule.getCategory());
            jsonObject.put("event_name", schedule.getEventName());
            jsonObject.put("exclusion", schedule.isExclusion());
            jsonObject.put("expected_spending", schedule.getExpectedSpending());
            jsonObject.put("type", schedule.getType());
            jsonObject.put("importance", schedule.getImportance());
            jsonObject.put("repeat_deadline", schedule.getRepeatDeadline());
            jsonObject.put("repeat_endDate", schedule.getRepeatEndDate());
            jsonObject.put("repeating_cycle",schedule.getRepeatingCycle());
            jsonObject.put("date",schedule.getDate());

            jsonArray.put(jsonObject);
        }
        obj.put("schedule", jsonArray);

        return obj;*/

        /*if (scheduleList.isEmpty()) {
            return new JSONArray("일정이 찾을 수 없음.");
        }

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
        return jsonArray;*/
    }

    // uuid로 일정 하나만 조회
    public ScheduleResponseDTO findOneSchedule(UUID uuid) {
        Schedule findSchedule = getSingleSchedule(uuid);

        ScheduleResponseDTO scheduleResponseDTO = ScheduleResponseDTO.builder()
                .id(findSchedule.getId())
                .eventName(findSchedule.getEventName())
                .alarm(findSchedule.isAlarm())
                .date(findSchedule.getDate())
                .startTime(findSchedule.getStartTime())
                .endTime(findSchedule.getEndTime())
                .repeatingCycle(findSchedule.getRepeatingCycle())
                .repeatDeadline(findSchedule.getRepeatDeadline())
                .repeatEndDate(findSchedule.getRepeatEndDate())
                .category(findSchedule.getCategory())
                .type(findSchedule.getType())
                .expectedSpending(findSchedule.getExpectedSpending())
                .importance(findSchedule.getImportance())
                .exclusion(findSchedule.isExclusion())
                .build();

        return scheduleResponseDTO;
    }
    // 일정 편집
    public boolean modifySchedule(ScheduleRequestDTO scheduleRequestDTO) {
        Schedule findSchedule = getSingleSchedule(scheduleRequestDTO.getId());

        if (findSchedule == null) {
            return false;
        } else {
            try {
                findSchedule.setEventName(scheduleRequestDTO.getEventName());
                findSchedule.setAlarm(scheduleRequestDTO.isAlarm());
                findSchedule.setDate(scheduleRequestDTO.getDate());
                findSchedule.setStartTime(scheduleRequestDTO.getStartTime());
                findSchedule.setEndTime(scheduleRequestDTO.getEndTime());
                findSchedule.setRepeatingCycle(scheduleRequestDTO.getRepeatingCycle());
                findSchedule.setRepeatDeadline(scheduleRequestDTO.getRepeatDeadLine());
                findSchedule.setRepeatEndDate(scheduleRequestDTO.getRepeatEndDate());
                findSchedule.setCategory(scheduleRequestDTO.getCategory());
                findSchedule.setType(scheduleRequestDTO.getType());
                findSchedule.setExpectedSpending(scheduleRequestDTO.getExpectedSpending());
                findSchedule.setImportance(scheduleRequestDTO.getImportance());
                findSchedule.setExclusion(scheduleRequestDTO.isExclusion());
                entityManager.merge(findSchedule);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }



    private Schedule getSingleSchedule(UUID uuid) {
        Schedule findSchedule = entityManager.createQuery("select s from Schedule s where s.id =: uuid", Schedule.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
        return findSchedule;
    }
}
