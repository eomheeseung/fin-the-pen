package project.fin_the_pen.finClient.data.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScheduleRequestDTO {
    public ScheduleRequestDTO() {
    }

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "user_id")
    private String userId;

    @JsonProperty(value = "alarm")
    private boolean alarm;

    @JsonProperty(value = "event_name")
    private String eventName;

    @JsonProperty(value = "date")
    private String date;

    @JsonProperty(value = "start_time")
    private String startTime;

    @JsonProperty(value = "end_time")
    private String endTime;

    @JsonProperty(value = "repeating_cycle")
    private String repeatingCycle;

    @JsonProperty(value = "repeat_deadline")
    private String repeatDeadLine;

    @JsonProperty(value = "repeat_endDate")
    private String repeatEndDate;

    @JsonProperty(value = "category")
    private String category;

    /*
    TODO
     1. request를 front에서 받을 때 string 형태로 받음.
     */
    @JsonProperty(value = "type")
    private String priceType;

    @JsonProperty(value = "regular_type")
    private String regularType;

    @JsonProperty(value = "expected_spending")
    private int expectedSpending;

    @JsonProperty(value = "exclusion")
    private boolean exclusion;

    @JsonProperty(value = "importance")
    private String importance;
}
