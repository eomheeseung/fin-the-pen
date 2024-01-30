package project.fin_the_pen.model.schedule.service.modify;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.repository.CRUDScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Getter
public class ModifySchedule {
    private final CRUDScheduleRepository crudScheduleRepository;
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
}
