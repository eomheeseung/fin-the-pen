package project.fin_the_pen.model.schedule.service.register;

import org.springframework.stereotype.Component;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.RepeatKind;
import project.fin_the_pen.model.schedule.entity.type.UnitedType;
import project.fin_the_pen.model.schedule.repository.CRUDScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.LocalDate;

@Component
public class RegisterDaySchedule extends RegisterSchedule implements RegisterXXXFunc{
    public RegisterDaySchedule(CRUDScheduleRepository crudScheduleRepository) {
        super(crudScheduleRepository);
    }

    @Override
    public Boolean registerSchedule(ScheduleRequestDTO dto) {
        try {
            boolean isDifferent = isDuplicatedSaveSchedule(dto);

            if (!isDifferent) {
                throw new DuplicatedScheduleException("중복된 일정 등록입니다.");
            } else {

                int intervalDays = Integer.parseInt(dto.getRepeat().getDayTypeVO().getValue());
                LocalDate currentDate = formatDate(dto.getStartDate());

                if (dto.getPeriod().isRepeatAgain()) {
                    for (int i = 0; i < 50; i++) {
                        Schedule schedule = Schedule.builder()
                                .userId(dto.getUserId())
                                .eventName(dto.getEventName())
                                .category(dto.getCategory())
                                .startDate(currentDate.toString())  // 수정된 부분
                                .endDate(currentDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeatKind(RepeatKind.DAY.toString())
                                .repeatOptions(UnitedType.builder().value(dto.getRepeat().getDayTypeVO().getValue()).build())
                                .isExclude(dto.isExclude())
                                .importance(dto.getImportance())
                                .amount(dto.getAmount())
                                .isFixAmount(dto.isFixAmount())
                                .period(createPeriodType(() -> {
                                    return PeriodType.builder()
                                            .isRepeatAgain(true)
                                            .repeatNumberOfTime("0")
                                            .repeatEndLine(null)
                                            .build();
                                }))
                                .priceType(judgmentPriceType(() -> {
                                    if (dto.getPriceType().equals(PriceType.Plus)) {
                                        return PriceType.Plus;
                                    } else return PriceType.Minus;
                                }))
                                .build();

                        super.getCrudScheduleRepository().save(schedule);

                        currentDate = currentDate.plusDays(intervalDays);
                    }
                } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
                    int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());

                    for (int i = 0; i < repeatNumberOfTime; i++) {
                        Schedule schedule = Schedule.builder()
                                .userId(dto.getUserId())
                                .eventName(dto.getEventName())
                                .category(dto.getCategory())
                                .startDate(currentDate.toString())  // 수정된 부분
                                .endDate(currentDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeatKind(RepeatKind.DAY.toString())
                                .repeatOptions(UnitedType.builder().value(dto.getRepeat().getDayTypeVO().getValue()).build())
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

                        super.getCrudScheduleRepository().save(schedule);

                        currentDate = currentDate.plusDays(intervalDays);
                    }
                } else if (dto.getPeriod().getRepeatEndLine() != null) {

                    LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());
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
                                .repeatKind(RepeatKind.DAY.toString())
                                .repeatOptions(UnitedType.builder().value(dto.getRepeat().getDayTypeVO().getValue()).build())
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

                        super.getCrudScheduleRepository().save(schedule);

                        currentDate = currentDate.plusDays(intervalDays);
                    }
                }
            }
        } catch (RuntimeException e) {
            return null;
        }
        return true;
    }
}
