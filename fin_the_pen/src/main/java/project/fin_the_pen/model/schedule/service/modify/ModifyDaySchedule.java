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

import java.time.LocalDate;

@Component
@Slf4j
public class ModifyDaySchedule extends ModifySchedule implements ModifyXXXFunc{
    public ModifyDaySchedule(CrudScheduleRepository crudScheduleRepository) {
        super(crudScheduleRepository);
    }

    @Override
    public void modifySchedule(ModifyScheduleDTO dto) {
        /*DayType bindingDayType = new DayType();
        bindingDayType.setValue(dto.getRepeat().getDayTypeVO().getValue());

        TypeManage typeManage = TypeManage.builder()
                .dayType(bindingDayType)
                .build();*/

        int intervalDays = Integer.parseInt(dto.getRepeat().getDayTypeVO().getRepeatTerm());
        LocalDate criteriaDate = formatDate(dto.getStartDate());
        int endRepeat = 50;

        String dtoPaymentType = dto.getPaymentType();
        PaymentType paymentType;

        if (dtoPaymentType.equals(PaymentType.ACCOUNT.name())) {
            paymentType = PaymentType.ACCOUNT;
        } else if (dtoPaymentType.equals(PaymentType.CASH.name())) {
            paymentType = PaymentType.CASH;
        } else{
            paymentType = PaymentType.CARD;
        }

        // day 1 logic
        if (dto.getPeriod().isRepeatAgain()) {
            for (int i = 0; i < endRepeat; i++) {
                Schedule schedule = Schedule.builder()
                        .userId(dto.getUserId())
                        .eventName(dto.getEventName())
                        .category(dto.getCategory())
                        .startDate(criteriaDate.toString())
                        .endDate(criteriaDate.toString())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .isAllDay(dto.isAllDay())
                        .repeatKind(RepeatKind.DAY.toString())
                        .repeatOptions(UnitedType.builder()
                                .term(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                .build()
                        )
                        .isExclude(dto.isExclude())
                        .paymentType(paymentType)
                        .amount(dto.getAmount())
                        .isFixAmount(dto.isFixAmount())
                        .period(createPeriodType(() -> PeriodType.builder()
                                .isRepeatAgain(true)
                                .repeatNumberOfTime("0")
                                .repeatEndLine(null)
                                .build()))
                        .priceType(judgmentPriceType(() -> dto.getPriceType().equals(PriceType.Plus) ? PriceType.Plus : PriceType.Minus))
                        .build();

                // entities 리스트를 모두 순회한 경우
                log.info("나머지 저장");
                log.info(schedule.getStartDate());
                getCrudScheduleRepository().save(schedule);

                criteriaDate = criteriaDate.plusDays(intervalDays);
            }
        } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
            int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());

            for (int i = 0; i < repeatNumberOfTime; i++) {
                Schedule schedule = Schedule.builder()
                        .userId(dto.getUserId())
                        .eventName(dto.getEventName())
                        .category(dto.getCategory())
                        .startDate(criteriaDate.toString())  // 수정된 부분
                        .endDate(criteriaDate.toString())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .isAllDay(dto.isAllDay())
                        .repeatKind(RepeatKind.DAY.toString())
                        .repeatOptions(UnitedType.builder()
                                .term(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                .build())
                        .isExclude(dto.isExclude())
                        .paymentType(paymentType)
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
                // entities 리스트를 모두 순회한 경우
                log.info("나머지 저장");
                log.info(schedule.getStartDate());
                getCrudScheduleRepository().save(schedule);
                criteriaDate = criteriaDate.plusDays(intervalDays);
            }

            // day 3 logic
        } else if (dto.getPeriod().getRepeatEndLine() != null) {
            LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());

            while (!criteriaDate.isAfter(endLine)) {
                Schedule schedule = Schedule.builder()
                        .userId(dto.getUserId())
                        .eventName(dto.getEventName())
                        .category(dto.getCategory())
                        .startDate(criteriaDate.toString())  // 수정된 부분
                        .endDate(criteriaDate.toString())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .isAllDay(dto.isAllDay())
                        .repeatKind(RepeatKind.DAY.toString())
                        .repeatOptions(UnitedType.builder()
                                .term(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                .build())
                        .isExclude(dto.isExclude())
                        .paymentType(paymentType)
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

                criteriaDate = criteriaDate.plusDays(intervalDays);
            }
        }
    }
}
