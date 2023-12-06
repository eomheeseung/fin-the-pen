package project.fin_the_pen.model.schedule.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.finClient.core.util.ScheduleTypeFunc;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.type.DayType;
import project.fin_the_pen.model.schedule.entity.type.TypeManage;
import project.fin_the_pen.model.schedule.entity.type.WeekType;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScheduleRepository {
    private final CRUDScheduleRepository crudScheduleRepository;
    private final CRUDRegularScheduleRepository regularScheduleRepository;
//    private final ManageRepository manageRepository;

    public Boolean registerNoneSchedule(ScheduleRequestDTO dto) {
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

                TypeManage typeManage = TypeManage.builder()
                        .value("none")
                        .kindType("none")
                        .build();

                Schedule schedule = Schedule.builder()
                        .userId(dto.getUserId())
                        .eventName(dto.getEventName())
                        .category(dto.getCategory())
                        .startDate(dto.getStartDate())  // 수정된 부분
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
            }
        } catch (RuntimeException e) {
            return null;
        }
        return true;
    }


    /**
     * "일" 단위 반복
     *
     * @param dto
     * @param repeatType
     * @return
     */
    public Boolean registerDaySchedule(ScheduleRequestDTO dto, DayType repeatType) {
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

                TypeManage typeManage = TypeManage.builder()
                        .value((repeatType).getValue())
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

            } /*else if (repeatType instanceof MonthType) {
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
                }*/
        } catch (
                RuntimeException e) {
            return null;
        }
        return true;
    }

    /**
     * "주" 단위 반복
     * @param dto
     * @param repeatType
     * @return
     */
    public Boolean registerWeekSchedule(ScheduleRequestDTO dto, WeekType repeatType) {
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
                StringTokenizer tokenizer = new StringTokenizer((repeatType).getMonthValue(), ",");

                // DateTimeFormatter 인스턴스 생성
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                // 문자열을 LocalDate 객체로 변환
                LocalDate startDate = LocalDate.parse(dto.getStartDate(), formatter);
                LocalDate endLine = LocalDate.parse(dto.getRepeatEndLine(), formatter);

                List<String> days = new ArrayList<>();


                // 선택된 요일을 토큰화해서 list에 저장
                while (tokenizer.hasMoreTokens()) {
                    days.add(tokenizer.nextToken().trim());
                }

                // currentDate: 움직일 임시 객체
                LocalDate currentDate = startDate;

                // TODO!!!!!!
                while (!currentDate.isAfter(endLine)) {
                    String targetDay = currentDate.getDayOfWeek().toString();

                    // 현재 날짜의 요일과 targetDay가 일치하면 스케줄 생성
                    if (days.contains(targetDay)) {
                        log.info("이동하는 요일: {}", targetDay);
                        log.info("일자: {}", currentDate);

                        TypeManage typeManage = TypeManage.builder()
                                .value(currentDate.getDayOfWeek().toString())
                                .kindType("week").build();

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

                        // java에서 한주의 끝은 SUN, 한주의 시작은 MON
                        crudScheduleRepository.save(schedule);
                        log.info(schedule.getUserId());
                        if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            log.info("term:{}", (repeatType.getRepeatValue()));
                            currentDate = currentDate.plusWeeks(repeatType.getRepeatValue());
                            currentDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                        } else currentDate = currentDate.plusDays(1);
                    } else {
                        if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            log.info("term:{}", (repeatType.getRepeatValue()));
                            currentDate = currentDate.plusWeeks(repeatType.getRepeatValue());
                            currentDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                        } else currentDate = currentDate.plusDays(1);
                    }
                }
            }
        } catch (Exception e) {
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
    private void isType(ScheduleRequestDTO dto, ScheduleTypeFunc callBack) {
        callBack.callbackMethod(dto);
    }

    public List<Schedule> findScheduleByCategory(CategoryRequestDTO categoryRequestDTO, String
            currentSession) {
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
