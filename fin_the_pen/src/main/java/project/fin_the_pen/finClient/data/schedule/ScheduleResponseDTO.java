package project.fin_the_pen.finClient.data.schedule;

import lombok.Builder;
import lombok.Data;
import project.fin_the_pen.finClient.data.schedule.type.ScheduleType;

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

    /*
    TODO
     1. response 할 때 어떻게 보여줄지..
     */
    private ScheduleType type;

    private int expectedSpending;
    private boolean exclusion;
    private String importance;
}
