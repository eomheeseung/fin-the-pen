package project.fin_the_pen.model.assets.periodic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import project.fin_the_pen.model.schedule.type.PriceType;

@Getter
@Setter
public class PeriodicAmountRequestDto {
    @JsonProperty(value = "user_id")
    @Schema(example = "test1234", description = "현재 로그인 된 사용자 id")
    private String userId;

    @JsonProperty(value = "nick_name")
    @Schema(example = "넷플릭스", description = "사용자가 입력한 출금이나 입금명")
    private String nickName;

    @JsonProperty(value = "amount")
    @Schema(example = "9000", description = "사용자가 입력한 입금/출금액")
    private String amount;

    @JsonProperty(value = "price_type")
    @Schema(example = "Minus",description = "입금이면 Plus, 출금이면 Minus")
    private PriceType priceType;

    // 기간
    @JsonProperty(value = "period")
    @Schema(example = "2024-06-15",description = "사용자가 선택한 기간")
    private String period;

    // 출금일
    @JsonProperty(value = "select_date")
    @Schema(example = "1",description = "사용자가 선택한 출금이나 입금일")
    private String selectDate;

    @JsonProperty(value = "fixed")
    @Schema(example = "true",description = "출금액을 고정시킬 것인지")
    private Boolean fixed;
}
