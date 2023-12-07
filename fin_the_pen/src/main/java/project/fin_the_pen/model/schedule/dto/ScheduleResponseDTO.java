package project.fin_the_pen.model.schedule.dto;

import lombok.Builder;
import lombok.Data;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.RepeatType;
import project.fin_the_pen.model.schedule.type.PriceType;

@Data
public class ScheduleResponseDTO {
    // schedule의 기본키
    private Long id;
    private String userId;
    private String eventName;
    private String category;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private boolean allDay;
    private RepeatType repeat;
    private PeriodType period;
    private String priceType;
    private boolean isExclude;
    private String importance;
    private String amount;
    private boolean isFixAmount;

    @Builder
    public ScheduleResponseDTO(Long id, String userId, String eventName, String category, String startDate, String endDate, String startTime, String endTime, boolean allDay, RepeatType repeat, PeriodType period, PriceType priceType, boolean isExclude, String importance, String amount, boolean isFixAmount) {
        this.id = id;
        this.userId = userId;
        this.eventName = eventName;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allDay = allDay;
        this.repeat = repeat;
        this.period = period;
        this.priceType = priceType.getType();
        this.isExclude = isExclude;
        this.importance = importance;
        this.amount = amount;
        this.isFixAmount = isFixAmount;
    }
}
