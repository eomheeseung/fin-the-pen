package project.fin_the_pen.model.schedule.service.modify;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.template.Template;
import project.fin_the_pen.model.schedule.template.TemplateRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class ModifySchedule {
    private final CrudScheduleRepository crudScheduleRepository;
    private final TemplateRepository templateRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @NotNull
    public LocalDate formatDate(String convertDate) {
        return LocalDate.parse(convertDate, formatter);
    }

    public PeriodType createPeriodType(Supplier<PeriodType> supplier) {
        return supplier.get();
    }

    public PriceType judgmentPriceType(Supplier<PriceType> supplier) {
        return supplier.get();
    }



    /**
     * TODO
     *  일정을 수정할 때 template에 포함되지 않을 수도 있다고 alert창을 띄워야 할 것 같음.
     *  *** 수정을 하는데 삭제가 됨...
     *
     */

    @Transactional
    public Optional<Template> exceptTemplate(ModifyScheduleDTO dto) {
        // 이미 기존 schedule는 삭제된 상태
        Optional<Template> optionalTemplate =
                getTemplateRepository().findByTemplateNameAndCategoryName(dto.getEventName(), dto.getCategory());

        log.info("template 존재 유무:{}", optionalTemplate.isPresent());


        return optionalTemplate;
    }


}
