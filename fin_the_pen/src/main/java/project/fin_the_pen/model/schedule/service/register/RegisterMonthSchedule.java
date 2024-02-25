package project.fin_the_pen.model.schedule.service.register;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.RepeatKind;
import project.fin_the_pen.model.schedule.entity.type.UnitedType;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Component
@Slf4j
public class RegisterMonthSchedule extends RegisterSchedule implements RegisterXXXFunc {
    public RegisterMonthSchedule(CrudScheduleRepository crudScheduleRepository) {
        super(crudScheduleRepository);
    }

    @Override
    public Boolean registerSchedule(ScheduleRequestDTO dto) {
        try {
            boolean isDifferent = isDuplicatedSaveSchedule(dto);

            if (!isDifferent) {
                throw new DuplicatedScheduleException("중복된 일정 등록입니다.");
            } else {
                StringTokenizer tokenizer =
                        new StringTokenizer(dto.getRepeat().getMonthTypeVO().getSelectedDate(), ",");

                LocalDate currentDate = formatDate(dto.getStartDate());
                LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

                List<Integer> dates = new ArrayList<>();

                if (tokenizer.countTokens() == 1) {
                    dates.add(Integer.parseInt(tokenizer.nextToken()));
                }
                while (tokenizer.hasMoreTokens()) {
                    int parseDate = Integer.parseInt(tokenizer.nextToken().trim());
                    log.info("파싱된 date: {}", parseDate);
                    dates.add(parseDate);
                }

                if (dto.getPeriod().isRepeatAgain()) {
                    for (int i = 0; i < 50; i++) {
                        int dayOfMonth = currentDate.getDayOfMonth();

                        log.info("현재 월에서의 날짜: {}", dayOfMonth);

                        if (dates.contains(dayOfMonth)) {
                            log.info("*중요 save date: {}", currentDate);

                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.MONTH.name())
                                    .repeatOptions(UnitedType.builder().value(dto.getRepeat().getMonthTypeVO().getRepeatTerm()).build())
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

                            currentDate = currentDate.plusDays(1);
                            log.info("이동된 날짜:{}", currentDate);


                            LocalDate lastDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                    .withDayOfMonth(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                            .lengthOfMonth());
                            log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                            if (currentDate.equals(lastDate)) {
                                currentDate = currentDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getRepeatTerm()));
                            }

                        } else {
                            i--;
                            currentDate = currentDate.plusDays(1);
                            log.info("이동된 날짜:{}", currentDate);

                            LocalDate lastDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                    .withDayOfMonth(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                            .lengthOfMonth());

                            log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                            if (currentDate.equals(lastDate)) {
                                currentDate = currentDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getRepeatTerm()));
                            }
                        }
                    }
                } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
                    int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());
                    int repeatValue = repeatNumberOfTime * Integer.parseInt(dto.getRepeat().getMonthTypeVO().getRepeatTerm());

                    for (int i = 0; i < repeatNumberOfTime; i++) {
                        for (Integer date : dates) {
                            try {
                                LocalDate tempDate = currentDate.withDayOfMonth(date);

                                if (!tempDate.isBefore(currentDate)) {
                                    getCrudScheduleRepository().save(
                                            Schedule.builder()
                                                    .userId(dto.getUserId())
                                                    .eventName(dto.getEventName())
                                                    .category(dto.getCategory())
                                                    .startDate(tempDate.toString())
                                                    .endDate(tempDate.toString())
                                                    .startTime(dto.getStartTime())
                                                    .endTime(dto.getEndTime())
                                                    .isAllDay(dto.isAllDay())
                                                    .repeatKind(RepeatKind.MONTH.name())
                                                    .repeatOptions(UnitedType.builder().value(dto.getRepeat().getMonthTypeVO().getRepeatTerm()).build())
                                                    .isExclude(dto.isExclude())
                                                    .importance(dto.getImportance())
                                                    .amount(dto.getAmount())
                                                    .isFixAmount(dto.isFixAmount())
                                                    .period(createPeriodType(() -> {
                                                        return PeriodType.builder()
                                                                .isRepeatAgain(false)
                                                                .repeatNumberOfTime(String.valueOf(repeatNumberOfTime))
                                                                .repeatEndLine("none")
                                                                .build();
                                                    }))
                                                    .priceType(judgmentPriceType(() -> {
                                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                                            return PriceType.Plus;
                                                        } else return PriceType.Minus;
                                                    })).build());
                                }
                            } catch (DateTimeException e) {
                                log.info("유효하지 않은 날짜입니다, 다음으로 넘어갑니다.");
                            }
                        }

                        currentDate = currentDate.plusMonths(Integer.parseInt(dto.getRepeat().getMonthTypeVO().getRepeatTerm()))
                                .withDayOfMonth(1);
                    }


                   /* for (int i = 0; i < repeatValue; i++) {
                        int dayOfMonth = currentDate.getDayOfMonth();

                        log.info("현재 월에서의 날짜: {}", dayOfMonth);

                        if (dates.contains(dayOfMonth)) {
                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.MONTH.name())
                                    .repeatOptions(UnitedType.builder().value(dto.getRepeat().getMonthTypeVO().getRepeatTerm()).build())
                                    .isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(false)
                                                .repeatNumberOfTime(String.valueOf(repeatNumberOfTime))
                                                .repeatEndLine("none")
                                                .build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);

                            currentDate = currentDate.plusDays(1);
                            log.info("이동된 날짜:{}", currentDate);

                            LocalDate lastDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                    .withDayOfMonth(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                            .lengthOfMonth());

                            log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                            currentDate = currentDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getRepeatTerm()));

                            if (currentDate.equals(lastDate)) {
                                currentDate = currentDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getRepeatTerm()));
                            }

                        } else {
                            i--;
                            currentDate = currentDate.plusDays(1);
                            log.info("이동된 날짜:{}", currentDate);

                            LocalDate lastDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                    .withDayOfMonth(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                            .lengthOfMonth());

                            log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                            if (currentDate.equals(lastDate)) {
                                currentDate = currentDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getRepeatTerm()));
                            }
                        }
                    }*/
                } else if (dto.getPeriod().getRepeatEndLine() != null) {
                    while (!currentDate.isAfter(endLine)) {
                        for (Integer date : dates) {
                            try {
                                LocalDate tempDate = currentDate.withDayOfMonth(date);

                                if (!tempDate.isBefore(currentDate)) {
                                    getCrudScheduleRepository().save(
                                            Schedule.builder()
                                                    .userId(dto.getUserId())
                                                    .eventName(dto.getEventName())
                                                    .category(dto.getCategory())
                                                    .startDate(tempDate.toString())
                                                    .endDate(tempDate.toString())
                                                    .startTime(dto.getStartTime())
                                                    .endTime(dto.getEndTime())
                                                    .isAllDay(dto.isAllDay())
                                                    .repeatKind(RepeatKind.MONTH.name())
                                                    .repeatOptions(UnitedType.builder().value(dto.getRepeat().getMonthTypeVO().getRepeatTerm()).build())
                                                    .isExclude(dto.isExclude())
                                                    .importance(dto.getImportance())
                                                    .amount(dto.getAmount())
                                                    .isFixAmount(dto.isFixAmount())
                                                    .period(createPeriodType(() -> {
                                                        return PeriodType.builder()
                                                                .isRepeatAgain(false)
                                                                .repeatNumberOfTime(String.valueOf("none"))
                                                                .repeatEndLine("none")
                                                                .build();
                                                    }))
                                                    .priceType(judgmentPriceType(() -> {
                                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                                            return PriceType.Plus;
                                                        } else return PriceType.Minus;
                                                    })).build());
                                }
                            } catch (DateTimeException e) {
                                log.info("유효하지 않은 날짜입니다, 다음으로 넘어갑니다.");
                            }
                        }

                        currentDate = currentDate.plusMonths(Integer.parseInt(dto.getRepeat().getMonthTypeVO().getRepeatTerm()))
                                .withDayOfMonth(1);
                    }



                    /*while (!currentDate.isAfter(endLine)) {
                        int dayOfMonth = currentDate.getDayOfMonth();

                        log.info("현재 월에서의 날짜: {}", dayOfMonth);

                        if (dates.contains(dayOfMonth)) {
                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.MONTH.name())
                                    .repeatOptions(UnitedType.builder().value(dto.getRepeat().getMonthTypeVO().getRepeatTerm()).build())
                                    .isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(false)
                                                .repeatNumberOfTime("none")
                                                .repeatEndLine(endLine.toString())
                                                .build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);

                            currentDate = currentDate.plusDays(1);
                            log.info("이동된 날짜:{}", currentDate);


                            LocalDate lastDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                    .withDayOfMonth(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                            .lengthOfMonth());
                            log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                            if (currentDate.equals(lastDate)) {
                                currentDate = currentDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getRepeatTerm()));
                            }

                        } else {
                            currentDate = currentDate.plusDays(1);
                            log.info("이동된 날짜:{}", currentDate);

                            LocalDate lastDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                    .withDayOfMonth(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)
                                            .lengthOfMonth());

                            log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                            if (currentDate.equals(lastDate)) {
                                currentDate = currentDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getRepeatTerm()));
                            }
                        }
                    }*/
                }
            }
        } catch (RuntimeException e) {
            return null;
        }
        return true;
    }
}
