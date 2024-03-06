package project.fin_the_pen.model.schedule.service.register;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.model.report.repository.ReportRepository;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.RepeatKind;
import project.fin_the_pen.model.schedule.entity.type.UnitedType;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Component
@Slf4j
public class RegisterWeekSchedule extends RegisterSchedule implements RegisterXXXFunc {
    private final ReportRepository reportRepository;

    public RegisterWeekSchedule(CrudScheduleRepository crudScheduleRepository,
                                ReportRepository reportRepository) {
        super(crudScheduleRepository);
        this.reportRepository = reportRepository;
    }

    @Override
    public Boolean registerSchedule(ScheduleRequestDTO dto) {
        try {
            boolean isDifferent = isDuplicatedSaveSchedule(dto);

            if (!isDifferent) {
                throw new DuplicatedScheduleException("중복된 일정 등록입니다.");
            } else {
                StringTokenizer tokenizer =
                        new StringTokenizer(dto.getRepeat().getWeekTypeVO().getRepeatDayOfWeek(), ",");

                int intervalWeeks = Integer.parseInt(dto.getRepeat().getWeekTypeVO().getRepeatTerm());
                // 문자열을 LocalDate 객체로 변환
                LocalDate startDate = formatDate(dto.getStartDate());
                LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

                List<String> days = new ArrayList<>();

                // 선택된 요일을 토큰화해서 list에 저장
                while (tokenizer.hasMoreTokens()) {
                    String temp = tokenizer.nextToken().trim();
                    days.add(temp);
                }

                // currentDate: 움직일 임시 객체
                LocalDate currentDate = startDate;

                if (dto.getPeriod().isRepeatAgain()) {
                    for (int i = 0; i < 50; i++) {
                        String targetDay = currentDate.getDayOfWeek().toString();

                        // 현재 날짜의 요일과 targetDay가 일치하면 스케줄 생성
                        if (days.contains(targetDay)) {
                            log.info("이동하는 요일: {}", targetDay);
                            log.info("일자: {}", currentDate);

                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.WEEK.toString())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                            .options(currentDate.getDayOfWeek().toString())
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
                            log.info("저장된 요일: {}", schedule.getRepeatOptions());

                            // java에서 한주의 끝은 SUN, 한주의 시작은 MON
                            if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                                currentDate = currentDate.plusWeeks(intervalWeeks);
                                currentDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                            } else currentDate = currentDate.plusDays(1);
                        } else {
                            i--;
                            if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                                currentDate = currentDate.plusWeeks(intervalWeeks);
                                currentDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                            } else currentDate = currentDate.plusDays(1);
                        }
                    }
                } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
                    int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());
                    LocalDate localDate = formatDate(dto.getStartDate());

                    log.info("요일 list size:{}", days.size());

                    for (int i = 0; i < repeatNumberOfTime; i++) {
                        for (String day : days) {
                            try {
                                LocalDate parseDate = localDate.with(DayOfWeek.valueOf(day)).plusWeeks(i * intervalWeeks);

                                if (parseDate.isBefore(localDate)) {
                                    continue;
                                }

                                getCrudScheduleRepository().save(
                                        Schedule.builder()
                                                .userId(dto.getUserId())
                                                .eventName(dto.getEventName())
                                                .category(dto.getCategory())
                                                .startDate(parseDate.toString())
                                                .endDate(parseDate.toString())
                                                .startTime(dto.getStartTime())
                                                .endTime(dto.getEndTime())
                                                .isAllDay(dto.isAllDay())
                                                .repeatKind(RepeatKind.WEEK.name())
                                                .repeatOptions(UnitedType.builder()
                                                        .value(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                                        .options(parseDate.getDayOfWeek().toString())
                                                        .build())
                                                .isExclude(dto.isExclude())
                                                .importance(dto.getImportance())
                                                .amount(dto.getAmount())
                                                .isFixAmount(dto.isFixAmount())
                                                .period(createPeriodType(() -> {
                                                    return PeriodType.builder()
                                                            .isRepeatAgain(false)
                                                            .repeatNumberOfTime(String.valueOf(repeatNumberOfTime))
                                                            .repeatEndLine(endLine.toString())
                                                            .build();
                                                }))
                                                .priceType(judgmentPriceType(() -> {
                                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                                        return PriceType.Plus;
                                                    } else return PriceType.Minus;
                                                }))
                                                .build());
                            } catch (DateTimeException e) {
                                log.info("유효하지 않은 날짜입니다.");
                            }
                        }
                    }
                } else if (dto.getPeriod().getRepeatEndLine() != null) {
                    LocalDate localDate = formatDate(dto.getStartDate());

                    while (!currentDate.isAfter(endLine)) {
                        for (String day : days) {
                            try {
                                LocalDate parseDate = currentDate.with(DayOfWeek.valueOf(day)).plusWeeks(intervalWeeks);

                                if (parseDate.isBefore(localDate)) {
                                    continue;
                                }

                                getCrudScheduleRepository().save(
                                        Schedule.builder()
                                                .userId(dto.getUserId())
                                                .eventName(dto.getEventName())
                                                .category(dto.getCategory())
                                                .startDate(parseDate.toString())
                                                .endDate(parseDate.toString())
                                                .startTime(dto.getStartTime())
                                                .endTime(dto.getEndTime())
                                                .isAllDay(dto.isAllDay())
                                                .repeatKind(RepeatKind.WEEK.name())
                                                .repeatOptions(UnitedType.builder()
                                                        .value(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                                        .options(parseDate.getDayOfWeek().toString())
                                                        .build())
                                                .isExclude(dto.isExclude())
                                                .importance(dto.getImportance())
                                                .amount(dto.getAmount())
                                                .isFixAmount(dto.isFixAmount())
                                                .period(createPeriodType(() -> {
                                                    return PeriodType.builder()
                                                            .isRepeatAgain(false)
                                                            .repeatNumberOfTime(String.valueOf("none"))
                                                            .repeatEndLine(endLine.toString())
                                                            .build();
                                                }))
                                                .priceType(judgmentPriceType(() -> {
                                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                                        return PriceType.Plus;
                                                    } else return PriceType.Minus;
                                                }))
                                                .build());
                                currentDate = parseDate;
                            } catch (DateTimeException e) {
                                log.info("유효하지 않은 날짜입니다.");
                            }
                        }

                        /*String targetDay = currentDate.getDayOfWeek().toString();

                        // 현재 날짜의 요일과 targetDay가 일치하면 스케줄 생성
                        if (days.contains(targetDay)) {
                            log.info("이동하는 요일: {}", targetDay);
                            log.info("일자: {}", currentDate);


                            Schedule schedule = Schedule.builder()
                                    .userId(dto.getUserId())
                                    .eventName(dto.getEventName())
                                    .category(dto.getCategory())
                                    .startDate(currentDate.toString())
                                    .endDate(currentDate.toString())
                                    .startTime(dto.getStartTime())
                                    .endTime(dto.getEndTime())
                                    .isAllDay(dto.isAllDay())
                                    .repeatKind(RepeatKind.WEEK.name())
                                    .repeatOptions(UnitedType.builder()
                                            .value(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                            .options(currentDate.getDayOfWeek().toString())
                                            .build())
                                    .isExclude(dto.isExclude())
                                    .importance(dto.getImportance())
                                    .amount(dto.getAmount())
                                    .isFixAmount(dto.isFixAmount())
                                    .period(createPeriodType(() -> {
                                        return PeriodType.builder()
                                                .isRepeatAgain(false)
                                                .repeatNumberOfTime("none")
                                                .repeatEndLine(endLine.toString()).build();
                                    }))
                                    .priceType(judgmentPriceType(() -> {
                                        if (dto.getPriceType().equals(PriceType.Plus)) {
                                            return PriceType.Plus;
                                        } else return PriceType.Minus;
                                    }))
                                    .build();

                            super.getCrudScheduleRepository().save(schedule);

                            // java에서 한주의 끝은 SUN, 한주의 시작은 MON
                            if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                                currentDate = currentDate.plusWeeks(intervalWeeks);
                                currentDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                            } else currentDate = currentDate.plusDays(1);
                            // TODO (수정) 일요일부터 시작하는 경우를 수정해야 함...
                        } else {
                            if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                                currentDate = currentDate.plusWeeks(intervalWeeks);
                                currentDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                            } else currentDate = currentDate.plusDays(1);
                        }*/
                    }
                }
            }
        } catch (
                Exception e) {
            return null;
        }
        return true;
    }

}
