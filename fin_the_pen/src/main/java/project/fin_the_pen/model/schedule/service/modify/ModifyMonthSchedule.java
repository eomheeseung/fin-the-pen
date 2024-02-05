package project.fin_the_pen.model.schedule.service.modify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.RepeatKind;
import project.fin_the_pen.model.schedule.entity.type.UnitedType;
import project.fin_the_pen.model.schedule.repository.CRUDScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Component
@Slf4j
public class ModifyMonthSchedule extends ModifySchedule implements ModifyXXXFunc{

    public ModifyMonthSchedule(CRUDScheduleRepository crudScheduleRepository) {
        super(crudScheduleRepository);
    }

    @Override
    public void modifySchedule(ModifyScheduleDTO dto) {
        StringTokenizer tokenizer =
                new StringTokenizer(dto.getRepeat().getMonthTypeVO().getSelectedDate(), ",");

        LocalDate criteriaDate = formatDate(dto.getStartDate());

        List<Integer> dates = new ArrayList<>();

        while (tokenizer.hasMoreTokens()) {
            int parseDate = Integer.parseInt(tokenizer.nextToken().trim());
            log.info("파싱된 date: {}", parseDate);
            dates.add(parseDate);
        }


        if (dto.getPeriod().isRepeatAgain()) {
            int endRepeat = 50;

            for (int i = 0; i < endRepeat; i++) {
                int dayOfMonth = criteriaDate.getDayOfMonth();

                log.info("현재 월에서의 날짜: {}", dayOfMonth);

                if (dates.contains(dayOfMonth)) {

                    /*MonthType bindingMonthType = new MonthType();
                    bindingMonthType.setMonthValue(dto.getRepeat().getMonthTypeVO().getValue());

                    log.info("*중요 save date: {}", criteriaDate);
                    TypeManage typeManage = TypeManage
                            .builder()
                            .monthType(bindingMonthType)
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
                                    .value(dto.getRepeat().getDayTypeVO().getValue())
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

                    criteriaDate = criteriaDate.plusDays(1);
                    log.info("이동된 날짜:{}", criteriaDate);


                    LocalDate lastDate = LocalDate.of(criteriaDate.getYear(), criteriaDate.getMonth(), 1)
                            .withDayOfMonth(LocalDate.of(criteriaDate.getYear(), criteriaDate.getMonth(), 1)
                                    .lengthOfMonth());
                    log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                    if (criteriaDate.equals(lastDate)) {
                        criteriaDate = criteriaDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getValue()));
                    }

                } else {
                    i--;
                    criteriaDate = criteriaDate.plusDays(1);
                    log.info("이동된 날짜:{}", criteriaDate);

                    LocalDate lastDate = LocalDate.of(criteriaDate.getYear(), criteriaDate.getMonth(), 1)
                            .withDayOfMonth(LocalDate.of(criteriaDate.getYear(), criteriaDate.getMonth(), 1)
                                    .lengthOfMonth());

                    log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                    if (criteriaDate.equals(lastDate)) {
                        criteriaDate = criteriaDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getValue()));
                    }
                }
            }
        } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
            int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());
            int repeatValue = repeatNumberOfTime * Integer.parseInt(dto.getRepeat().getMonthTypeVO().getValue());

            for (int i = 0; i < repeatValue; i++) {
                int dayOfMonth = criteriaDate.getDayOfMonth();

                log.info("현재 월에서의 날짜: {}", dayOfMonth);

                if (dates.contains(dayOfMonth)) {
                    /*MonthType bindingMonthType = new MonthType();
                    bindingMonthType.setMonthValue(dto.getRepeat().getMonthTypeVO().getValue());

                    log.info("*중요 save date: {}", criteriaDate);
                    TypeManage typeManage = TypeManage
                            .builder()
                            .monthType(bindingMonthType)
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
                                    .value(dto.getRepeat().getDayTypeVO().getValue())
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

                    criteriaDate = criteriaDate.plusDays(1);
                    log.info("이동된 날짜:{}", criteriaDate);


                    LocalDate lastDate = LocalDate.of(criteriaDate.getYear(), criteriaDate.getMonth(), 1)
                            .withDayOfMonth(LocalDate.of(criteriaDate.getYear(), criteriaDate.getMonth(), 1)
                                    .lengthOfMonth());
                    log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                    if (criteriaDate.equals(lastDate)) {
                        criteriaDate = criteriaDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getValue()));
                    }

                } else {
                    i--;
                    criteriaDate = criteriaDate.plusDays(1);
                    log.info("이동된 날짜:{}", criteriaDate);

                    LocalDate lastDate = LocalDate.of(criteriaDate.getYear(), criteriaDate.getMonth(), 1)
                            .withDayOfMonth(LocalDate.of(criteriaDate.getYear(), criteriaDate.getMonth(), 1)
                                    .lengthOfMonth());

                    log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                    if (criteriaDate.equals(lastDate)) {
                        criteriaDate = criteriaDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getValue()));
                    }
                }
            }
        } else if (dto.getPeriod().getRepeatEndLine() != null) {
            LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

            while (!criteriaDate.isAfter(endLine)) {
                int dayOfMonth = criteriaDate.getDayOfMonth();

                log.info("현재 월에서의 날짜: {}", dayOfMonth);

                if (dates.contains(dayOfMonth)) {
                    /*MonthType bindingMonthType = new MonthType();
                    bindingMonthType.setMonthValue(dto.getRepeat().getMonthTypeVO().getValue());

                    log.info("*중요 save date: {}", criteriaDate);
                    TypeManage typeManage = TypeManage
                            .builder()
                            .monthType(bindingMonthType)
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
                                    .value(dto.getRepeat().getDayTypeVO().getValue())
                                    .build())
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

                    getCrudScheduleRepository().save(schedule);

                    criteriaDate = criteriaDate.plusDays(1);
                    log.info("이동된 날짜:{}", criteriaDate);


                    LocalDate lastDate = LocalDate.of(criteriaDate.getYear(), criteriaDate.getMonth(), 1)
                            .withDayOfMonth(LocalDate.of(criteriaDate.getYear(), criteriaDate.getMonth(), 1)
                                    .lengthOfMonth());
                    log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                    if (criteriaDate.equals(lastDate)) {
                        criteriaDate = criteriaDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getValue()));
                    }

                } else {
                    criteriaDate = criteriaDate.plusDays(1);
                    log.info("이동된 날짜:{}", criteriaDate);

                    LocalDate lastDate = LocalDate.of(criteriaDate.getYear(), criteriaDate.getMonth(), 1)
                            .withDayOfMonth(LocalDate.of(criteriaDate.getYear(), criteriaDate.getMonth(), 1)
                                    .lengthOfMonth());

                    log.info("해당 하는 달의 마지막 날짜:{}", lastDate);

                    if (criteriaDate.equals(lastDate)) {
                        criteriaDate = criteriaDate.plusMonths(Long.parseLong(dto.getRepeat().getMonthTypeVO().getValue()));
                    }
                }
            }
        }
    }
}
