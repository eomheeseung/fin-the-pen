package project.fin_the_pen.data.schedule;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class ScheduleResponseDTO {
    private UUID id;
    private String userId;
    private boolean alarm;
    private String eventName;
    private Date date;
    private Date startTime;
    private Date endTime;
    private String repeatingCycle;
    private String repeatDeadline;
    private String repeatEndDate;
    private String category;
    private String type;
    private int expectedSpending;
    private boolean exclusion;
    private String importance;
}
