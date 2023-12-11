package project.fin_the_pen.model.schedule.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.type.*;
import project.fin_the_pen.model.schedule.entity.type.day.DayType;
import project.fin_the_pen.model.schedule.entity.type.month.MonthType;
import project.fin_the_pen.model.schedule.entity.type.week.WeekType;
import project.fin_the_pen.model.schedule.entity.type.year.YearScheduleFunc;
import project.fin_the_pen.model.schedule.entity.type.year.YearType;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.schedule.entity.type.year.YearCategory;

import java.time.*;
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

    /**
     * 반복이 아닐 때 (단일일정)
     *
     * @param dto
     * @return
     */
    public Boolean registerNoneSchedule(ScheduleRequestDTO dto) {
        try {
            boolean isDifferent = isDuplicatedSaveSchedule(dto);

            if (!isDifferent) {
                throw new DuplicatedScheduleException("중복된 일정 등록입니다.");
            } else {
                NoneType noneType = new NoneType();
                noneType.setValue("none");

                TypeManage typeManage = TypeManage.builder()
                        .noneType(noneType)
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
                        .priceType(judgmentPriceType(() -> {
                            if (dto.getPriceType().equals(PriceType.Plus)) {
                                return PriceType.Plus;
                            } else return PriceType.Minus;
                        }))
                        .build();

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
     * @return
     */
    public Boolean registerDaySchedule(ScheduleRequestDTO dto) {
        try {
            boolean isDifferent = isDuplicatedSaveSchedule(dto);

            if (!isDifferent) {
                throw new DuplicatedScheduleException("중복된 일정 등록입니다.");
            } else {
                DayType bindingDayType = new DayType();
                bindingDayType.setValue(dto.getRepeat().getDayTypeVO().getValue());

                TypeManage typeManage = TypeManage.builder()
                        .dayType(bindingDayType)
                        .build();

                int intervalDays = Integer.parseInt(dto.getRepeat().getDayTypeVO().getValue());
                LocalDate currentDate = formatDate(dto.getStartDate());
                LocalDate endLine = formatDate(dto.getRepeatEndLine());

                while (!currentDate.isAfter(endLine)) {
                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(currentDate.toString())  // 수정된 부분
                            .endDate(currentDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeat(typeManage)
                            .isExclude(dto.isExclude())
                            .importance(dto.getImportance())
                            .amount(dto.getAmount())
                            .isFixAmount(dto.isFixAmount())
                            .repeatEndLine(dto.getRepeatEndLine())
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();

                    crudScheduleRepository.save(schedule);

                    currentDate = currentDate.plusDays(intervalDays);
                }
            }
        } catch (
                RuntimeException e) {
            return null;
        }
        return true;
    }

    /**
     * "주" 단위 반복
     *
     * @param dto
     * @return
     */
    public Boolean registerWeekSchedule(ScheduleRequestDTO dto) {
        try {
            boolean isDifferent = isDuplicatedSaveSchedule(dto);

            if (!isDifferent) {
                throw new DuplicatedScheduleException("중복된 일정 등록입니다.");
            } else {
                StringTokenizer tokenizer =
                        new StringTokenizer(dto.getRepeat().getWeekTypeVO().getRepeatDayOfWeek(), ",");

                int intervalWeeks = Integer.parseInt(dto.getRepeat().getWeekTypeVO().getValue());
                // 문자열을 LocalDate 객체로 변환
                LocalDate startDate = formatDate(dto.getStartDate());
                LocalDate endLine = formatDate(dto.getRepeatEndLine());

                List<String> days = new ArrayList<>();

                // 선택된 요일을 토큰화해서 list에 저장
                while (tokenizer.hasMoreTokens()) {
                    String temp = tokenizer.nextToken().trim();
                    days.add(temp);
                }


                // currentDate: 움직일 임시 객체
                LocalDate currentDate = startDate;

                while (!currentDate.isAfter(endLine)) {
                    String targetDay = currentDate.getDayOfWeek().toString();

                    // 현재 날짜의 요일과 targetDay가 일치하면 스케줄 생성
                    if (days.contains(targetDay)) {
                        log.info("이동하는 요일: {}", targetDay);
                        log.info("일자: {}", currentDate);

                        WeekType bindingWeekType = new WeekType();
                        bindingWeekType.setDayOfWeek(currentDate.getDayOfWeek().toString());
                        bindingWeekType.setValue(dto.getRepeat().getWeekTypeVO().getValue());

                        TypeManage typeManage =
                                TypeManage.builder()
                                        .weekType(bindingWeekType)
                                        .build();

                        Schedule schedule = Schedule.builder()
                                .userId(dto.getUserId())
                                .eventName(dto.getEventName())
                                .category(dto.getCategory())
                                .startDate(currentDate.toString())
                                .endDate(currentDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeat(typeManage)
                                .isExclude(dto.isExclude())
                                .importance(dto.getImportance())
                                .amount(dto.getAmount())
                                .isFixAmount(dto.isFixAmount())
                                .priceType(judgmentPriceType(() -> {
                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                        return PriceType.Plus;
                                    } else return PriceType.Minus;
                                }))
                                .build();

                        crudScheduleRepository.save(schedule);
                        log.info("저장된 요일: {}", schedule.getRepeat().getWeekType().getDayOfWeek());

                        // java에서 한주의 끝은 SUN, 한주의 시작은 MON
                        if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            currentDate = currentDate.plusWeeks(intervalWeeks);
                            currentDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                        } else currentDate = currentDate.plusDays(1);
                    } else {
                        if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            currentDate = currentDate.plusWeeks(intervalWeeks);
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
     * "월" 단위 반복
     */
    public Boolean registerMonthSchedule(ScheduleRequestDTO dto) {
        try {
            boolean isDifferent = isDuplicatedSaveSchedule(dto);

            if (!isDifferent) {
                throw new DuplicatedScheduleException("중복된 일정 등록입니다.");
            } else {
                StringTokenizer tokenizer =
                        new StringTokenizer(dto.getRepeat().getMonthTypeVO().getSelectedDate(), ",");

                LocalDate currentDate = formatDate(dto.getStartDate());
                LocalDate endLine = formatDate(dto.getRepeatEndLine());

                List<Integer> dates = new ArrayList<>();

                while (tokenizer.hasMoreTokens()) {
                    int parseDate = Integer.parseInt(tokenizer.nextToken().trim());
                    log.info("파싱된 date: {}", parseDate);
                    dates.add(parseDate);
                }

                log.info("초기날짜: {}", currentDate.getDayOfMonth());

                while (!currentDate.isAfter(endLine)) {
                    int dayOfMonth = currentDate.getDayOfMonth();

                    log.info("현재 월에서의 날짜: {}", dayOfMonth);

                    if (dates.contains(dayOfMonth)) {
                        MonthType bindingMonthType = new MonthType();
                        bindingMonthType.setMonthValue(dto.getRepeat().getMonthTypeVO().getValue());

                        log.info("*중요 save date: {}", currentDate);
                        TypeManage typeManage = TypeManage
                                .builder()
                                .monthType(bindingMonthType)
                                .build();

                        Schedule schedule = Schedule.builder()
                                .userId(dto.getUserId())
                                .eventName(dto.getEventName())
                                .category(dto.getCategory())
                                .startDate(currentDate.toString())
                                .endDate(currentDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeat(typeManage)
                                .isExclude(dto.isExclude())
                                .importance(dto.getImportance())
                                .amount(dto.getAmount())
                                .isFixAmount(dto.isFixAmount())
                                .priceType(judgmentPriceType(() -> {
                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                        return PriceType.Plus;
                                    } else return PriceType.Minus;
                                }))
                                .build();

                        crudScheduleRepository.save(schedule);

                        currentDate = currentDate.plusDays(1);
                        log.info("이동된 날짜:{}", currentDate);


                        LocalDate lastDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                .withDayOfMonth(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                        .lengthOfMonth());
                        log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                        if (currentDate.equals(lastDate)) {
                            currentDate = currentDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getValue()));
                        }

                    } else {
                        currentDate = currentDate.plusDays(1);
                        log.info("이동된 날짜:{}", currentDate);

                        LocalDate lastDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                .withDayOfMonth(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                        .lengthOfMonth());

                        log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                        if (currentDate.equals(lastDate)) {
                            currentDate = currentDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getValue()));
                        }
                    }
                }


            }
        } catch (RuntimeException e) {
            return null;
        }
        return true;
    }

    public Boolean registerYearSchedule(ScheduleRequestDTO dto) {
        YearScheduleFunc yearScheduleFunc = new YearScheduleFunc();

        try {
            boolean isDifferent = isDuplicatedSaveSchedule(dto);

            if (!isDifferent) {
                throw new DuplicatedScheduleException("중복된 일정 등록입니다.");
            } else {
                // MM월 DD일인 경우 / 어느정도 완성된 듯...
                String yearCategory = dto.getRepeat().getYearTypeVO().getYearCategory();

                if (yearCategory.equals(YearCategory.MonthAndDay.toString())) {
                    LocalDate currentDate = formatDate(dto.getStartDate());
                    LocalDate endLine = formatDate(dto.getRepeatEndLine());

                    // 현재 연도를 가져와서 연도 정보를 추가하여 LocalDate로 변환
                    LocalDate repeatDate = LocalDate.parse(Year.now().getValue() + "-" + dto.getRepeat().getYearTypeVO().getYearRepeat(), formatter);

                    log.info("convert repeatDate:{}", repeatDate);

                    if (repeatDate.isBefore(currentDate)) {
                        repeatDate = repeatDate.plusYears(1);
                    }

                    currentDate = repeatDate;

                    while (!currentDate.isAfter(endLine)) {
                        YearType bindingYearType = new YearType();
                        bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                        bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory());

                        TypeManage typeManage = TypeManage
                                .builder()
                                .yearType(bindingYearType)
                                .build();

                        log.info("저장되는 date: {}", currentDate);

                        Schedule schedule = Schedule.builder()
                                .userId(dto.getUserId())
                                .eventName(dto.getEventName())
                                .category(dto.getCategory())
                                .startDate(currentDate.toString())
                                .endDate(currentDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeat(typeManage)
                                .isExclude(dto.isExclude())
                                .importance(dto.getImportance())
                                .amount(dto.getAmount())
                                .isFixAmount(dto.isFixAmount())
                                .priceType(judgmentPriceType(() -> {
                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                        return PriceType.Plus;
                                    } else return PriceType.Minus;
                                }))
                                .build();

                        crudScheduleRepository.save(schedule);

                        currentDate = currentDate.plusYears(Long.parseLong(dto.getRepeat().getYearTypeVO().getValue()));
                    }
                }

                //  MM월 N번째 D요일
                else if (yearCategory.equals(YearCategory.NthDayOfMonth.toString())) {
                    LocalDate currentDate = formatDate(dto.getStartDate());
                    LocalDate endLine = formatDate(dto.getRepeatEndLine());
                    String yearRepeat = dto.getRepeat().getYearTypeVO().getYearRepeat();
                    log.info("반복되는 조건:{}", yearRepeat);

                    StringTokenizer tokenizer = new StringTokenizer(yearRepeat, " ");
                    List<String> parseDatesList = new ArrayList<>();

                    while (tokenizer.hasMoreTokens()) {
                        String parseData = tokenizer.nextToken().trim();
                        log.info("파싱된 data:{}", parseData);
                        parseDatesList.add(parseData);
                    }

                    String parseMonth = parseDatesList.get(0).replaceAll("[^0-9]", "");
                    int weekValue = Integer.parseInt(parseDatesList.get(1).replaceAll("[^0-9]", ""));
                    DayOfWeek dayOfWeek = yearScheduleFunc.parseKoreanDayOfWeek(parseDatesList.get(2));

                    LocalDate repeatDate = yearScheduleFunc.parseMonthlyDate(parseMonth, weekValue, dayOfWeek);

                    if (currentDate.isBefore(repeatDate)) {
                        currentDate = repeatDate;

                        log.info("*중요 저장될 date:{}", currentDate);

                        YearType bindingYearType = new YearType();
                        bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                        bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory());

                        TypeManage typeManage = TypeManage
                                .builder()
                                .yearType(bindingYearType)
                                .build();

                        Schedule schedule = Schedule.builder()
                                .userId(dto.getUserId())
                                .eventName(dto.getEventName())
                                .category(dto.getCategory())
                                .startDate(currentDate.toString())
                                .endDate(currentDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeat(typeManage)
                                .isExclude(dto.isExclude())
                                .importance(dto.getImportance())
                                .amount(dto.getAmount())
                                .isFixAmount(dto.isFixAmount())
                                .priceType(judgmentPriceType(() -> {
                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                        return PriceType.Plus;
                                    } else return PriceType.Minus;
                                }))
                                .build();

                        crudScheduleRepository.save(schedule);

                    }

                    while (!currentDate.isAfter(endLine)) {
                        int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getValue());

                        LocalDate nextDay = yearScheduleFunc.parseNextMonthlyDate(currentDate.plusYears(value),
                                Integer.parseInt(parseMonth),
                                weekValue,
                                dayOfWeek);

                        currentDate = nextDay;

                        // 종료 조건 추가: currentDate가 endLine을 초과하면 반복문을 빠져나옴
                        if (currentDate.isAfter(endLine)) {
                            break;
                        }

                        log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                        YearType bindingYearType = new YearType();
                        bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                        bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory().toString());

                        log.info("저장되는 repeatDate: {}", currentDate);

                        TypeManage typeManage = TypeManage
                                .builder()
                                .yearType(bindingYearType)
                                .build();

                        Schedule schedule = Schedule.builder()
                                .userId(dto.getUserId())
                                .eventName(dto.getEventName())
                                .category(dto.getCategory())
                                .startDate(currentDate.toString())
                                .endDate(currentDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeat(typeManage)
                                .isExclude(dto.isExclude())
                                .importance(dto.getImportance())
                                .amount(dto.getAmount())
                                .isFixAmount(dto.isFixAmount())
                                .priceType(judgmentPriceType(() -> {
                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                        return PriceType.Plus;
                                    } else return PriceType.Minus;
                                }))
                                .build();

                        crudScheduleRepository.save(schedule);
                    }
                } else if (yearCategory.equals(YearCategory.LastDayOfMonth.toString())) {
                    LocalDate currentDate = formatDate(dto.getStartDate());
                    LocalDate endLine = formatDate(dto.getRepeatEndLine());

                    String yearRepeat = dto.getRepeat().getYearTypeVO().getYearRepeat();
                    log.info("반복되는 조건:{}", yearRepeat);

                    StringTokenizer tokenizer = new StringTokenizer(yearRepeat, " ");
                    List<String> parseDatesList = new ArrayList<>();

                    while (tokenizer.hasMoreTokens()) {
                        String parseData = tokenizer.nextToken().trim();
                        log.info("파싱된 data:{}", parseData);
                        parseDatesList.add(parseData);
                    }

                    String parseMonth = parseDatesList.get(0).replaceAll("[^0-9]", "");
                    DayOfWeek dayOfWeek = yearScheduleFunc.parseKoreanDayOfWeek(parseDatesList.get(2));

                    LocalDate repeatDate = yearScheduleFunc.parseMonthlyLastDate(currentDate, parseMonth, dayOfWeek);

                    if (currentDate.isBefore(repeatDate)) {
                        currentDate = repeatDate;

                        log.info("*중요 저장될 date:{}", currentDate);

                        YearType bindingYearType = new YearType();
                        bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                        bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory());

                        TypeManage typeManage = TypeManage
                                .builder()
                                .yearType(bindingYearType)
                                .build();

                        Schedule schedule = Schedule.builder()
                                .userId(dto.getUserId())
                                .eventName(dto.getEventName())
                                .category(dto.getCategory())
                                .startDate(currentDate.toString())
                                .endDate(currentDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeat(typeManage)
                                .isExclude(dto.isExclude())
                                .importance(dto.getImportance())
                                .amount(dto.getAmount())
                                .isFixAmount(dto.isFixAmount())
                                .priceType(judgmentPriceType(() -> {
                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                        return PriceType.Plus;
                                    } else return PriceType.Minus;
                                }))
                                .build();

                        crudScheduleRepository.save(schedule);
                    }

                    while (!currentDate.isAfter(endLine)) {
                        int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getValue());

                        LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(currentDate.plusYears(value), parseMonth, dayOfWeek);

                        currentDate = nextDay;

                        // 종료 조건 추가: currentDate가 endLine을 초과하면 반복문을 빠져나옴
                        if (currentDate.isAfter(endLine)) {
                            break;
                        }

                        log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                        YearType bindingYearType = new YearType();
                        bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                        bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory().toString());

                        log.info("저장되는 repeatDate: {}", currentDate);

                        TypeManage typeManage = TypeManage
                                .builder()
                                .yearType(bindingYearType)
                                .build();

                        Schedule schedule = Schedule.builder()
                                .userId(dto.getUserId())
                                .eventName(dto.getEventName())
                                .category(dto.getCategory())
                                .startDate(currentDate.toString())
                                .endDate(currentDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeat(typeManage)
                                .isExclude(dto.isExclude())
                                .importance(dto.getImportance())
                                .amount(dto.getAmount())
                                .isFixAmount(dto.isFixAmount())
                                .priceType(judgmentPriceType(() -> {
                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                        return PriceType.Plus;
                                    } else return PriceType.Minus;
                                }))
                                .build();

                        crudScheduleRepository.save(schedule);
                    }

                }
            }
        } catch (Exception e) {
            return null;
        }
        return true;
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
