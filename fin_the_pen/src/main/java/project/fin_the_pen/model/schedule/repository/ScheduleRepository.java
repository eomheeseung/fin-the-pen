package project.fin_the_pen.model.schedule.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.finClient.core.util.ScheduleTypeFunc;
import project.fin_the_pen.finClient.core.util.TokenManager;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.type.*;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.schedule.type.YearCategory;
import project.fin_the_pen.model.schedule.vo.YearTypeVO;

import javax.security.auth.callback.Callback;
import java.text.DateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
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

    /**
     * 반복이 아닐 때 (단일일정)
     *
     * @param dto
     * @return
     */
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

                /*schedule.setPriceType(() -> {
                    if (dto.getPriceType().equals(PriceType.Plus)) {
                        return PriceType.Plus;
                    } else return PriceType.Minus;
                });*/

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
                DayType bindingDayType = new DayType();
                bindingDayType.setValue(dto.getRepeat().getDayTypeVO().getValue());

                TypeManage typeManage = TypeManage.builder()
                        .dayType(bindingDayType)
                        .build();

                log.info(dto.getUserId());
                log.info(dto.getAmount());

                int intervalDays = Integer.parseInt(dto.getRepeat().getDayTypeVO().getValue());
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

                    /*schedule.setPriceType(() -> {
                        if (dto.getPriceType().equals(PriceType.Plus)) {
                            return PriceType.Plus;
                        } else return PriceType.Minus;
                    });*/

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
     *
     * @param dto
     * @return
     */
    public Boolean registerWeekSchedule(ScheduleRequestDTO dto) {
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
                StringTokenizer tokenizer =
                        new StringTokenizer(dto.getRepeat().getWeekTypeVO().getRepeatDayOfWeek(), ",");

                // DateTimeFormatter 인스턴스 생성
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                int intervalWeeks = Integer.parseInt(dto.getRepeat().getWeekTypeVO().getValue());
                // 문자열을 LocalDate 객체로 변환
                LocalDate startDate = LocalDate.parse(dto.getStartDate(), formatter);
                LocalDate endLine = LocalDate.parse(dto.getRepeatEndLine(), formatter);

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
                            log.info("term:{}", intervalWeeks);
                            currentDate = currentDate.plusWeeks(intervalWeeks);
                            currentDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                        } else currentDate = currentDate.plusDays(1);
                    } else {
                        if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            log.info("term:{}", intervalWeeks);
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
                StringTokenizer tokenizer =
                        new StringTokenizer(dto.getRepeat().getMonthTypeVO().getSelectedDate(), ",");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDate startDate = LocalDate.parse(dto.getStartDate(), formatter);
                LocalDate endLine = LocalDate.parse(dto.getRepeatEndLine(), formatter);

                LocalDate currentDate = startDate;

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
                // MM월 DD일인 경우 / 어느정도 완성된 듯...
                log.info("입력받은 repeat data: {}", dto.getRepeat().getYearTypeVO().getYearRepeat());
                log.info("입력받은 repeat category: {}", dto.getRepeat().getYearTypeVO().getYearCategory());
                log.info("조건값:{}", dto.getRepeat().getYearTypeVO().getYearCategory().equals("MonthAndDay"));

                if (dto.getRepeat().getYearTypeVO().getYearCategory().equals(YearCategory.MonthAndDay.toString())) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate currentDate = LocalDate.parse(dto.getStartDate(), formatter);
                    LocalDate endLine = LocalDate.parse(dto.getRepeatEndLine(), formatter);


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
            }
            // TODO!!!!!!
            if (dto.getRepeat().getYearTypeVO().getYearCategory().equals(YearCategory.NthDayOfMonth.toString())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 N번째 E요일", Locale.KOREAN);

                LocalDate currentDate = LocalDate.parse(dto.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate endLine = LocalDate.parse(dto.getRepeatEndLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate repeatDate = LocalDate.parse(dto.getRepeat().getYearTypeVO().getYearRepeat(), formatter);

                // MM월 맞추기
                while (!currentDate.isAfter(endLine)) {
                    if (currentDate.getMonthValue() < repeatDate.getMonthValue()) {
                        currentDate = currentDate.plusMonths(1);

                        // n번째 주 맞추기
                    } else if (currentDate.getMonthValue() == repeatDate.getMonthValue()) {
                        // 현재 날짜의 월의 첫 번째 날을 구한다.
                        LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());

                        // 현재 날짜가 몇 번째 주에 속하는지 계산.
                        int weekOfMonth = (firstDayOfMonth.getDayOfMonth() - 1) / 7 + 1;
                        int repeatWeekOfMonth = (repeatDate.getDayOfMonth() - 1) / 7 + 1;

                        while (weekOfMonth < repeatWeekOfMonth) {
                            currentDate = currentDate.plusWeeks(1);
                            firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
                            weekOfMonth = (firstDayOfMonth.getDayOfMonth() - 1) / 7 + 1;
                        }

                        // 요일 맞추기
                        while (!currentDate.getDayOfWeek().equals(repeatDate.getDayOfWeek())) {
                            currentDate = currentDate.plusDays(1);
                        }

                        YearType bindingYearType = new YearType();
                        bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                        bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory().toString());

                        log.info("저장되는 date: {}", currentDate);

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

                        currentDate = currentDate.plusYears(Long.parseLong(dto.getRepeat().getYearTypeVO().getValue()));
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
     * TODO 수정
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

    private PriceType judgmentPriceType(Supplier<PriceType> supplier) {
        return supplier.get();
    }
}
