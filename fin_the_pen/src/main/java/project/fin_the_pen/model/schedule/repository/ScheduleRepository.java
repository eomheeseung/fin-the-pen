package project.fin_the_pen.model.schedule.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.finClient.core.util.ScheduleTypeFunc;
import project.fin_the_pen.model.schedule.dto.ScheduleDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.type.*;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScheduleRepository {
    private final CRUDScheduleRepository crudScheduleRepository;
    private final CRUDRegularScheduleRepository regularScheduleRepository;
//    private final ManageRepository manageRepository;

    /*
    TODO 등록할 때 accessToken으로만 일정을 찾으면, 로그아웃할 때 accessToken이 파기될 것
     -> 그럼 다시 재 로그인할 때 일정을 어떻게 찾지?
     => 토큰은 인증 용도로만 사용하고 user_id로 조회하자.
     */
    public Boolean registerSchedule(ScheduleDTO dto, RepeatType repeatType) {
        try {
            List<Schedule> allSchedule = findAllSchedule(dto.getUserId());

            boolean isDifferent = allSchedule.stream().noneMatch(it ->
                    it.getUserId().equals(dto.getUserId()) &&
                            it.getEventName().equals(dto.getEventName()) &&
                            it.getCategory().equals(dto.getCategory()) &&
                            it.getStartDate().equals(dto.getStartDate()) &&
                            it.getEndDate().equals(dto.getEndDate()) &&
                            it.getStartTime().equals(dto.getStartTime()) &&
                            it.getEndTime().equals(dto.getEndTime()));

            if (!isDifferent) {
                throw new DuplicatedScheduleException("중복된 일정 등록입니다.");
            } else {
                // 어느정도 만든 듯
                if (repeatType instanceof DayType) {

                    TypeManage typeManage = TypeManage.builder()
                            .value(((DayType) repeatType).getValue())
                            .kindType("day").build();


                    log.info(dto.getUserId());
                    log.info(dto.getAmount());

                    int intervalDays = Integer.parseInt(typeManage.getValue());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate startDate = LocalDate.parse(dto.getStartDate(), formatter);
                    LocalDate endDate = LocalDate.parse(dto.getRepeatEndLine(), formatter);
                    LocalDate currentDate = startDate;

                    while (!currentDate.isAfter(endDate)) {
                        Schedule schedule = Schedule.builder()
                                .userId(dto.getUserId())
                                .eventName(dto.getEventName())
                                .category(dto.getCategory())
                                .startDate(currentDate.toString())  // 수정된 부분
                                .endDate(dto.getEndDate())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeat(typeManage)
                                .isExclude(dto.isExclude())
                                .importance(dto.getImportance())
                                .amount(dto.getAmount())
                                .isFixAmount(dto.isFixAmount())
                                .repeatEndLine(dto.getRepeatEndLine())
                                .build();

                        schedule.setPriceType(() -> {
                            if (dto.getPriceType().equals(PriceType.Plus)) {
                                return PriceType.Plus;
                            } else return PriceType.Minus;
                        });

                        crudScheduleRepository.save(schedule);

                        currentDate = currentDate.plusDays(intervalDays);
                    }
                /*
                TODO
                 2. 주간 반복 해야 함.
                 */
                } else if (repeatType instanceof WeekType) {
                    TypeManage typeManage = TypeManage.builder()
                            .value(((WeekType) repeatType).getValue())
                            .kindType("month").build();

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(dto.getStartDate())
                            .endDate(dto.getEndDate())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeat(typeManage)
                            .isExclude(dto.isExclude())
                            .importance(dto.getImportance())
                            .amount(dto.getAmount())
                            .isFixAmount(dto.isFixAmount())
                            .build();

                    schedule.setPriceType(() -> {
                        if (dto.getPriceType().equals(PriceType.Plus)) {
                            return PriceType.Plus;
                        } else return PriceType.Minus;
                    });


                    log.info(dto.getUserId());
                    log.info(dto.getAmount());
                    crudScheduleRepository.save(schedule);
                    log.info(schedule.getUserId());
                } else if (repeatType instanceof MonthType) {
                    TypeManage typeManage = TypeManage.builder()
                            .value(((MonthType) repeatType).getValue())
                            .kindType("month").build();

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(dto.getStartDate())
                            .endDate(dto.getEndDate())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeat(typeManage)
                            .isExclude(dto.isExclude())
                            .importance(dto.getImportance())
                            .amount(dto.getAmount())
                            .isFixAmount(dto.isFixAmount())
                            .build();

                    schedule.setPriceType(() -> {
                        if (dto.getPriceType().equals(PriceType.Plus)) {
                            return PriceType.Plus;
                        } else return PriceType.Minus;
                    });


                    log.info(dto.getUserId());
                    log.info(dto.getAmount());
                    crudScheduleRepository.save(schedule);
                    log.info(schedule.getUserId());
                } else if (repeatType instanceof YearType) {
                    TypeManage typeManage = TypeManage.builder()
                            .value(((YearType) repeatType).getValue())
                            .kindType("month").build();

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(dto.getStartDate())
                            .endDate(dto.getEndDate())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeat(typeManage)
                            .isExclude(dto.isExclude())
                            .importance(dto.getImportance())
                            .amount(dto.getAmount())
                            .isFixAmount(dto.isFixAmount())
                            .build();

                    schedule.setPriceType(() -> {
                        if (dto.getPriceType().equals(PriceType.Plus)) {
                            return PriceType.Plus;
                        } else return PriceType.Minus;
                    });


                    log.info(dto.getUserId());
                    log.info(dto.getAmount());
                    crudScheduleRepository.save(schedule);
                    log.info(schedule.getUserId());
                }
            }
        } catch (RuntimeException e) {
            return null;
        }
        return true;
    }


    /**
     *
     */
    public List<Schedule> findByContainsName(String name) {
        return crudScheduleRepository.findByContainsName(name);
    }

    /**
     * 전체 일정 조회 userId에 따라서
     *
     * @return
     */
    public List<Schedule> findAllSchedule(String userId) {
        return crudScheduleRepository.findByUserId(userId);
    }

    /**
     * 월별로 일정 조회
     *
     * @param date
     * @return
     */
    public List<Schedule> findMonthSchedule(String date, String userId) {
        return crudScheduleRepository.findByMonthSchedule(date, userId);

    }

    public List<Schedule> findMonthSectionSchedule(String startDate, String endDate, String userId) {
        return crudScheduleRepository.findScheduleByDateContains(startDate, endDate, userId);
    }

    /**
     * 일정 하나만 조회인데 필요할지 안 필요할지....
     *
     * @param uuid
     * @return
     */
    /*public ScheduleResponseDTO findOneSchedule(String uuid) {
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
    }*/

    /**
     * 일정 수정
     * TODO
     *  만약, "매주 반복 + 2024.01.01"로 생성했는데
     *  수정에서 "매월 반복 + 2024.02.01"로 수정되면, 매주 반복을 없애고 매월 반복으로 바꿔야 함...
     */
    /*public Boolean modifySchedule(ScheduleDTO dto, PriceType priceType) {

        try {
            Optional<Schedule> optionalSchedule = Optional.of(getSingleSchedule(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("error")));

            Certain result = getCertain(dto);

            Schedule findSchedule = optionalSchedule.get();
            findSchedule.setEventName(dto.getEventName());
            findSchedule.setCategory(dto.getCategory());
            findSchedule.setStartDate(dto.getStartDate());
            findSchedule.setEndDate(dto.getEndDate());
            findSchedule.setStartTime(dto.getStartTime());
            findSchedule.setEndTime(dto.getEndTime());
            findSchedule.setAllDay(dto.isAllDay());
            findSchedule.setRepeat(result.repeatType);
            findSchedule.setPeriod(result.periodType);
            findSchedule.setPriceType(priceType);
            findSchedule.setExclude(dto.isExclude());
            findSchedule.setImportance(dto.getImportance());
            findSchedule.setAmount(dto.getAmount());
            findSchedule.setFixAmount(dto.isFixAmount());

            crudScheduleRepository.save(findSchedule);
            return true;

        } catch (RuntimeException e) {
            return false;
        }
    }*/


    /**
     * callback method
     * enum type에 따라서 다르게 overriding
     *
     * @param dto
     * @param callBack
     */
    private void isType(ScheduleDTO dto, ScheduleTypeFunc callBack) {
        callBack.callbackMethod(dto);
    }

    public List<Schedule> findScheduleByCategory(CategoryRequestDTO categoryRequestDTO, String currentSession) {
        return crudScheduleRepository.findScheduleByCategory(currentSession, categoryRequestDTO.getCategoryName());
    }

    /*public boolean deleteSchedule(String uuid) {
        Schedule singleSchedule = getSingleSchedule(uuid);
        try {
            scheduleRepository.delete(singleSchedule);

        } catch (Exception e) {
            return false;
        }
        return true;
    }*/

//    private Optional<Schedule> getSingleSchedule(String uuid) {
//        return crudScheduleRepository.findById(uuid);
//    }

    /*private static void manageSave(Schedule schedule) {
        ScheduleManage manage = new ScheduleManage();
        manage.setDeleteFlag(false);
        manage.setSchedule(schedule);
        schedule.setScheduleManage(manage);
    }*/
}
