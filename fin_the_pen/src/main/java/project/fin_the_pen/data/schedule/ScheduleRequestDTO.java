package project.fin_the_pen.data.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ScheduleRequestDTO {
    public ScheduleRequestDTO() {
    }
    @JsonProperty(value = "id")
    private UUID id;
    @JsonProperty(value = "user_id")
    private String userId;
    @JsonProperty(value = "alarm")
    private boolean alarm;
    @JsonProperty(value = "event_name")
    private String eventName;
    @JsonProperty(value = "date")
    private Date date;
    @JsonProperty(value = "start_time")
    private Date startTime;
    @JsonProperty(value = "end_time")
    private Date endTime;
    @JsonProperty(value = "repeating_cycle")
    private String repeatingCycle;
    @JsonProperty(value = "repeat_deadline")
    private String repeatDeadLine;
    @JsonProperty(value = "repeat_endDate")
    private String repeatEndDate;
    @JsonProperty(value = "category")
    private String category;
    @JsonProperty(value = "type")
    private String type;
    @JsonProperty(value = "expected_spending")
    private int expectedSpending;
    @JsonProperty(value = "exclusion")
    private boolean exclusion;
    @JsonProperty(value = "importance")
    private String importance;
}
