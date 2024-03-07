package project.fin_the_pen.model.assets.dto.targetAmount;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetAmountRequestDto {
    @JsonProperty(value = "user_id")
    @Schema(example = "test1234", description = "현재 로그인 된 사용자 id")
    private String userId;

    @JsonProperty(value = "years_goal_amount")
    @Schema(example = "10000000",description = "한 해 동안의 저축 목표액 설정")
    private String yearsGoalAmount;
}
