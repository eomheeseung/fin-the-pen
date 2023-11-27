package project.fin_the_pen.model.schedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import project.fin_the_pen.model.schedule.type.PriceType;

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
    private String repeat;

    // 반복 기간
    @JsonProperty(value = "period")
    private String period;

    @JsonProperty(value = "price_type")
    private PriceType priceType;

    // 예산에서 제외할 것인지
    @JsonProperty(value = "exclusion")
    private boolean isExclude;

    // 중요도
    @JsonProperty(value = "importance")
    private String importance;

    // 자산 설정
    @JsonProperty(value = "set_amount")
    private String amount;

    // 금액 고정
    @JsonProperty(value = "fix_amount")
    private boolean isFixAmount;
}
