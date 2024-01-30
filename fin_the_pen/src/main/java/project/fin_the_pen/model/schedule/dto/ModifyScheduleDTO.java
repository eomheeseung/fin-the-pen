
package project.fin_the_pen.model.schedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import project.fin_the_pen.model.schedule.type.PriceType;

@Data
public class ModifyScheduleDTO {
    @JsonProperty(value = "schedule_id")
    @Schema(description = "수정하려고 하는 schedule의 id", example = "2")
    private String scheduleId;

    /**
     * nowFromAfter : 선택된 현재 일정부터 이후까지
     * exceptNowAfter : 현재 일정 제외하고 이후
     * all : 모든 일정
     */
    @JsonProperty(value = "modify_options")
    @Schema(description = "수정하려는 옵션",example = "nowFromAfter")
    private String options;

    @JsonProperty(value = "user_id")
    @Schema(description = "로그인 할 id", example = "test1234")
    private String userId;

    @JsonProperty(value = "event_name")
    @Schema(description = "등록할 일정 이름", example = "수정")
    private String eventName;

    @JsonProperty(value = "category")
    @Schema(description = "분류될 카테고리", example = "수정")
    private String category;

    // 시작 일자
    @JsonProperty(value = "start_date")
    @Schema(description = "일정 시작 일자", example = "2024-02-22")
    private String startDate;

    // 종료 일자
    @JsonProperty(value = "end_date")
    @Schema(description = "일정 종료 일자", example = "2024-02-22")
    private String endDate;

    // 시작 시간
    @JsonProperty(value = "start_time")
    @Schema(description = "일정 시작 시간", example = "18:00")
    private String startTime;

    // 종료 시간
    @JsonProperty(value = "end_time")
    @Schema(description = "일정 종료 시간", example = "21:00")
    private String endTime;

    // 하루 종일
    @JsonProperty(value = "is_all_day")
    @Schema(description = "일정에 대해 하루 종일 반복인지", example = "false")
    private boolean isAllDay;

    // 반복
    @JsonProperty(value = "repeat")
    @Schema(description = "반복을 얼만큼 할건지")
    private TypeManageDTO repeat;

    // 반복 기간
    @JsonProperty(value = "period")
    @Schema(description = "반복을 언제까지 할 건지")
    private PeriodTypeDTO period;

    @JsonProperty(value = "price_type")
    @Schema(description = "수입인지 지출인지", example = "Minus")
    private PriceType priceType;

    // 예산에서 제외할 것인지
    @JsonProperty(value = "exclusion")
    @Schema(description = "예산에서 제외할 것인지", example = "false")
    private boolean isExclude;

    // 중요도
    @JsonProperty(value = "importance")
    @Schema(description = "중요도", example = "상")
    private String importance;

    // 자산 설정
    @JsonProperty(value = "set_amount")
    @Schema(description = "자산 설정", example = "30000")
    private String amount;

    // 금액 고정
    @JsonProperty(value = "fix_amount")
    @Schema(description = "금액을 고정할 것인지", example = "false")
    private boolean isFixAmount;
}
