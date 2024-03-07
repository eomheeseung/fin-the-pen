package project.fin_the_pen.model.assets.dto.personal;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalRequestDto {
    @JsonProperty(value = "user_id")
    @Schema(example = "test1234", description = "현재 로그인 된 사용자 id")
    private String userId;

    @JsonProperty(value = "personal_goal")
    @Schema(example = "맥북", description = "설정할 목표 이름")
    private String goalName;

    @JsonProperty(value = "goal_amount")
    @Schema(example = "2000000", description = "개인적인 목표 설정 금액")
    private String goalAmount;

    @JsonProperty(value = "period")
    @Schema(example = "2024-04-01", description = "기간 설정")
    private String period;

    @JsonProperty(value = "criteria")
    @Schema(example = "month", description = "day or month")
    private String criteria;

    @JsonProperty(value = "required_amount")
    @Schema(example = "200000", description = "필요한 적금액")
    private String requiredAmount;

    @JsonProperty(value = "is_remittance")
    @Schema(example = "true", description = "적금액 송금 설정 true or false")
    private boolean isRemittance;

    @JsonProperty(value = "pop_on")
    @Schema(example = "true", description = "팝업창 설정 true or false")
    private boolean isPopOn;
}
