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
import project.fin_the_pen.model.schedule.template.Template;
import project.fin_the_pen.model.schedule.template.TemplateRepository;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.schedule.type.RegularType;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class RegisterDaySchedule extends RegisterSchedule implements RegisterXXXFunc {
    public RegisterDaySchedule(CrudScheduleRepository crudScheduleRepository,
                               TemplateRepository templateRepository) {
        super(crudScheduleRepository, templateRepository);
    }

    @Override
    public Boolean registerSchedule(ScheduleRequestDTO dto) {
        String userId = dto.getUserId();
        String category = dto.getCategory();
        String eventName = dto.getEventName();

        boolean isDuplicated = isDuplicatedRegular(userId, category, eventName);

        // template를 설정 and 저장된 템플릿이 없는경우 생성해서 넣기
        if (dto.isTemplate() && isDuplicatedTemplate(userId, category, eventName)) {
            Template template = Template.builder()
                    .userId(userId)
                    .templateName(eventName)
                    .categoryName(category)
                    .build();

            template.init();

            getTemplateRepository().save(template);
        }

        // template를 사용하고 이미 템플릿이 존재한다면 -> 템플릿을 찾아서 그냥 넣어주기만 하면 된다.
        else if (dto.isTemplate() && !isDuplicatedTemplate(userId, category, eventName)) {
            Optional<Template> optionalTemplate =
                    getTemplateRepository().findByUserIdAndTemplateNameAndCategoryName(userId, category, eventName);

            if (optionalTemplate.isEmpty()) {
                throw new RuntimeException();
            }

            if (!isDuplicated) {
                throw new DuplicatedScheduleException("정기 일정 등록시 중복됩니다.");
            } else {
                try {
                    Template template = optionalTemplate.get();

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

                        int intervalDays = Integer.parseInt(dto.getRepeat().getDayTypeVO().getRepeatTerm());
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
                                        .repeatOptions(UnitedType.builder()
                                                .term(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                                .options("none")
                                                .build())
                                        .isExclude(dto.isExclude())
                                        .paymentType(paymentType)
                                        .amount(dto.getAmount())
                                        .isFixAmount(dto.isFixAmount())
                                        .period(createPeriodType(() -> {
                                            return PeriodType.builder()
                                                    .isRepeatAgain(true)
                                                    .repeatNumberOfTime("0")
                                                    .repeatEndLine("none")
                                                    .build();
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
                                        .repeatOptions(UnitedType.builder()
                                                .options("none")
                                                .term(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                                .build())
                                        .isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(template);
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
                                        .repeatOptions(UnitedType.builder().term(dto.getRepeat().getDayTypeVO().getRepeatTerm())
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
                                                    .repeatEndLine(endLine.toString())
                                                    .build();
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

                                currentDate = currentDate.plusDays(intervalDays);
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    return null;
                }
            }
        }
        // template를 사용하지 않는다면
        else if (!dto.isTemplate() && !isDuplicatedTemplate(userId, category, eventName)) {
            if (!isDuplicated) {
                throw new DuplicatedScheduleException("정기 일정 등록시 중복됩니다.");
            } else {
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

                        int intervalDays = Integer.parseInt(dto.getRepeat().getDayTypeVO().getRepeatTerm());
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
                                        .repeatOptions(UnitedType.builder()
                                                .term(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                                .options("none")
                                                .build())
                                        .isExclude(dto.isExclude())
                                        .paymentType(paymentType)
                                        .amount(dto.getAmount())
                                        .isFixAmount(dto.isFixAmount())
                                        .period(createPeriodType(() -> {
                                            return PeriodType.builder()
                                                    .isRepeatAgain(true)
                                                    .repeatNumberOfTime("0")
                                                    .repeatEndLine("none")
                                                    .build();
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
                                        .repeatOptions(UnitedType.builder()
                                                .options("none")
                                                .term(dto.getRepeat().getDayTypeVO().getRepeatTerm())
                                                .build())
                                        .isExclude(dto.isExclude())
                                        .paymentType(paymentType)
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
                                        .regularType(RegularType.REGULAR)
                                        .build();

                                schedule.setTemplate(null);
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
                                        .repeatOptions(UnitedType.builder().term(dto.getRepeat().getDayTypeVO().getRepeatTerm())
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
                                                    .repeatEndLine(endLine.toString())
                                                    .build();
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

                                currentDate = currentDate.plusDays(intervalDays);
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    return null;
                }
            }
        }
        return true;
    }
}
