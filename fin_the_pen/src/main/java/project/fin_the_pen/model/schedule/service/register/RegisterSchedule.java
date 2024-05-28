package project.fin_the_pen.model.schedule.service.register;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.template.Template;
import project.fin_the_pen.model.schedule.template.TemplateRepository;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.schedule.type.RegularType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Getter
public abstract class RegisterSchedule {
    private final CrudScheduleRepository crudScheduleRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final TemplateRepository templateRepository;

    @NotNull
    public LocalDate formatDate(String convertDate) {
        return LocalDate.parse(convertDate, formatter);
    }

    // 중복되는 일정이 등록되는지 검사하는 method
    public boolean isDuplicatedSaveSchedule(ScheduleRequestDTO dto) {
        List<Schedule> allSchedule = findAllSchedule(dto.getUserId());

        return allSchedule.stream().noneMatch(it ->
                it.getUserId().equals(dto.getUserId()) &&
                        it.getEventName().equals(dto.getEventName()) &&
                        it.getCategory().equals(dto.getCategory()) &&
                        it.getStartDate().equals(dto.getStartDate()) &&
                        it.getEndDate().equals(dto.getEndDate()) &&
                        it.getStartTime().equals(dto.getStartTime()) &&
                        it.getEndTime().equals(dto.getEndTime()));
    }

    public List<Schedule> findAllSchedule(String userId) {
        return crudScheduleRepository.findByUserId(userId);
    }

    public PriceType judgmentPriceType(Supplier<PriceType> supplier) {
        return supplier.get();
    }

    public PeriodType createPeriodType(Supplier<PeriodType> supplier) {
        return supplier.get();
    }

    public boolean isDuplicatedRegular(String userId, String eventName, String category) {
        Optional<Schedule> optionalSchedule =
                crudScheduleRepository
                        .findByUserIdAndEventNameAndCategory(userId, eventName, category);

        Optional<Schedule> findSchedule = optionalSchedule
                .filter(schedule -> schedule.getRegularType().equals(RegularType.REGULAR))
                .stream()
                .findFirst();

        if (findSchedule.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isDuplicatedTemplate(String userId, String eventName, String category) {
        Optional<Template> optionalTemplate =
                templateRepository.findByUserIdAndTemplateNameAndCategoryName(userId, eventName, category);

        if (optionalTemplate.isEmpty()) {
            return true;
        } else return false;
    }

    /*
     우선 템플릿의 이름은 일정의 이름과 동일하게 설정함.
     템플릿을 따로 설정하는 곳이 있어야 함.
     */
    public Template createTemplate(String userId, String category, String eventName){
        Template template = null;

        // 중복된 템플릿이 없는 경우 생성해서
        if (isDuplicatedTemplate(userId, category, eventName)) {
            template = Template.builder()
                    .userId(userId)
                    .templateName(eventName)
                    .categoryName(category)
                    .build();

            template.init();
            getTemplateRepository().save(template);
            return template;

            // 중복된 템플릿이 있다면 이미 저장된 것이므로 DB에서 가져와서 설정해줌
        } else {
            Optional<Template> optionalTemplate =
                    getTemplateRepository().findByUserIdAndTemplateNameAndCategoryName(userId, category, eventName);

            if (optionalTemplate.isEmpty()) {
                throw new RuntimeException();
            } else {
                template = optionalTemplate.get();
            }
        }

        return template;
    }
}
