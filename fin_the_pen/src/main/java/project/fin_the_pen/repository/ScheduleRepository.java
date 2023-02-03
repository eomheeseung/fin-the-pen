package project.fin_the_pen.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.data.schedule.Schedule;
import project.fin_the_pen.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.data.schedule.ScheduleResponseDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ScheduleRepository {
    @PersistenceContext
    EntityManager entityManager;

    private final CRUDScheduleRepository repository;

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
    public JSONArray findAllSchedule(String id) {
        List<Schedule> scheduleList = entityManager.createQuery("select s from Schedule s where s.userId = :id", Schedule.class)
                .setParameter("id", id)
                .getResultList();


        List<ScheduleResponseDTO> schduleResponseList = new ArrayList<>();
        log.info(String.valueOf(scheduleList.size()));
        JSONArray jsonArray = new JSONArray(schduleResponseList);
        if (scheduleList.isEmpty()) {
            return new JSONArray((JSONArray) null);
        }

        log.info(String.valueOf(scheduleList.size()));

        return getJsonArrayBySchedule(scheduleList, jsonArray);
    }

    /**
     * 월별로 일정 조회
     * @param month
     * @return
     */
    public JSONArray findMonthSchedule(String month) {
        List<Schedule> byMonthSchedule = repository.findByMonthSchedule(month);
        JSONArray jsonArray = new JSONArray();

        return getJsonArrayBySchedule(byMonthSchedule, jsonArray);

    }



    // TODO 나중에 jsonObject로 바꿔야 할 수도
    /**
     * 일정 하나만 조회인데 필요할지 안 필요할지....
     * @param uuid
     * @return
     */
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
    //TODO 나중에 해야 함 1.
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

    //TODO 나중에 해야 함 2.
    public boolean deleteSchedule(UUID uuid) {
        Schedule singleSchedule = getSingleSchedule(uuid);
        try {
            entityManager.remove(singleSchedule);
        } catch (Exception e) {
            return false;
        }
        return true;
    }



    private Schedule getSingleSchedule(UUID uuid) {
        Schedule findSchedule = entityManager.createQuery("select s from Schedule s where s.id =: uuid", Schedule.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
        return findSchedule;
    }

    private JSONArray getJsonArrayBySchedule(List<Schedule> scheduleList, JSONArray jsonArray) {
        for (Schedule schedule : scheduleList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", schedule.getId());
            jsonObject.put("alarm", schedule.isAlarm());
            jsonObject.put("event_name", schedule.getEventName());
            jsonObject.put("date", schedule.getDate());
            jsonObject.put("start_time", schedule.getStartTime());
            jsonObject.put("end_time", schedule.getEndTime());
            jsonObject.put("type", schedule.getType());
            jsonObject.put("repeating_cycle", schedule.getRepeatingCycle());
            jsonObject.put("repeat_deadline", schedule.getRepeatDeadline());
            jsonObject.put("repeat_endDate", schedule.getRepeatEndDate());
            jsonObject.put("category", schedule.getCategory());
            jsonObject.put("exclusion", schedule.isExclusion());
            jsonObject.put("importance", schedule.getImportance());
            jsonObject.put("expected_spending", schedule.getExpectedSpending());

            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
}
