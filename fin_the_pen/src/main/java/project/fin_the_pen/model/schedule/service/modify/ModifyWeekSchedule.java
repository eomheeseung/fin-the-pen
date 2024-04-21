package project.fin_the_pen.model.schedule.service.modify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.PaymentType;
import project.fin_the_pen.model.schedule.entity.type.RepeatKind;
import project.fin_the_pen.model.schedule.entity.type.UnitedType;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Component
@Slf4j
public class ModifyWeekSchedule extends ModifySchedule implements ModifyXXXFunc {
    public ModifyWeekSchedule(CrudScheduleRepository crudScheduleRepository) {
        super(crudScheduleRepository);
    }

    @Override
    public void modifySchedule(ModifyScheduleDTO dto) {
        StringTokenizer tokenizer =
                new StringTokenizer(dto.getRepeat().getWeekTypeVO().getRepeatDayOfWeek(), ",");

        int intervalWeeks = Integer.parseInt(dto.getRepeat().getWeekTypeVO().getRepeatTerm());

        List<String> days = new ArrayList<>();

        String dtoPaymentType = dto.getPaymentType();
        PaymentType paymentType;

        if (dtoPaymentType.equals(PaymentType.ACCOUNT.name())) {
            paymentType = PaymentType.ACCOUNT;
        } else if (dtoPaymentType.equals(PaymentType.CASH.name())) {
            paymentType = PaymentType.CASH;
        } else{
            paymentType = PaymentType.CARD;
        }

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

            /*
             week logic 1 => 반복횟수가 없이 default(50)으로 저장
             */
            for (int i = 0; i < endRepeat; i++) {
                String targetDay = criteriaDate.getDayOfWeek().toString();

                /*WeekType bindingWeekType = new WeekType();
                bindingWeekType.setDayOfWeek(criteriaDate.getDayOfWeek().toString());
                bindingWeekType.setValue(dto.getRepeat().getWeekTypeVO().getValue());

                TypeManage typeManage =
                        TypeManage.builder()
                                .weekType(bindingWeekType)
                                .build();*/

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
                            .repeatKind(RepeatKind.WEEK.name())
                            .repeatOptions(UnitedType.builder()
                                    .term(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                    .options(criteriaDate.getDayOfWeek().toString())
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
                            .build();

                    getCrudScheduleRepository().save(schedule);
                    log.info("저장된 요일: {}", schedule.getRepeatOptions());

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

            int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());
            int repeatValue = repeatNumberOfTime * intervalWeeks;

            for (int i = 0; i < repeatValue; i++) {
                String targetDay = criteriaDate.getDayOfWeek().toString();

                if (days.contains(targetDay)) {
                    log.info("이동하는 요일: {}", targetDay);
                    log.info("일자: {}", criteriaDate);

                    /*WeekType bindingWeekType = new WeekType();
                    bindingWeekType.setDayOfWeek(criteriaDate.getDayOfWeek().toString());
                    bindingWeekType.setValue(dto.getRepeat().getWeekTypeVO().getValue());

                    TypeManage typeManage =
                            TypeManage.builder()
                                    .weekType(bindingWeekType)
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
                            .repeatKind(RepeatKind.WEEK.name())
                            .repeatOptions(UnitedType.builder()
                                    .term(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                    .options(criteriaDate.getDayOfWeek().toString())
                                    .build()).isExclude(dto.isExclude())
                            .paymentType(paymentType)
                            .amount(dto.getAmount())
                            .isFixAmount(dto.isFixAmount())
                            .period(createPeriodType(() -> {
                                return PeriodType.builder()
                                        .isRepeatAgain(false)
                                        .repeatNumberOfTime(dto.getRepeat().getWeekTypeVO().getRepeatTerm())
                                        .repeatEndLine(endLine.toString()).build();
                            }))
                            .priceType(judgmentPriceType(() -> {
                                if (dto.getPriceType().equals(PriceType.Plus)) {
                                    return PriceType.Plus;
                                } else return PriceType.Minus;
                            }))
                            .build();

                    getCrudScheduleRepository().save(schedule);

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

            while (!criteriaDate.isAfter(endLine)) {
                String targetDay = criteriaDate.getDayOfWeek().toString();

                // 현재 날짜의 요일과 targetDay가 일치하면 스케줄 생성
                if (days.contains(targetDay)) {
                    log.info("이동하는 요일: {}", targetDay);
                    log.info("일자: {}", criteriaDate);

                    /*WeekType bindingWeekType = new WeekType();
                    bindingWeekType.setDayOfWeek(criteriaDate.getDayOfWeek().toString());
                    bindingWeekType.setValue(dto.getRepeat().getWeekTypeVO().getValue());

                    TypeManage typeManage =
                            TypeManage.builder()
                                    .weekType(bindingWeekType)
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
                            .repeatKind(RepeatKind.WEEK.name())
                            .repeatOptions(UnitedType.builder()
                                    .term(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                    .options(criteriaDate.getDayOfWeek().toString())
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
                            .build();

                    getCrudScheduleRepository().save(schedule);

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
}
