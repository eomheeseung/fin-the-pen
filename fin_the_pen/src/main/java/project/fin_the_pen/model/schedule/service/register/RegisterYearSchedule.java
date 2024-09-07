package project.fin_the_pen.model.schedule.service.register;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.PaymentType;
import project.fin_the_pen.model.schedule.entity.type.RepeatKind;
import project.fin_the_pen.model.schedule.entity.type.UnitedType;
import project.fin_the_pen.model.schedule.entity.type.year.YearCategory;
import project.fin_the_pen.model.schedule.entity.type.year.YearScheduleFunc;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.template.Template;
import project.fin_the_pen.model.schedule.template.TemplateBankStatement;
import project.fin_the_pen.model.schedule.template.TemplateRepository;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.schedule.type.RegularType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Component
@Slf4j
public class RegisterYearSchedule extends RegisterSchedule implements RegisterXXXFunc {
    public RegisterYearSchedule(CrudScheduleRepository crudScheduleRepository, TemplateRepository templateRepository) {
        super(crudScheduleRepository, templateRepository);
    }


    /**
     * TODO !!!!
     *  week(O)
     *  year(X) => 일 때 template 적용 문제
     * 각각 MM월 DD일, MonthAndDay(0),
     * MM월 N번째 D요일, NthDayOfMonth(1),
     * MM월 마지막 D요일, LastDayOfMonth(2);
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean registerSchedule(ScheduleRequestDTO dto) {
        YearScheduleFunc yearScheduleFunc = new YearScheduleFunc();
        String userId = dto.getUserId();
        String category = dto.getCategory();
        String eventName = dto.getEventName();


        boolean isDuplicated = isDuplicatedRegular(userId, eventName, category);

        if (!isDuplicated && !dto.isRegisterTemplate()) {
            return false;
        }

        // template을 사용하는 경우
        if (dto.isRegisterTemplate()) {
            Template template = createTemplate(userId, category, eventName);

            if (dto.getPriceType().equals(PriceType.Plus)) {
                template.updateStatement(TemplateBankStatement.DEPOSIT);
            } else {
                template.updateStatement(TemplateBankStatement.WITHDRAW);
            }

            try {
                boolean isDifferent = isDuplicatedSaveSchedule(dto);

                if (isDifferent) {
                    throw new DuplicatedScheduleException("중복된 일정 등록입니다. (year register method error)");
                } else {
                    String dtoPaymentType = dto.getPaymentType();
                    PaymentType paymentType;

                    if (dtoPaymentType.equals(PaymentType.ACCOUNT.name())) {
                        paymentType = PaymentType.ACCOUNT;
                    } else if (dtoPaymentType.equals(PaymentType.CASH.name())) {
                        paymentType = PaymentType.CASH;
                    } else {
                        paymentType = PaymentType.CARD;
                    }

                    // MM월 DD일인 경우
                    // 09-07
                    String yearCategory = dto.getRepeat().getYearTypeVO().getYearCategory();

                    if (yearCategory.equals(YearCategory.MonthAndDay.toString())) {
                        // 현재 날짜
                        LocalDate currentDate = formatDate(dto.getStartDate());

                        // period의 repeat_end_line를 가져와서 사용
                        LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

                        // 09-07이 반복 조건이라면,
                        // 2024-09-07을 만드는 과정
                        LocalDate repeatDate =
                                LocalDate.parse(Year.now().getValue() +
                                                "-" + dto.getRepeat().getYearTypeVO().getYearRepeat(),
                                        getFormatter());

                        log.info("convert repeatDate:{}", repeatDate);

                        if (repeatDate.isBefore(currentDate)) {
                            repeatDate = repeatDate.plusYears(1);
                        }

                        // 왜 할당했지...?
                        // currentDate = repeatDate;
                        // 아래의 코드에서 currentDate -> repeatDate로 바꿈!

                        /*
                            isRepeatAgain : true
                            => default로 50번만 반복
                         */
                        if (dto.getPeriod().isRepeatAgain()) {

                            log.info("MonthAndDay case 1");

                            for (int i = 0; i < 50; i++) {

                                log.info("저장되는 date: {}", repeatDate);

                                Schedule schedule = Schedule.builder()
                                        .userId(dto.getUserId())
                                        .eventName(dto.getEventName())
                                        .category(dto.getCategory())
                                        .startDate(repeatDate.toString())
                                        .endDate(repeatDate.toString())
                                        .startTime(dto.getStartTime())
                                        .endTime(dto.getEndTime())
                                        .isAllDay(dto.isAllDay())
                                        .repeatKind(RepeatKind.YEAR.name())
                                        .repeatOptions(UnitedType.builder()
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build())
                                        .isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(template);
                                super.getCrudScheduleRepository().save(schedule);

                                repeatDate = repeatDate.plusYears(Long.parseLong(dto.getRepeat().getYearTypeVO().getRepeatTerm()));
                            }

                            // 반복 횟수가 정해진 경우
                        } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {

                            log.info("MonthAndDay case 2");

                            int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());

                            for (int i = 0; i < repeatNumberOfTime; i++) {
                                log.info("저장되는 date: {}", repeatDate);

                                Schedule schedule = Schedule.builder()
                                        .userId(dto.getUserId())
                                        .eventName(dto.getEventName())
                                        .category(dto.getCategory())
                                        .startDate(repeatDate.toString())
                                        .endDate(repeatDate.toString())
                                        .startTime(dto.getStartTime())
                                        .endTime(dto.getEndTime())
                                        .isAllDay(dto.isAllDay())
                                        .repeatKind(RepeatKind.YEAR.name())
                                        .repeatOptions(UnitedType.builder()
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build())
                                        .isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(template);
                                super.getCrudScheduleRepository().save(schedule);

                                repeatDate = repeatDate.plusYears(Long.parseLong(dto.getRepeat().getYearTypeVO().getRepeatTerm()));
                            }

                            /*
                             지정된 기간까지 반복
                             횟수가 아님!
                             */
                            // 아래의 조건을 null이 아닌 none와 같은 string 형식으로 바꾸자
                        } else if (dto.getPeriod().getRepeatEndLine() != null) {

                            log.info("MonthAndDay case 3");

                            while (!repeatDate.isAfter(endLine)) {
                                log.info("저장되는 date: {}", currentDate);

                                Schedule schedule = Schedule.builder()
                                        .userId(dto.getUserId())
                                        .eventName(dto.getEventName())
                                        .category(dto.getCategory())
                                        .startDate(repeatDate.toString())
                                        .endDate(repeatDate.toString())
                                        .startTime(dto.getStartTime())
                                        .endTime(dto.getEndTime())
                                        .isAllDay(dto.isAllDay())
                                        .repeatKind(RepeatKind.YEAR.name())
                                        .repeatOptions(UnitedType.builder()
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build())
                                        .isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(template);
                                super.getCrudScheduleRepository().save(schedule);

                                repeatDate = repeatDate.plusYears(Long.parseLong(dto.getRepeat().getYearTypeVO().getRepeatTerm()));
                            }
                        }
                    }

                    //  MM월 N번째 D요일
                    else if (yearCategory.equals(YearCategory.NthDayOfMonth.toString())) {

                        // default로 50번 반복
                        if (dto.getPeriod().isRepeatAgain()) {
                            log.info("NthDayOfMonth case 1");
                            LocalDate currentDate = formatDate(dto.getStartDate());

                            // "MM월 N번째 D요일"
                            String yearCondition = dto.getRepeat().getYearTypeVO().getYearRepeat();

                            log.info("반복되는 조건:{}", yearCondition);

                            StringTokenizer tokenizer = new StringTokenizer(yearCondition, " ");
                            List<String> parseTokens = new ArrayList<>();

                            while (tokenizer.hasMoreTokens()) {
                                String parseData = tokenizer.nextToken().trim();
                                log.info("파싱된 data:{}", parseData);
                                parseTokens.add(parseData);
                            }

                            // MM만 추출
                            String parseMonth = parseTokens.get(0).substring(0, 2);

                            // N만 추출
                            int weekValue = Integer.parseInt(parseTokens.get(1).substring(0, 1));

                            // string "수요일"을 DayOfWeek type으로 변환
                            DayOfWeek dayOfWeek = yearScheduleFunc.parseKoreanDayOfWeek(parseTokens.get(2));

                            int currentYear = currentDate.getYear();
                            LocalDate repeatDate = yearScheduleFunc.parseMonthlyDate(currentYear, parseMonth, weekValue, dayOfWeek);

                            for (int i = 0; i < 50; i++) {
                                if (currentDate.isBefore(repeatDate)) {
                                    log.info("* 중요 저장될 date:{}", repeatDate);

                                    Schedule schedule = Schedule.builder()
                                            .userId(dto.getUserId())
                                            .eventName(dto.getEventName())
                                            .category(dto.getCategory())
                                            .startDate(repeatDate.toString())
                                            .endDate(repeatDate.toString())
                                            .startTime(dto.getStartTime())
                                            .endTime(dto.getEndTime())
                                            .isAllDay(dto.isAllDay())
                                            .repeatKind(RepeatKind.YEAR.name())
                                            .repeatOptions(UnitedType.builder()
                                                    .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                    .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                    .build()).isExclude(dto.isExclude())
                                            .paymentType(paymentType)
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
                                            .regularType(RegularType.REGULAR)
                                            .build();

                                    schedule.setTemplate(template);
                                    super.getCrudScheduleRepository().save(schedule);
                                }

                                int repeatTerm = Integer.parseInt(dto.getRepeat().getYearTypeVO().getRepeatTerm());

                                repeatDate = yearScheduleFunc.parseMonthlyDate(repeatDate.plusYears(repeatTerm).getYear(), parseMonth, weekValue, dayOfWeek);

//                                repeatDate = yearScheduleFunc.parseMonthlyLastDate(repeatDate.plusYears(repeatTerm), parseMonth, dayOfWeek);


                                log.info("다음 년도의 조건에 해당하는 date:{}", repeatDate);

                                Schedule schedule = Schedule.builder()
                                        .userId(dto.getUserId())
                                        .eventName(dto.getEventName())
                                        .category(dto.getCategory())
                                        .startDate(repeatDate.toString())
                                        .endDate(repeatDate.toString())
                                        .startTime(dto.getStartTime())
                                        .endTime(dto.getEndTime())
                                        .isAllDay(dto.isAllDay())
                                        .repeatKind(RepeatKind.YEAR.name())
                                        .repeatOptions(UnitedType.builder()
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(template);
                                super.getCrudScheduleRepository().save(schedule);
                            }
                        } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {

                            log.info("NthDayOfMonth case 2");

                            int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());

                            LocalDate currentDate = formatDate(dto.getStartDate());
                            String yearCondition = dto.getRepeat().getYearTypeVO().getYearRepeat();

                            log.info("반복되는 조건:{}", yearCondition);

                            StringTokenizer tokenizer = new StringTokenizer(yearCondition, " ");
                            List<String> parseTokens = new ArrayList<>();

                            while (tokenizer.hasMoreTokens()) {
                                String parseData = tokenizer.nextToken().trim();
                                log.info("파싱된 data:{}", parseData);
                                parseTokens.add(parseData);
                            }

                            // MM만 추출
                            String parseMonth = parseTokens.get(0).substring(0, 2);

                            // N만 추출
                            int weekValue = Integer.parseInt(parseTokens.get(1).substring(0, 1));

                            // string "수요일"을 DayOfWeek type으로 변환
                            DayOfWeek dayOfWeek = yearScheduleFunc.parseKoreanDayOfWeek(parseTokens.get(2));

                            LocalDate repeatDate = yearScheduleFunc.parseMonthlyDate(currentDate.getYear(), parseMonth, weekValue, dayOfWeek);

                            for (int i = 0; i < repeatNumberOfTime; i++) {
                                if (currentDate.isBefore(repeatDate)) {

                                    log.info("* 중요 저장될 date:{}", repeatDate);

                                    Schedule schedule = Schedule.builder()
                                            .userId(dto.getUserId())
                                            .eventName(dto.getEventName())
                                            .category(dto.getCategory())
                                            .startDate(repeatDate.toString())
                                            .endDate(repeatDate.toString())
                                            .startTime(dto.getStartTime())
                                            .endTime(dto.getEndTime())
                                            .isAllDay(dto.isAllDay())
                                            .repeatKind(RepeatKind.YEAR.name())
                                            .repeatOptions(UnitedType.builder()
                                                    .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                    .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                    .build()).isExclude(dto.isExclude())
                                            .paymentType(paymentType)
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
                                            .regularType(RegularType.REGULAR)
                                            .build();

                                    schedule.setTemplate(template);
                                    super.getCrudScheduleRepository().save(schedule);
                                }

                                int repeatTerm = Integer.parseInt(dto.getRepeat().getYearTypeVO().getRepeatTerm());

                                repeatDate = yearScheduleFunc.parseMonthlyDate(repeatDate.plusYears(repeatTerm).getYear(), parseMonth, weekValue, dayOfWeek);
//                                repeatDate = yearScheduleFunc.parseMonthlyLastDate(currentDate.plusYears(repeatTerm), parseMonth, dayOfWeek);

                                log.info("다음 년도의 조건에 해당하는 date:{}", repeatDate);

                                Schedule schedule = Schedule.builder()
                                        .userId(dto.getUserId())
                                        .eventName(dto.getEventName())
                                        .category(dto.getCategory())
                                        .startDate(repeatDate.toString())
                                        .endDate(repeatDate.toString())
                                        .startTime(dto.getStartTime())
                                        .endTime(dto.getEndTime())
                                        .isAllDay(dto.isAllDay())
                                        .repeatKind(RepeatKind.YEAR.name())
                                        .repeatOptions(UnitedType.builder()
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(template);
                                super.getCrudScheduleRepository().save(schedule);

                            }
                        }

                        else if (dto.getPeriod().getRepeatEndLine() != null) {
                            log.info("NthDayOfMonth case 3");
                            LocalDate currentDate = formatDate(dto.getStartDate());
                            LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());
                            String yearCondition = dto.getRepeat().getYearTypeVO().getYearRepeat();
                            log.info("반복되는 조건:{}", yearCondition);

                            StringTokenizer tokenizer = new StringTokenizer(yearCondition, " ");
                            List<String> parseTokens = new ArrayList<>();

                            while (tokenizer.hasMoreTokens()) {
                                String parseData = tokenizer.nextToken().trim();
                                log.info("파싱된 data: {}", parseData);
                                parseTokens.add(parseData);
                            }

                            // MM만 추출
                            String parseMonth = parseTokens.get(0).substring(0, 2);

                            // N만 추출
                            int weekValue = Integer.parseInt(parseTokens.get(1).substring(0, 1));

                            // string "수요일"을 DayOfWeek type으로 변환
                            DayOfWeek dayOfWeek = yearScheduleFunc.parseKoreanDayOfWeek(parseTokens.get(2));

                            LocalDate repeatDate = yearScheduleFunc.parseMonthlyDate(currentDate.getYear(), parseMonth, weekValue, dayOfWeek);

                            // ?????
                            /*if (currentDate.isBefore(repeatDate)) {

                                log.info("*중요 저장될 date:{}", repeatDate);

                                Schedule schedule = Schedule.builder()
                                        .userId(dto.getUserId())
                                        .eventName(dto.getEventName())
                                        .category(dto.getCategory())
                                        .startDate(repeatDate.toString())
                                        .endDate(repeatDate.toString())
                                        .startTime(dto.getStartTime())
                                        .endTime(dto.getEndTime())
                                        .isAllDay(dto.isAllDay())
                                        .repeatKind(RepeatKind.YEAR.name())
                                        .repeatOptions(UnitedType.builder()
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(template);
                                super.getCrudScheduleRepository().save(schedule);

                            }*/

                            int repeatTerm = Integer.parseInt(dto.getRepeat().getYearTypeVO().getRepeatTerm());

                            while (!repeatDate.isAfter(endLine)) {
                                repeatDate = yearScheduleFunc.parseMonthlyDate(repeatDate.getYear(), parseMonth, weekValue, dayOfWeek);
//                                repeatDate = yearScheduleFunc.parseMonthlyLastDate(currentDate.plusYears(repeatTerm), parseMonth, dayOfWeek);

                                if (repeatDate.isAfter(endLine)) {
                                    break;
                                }

                                log.info("다음 년도의 조건에 해당하는 date: {}", repeatDate);

                                Schedule schedule = Schedule.builder()
                                        .userId(dto.getUserId())
                                        .eventName(dto.getEventName())
                                        .category(dto.getCategory())
                                        .startDate(repeatDate.toString())
                                        .endDate(repeatDate.toString())
                                        .startTime(dto.getStartTime())
                                        .endTime(dto.getEndTime())
                                        .isAllDay(dto.isAllDay())
                                        .repeatKind(RepeatKind.YEAR.name())
                                        .repeatOptions(UnitedType.builder()
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(template);
                                super.getCrudScheduleRepository().save(schedule);

                                repeatDate = repeatDate.plusYears(repeatTerm);
                                log.info("next repeatDate : {}", repeatDate);
                            }
                        }


                    } else if (yearCategory.equals(YearCategory.LastDayOfMonth.toString())) {
                        if (dto.getPeriod().isRepeatAgain()) {

                            log.info("LastDayOfMonth case 1");

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

                                    log.info("* 중요 저장될 date:{}", currentDate);

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
                                                    .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                    .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                    .build()).isExclude(dto.isExclude())
                                            .paymentType(paymentType)
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
                                            .regularType(RegularType.REGULAR)
                                            .build();

                                    schedule.setTemplate(template);
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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(template);
                                super.getCrudScheduleRepository().save(schedule);
                            }
                        } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {

                            log.info("LastDayOfMonth case 2");
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
                                                    .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                    .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                    .build()).isExclude(dto.isExclude())
                                            .paymentType(paymentType)
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
                                            .regularType(RegularType.REGULAR)
                                            .build();

                                    schedule.setTemplate(template);
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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(template);
                                super.getCrudScheduleRepository().save(schedule);
                            }
                        } else if (dto.getPeriod().getRepeatEndLine() != null) {

                            log.info("LastDayOfMonth case 3");

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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(template);
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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(template);
                                super.getCrudScheduleRepository().save(schedule);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                return null;
            }
        }

        else {
            try {
                boolean isDifferent = isDuplicatedSaveSchedule(dto);

                String dtoPaymentType = dto.getPaymentType();
                PaymentType paymentType;

                if (dtoPaymentType.equals(PaymentType.ACCOUNT.name())) {
                    paymentType = PaymentType.ACCOUNT;
                } else if (dtoPaymentType.equals(PaymentType.CASH.name())) {
                    paymentType = PaymentType.CASH;
                } else {
                    paymentType = PaymentType.CARD;
                }

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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build())
                                        .isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(null);
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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(null);
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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build())
                                        .isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(null);
                                super.getCrudScheduleRepository().save(schedule);

                                currentDate = currentDate.plusYears(Long.parseLong(dto.getRepeat().getYearTypeVO().getRepeatTerm()));
                            }
                        }
                    }

                    //  MM월 N번째 D요일
                    else if (yearCategory.equals(YearCategory.NthDayOfMonth.toString())) {
                        if (dto.getPeriod().isRepeatAgain()) {
                            LocalDate currentDate = formatDate(dto.getStartDate());

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

                            LocalDate repeatDate = yearScheduleFunc.parseMonthlyDate(currentDate.getYear(), parseMonth, weekValue, dayOfWeek);

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
                                                    .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                    .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                    .build()).isExclude(dto.isExclude())
                                            .paymentType(paymentType)
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
                                            .regularType(RegularType.REGULAR)
                                            .build();

                                    schedule.setTemplate(null);
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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(null);
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

                            LocalDate repeatDate = yearScheduleFunc.parseMonthlyDate(currentDate.getYear(), parseMonth, weekValue, dayOfWeek);

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
                                                    .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                    .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                    .build()).isExclude(dto.isExclude())
                                            .paymentType(paymentType)
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
                                            .regularType(RegularType.REGULAR)
                                            .build();

                                    schedule.setTemplate(null);
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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(null);
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

                            LocalDate repeatDate = yearScheduleFunc.parseMonthlyDate(currentDate.getYear(), parseMonth, weekValue, dayOfWeek);

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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(null);
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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(null);
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
                                                    .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                    .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                    .build()).isExclude(dto.isExclude())
                                            .paymentType(paymentType)
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
                                            .regularType(RegularType.REGULAR)
                                            .build();

                                    schedule.setTemplate(null);
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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(null);
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
                                                    .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                    .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                    .build()).isExclude(dto.isExclude())
                                            .paymentType(paymentType)
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
                                            .regularType(RegularType.REGULAR)
                                            .build();

                                    schedule.setTemplate(null);
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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(null);
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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(null);
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
                                                .term(dto.getRepeat().getYearTypeVO().getRepeatTerm())
                                                .options(dto.getRepeat().getYearTypeVO().getYearCategory())
                                                .build()).isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(null);
                                super.getCrudScheduleRepository().save(schedule);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                return null;
            }
        }
        return true;
    }
}
