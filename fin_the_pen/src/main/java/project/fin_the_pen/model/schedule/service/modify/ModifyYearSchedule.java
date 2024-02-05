package project.fin_the_pen.model.schedule.service.modify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.RepeatKind;
import project.fin_the_pen.model.schedule.entity.type.UnitedType;
import project.fin_the_pen.model.schedule.entity.type.year.YearCategory;
import project.fin_the_pen.model.schedule.entity.type.year.YearScheduleFunc;
import project.fin_the_pen.model.schedule.repository.CRUDScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Component
@Slf4j
public class ModifyYearSchedule extends ModifySchedule implements ModifyXXXFunc {
    public ModifyYearSchedule(CRUDScheduleRepository crudScheduleRepository) {
        super(crudScheduleRepository);
    }

    @Override
    public void modifySchedule(ModifyScheduleDTO dto) {
        YearScheduleFunc yearScheduleFunc = new YearScheduleFunc();

        // MM월 DD일인 경우 / 어느정도 완성된 듯...
        String yearCategory = dto.getRepeat().getYearTypeVO().getYearCategory();

        LocalDate criteriaDate = formatDate(dto.getStartDate());
        LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

        if (yearCategory.equals(YearCategory.MonthAndDay.toString())) {
            // 현재 연도를 가져와서 연도 정보를 추가하여 LocalDate로 변환


            LocalDate repeatDate = LocalDate.parse(Year.now().getValue() + "-" + dto.getRepeat().getYearTypeVO().getYearRepeat(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            log.info("convert repeatDate:{}", repeatDate);

            if (repeatDate.isBefore(criteriaDate)) {
                repeatDate = repeatDate.plusYears(1);
            }

            criteriaDate = repeatDate;


            if (dto.getPeriod().isRepeatAgain()) {
                int endRepeat = 50;

                for (int i = 0; i < endRepeat; i++) {
                    /*YearType bindingYearType = new YearType();
                    bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                    bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory());

                    TypeManage typeManage = TypeManage
                            .builder()
                            .yearType(bindingYearType)
                            .build();*/

                    log.info("저장되는 date: {}", criteriaDate);

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeatKind(RepeatKind.YEAR.name())
                            .repeatOptions(UnitedType.builder()
                                    .value(dto.getRepeat().getYearTypeVO().getValue())
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
                                        .repeatEndLine(null).build();
                            }))
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();

                    getCrudScheduleRepository().save(schedule);

                    criteriaDate = criteriaDate.plusYears(Long.parseLong(dto.getRepeat().getYearTypeVO().getValue()));
                }
            } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
                int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());

                for (int i = 0; i < repeatNumberOfTime; i++) {
                    /*YearType bindingYearType = new YearType();
                    bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                    bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory());

                    TypeManage typeManage = TypeManage
                            .builder()
                            .yearType(bindingYearType)
                            .build();*/

                    log.info("저장되는 date: {}", criteriaDate);

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeatKind(RepeatKind.YEAR.name())
                            .repeatOptions(UnitedType.builder()
                                    .value(dto.getRepeat().getYearTypeVO().getValue())
                                    .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                    .build())
                            .isExclude(dto.isExclude())
                            .importance(dto.getImportance())
                            .amount(dto.getAmount())
                            .isFixAmount(dto.isFixAmount())
                            .period(createPeriodType(() -> {
                                return PeriodType.builder()
                                        .isRepeatAgain(false)
                                        .repeatEndLine(null).build();
                            }))
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();

                    getCrudScheduleRepository().save(schedule);

                    criteriaDate = criteriaDate.plusYears(Long.parseLong(dto.getRepeat().getYearTypeVO().getValue()));
                }
            } else if (dto.getPeriod().getRepeatEndLine() != null) {

                while (!criteriaDate.isAfter(endLine)) {
                    /*YearType bindingYearType = new YearType();
                    bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                    bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory());

                    TypeManage typeManage = TypeManage
                            .builder()
                            .yearType(bindingYearType)
                            .build();*/

                    log.info("저장되는 date: {}", criteriaDate);

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeatKind(RepeatKind.YEAR.name())
                            .repeatOptions(UnitedType.builder()
                                    .value(dto.getRepeat().getYearTypeVO().getValue())
                                    .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                    .build())
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

                    getCrudScheduleRepository().save(schedule);

                    criteriaDate = criteriaDate.plusYears(Long.parseLong(dto.getRepeat().getYearTypeVO().getValue()));
                }
            }
        }

        //  MM월 N번째 D요일
        else if (yearCategory.equals(YearCategory.NthDayOfMonth.toString())) {

            if (dto.getPeriod().isRepeatAgain()) {
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
                    if (criteriaDate.isBefore(repeatDate)) {
                        criteriaDate = repeatDate;

                        log.info("*중요 저장될 date:{}", criteriaDate);

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
                                .startDate(criteriaDate.toString())
                                .endDate(criteriaDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeatKind(RepeatKind.YEAR.name())
                                .repeatOptions(UnitedType.builder()
                                        .value(dto.getRepeat().getYearTypeVO().getValue())
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
                                            .repeatEndLine(null).build();
                                }))
                                .priceType(judgmentPriceType(() -> {
                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                        return PriceType.Plus;
                                    } else return PriceType.Minus;
                                }))
                                .build();

                        getCrudScheduleRepository().save(schedule);
                    }
                    int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getValue());

                    LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(criteriaDate.plusYears(value), parseMonth, dayOfWeek);

                    criteriaDate = nextDay;

                    log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                    /*YearType bindingYearType = new YearType();
                    bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                    bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory().toString());

                    log.info("저장되는 repeatDate: {}", criteriaDate);

                    TypeManage typeManage = TypeManage
                            .builder()
                            .yearType(bindingYearType)
                            .build();*/

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeatKind(RepeatKind.YEAR.name())
                            .repeatOptions(UnitedType.builder()
                                    .value(dto.getRepeat().getYearTypeVO().getValue())
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
                                        .repeatEndLine(null).build();
                            }))
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();

                    getCrudScheduleRepository().save(schedule);
                }
            } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
                int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());

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
                    if (criteriaDate.isBefore(repeatDate)) {
                        criteriaDate = repeatDate;

                        log.info("*중요 저장될 date:{}", criteriaDate);

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
                                .startDate(criteriaDate.toString())
                                .endDate(criteriaDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeatKind(RepeatKind.YEAR.name())
                                .repeatOptions(UnitedType.builder()
                                        .value(dto.getRepeat().getYearTypeVO().getValue())
                                        .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                        .build())
                                .isExclude(dto.isExclude())
                                .importance(dto.getImportance())
                                .amount(dto.getAmount())
                                .isFixAmount(dto.isFixAmount())
                                .period(createPeriodType(() -> {
                                    return PeriodType.builder()
                                            .isRepeatAgain(false)
                                            .repeatEndLine(null).build();
                                }))
                                .priceType(judgmentPriceType(() -> {
                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                        return PriceType.Plus;
                                    } else return PriceType.Minus;
                                }))
                                .build();

                        getCrudScheduleRepository().save(schedule);
                    }
                    int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getValue());
                    LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(criteriaDate.plusYears(value), parseMonth, dayOfWeek);
                    criteriaDate = nextDay;

                    log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                    /*YearType bindingYearType = new YearType();
                    bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                    bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory().toString());


                    TypeManage typeManage = TypeManage
                            .builder()
                            .yearType(bindingYearType)
                            .build();*/

                    log.info("저장되는 repeatDate: {}", criteriaDate);

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeatKind(RepeatKind.YEAR.name())
                            .repeatOptions(UnitedType.builder()
                                    .value(dto.getRepeat().getYearTypeVO().getValue())
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
                                        .repeatEndLine(null).build();
                            }))
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();

                    getCrudScheduleRepository().save(schedule);
                }
            } else if (dto.getPeriod().getRepeatEndLine() != null) {
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

                if (criteriaDate.isBefore(repeatDate)) {
                    criteriaDate = repeatDate;

                    log.info("*중요 저장될 date:{}", criteriaDate);

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
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeatKind(RepeatKind.YEAR.name())
                            .repeatOptions(UnitedType.builder()
                                    .value(dto.getRepeat().getYearTypeVO().getValue())
                                    .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                    .build())
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

                    getCrudScheduleRepository().save(schedule);
                }

                while (!criteriaDate.isAfter(endLine)) {
                    int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getValue());
                    LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(criteriaDate.plusYears(value), parseMonth, dayOfWeek);
                    criteriaDate = nextDay;

                    if (criteriaDate.isAfter(endLine)) {
                        break;
                    }
                    log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                    /*YearType bindingYearType = new YearType();
                    bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                    bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory().toString());


                    TypeManage typeManage = TypeManage
                            .builder()
                            .yearType(bindingYearType)
                            .build();*/

                    log.info("저장되는 repeatDate: {}", criteriaDate);

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeatKind(RepeatKind.YEAR.name())
                            .repeatOptions(UnitedType.builder()
                                    .value(dto.getRepeat().getYearTypeVO().getValue())
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
                                        .repeatEndLine(null).build();
                            }))
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();

                    getCrudScheduleRepository().save(schedule);
                }
            }
        }

        else if (yearCategory.equals(YearCategory.LastDayOfMonth.toString())) {

            if (dto.getPeriod().isRepeatAgain()) {

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

                LocalDate repeatDate = yearScheduleFunc.parseMonthlyLastDate(criteriaDate, parseMonth, dayOfWeek);

                for (int i = 0; i < 50; i++) {
                    if (criteriaDate.isBefore(repeatDate)) {
                        criteriaDate = repeatDate;

                        log.info("*중요 저장될 date:{}", criteriaDate);

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
                                .startDate(criteriaDate.toString())
                                .endDate(criteriaDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeatKind(RepeatKind.YEAR.name())
                                .repeatOptions(UnitedType.builder()
                                        .value(dto.getRepeat().getYearTypeVO().getValue())
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
                                            .repeatEndLine(null).build();
                                }))
                                .priceType(judgmentPriceType(() -> {
                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                        return PriceType.Plus;
                                    } else return PriceType.Minus;
                                }))
                                .build();

                        getCrudScheduleRepository().save(schedule);
                    }

                    int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getValue());

                    LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(criteriaDate.plusYears(value), parseMonth, dayOfWeek);

                    criteriaDate = nextDay;

                    // 종료 조건 추가: currentDate가 endLine을 초과하면 반복문을 빠져나옴
                    if (criteriaDate.isAfter(endLine)) {
                        break;
                    }

                    log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                    /*YearType bindingYearType = new YearType();
                    bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                    bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory().toString());

                    log.info("저장되는 repeatDate: {}", criteriaDate);

                    TypeManage typeManage = TypeManage
                            .builder()
                            .yearType(bindingYearType)
                            .build();*/

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeatKind(RepeatKind.YEAR.name())
                            .repeatOptions(UnitedType.builder()
                                    .value(dto.getRepeat().getYearTypeVO().getValue())
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
                                        .repeatEndLine(null).build();
                            }))
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();

                    getCrudScheduleRepository().save(schedule);
                }
            } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
                int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());

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

                LocalDate repeatDate = yearScheduleFunc.parseMonthlyLastDate(criteriaDate, parseMonth, dayOfWeek);

                for (int i = 0; i < repeatNumberOfTime; i++) {
                    if (criteriaDate.isBefore(repeatDate)) {
                        criteriaDate = repeatDate;

                        log.info("*중요 저장될 date:{}", criteriaDate);

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
                                .startDate(criteriaDate.toString())
                                .endDate(criteriaDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeatKind(RepeatKind.YEAR.name())
                                .repeatOptions(UnitedType.builder()
                                        .value(dto.getRepeat().getYearTypeVO().getValue())
                                        .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                        .build())
                                .isExclude(dto.isExclude())
                                .importance(dto.getImportance())
                                .amount(dto.getAmount())
                                .isFixAmount(dto.isFixAmount())
                                .period(createPeriodType(() -> {
                                    return PeriodType.builder()
                                            .isRepeatAgain(false)
                                            .repeatEndLine(null).build();
                                }))
                                .priceType(judgmentPriceType(() -> {
                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                        return PriceType.Plus;
                                    } else return PriceType.Minus;
                                }))
                                .build();

                        getCrudScheduleRepository().save(schedule);
                    }

                    int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getValue());

                    LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(criteriaDate.plusYears(value), parseMonth, dayOfWeek);

                    criteriaDate = nextDay;

                    log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                    /*YearType bindingYearType = new YearType();
                    bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                    bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory().toString());


                    TypeManage typeManage = TypeManage
                            .builder()
                            .yearType(bindingYearType)
                            .build();*/

                    log.info("저장되는 repeatDate: {}", criteriaDate);

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeatKind(RepeatKind.YEAR.name())
                            .repeatOptions(UnitedType.builder()
                                    .value(dto.getRepeat().getYearTypeVO().getValue())
                                    .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                    .build())
                            .isExclude(dto.isExclude())
                            .importance(dto.getImportance())
                            .amount(dto.getAmount())
                            .isFixAmount(dto.isFixAmount())
                            .period(createPeriodType(() -> {
                                return PeriodType.builder()
                                        .isRepeatAgain(false)
                                        .repeatEndLine(null).build();
                            }))
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();

                    getCrudScheduleRepository().save(schedule);
                }
            } else if (dto.getPeriod().getRepeatEndLine() != null) {
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

                LocalDate repeatDate = yearScheduleFunc.parseMonthlyLastDate(criteriaDate, parseMonth, dayOfWeek);

                if (criteriaDate.isBefore(repeatDate)) {
                    criteriaDate = repeatDate;

                    log.info("*중요 저장될 date:{}", criteriaDate);

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
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeatKind(RepeatKind.YEAR.name())
                            .repeatOptions(UnitedType.builder()
                                    .value(dto.getRepeat().getYearTypeVO().getValue())
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

                    getCrudScheduleRepository().save(schedule);
                }

                while (!criteriaDate.isAfter(endLine)) {
                    int value = Integer.parseInt(dto.getRepeat().getYearTypeVO().getValue());

                    LocalDate nextDay = yearScheduleFunc.parseMonthlyLastDate(criteriaDate.plusYears(value), parseMonth, dayOfWeek);

                    criteriaDate = nextDay;

                    // 종료 조건 추가: currentDate가 endLine을 초과하면 반복문을 빠져나옴
                    if (criteriaDate.isAfter(endLine)) {
                        break;
                    }

                    log.info("다음 년도의 조건에 해당하는 date:{}", nextDay);

                    /*YearType bindingYearType = new YearType();
                    bindingYearType.setValue(dto.getRepeat().getYearTypeVO().getValue());
                    bindingYearType.setYearCategory(dto.getRepeat().getYearTypeVO().getYearCategory().toString());

                    log.info("저장되는 repeatDate: {}", criteriaDate);

                    TypeManage typeManage = TypeManage
                            .builder()
                            .yearType(bindingYearType)
                            .build();*/

                    Schedule schedule = Schedule.builder()
                            .userId(dto.getUserId())
                            .eventName(dto.getEventName())
                            .category(dto.getCategory())
                            .startDate(criteriaDate.toString())
                            .endDate(criteriaDate.toString())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .isAllDay(dto.isAllDay())
                            .repeatKind(RepeatKind.YEAR.name())
                            .repeatOptions(UnitedType.builder()
                                    .value(dto.getRepeat().getYearTypeVO().getValue())
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

                    getCrudScheduleRepository().save(schedule);
                }
            }
        }
    }
}
