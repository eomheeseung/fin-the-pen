package project.fin_the_pen.model.schedule.template.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TemplateModifySelectedScheduleRequestDto {
    @JsonProperty(value = "template_id")
    @Schema(description = "현재 template의 Id", example = "2")
    private String templateId;

    @JsonProperty(value = "user_id")
    @Schema(description = "로그인된 아이디", example = "test1234")
    private String userId;

    @JsonProperty(value = "schedule_id_list")
    @Schema(description = "수정하려는 일정들의 id", example = "2,3")
    private String scheduleIdList;

    @JsonProperty(value = "priceType")
    @Schema(description = "수정할 금액의 타입 {입금 / 출금}", example = "deposit")
    private String priceType;

    @JsonProperty(value = "amount")
    @Schema(description = "수정할 금액", example = "33333")
    private String amount;

    @JsonProperty(value = "is_fixed")
    @Schema(description = "금액 고정의 여부", example = "true")
    private String isFixed;

    @JsonProperty(value = "payment_type")
    @Schema(description = "입출금의 수단", example = "card")
    private String paymentType;

    @JsonProperty(value = "is_excluded")
    @Schema(description = "자산에서 제외할 것인지", example = "false")
    private String isExcluded;
}
