package project.fin_the_pen.model.schedule.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.TypeManage;
import project.fin_the_pen.model.schedule.entity.type.day.DayType;
import project.fin_the_pen.model.schedule.entity.type.week.WeekType;
import project.fin_the_pen.model.schedule.service.register.*;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Supplier;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScheduleRepository {
    private final CRUDScheduleRepository crudScheduleRepository;
    private final CRUDRegularScheduleRepository regularScheduleRepository;
    //    private final ManageRepository manageRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final RegisterNoneSchedule registerNoneSchedule;
    private final RegisterDaySchedule registerDaySchedule;
    private final RegisterWeekSchedule registerWeekSchedule;
    private final RegisterMonthSchedule registerMonthSchedule;
    private final RegisterYearSchedule registerYearSchedule;

    /**
     * 반복이 아닐 때 (단일일정)
     *
     * @param dto
     * @return
     */
    public Boolean registerNoneSchedule(ScheduleRequestDTO dto) {
        return registerNoneSchedule.registerSchedule(dto);
    }

    /**
     * "일" 단위 반복
     *
     * @return
     */
    public Boolean registerDaySchedule(ScheduleRequestDTO dto) {
        return registerDaySchedule.registerSchedule(dto);
    }

    /**
     * "주" 단위 반복
     *
     * @return
     */
    public boolean registerWeekSchedule(ScheduleRequestDTO dto) {
        return registerWeekSchedule.registerSchedule(dto);
    }

    /**
     * "월" 단위 반복
     */
    public Boolean registerMonthSchedule(ScheduleRequestDTO dto) {
        return registerMonthSchedule.registerSchedule(dto);
    }

    public Boolean registerYearSchedule(ScheduleRequestDTO dto) {
        return registerYearSchedule.registerSchedule(dto);
    }

    /**
     * 현재부터 이 이후의 일정들
     * 1. week의 경우
     * => 1.21일이 저장하려는 요일의 조건에 해당되지 않은 경우 1.21은 삭제됨
     *
     * @param dto
     * @return
     */
    public Boolean modifyNowFromAfter(ModifyScheduleDTO dto, String repeatType) {
        Optional<Schedule> findModifySchedule = crudScheduleRepository.findByIdAndUserId(dto.getUserId(), Long.parseLong(dto.getScheduleId()));

        if (findModifySchedule.isPresent()) {
            String targetDate = findModifySchedule.get().getStartDate();
            List<Schedule> entities = crudScheduleRepository.findByAllDayNowAfter(targetDate);

            log.info("수정할 list 사이즈:{}", entities.size());

            int size = entities.size() - 1;

            if (repeatType.equals("day")) {
                modifyDay(dto, targetDate, size, entities);
            } else if (repeatType.equals("week")) {
                modifyWeek(dto, entities);
                // TODO!!!!!!
            } else if (repeatType.equals("month")) {

                // return은 다 만들고 지워야 함.
                return true;
            }
        }
        return true;
    }

    private void modifyWeek(ModifyScheduleDTO dto, List<Schedule> entities) {
        StringTokenizer tokenizer =
                new StringTokenizer(dto.getRepeat().getWeekTypeVO().getRepeatDayOfWeek(), ",");

        int intervalWeeks = Integer.parseInt(dto.getRepeat().getWeekTypeVO().getValue());

        List<String> days = new ArrayList<>();

        // 선택된 요일을 토큰화해서 list에 저장
        while (tokenizer.hasMoreTokens()) {
            String temp = tokenizer.nextToken().trim();
            days.add(temp);
        }

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // currentDate: 움직일 임시 객체
        LocalDate criteriaDate = LocalDate.parse(dto.getStartDate(), formatter1);
        int endRepeat = 50;

        if (dto.getPeriod().isRepeatAgain()) {
            crudScheduleRepository.deleteAll(entities);

            /*
             week logic 1 => 반복횟수가 없이 default(50)으로 저장
             */
            for (int i = 0; i < endRepeat; i++) {
                String targetDay = criteriaDate.getDayOfWeek().toString();

                WeekType bindingWeekType = new WeekType();
                bindingWeekType.setDayOfWeek(criteriaDate.getDayOfWeek().toString());
                bindingWeekType.setValue(dto.getRepeat().getWeekTypeVO().getValue());

                TypeManage typeManage =
                        TypeManage.builder()
                                .weekType(bindingWeekType)
                                .build();

                // 현재 날짜의 요일과 targetDay가 일치하면 스케줄 생성
                if (days.contains(targetDay)) {
                    log.info("이동하는 요일: {}", targetDay);
                    log.info("일자: {}", criteriaDate);

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeat(typeManage)
                            .isExclude(dto.isExclude())
                            .importance(dto.getImportance())
                            .amount(dto.getAmount())
                            .isFixAmount(dto.isFixAmount())
                            .period(createPeriodType(() -> {
                                return PeriodType.builder()
                                        .isRepeatAgain(true)
                                        .repeatNumberOfTime("0")
                                        .repeatEndLine(null).build();
                            }))
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();

                    crudScheduleRepository.save(schedule);
                    log.info("저장된 요일: {}", schedule.getRepeat().getWeekType().getDayOfWeek());

                    // java에서 한주의 끝은 SUN, 한주의 시작은 MON
                    if (criteriaDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        criteriaDate = criteriaDate.plusWeeks(intervalWeeks);
                        criteriaDate = criteriaDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                    } else criteriaDate = criteriaDate.plusDays(1);
                } else {
                    i--;
                    if (criteriaDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        criteriaDate = criteriaDate.plusWeeks(intervalWeeks);
                        criteriaDate = criteriaDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                    } else criteriaDate = criteriaDate.plusDays(1);
                }
            }

            // week logic 2 => 반복횟수가 주어진 경우
        } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
            LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

            crudScheduleRepository.deleteAll(entities);

            int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());
            int repeatValue = repeatNumberOfTime * intervalWeeks;

            for (int i = 0; i < repeatValue; i++) {
                String targetDay = criteriaDate.getDayOfWeek().toString();

                if (days.contains(targetDay)) {
                    log.info("이동하는 요일: {}", targetDay);
                    log.info("일자: {}", criteriaDate);

                    WeekType bindingWeekType = new WeekType();
                    bindingWeekType.setDayOfWeek(criteriaDate.getDayOfWeek().toString());
                    bindingWeekType.setValue(dto.getRepeat().getWeekTypeVO().getValue());

                    TypeManage typeManage =
                            TypeManage.builder()
                                    .weekType(bindingWeekType)
                                    .build();

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeat(typeManage)
                            .isExclude(dto.isExclude())
                            .importance(dto.getImportance())
                            .amount(dto.getAmount())
                            .isFixAmount(dto.isFixAmount())
                            .period(createPeriodType(() -> {
                                return PeriodType.builder()
                                        .isRepeatAgain(false)
                                        .repeatEndLine(endLine.toString()).build();
                            }))
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();

                    crudScheduleRepository.save(schedule);

                    // java에서 한주의 끝은 SUN, 한주의 시작은 MON
                    if (criteriaDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        criteriaDate = criteriaDate.plusWeeks(intervalWeeks);
                        criteriaDate = criteriaDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                    } else criteriaDate = criteriaDate.plusDays(1);
                    // TODO (수정) 일요일부터 시작하는 경우를 수정해야 함...
                } else {
                    i--;
                    if (criteriaDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        criteriaDate = criteriaDate.plusWeeks(intervalWeeks);
                        criteriaDate = criteriaDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                    } else criteriaDate = criteriaDate.plusDays(1);
                }
            }

            // week logic 3 => 특정 기간까지 반복하는 경우
        } else if (dto.getPeriod().getRepeatEndLine() != null) {
            LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());
            crudScheduleRepository.deleteAll(entities);

            while (!criteriaDate.isAfter(endLine)) {
                String targetDay = criteriaDate.getDayOfWeek().toString();

                // 현재 날짜의 요일과 targetDay가 일치하면 스케줄 생성
                if (days.contains(targetDay)) {
                    log.info("이동하는 요일: {}", targetDay);
                    log.info("일자: {}", criteriaDate);

                    WeekType bindingWeekType = new WeekType();
                    bindingWeekType.setDayOfWeek(criteriaDate.getDayOfWeek().toString());
                    bindingWeekType.setValue(dto.getRepeat().getWeekTypeVO().getValue());

                    TypeManage typeManage =
                            TypeManage.builder()
                                    .weekType(bindingWeekType)
                                    .build();

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeat(typeManage)
                            .isExclude(dto.isExclude())
                            .importance(dto.getImportance())
                            .amount(dto.getAmount())
                            .isFixAmount(dto.isFixAmount())
                            .period(createPeriodType(() -> {
                                return PeriodType.builder()
                                        .isRepeatAgain(false)
                                        .repeatEndLine(endLine.toString()).build();
                            }))
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();

                    crudScheduleRepository.save(schedule);

                    // java에서 한주의 끝은 SUN, 한주의 시작은 MON
                    if (criteriaDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        criteriaDate = criteriaDate.plusWeeks(intervalWeeks);
                        criteriaDate = criteriaDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                    } else criteriaDate = criteriaDate.plusDays(1);
                    // TODO (수정) 일요일부터 시작하는 경우를 수정해야 함...
                } else {
                    if (criteriaDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        criteriaDate = criteriaDate.plusWeeks(intervalWeeks);
                        criteriaDate = criteriaDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                    } else criteriaDate = criteriaDate.plusDays(1);
                }
            }
        }
    }


    private void modifyDay(ModifyScheduleDTO dto, String targetDate, int size, List<Schedule> entities) {
        DayType bindingDayType = new DayType();
        bindingDayType.setValue(dto.getRepeat().getDayTypeVO().getValue());

        TypeManage typeManage = TypeManage.builder()
                .dayType(bindingDayType)
                .build();

        int intervalDays = Integer.parseInt(dto.getRepeat().getDayTypeVO().getValue());
        LocalDate criteriaDate = formatDate(targetDate);
        int endRepeat = 50;

        // day 1 logic
        if (dto.getPeriod().isRepeatAgain()) {
            for (int i = 0; i < endRepeat; i++) {
                if (i <= size) {
                    // entities 리스트에 엔터티가 이미 존재할 때
                    Schedule existingSchedule = entities.get(i);

                    existingSchedule.updateFrom(
                            dto, criteriaDate.toString(), criteriaDate.toString(),
                            typeManage,
                            createPeriodType(() -> PeriodType.builder()
                                    .isRepeatAgain(true)
                                    .repeatNumberOfTime("0")
                                    .repeatEndLine(null)
                                    .build()),
                            judgmentPriceType(() -> dto.getPriceType().equals(PriceType.Plus) ? PriceType.Plus : PriceType.Minus));
                    crudScheduleRepository.save(existingSchedule);
                    log.info("수정 성공");
                } else {
                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeat(typeManage)
                            .isExclude(dto.isExclude())
                            .importance(dto.getImportance())
                            .amount(dto.getAmount())
                            .isFixAmount(dto.isFixAmount())
                            .period(createPeriodType(() -> PeriodType.builder()
                                    .isRepeatAgain(true)
                                    .repeatNumberOfTime("0")
                                    .repeatEndLine(null)
                                    .build()))
                            .priceType(judgmentPriceType(() -> dto.getPriceType().equals(PriceType.Plus) ? PriceType.Plus : PriceType.Minus))
                            .build();

                    // entities 리스트를 모두 순회한 경우
                    log.info("나머지 저장");
                    log.info(schedule.getStartDate());
                    crudScheduleRepository.save(schedule);
                }
                criteriaDate = criteriaDate.plusDays(intervalDays);
            }
            // test 필요
            if (endRepeat < entities.size()) {
                for (int j = endRepeat; j < entities.size(); j++) {
                    crudScheduleRepository.delete(entities.get(j));
                }
            }

            // day 2 logic
        } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
            int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());
            for (int i = 0; i < repeatNumberOfTime; i++) {
                if (i <= size) {
                    // entities 리스트에 엔터티가 이미 존재할 때
                    Schedule existingSchedule = entities.get(i);

                    existingSchedule.updateFrom(
                            dto, criteriaDate.toString(), criteriaDate.toString(),
                            typeManage,
                            createPeriodType(() -> PeriodType.builder()
                                    .isRepeatAgain(true)
                                    .repeatNumberOfTime("0")
                                    .repeatEndLine(null)
                                    .build()),
                            judgmentPriceType(() -> dto.getPriceType().equals(PriceType.Plus) ? PriceType.Plus : PriceType.Minus));
                    crudScheduleRepository.save(existingSchedule);
                    log.info("수정 성공");
                } else {
                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())  // 수정된 부분
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeat(typeManage)
                            .isExclude(dto.isExclude())
                            .importance(dto.getImportance())
                            .amount(dto.getAmount())
                            .isFixAmount(dto.isFixAmount())
                            .period(createPeriodType(() -> {
                                return PeriodType.builder()
                                        .isRepeatAgain(false)
                                        .repeatEndLine(null)
                                        .build();
                            }))
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();
                    // entities 리스트를 모두 순회한 경우
                    log.info("나머지 저장");
                    log.info(schedule.getStartDate());
                    crudScheduleRepository.save(schedule);
                }
                criteriaDate = criteriaDate.plusDays(intervalDays);
            }

            // day 3 logic
        } else if (dto.getPeriod().getRepeatEndLine() != null) {
            LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

            while (!criteriaDate.isAfter(endLine)) {
                Schedule schedule = Schedule.builder()
                        .userId(dto.getUserId())
                        .eventName(dto.getEventName())
                        .category(dto.getCategory())
                        .startDate(criteriaDate.toString())  // 수정된 부분
                        .endDate(criteriaDate.toString())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .isAllDay(dto.isAllDay())
                        .repeat(typeManage)
                        .isExclude(dto.isExclude())
                        .importance(dto.getImportance())
                        .amount(dto.getAmount())
                        .isFixAmount(dto.isFixAmount())
                        .period(createPeriodType(() -> {
                            return PeriodType.builder()
                                    .isRepeatAgain(false)
                                    .repeatEndLine(endLine.toString())
                                    .build();
                        }))
                        .priceType(judgmentPriceType(() -> {
                            if (dto.getPriceType().equals(PriceType.Plus)) {
                                return PriceType.Plus;
                            } else return PriceType.Minus;
                        }))
                        .build();

                crudScheduleRepository.save(schedule);

                criteriaDate = criteriaDate.plusDays(intervalDays);
            }
        }
    }

    @NotNull
    private LocalDate formatDate(String convertDate) {
        return LocalDate.parse(convertDate, formatter);
    }


    /**
     *
     */
    public List<Schedule> findByContainsName(String name) {
        return crudScheduleRepository.findByContainsName(name);
    }

    private PeriodType createPeriodType(Supplier<PeriodType> supplier) {
        return supplier.get();
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


    public List<Schedule> findScheduleByCategory(CategoryRequestDTO categoryRequestDTO, String
            currentSession) {
        return crudScheduleRepository.findScheduleByCategory(currentSession, categoryRequestDTO.getCategoryName());
    }

    private PriceType judgmentPriceType(Supplier<PriceType> supplier) {
        return supplier.get();
    }

    // 중복되는 일정이 등록되는지 검사하는 method
    private boolean isDuplicatedSaveSchedule(ScheduleRequestDTO dto) {
        List<Schedule> allSchedule = findAllSchedule(dto.getUserId());

        return allSchedule.stream().noneMatch(it ->
                it.getUserId().equals(dto.getUserId()) &&
                        it.getEventName().equals(dto.getEventName()) &&
                        it.getCategory().equals(dto.getCategory()) &&
                        it.getStartDate().equals(dto.getStartDate()) &&
                        it.getEndDate().equals(dto.getEndDate()) &&
                        it.getStartTime().equals(dto.getStartTime()) &&
                        it.getEndTime().equals(dto.getEndTime()));
    }


}
