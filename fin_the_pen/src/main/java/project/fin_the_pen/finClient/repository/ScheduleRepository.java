package project.fin_the_pen.finClient.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.finClient.data.schedule.Schedule;
import project.fin_the_pen.finClient.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.finClient.data.schedule.ScheduleResponseDTO;
import project.fin_the_pen.finClient.data.schedule.category.CategoryRequestDTO;

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

    /**
     * TODO 포함된 이름으로 검색 테스트 필요함.
     */
    public JSONArray findByContainsName(String name) {
        List<Schedule> byContainsNameList = repository.findByContainsName(name);
        JSONArray jsonArray = new JSONArray(new ArrayList<ScheduleResponseDTO>());

        if (byContainsNameList.isEmpty()) {
            return new JSONArray((JSONArray) null);
        }

        log.info(String.valueOf(byContainsNameList.size()));

        return getJsonArrayBySchedule(byContainsNameList, jsonArray);
    }

    /**
     * 전체 일정 조회 userId에 따라서
     *
     * @param id
     * @return
     */
    public JSONArray findAllSchedule(String id) {
        List<Schedule> scheduleList = repository.findScheduleByUserId(id);

        log.info(String.valueOf(scheduleList.size()));
        JSONArray jsonArray = new JSONArray(new ArrayList<ScheduleResponseDTO>());

        if (scheduleList.isEmpty()) {
            return new JSONArray((JSONArray) null);
        }

        log.info(String.valueOf(scheduleList.size()));

        return getJsonArrayBySchedule(scheduleList, jsonArray);
    }

    /**
     * 월별로 일정 조회
     *
     * @param date
     * @return
     */
    public JSONArray findMonthSchedule(String date, String userId) {
        List<Schedule> byMonthSchedule = repository.findByMonthSchedule(date, userId);

        JSONArray jsonArray = new JSONArray();

        return getJsonArrayBySchedule(byMonthSchedule, jsonArray);
    }

    /**
     * 일정 하나만 조회인데 필요할지 안 필요할지....
     *
     * @param uuid
     * @return
     */
    public ScheduleResponseDTO findOneSchedule(String uuid) {
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

    public JSONArray findScheduleByCategory(CategoryRequestDTO categoryRequestDTO, String currentSession) {
        /*List<Schedule> resultList =
                entityManager.createQuery("select s from Schedule s where s.userId= :userId and s.category = :categoryName", Schedule.class)
                        .setParameter("userId", currentSession)
                        .setParameter("categoryName", categoryRequestDTO.getCategoryName())
                        .getResultList();*/
        List<Schedule> resultList = repository.findScheduleByCategory(currentSession, categoryRequestDTO.getCategoryName());
        return getJsonArrayBySchedule(resultList, new JSONArray());
    }

    public boolean deleteSchedule(String uuid) {
        Schedule singleSchedule = getSingleSchedule(uuid);

        try {
            repository.delete(singleSchedule);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private Schedule getSingleSchedule(String uuid) {
        return repository.findById(UUID.fromString(uuid)).get();
        /*return entityManager.createQuery("select s from Schedule s where s.id =: uuid", Schedule.class)
                .setParameter("uuid", uuid)
                .getSingleResult();*/
    }

    private JSONArray getJsonArrayBySchedule(List<Schedule> scheduleList, JSONArray jsonArray) {
        scheduleList.stream()
                .forEach(schedule -> {
                    JSONObject jsonObject = new JSONObject()
                            .put("id", schedule.getId())
                            .put("alarm", schedule.isAlarm())
                            .put("event_name", schedule.getEventName())
                            .put("date", schedule.getDate())
                            .put("start_time", schedule.getStartTime())
                            .put("end_time", schedule.getEndTime())
                            .put("type", schedule.getType())
                            .put("repeating_cycle", schedule.getRepeatingCycle())
                            .put("repeat_deadline", schedule.getRepeatDeadline())
                            .put("repeat_endDate", schedule.getRepeatEndDate())
                            .put("category", schedule.getCategory())
                            .put("exclusion", schedule.isExclusion())
                            .put("importance", schedule.getImportance())
                            .put("expected_spending", schedule.getExpectedSpending());

                    jsonArray.put(jsonObject);
                });

        return jsonArray;
    }
}
