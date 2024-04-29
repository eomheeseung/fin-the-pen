package project.fin_the_pen.model.schedule.service.register;

import org.springframework.stereotype.Component;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.PaymentType;
import project.fin_the_pen.model.schedule.entity.type.RepeatKind;
import project.fin_the_pen.model.schedule.entity.type.UnitedType;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.schedule.type.RegularType;

import java.util.Optional;

@Component
public class RegisterNoneSchedule extends RegisterSchedule implements RegisterXXXFunc {
    public RegisterNoneSchedule(CrudScheduleRepository crudScheduleRepository) {
        super(crudScheduleRepository);
    }

    private Optional<Schedule> getSchedule(String userId, String eventName, String category) {
        CrudScheduleRepository crudScheduleRepository = getCrudScheduleRepository();
        Optional<Schedule> findBeforeSaveSchedule =
                crudScheduleRepository.findByUserIdAndEventNameAndCategory(userId, eventName, category);

        if (findBeforeSaveSchedule.isEmpty()) {
            return Optional.empty();
        } else {
            Optional<Schedule> regularSchedule = findBeforeSaveSchedule
                    .filter(schedule -> schedule.getRegularType().equals(RegularType.REGULAR))
                    .stream()
                    .findFirst();

            return regularSchedule.or(Optional::empty);
        }
    }

    /**
     * 반복이 아닐 때 (단일일정)
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean registerSchedule(ScheduleRequestDTO dto) {
        String userId = dto.getUserId();
        String eventName = dto.getEventName();
        String category = dto.getCategory();

        try {
            boolean isDifferent = super.isDuplicatedSaveSchedule(dto);

            if (!isDifferent) {
                throw new DuplicatedScheduleException("중복된 일정 등록입니다.");
            }

            String dtoPaymentType = dto.getPaymentType();

            PaymentType paymentType;

            if (dtoPaymentType.equals(PaymentType.ACCOUNT.name())) {
                paymentType = PaymentType.ACCOUNT;
            } else if (dtoPaymentType.equals(PaymentType.CASH.name())) {
                paymentType = PaymentType.CASH;
            } else {
                paymentType = PaymentType.CARD;
            }

            Optional<Schedule> optionalSchedule = getSchedule(userId, eventName, category);

            if (optionalSchedule.isEmpty()) {
                Schedule schedule = Schedule.builder()
                        .userId(dto.getUserId())
                        .eventName(dto.getEventName())
                        .category(dto.getCategory())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .isAllDay(dto.isAllDay())
                        .repeatKind(RepeatKind.NONE.toString())
                        .repeatOptions(UnitedType.builder().term("0")
                                .options("none")
                                .build())
                        .isExclude(dto.isExclude())
                        .paymentType(paymentType)
                        .amount(dto.getAmount())
                        .isFixAmount(dto.isFixAmount())
                        .period(createPeriodType(() -> {
                            return PeriodType.builder()
                                    .isRepeatAgain(false)
                                    .repeatNumberOfTime("none")
                                    .repeatEndLine("none").build();
                        }))
                        .priceType(super.judgmentPriceType(() -> {
                            if (dto.getPriceType().equals(PriceType.Plus)) {
                                return PriceType.Plus;
                            } else return PriceType.Minus;
                        }))
                        .regularType(RegularType.EACH)
                        .build();

                super.getCrudScheduleRepository().save(schedule);
            } else {
                Schedule findSchedule = optionalSchedule.get();

                Schedule schedule = Schedule.builder()
                        .userId(findSchedule.getUserId())
                        .eventName(findSchedule.getEventName())
                        .category(findSchedule.getCategory())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .isAllDay(dto.isAllDay())
                        .repeatKind(findSchedule.getRepeatKind())
                        .repeatOptions(findSchedule.getRepeatOptions())
                        .isExclude(dto.isExclude())
                        .paymentType(paymentType)
                        .amount(dto.getAmount())
                        .isFixAmount(dto.isFixAmount())
                        .period(findSchedule.getPeriod())
                        .priceType(super.judgmentPriceType(() -> {
                            if (dto.getPriceType().equals(PriceType.Plus)) {
                                return PriceType.Plus;
                            } else return PriceType.Minus;
                        }))
                        .regularType(findSchedule.getRegularType())
                        .build();

                super.getCrudScheduleRepository().save(schedule);
            }
        } catch (RuntimeException e) {
            return null;
        }
        return true;
    }
}
