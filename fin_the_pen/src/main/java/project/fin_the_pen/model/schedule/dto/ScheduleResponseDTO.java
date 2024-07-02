package project.fin_the_pen.model.schedule.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.UnitedType;
import project.fin_the_pen.model.schedule.type.PriceType;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class ScheduleResponseDTO {
    // schedule의 기본키
    private Long scheduleId;
    private String userId;
    private String eventName;
    private String category;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private boolean allDay;
    private UnitedType repeatOptions;
    private PeriodType period;
    private String priceType;
    private boolean isExclude;
    private String paymentType;
    private String amount;
    private boolean isFixAmount;
    private String repeatKind;

    @Builder
    public ScheduleResponseDTO(Long scheduleId, String userId, String eventName, String category,
                               String startDate, String endDate, String startTime, String endTime, boolean allDay,
                               UnitedType repeatOptions, PeriodType period, PriceType priceType,
                               boolean isExclude, String paymentType, String amount, boolean isFixAmount,
                               String repeatKind) {
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.eventName = eventName;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allDay = allDay;
        this.repeatOptions = repeatOptions;
        this.period = period;
        this.priceType = priceType.getType();
        this.isExclude = isExclude;
        this.paymentType = paymentType;
        this.amount = amount;
        this.isFixAmount = isFixAmount;
        this.repeatKind = repeatKind;
    }
}
