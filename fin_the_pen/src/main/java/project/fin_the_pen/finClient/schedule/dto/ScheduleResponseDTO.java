package project.fin_the_pen.finClient.schedule.dto;

import lombok.Builder;
import lombok.Data;
import project.fin_the_pen.model.schedule.type.PriceType;

@Data
@Builder
public class ScheduleResponseDTO {
    private String id;
    //    private String userId;
    private boolean alarm;
    private String eventName;
    private String date;
    private String startTime;
    private String endTime;
    private String repeatingCycle;
    private String repeatDeadline;
    private String repeatEndDate;
    private String category;
    private PriceType type;

    private int expectedSpending;
    private boolean exclusion;
    private String importance;
}
