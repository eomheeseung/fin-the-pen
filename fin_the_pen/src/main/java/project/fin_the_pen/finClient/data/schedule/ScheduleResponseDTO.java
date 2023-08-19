package project.fin_the_pen.finClient.data.schedule;

import lombok.Builder;
import lombok.Data;

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
    private String type;
    private int expectedSpending;
    private boolean exclusion;
    private String importance;
}
