package project.fin_the_pen.model.assets.spend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpendAmountRequestDto {
    @JsonProperty(value = "user_id")
    @Schema(example = "test1234", description = "현재 로그인되어 있는 사용자 id")
    private String userId;

    @JsonProperty(value = "start_date")
    @Schema(example = "2024-03", description = "설정된 시작날짜")
    private String startDate;

    @JsonProperty(value = "end_date")
    @Schema(example = "2024-03", description = "설정된 종료날짜")
    private String endDate;

    @JsonProperty(value = "regular")
    @Schema(example = "OFF", description = "ON이면 정기로 설정, 시작날짜와 종료날짜가 다름, OFF이면 시작날짜와 종료날짜가 같음")
    private String regular;

    @JsonProperty(value = "spend_goal_amount")
    @Schema(example = "200000", description = "지출 목표액 설정")
    private String spendGoalAmount;

    @JsonProperty(value = "is_batch")
    @Schema(example = "false", description = "정기로 일괄 적용할지 말지 true(일괄적용 O), false(일괄적용 X)")
    private Boolean isBatch;
}
