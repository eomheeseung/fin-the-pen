package project.fin_the_pen.finClient.dev;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.RepeatKind;
import project.fin_the_pen.model.schedule.entity.type.UnitedType;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DevService {
    private final CrudScheduleRepository repository;

    public HttpStatus modify(DevModifyDto dto) {
        Long scheduleId = Long.valueOf(dto.getScheduleId());

        String userId = dto.getUserId();
        String eventName = dto.getEventName();
        String startDate = dto.getStartDate();
        String endDate = dto.getEndDate();
        String startTime = dto.getStartTime();
        String endTime = dto.getEndTime();
        String category = dto.getCategory();
        String amount = dto.getAmount();
        boolean allDay = dto.isAllDay();
        PriceType priceType = dto.getPriceType();
        boolean exclude = dto.isExclude();
        String dtoPaymentType = dto.getPaymentType();
        boolean fixAmount = dto.isFixAmount();

        String kindType = dto.getRepeat().getKindType();

        RepeatKind repeatKind;

        switch (kindType) {
            case "WEEK":
                repeatKind = RepeatKind.WEEK;
                break;
            case "DAY":
                repeatKind = RepeatKind.DAY;
                break;
            case "YEAR":
                repeatKind = RepeatKind.YEAR;
                break;
            case "MONTH":
                repeatKind = RepeatKind.MONTH;
                break;
            default:
                repeatKind = RepeatKind.NONE;
                break;
        }


        Optional<Schedule> findBySchedule = repository.findById(scheduleId);

        try {
            if (findBySchedule.isPresent()) {
                Schedule schedule = findBySchedule.get();

                UnitedType unitedType = UnitedType.builder().term(schedule.getRepeatOptions().getTerm())
                        .options(schedule.getRepeatOptions().getOptions())
                        .build();

                PeriodType periodType = PeriodType.builder()
                        .isRepeatAgain(schedule.getPeriod().getIsRepeatAgain())
                        .repeatNumberOfTime(schedule.getPeriod().getRepeatNumberOfTime())
                        .repeatEndLine(schedule.getPeriod().getRepeatEndLine())
                        .build();

                schedule.update(userId, eventName, category, startDate, endDate,
                        startTime, endTime, allDay, repeatKind.name(), unitedType, periodType,
                        priceType, exclude, dtoPaymentType, amount, fixAmount,schedule.getRegularType());

                repository.save(schedule);
            }
        } catch (RuntimeException e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.OK;
    }

}
