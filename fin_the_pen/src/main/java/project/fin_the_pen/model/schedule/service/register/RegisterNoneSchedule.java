package project.fin_the_pen.model.schedule.service.register;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.NoneType;
import project.fin_the_pen.model.schedule.entity.type.TypeManage;
import project.fin_the_pen.model.schedule.repository.CRUDScheduleRepository;
import project.fin_the_pen.model.schedule.repository.ScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.util.List;

@Component
public class RegisterNoneSchedule extends RegisterSchedule implements RegisterXXXFunc{
    public RegisterNoneSchedule(CRUDScheduleRepository crudScheduleRepository) {
        super(crudScheduleRepository);
    }
    /**
     * 반복이 아닐 때 (단일일정)
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean registerSchedule(ScheduleRequestDTO dto) {
        try {
            boolean isDifferent = super.isDuplicatedSaveSchedule(dto);

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
                        .period(createPeriodType(() -> {
                            return PeriodType.builder()
                                    .isRepeatAgain(false)
                                    .repeatNumberOfTime("0")
                                    .repeatEndLine(null).build();
                        }))
                        .priceType(super.judgmentPriceType(() -> {
                            if (dto.getPriceType().equals(PriceType.Plus)) {
                                return PriceType.Plus;
                            } else return PriceType.Minus;
                        }))
                        .build();

                super.getCrudScheduleRepository().save(schedule);
            }
        } catch (RuntimeException e) {
            return null;
        }
        return true;
    }
}
