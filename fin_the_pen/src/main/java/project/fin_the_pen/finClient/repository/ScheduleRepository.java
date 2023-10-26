package project.fin_the_pen.finClient.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.finClient.data.schedule.RegularSchedule;
import project.fin_the_pen.finClient.data.schedule.Schedule;
import project.fin_the_pen.finClient.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.finClient.data.schedule.ScheduleResponseDTO;
import project.fin_the_pen.finClient.data.schedule.category.CategoryRequestDTO;
import project.fin_the_pen.finClient.data.schedule.type.PriceType;
import project.fin_the_pen.finClient.data.schedule.type.RegularType;
import project.fin_the_pen.finClient.util.ScheduleTypeFunc;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScheduleRepository {
    private final CRUDScheduleRepository repository;
    private final CRUDRegularScheduleRepository regularScheduleRepository;

    public Boolean registerSchedule(ScheduleRequestDTO dto, PriceType priceType, RegularType regularType) {
        try {
            Schedule schedule = Schedule.builder().id(dto.getId()).userId(dto.getUserId())
                    .eventName(dto.getEventName()).alarm(dto.isAlarm()).date(dto.getDate())
                    .startTime(dto.getStartTime()).endTime(dto.getEndTime()).category(dto.getCategory())
                    .priceType(priceType).regularType(regularType).expectedSpending(dto.getExpectedSpending())
                    .repeatingCycle(dto.getRepeatingCycle())
                    .repeatDeadline(dto.getRepeatDeadLine()).repeatEndDate(dto.getRepeatEndDate())
                    .exclusion(dto.isExclusion()).importance(dto.getImportance())
                    .build();

            repository.save(schedule);
            log.info(schedule.getUserId());

        } catch (Exception e) {
            return null;
        }
        return true;
    }

    /**
     * TODO 1
     */
    public org.json.simple.JSONArray findByContainsName(String name) {
        List<Schedule> byContainsNameList = repository.findByContainsName(name);
        org.json.simple.JSONArray jsonArray = new org.json.simple.JSONArray();
        jsonArray.add(new ArrayList<ScheduleResponseDTO>());

        if (byContainsNameList.isEmpty()) {
            jsonArray.add(null);
            return jsonArray;
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
    public org.json.simple.JSONArray findAllSchedule(String id) {
        List<Schedule> scheduleList = repository.findScheduleByUserId(id);

        org.json.simple.JSONArray jsonArray = new org.json.simple.JSONArray();
        jsonArray.add(new ArrayList<ScheduleResponseDTO>());

        if (scheduleList.isEmpty()) {
            jsonArray.add(null);
            return jsonArray;
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
    public org.json.simple.JSONArray findMonthSchedule(String date, String userId) {
        List<Schedule> byMonthSchedule = repository.findByMonthSchedule(date, userId);

        org.json.simple.JSONArray jsonArray = new org.json.simple.JSONArray();

        return getJsonArrayBySchedule(byMonthSchedule, jsonArray);
    }

    public org.json.simple.JSONArray findMonthSectionSchedule(String startDate, String endDate, String userId) {
        List<Schedule> byMonthSchedule = repository.findScheduleByDateContains(startDate, endDate, userId);

        org.json.simple.JSONArray jsonArray = new org.json.simple.JSONArray();

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
                .type(findSchedule.getPriceType())
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
            // 정기 일정인 경우
            if (!findSchedule.getRegularType().equals(RegularType.None)) {
                RegularType regularType = null;
                PriceType priceType = null;

                if (!scheduleRequestDTO.getRepeatingCycle().equals("없음")) {
                    if (scheduleRequestDTO.getPriceType().equals("+")) {
                        priceType = PriceType.Plus;
                        regularType = RegularType.Deposit;
                    } else {
                        priceType = PriceType.Minus;
                        regularType = RegularType.Withdrawal;
                    }
                }


                RegularSchedule regularSchedule = RegularSchedule.builder().userId(findSchedule.getUserId())
                        .scheduleId(findSchedule.getId())
                        .eventName(scheduleRequestDTO.getEventName())
                        .alarm(scheduleRequestDTO.isAlarm())
                        .date(scheduleRequestDTO.getDate())
                        .startTime(scheduleRequestDTO.getStartTime())
                        .endTime(scheduleRequestDTO.getEndTime())
                        .repeatingCycle(scheduleRequestDTO.getRepeatingCycle())
                        .repeatDeadline(scheduleRequestDTO.getRepeatDeadLine())
                        .repeatEndDate(scheduleRequestDTO.getRepeatEndDate())
                        .category(scheduleRequestDTO.getCategory())
                        .expectedSpending(scheduleRequestDTO.getExpectedSpending())
                        .importance(scheduleRequestDTO.getImportance())
                        .exclusion(scheduleRequestDTO.isExclusion())
                        .regularType(regularType)
                        .priceType(priceType).build();

                regularScheduleRepository.save(regularSchedule);
            } else {
                // 정기 일정이 아닌경우
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

                    isType(scheduleRequestDTO, dto -> {
                        if (dto.getPriceType().equals("+")) {
                            findSchedule.setPriceType(PriceType.Plus);
                        } else {
                            findSchedule.setPriceType(PriceType.Minus);
                        }
                    });


                    findSchedule.setExpectedSpending(scheduleRequestDTO.getExpectedSpending());
                    findSchedule.setImportance(scheduleRequestDTO.getImportance());
                    findSchedule.setExclusion(scheduleRequestDTO.isExclusion());
//                entityManager.merge(findSchedule);
                    repository.save(findSchedule);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return true;
    }

    /**
     * callback method
     * enum type에 따라서 다르게 overriding
     *
     * @param dto
     * @param callBack
     */
    private void isType(ScheduleRequestDTO dto, ScheduleTypeFunc callBack) {
        callBack.callbackMethod(dto);
    }

    public org.json.simple.JSONArray findScheduleByCategory(CategoryRequestDTO categoryRequestDTO, String currentSession) {
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
        return repository.findById(uuid).get();
    }

    private org.json.simple.JSONArray getJsonArrayBySchedule(List<Schedule> scheduleList, org.json.simple.JSONArray jsonArray) {
        scheduleList.stream()
                .forEach(schedule -> {
                    JSONObject jsonObject = new JSONObject()
                            .put("id", schedule.getId())
                            .put("alarm", schedule.isAlarm())
                            .put("event_name", schedule.getEventName())
                            .put("date", schedule.getDate())
                            .put("start_time", schedule.getStartTime())
                            .put("end_time", schedule.getEndTime())
                            .put("repeating_cycle", schedule.getRepeatingCycle())
                            .put("repeat_deadline", schedule.getRepeatDeadline())
                            .put("repeat_endDate", schedule.getRepeatEndDate())
                            .put("category", schedule.getCategory())
                            .put("exclusion", schedule.isExclusion())
                            .put("importance", schedule.getImportance())
                            .put("expected_spending", schedule.getExpectedSpending());

                    // enum으로 저장하거나 사용하면 find할때돼 enum타입을 사용해야 함.
                    // "Minus".equals()와 같이 사용하면 찾을 수 없음
                    if (PriceType.Minus.equals(schedule.getPriceType())) {
                        jsonObject.put("type", "-");
                    } else if (PriceType.Plus.equals(schedule.getPriceType())) {
                        jsonObject.put("type", "+");
                    }

                    log.info(jsonObject.get("type").toString());

                    jsonArray.add(jsonObject);
                });

        return jsonArray;
    }
}
