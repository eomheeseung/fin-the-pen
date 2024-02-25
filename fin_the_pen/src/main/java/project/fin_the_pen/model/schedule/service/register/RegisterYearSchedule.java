package project.fin_the_pen.model.schedule.service.register;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.RepeatKind;
import project.fin_the_pen.model.schedule.entity.type.UnitedType;
import project.fin_the_pen.model.schedule.entity.type.year.YearCategory;
import project.fin_the_pen.model.schedule.entity.type.year.YearScheduleFunc;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Component
@Slf4j
public class RegisterYearSchedule extends RegisterSchedule implements RegisterXXXFunc {
    public RegisterYearSchedule(CrudScheduleRepository crudScheduleRepository) {
        super(crudScheduleRepository);
    }


    @Override
    public Boolean registerSchedule(ScheduleRequestDTO dto) {
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
                    LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

                    // 현재 연도를 가져와서 연도 정보를 추가하여 LocalDate로 변환
                    LocalDate repeatDate = LocalDate.parse(Year.now().getValue() + "-" + dto.getRepeat().getYearTypeVO().getYearRepeat(), getFormatter());

                    log.info("convert repeatDate:{}", repeatDate);

                    if (repeatDate.isBefore(currentDate)) {
                        repeatDate = repeatDate.plusYears(1);
                    }

                    currentDate = repeatDate;


                    if (dto.getPeriod().isRepeatAgain()) {
                        for (int i = 0; i < 50; i++) {

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
                                    .repeatKind(RepeatKind.YEAR.name())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                            .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                            .build())
                                    .isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(true)
                                                .repeatNumberOfTime("0")
                                                .repeatEndLine("none").build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);

                            currentDate = currentDate.plusYears(Long.parseLong(dto.getRepeat().getYearTypeVO().getRepeatTerm()));
                        }
                    } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
                        int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());

                        for (int i = 0; i < repeatNumberOfTime; i++) {
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
                                    .repeatKind(RepeatKind.YEAR.name())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                            .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                            .build()).isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(false)
                                                .repeatNumberOfTime(String.valueOf(repeatNumberOfTime))
                                                .repeatEndLine(null).build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);

                            currentDate = currentDate.plusYears(Long.parseLong(dto.getRepeat().getYearTypeVO().getRepeatTerm()));
                        }
                    } else if (dto.getPeriod().getRepeatEndLine() != null) {
                        while (!currentDate.isAfter(endLine)) {
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
                                    .repeatKind(RepeatKind.YEAR.name())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                            .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                            .build())
                                    .isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(false)
                                                .repeatNumberOfTime("0")
                                                .repeatEndLine(endLine.toString()).build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);

                            currentDate = currentDate.plusYears(Long.parseLong(dto.getRepeat().getYearTypeVO().getRepeatTerm()));
                        }
                    }
                }

                //  MM월 N번째 D요일
                else if (yearCategory.equals(YearCategory.NthDayOfMonth.toString())) {
                    if (dto.getPeriod().isRepeatAgain()) {
                        LocalDate currentDate = formatDate(dto.getStartDate());
                        LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

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

                        for (int i = 0; i < 50; i++) {
                            if (currentDate.isBefore(repeatDate)) {
                                currentDate = repeatDate;

                                log.info("*중요 저장될 date:{}", currentDate);


                                Schedule schedule = Schedule.builder()
                                        .userId(dto.getUserId())
                                        .eventName(dto.getEventName())
                                        .category(dto.getCategory())
                                        .startDate(currentDate.toString())
                                        .endDate(currentDate.toString())
                                        .startTime(dto.getStartTime())
                                        .endTime(dto.getEndTime())
                                        .isAllDay(dto.isAllDay())
                                        .repeatKind(RepeatKind.YEAR.name())
                                        .repeatOptions(UnitedType.builder()
                                                .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .importance(dto.getImportance())
                                        .amount(dto.getAmount())
                                        .isFixAmount(dto.isFixAmount())
                                        .period(createPeriodType(() -> {
                                            return PeriodType.builder()
                                                    .isRepeatAgain(true)
                                                    .repeatNumberOfTime("0")
                                                    .repeatEndLine("none").build();
                                        }))
                                        .priceType(judgmentPriceType(() -> {
                                            if (dto.getPriceType().equals(PriceType.Plus)) {
                                                return PriceType.Plus;
                                            } else return PriceType.Minus;
                                        }))
                                        .build();

                                super.getCrudScheduleRepository().save(schedule);
                            }
                            int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getRepeatTerm());

                            LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(currentDate.plusYears(value), parseMonth, dayOfWeek);

                            currentDate = nextDay;

                            log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.YEAR.name())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                            .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                            .build()).isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(true)
                                                .repeatNumberOfTime("0")
                                                .repeatEndLine("none").build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);
                        }
                    } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
                        int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());

                        LocalDate currentDate = formatDate(dto.getStartDate());
                        LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());
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

                        for (int i = 0; i < repeatNumberOfTime; i++) {
                            if (currentDate.isBefore(repeatDate)) {
                                currentDate = repeatDate;

                                log.info("*중요 저장될 date:{}", currentDate);

                                Schedule schedule = Schedule.builder()
                                        .userId(dto.getUserId())
                                        .eventName(dto.getEventName())
                                        .category(dto.getCategory())
                                        .startDate(currentDate.toString())
                                        .endDate(currentDate.toString())
                                        .startTime(dto.getStartTime())
                                        .endTime(dto.getEndTime())
                                        .isAllDay(dto.isAllDay())
                                        .repeatKind(RepeatKind.YEAR.name())
                                        .repeatOptions(UnitedType.builder()
                                                .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .importance(dto.getImportance())
                                        .amount(dto.getAmount())
                                        .isFixAmount(dto.isFixAmount())
                                        .period(createPeriodType(() -> {
                                            return PeriodType.builder()
                                                    .isRepeatAgain(false)
                                                    .repeatNumberOfTime(String.valueOf(repeatNumberOfTime))
                                                    .repeatEndLine("none").build();
                                        }))
                                        .priceType(judgmentPriceType(() -> {
                                            if (dto.getPriceType().equals(PriceType.Plus)) {
                                                return PriceType.Plus;
                                            } else return PriceType.Minus;
                                        }))
                                        .build();

                                super.getCrudScheduleRepository().save(schedule);
                            }

                            int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getRepeatTerm());
                            LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(currentDate.plusYears(value), parseMonth, dayOfWeek);
                            currentDate = nextDay;

                            log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.YEAR.name())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                            .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                            .build()).isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(true)
                                                .repeatNumberOfTime(String.valueOf(repeatNumberOfTime))
                                                .repeatEndLine("none").build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);

                        }
                    } else if (dto.getPeriod().getRepeatEndLine() != null) {
                        LocalDate currentDate = formatDate(dto.getStartDate());
                        LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());
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

                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.YEAR.name())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                            .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                            .build()).isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(false)
                                                .repeatNumberOfTime("0")
                                                .repeatEndLine(endLine.toString()).build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);

                        }

                        while (!currentDate.isAfter(endLine)) {
                            int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getRepeatTerm());
                            LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(currentDate.plusYears(value), parseMonth, dayOfWeek);
                            currentDate = nextDay;

                            if (currentDate.isAfter(endLine)) {
                                break;
                            }
                            log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.YEAR.name())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                            .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                            .build()).isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(true)
                                                .repeatNumberOfTime("0")
                                                .repeatEndLine("none").build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);
                        }
                    }
                } else if (yearCategory.equals(YearCategory.LastDayOfMonth.toString())) {
                    if (dto.getPeriod().isRepeatAgain()) {
                        LocalDate currentDate = formatDate(dto.getStartDate());
                        LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

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

                        for (int i = 0; i < 50; i++) {
                            if (currentDate.isBefore(repeatDate)) {
                                currentDate = repeatDate;

                                log.info("*중요 저장될 date:{}", currentDate);

                                Schedule schedule = Schedule.builder()
                                        .userId(dto.getUserId())
                                        .eventName(dto.getEventName())
                                        .category(dto.getCategory())
                                        .startDate(currentDate.toString())
                                        .endDate(currentDate.toString())
                                        .startTime(dto.getStartTime())
                                        .endTime(dto.getEndTime())
                                        .isAllDay(dto.isAllDay())
                                        .repeatKind(RepeatKind.YEAR.name())
                                        .repeatOptions(UnitedType.builder()
                                                .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .importance(dto.getImportance())
                                        .amount(dto.getAmount())
                                        .isFixAmount(dto.isFixAmount())
                                        .period(createPeriodType(() -> {
                                            return PeriodType.builder()
                                                    .isRepeatAgain(true)
                                                    .repeatNumberOfTime("0")
                                                    .repeatEndLine("none").build();
                                        }))
                                        .priceType(judgmentPriceType(() -> {
                                            if (dto.getPriceType().equals(PriceType.Plus)) {
                                                return PriceType.Plus;
                                            } else return PriceType.Minus;
                                        }))
                                        .build();

                                super.getCrudScheduleRepository().save(schedule);
                            }

                            int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getRepeatTerm());

                            LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(currentDate.plusYears(value), parseMonth, dayOfWeek);

                            currentDate = nextDay;

                            // 종료 조건 추가: currentDate가 endLine을 초과하면 반복문을 빠져나옴
                            if (currentDate.isAfter(endLine)) {
                                break;
                            }

                            log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.YEAR.name())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                            .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                            .build()).isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(true)
                                                .repeatNumberOfTime("0")
                                                .repeatEndLine("none").build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);
                        }
                    } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
                        int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());

                        LocalDate currentDate = formatDate(dto.getStartDate());
                        LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

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

                        for (int i = 0; i < repeatNumberOfTime; i++) {
                            if (currentDate.isBefore(repeatDate)) {
                                currentDate = repeatDate;

                                log.info("*중요 저장될 date:{}", currentDate);

                                /*YearType bindingYearType = new YearType();
                                bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                                bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory());

                                TypeManage typeManage = TypeManage
                                        .builder()
                                        .yearType(bindingYearType)
                                        .build();*/

                                Schedule schedule = Schedule.builder()
                                        .userId(dto.getUserId())
                                        .eventName(dto.getEventName())
                                        .category(dto.getCategory())
                                        .startDate(currentDate.toString())
                                        .endDate(currentDate.toString())
                                        .startTime(dto.getStartTime())
                                        .endTime(dto.getEndTime())
                                        .isAllDay(dto.isAllDay())
                                        .repeatKind(RepeatKind.YEAR.name())
                                        .repeatOptions(UnitedType.builder()
                                                .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .importance(dto.getImportance())
                                        .amount(dto.getAmount())
                                        .isFixAmount(dto.isFixAmount())
                                        .period(createPeriodType(() -> {
                                            return PeriodType.builder()
                                                    .isRepeatAgain(false)
                                                    .repeatNumberOfTime(String.valueOf(repeatNumberOfTime))
                                                    .repeatEndLine("none").build();
                                        }))
                                        .priceType(judgmentPriceType(() -> {
                                            if (dto.getPriceType().equals(PriceType.Plus)) {
                                                return PriceType.Plus;
                                            } else return PriceType.Minus;
                                        }))
                                        .build();

                                super.getCrudScheduleRepository().save(schedule);
                            }

                            int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getRepeatTerm());

                            LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(currentDate.plusYears(value), parseMonth, dayOfWeek);

                            currentDate = nextDay;

                            log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                            /*YearType bindingYearType = new YearType();
                            bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                            bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory().toString());

                            log.info("저장되는 repeatDate: {}", currentDate);

                            TypeManage typeManage = TypeManage
                                    .builder()
                                    .yearType(bindingYearType)
                                    .build();*/

                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.YEAR.name())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                            .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                            .build()).isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(false)
                                                .repeatNumberOfTime(String.valueOf(repeatNumberOfTime))
                                                .repeatEndLine("none").build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);
                        }
                    } else if (dto.getPeriod().getRepeatEndLine() != null) {
                        LocalDate currentDate = formatDate(dto.getStartDate());
                        LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

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

                            /*YearType bindingYearType = new YearType();
                            bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                            bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory());

                            TypeManage typeManage = TypeManage
                                    .builder()
                                    .yearType(bindingYearType)
                                    .build();*/

                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.YEAR.name())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                            .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                            .build()).isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(false)
                                                .repeatNumberOfTime("0")
                                                .repeatEndLine(endLine.toString()).build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);
                        }

                        while (!currentDate.isAfter(endLine)) {
                            int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getRepeatTerm());

                            LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(currentDate.plusYears(value), parseMonth, dayOfWeek);

                            currentDate = nextDay;

                            // 종료 조건 추가: currentDate가 endLine을 초과하면 반복문을 빠져나옴
                            if (currentDate.isAfter(endLine)) {
                                break;
                            }

                            log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                            /*YearType bindingYearType = new YearType();
                            bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                            bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory().toString());

                            log.info("저장되는 repeatDate: {}", currentDate);

                            TypeManage typeManage = TypeManage
                                    .builder()
                                    .yearType(bindingYearType)
                                    .build();*/

                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.YEAR.name())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                            .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                            .build()).isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(false)
                                                .repeatNumberOfTime("0")
                                                .repeatEndLine(endLine.toString()).build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return true;
    }
}
