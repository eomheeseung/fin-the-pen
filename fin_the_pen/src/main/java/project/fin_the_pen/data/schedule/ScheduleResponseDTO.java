package project.fin_the_pen.data.schedule;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduleResponseDTO {
//    private int eventKey;
    private String userId;
    private String eventName;
    private Date eventDate;
    private Date startDateTime;
    private Date endTime;
    private String period;
    private String categories;
    private String type;
    private int expected_spending;
    private boolean exclusion;
}
