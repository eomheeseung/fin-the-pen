package project.fin_the_pen.model.schedule.service.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.repository.CRUDScheduleRepository;
import project.fin_the_pen.model.schedule.repository.ScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Getter
public abstract class RegisterSchedule {
    private final CRUDScheduleRepository crudScheduleRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

}
