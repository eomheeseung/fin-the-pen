package project.fin_the_pen.finClient.data.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import project.fin_the_pen.finClient.data.schedule.type.RepeatType;

@Data
public class ScheduleDTO {
    public ScheduleDTO() {
    }

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "user_id")
    private String userId;

    @JsonProperty(value = "event_name")
    private String eventName;

    @JsonProperty(value = "category")
    private String category;

    // 시작 일자
    @JsonProperty(value = "start_date")
    private String startDate;

    // 종료 일자
    @JsonProperty(value = "end_date")
    private String endDate;

    // 시작 시간
    @JsonProperty(value = "start_time")
    private String startTime;

    // 종료 시간
    @JsonProperty(value = "end_time")
    private String endTime;

    // 하루 종일
    @JsonProperty(value = "is_all_day")
    private boolean allDay;

    // 반복
    @JsonProperty(value = "repeat")
    private RepeatType repeat;

    // 반복 기간
    @JsonProperty(value = "period")
    private String period;
}
