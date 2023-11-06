package project.fin_the_pen.finClient.schedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import project.fin_the_pen.model.schedule.type.PriceType;

@Setter
@Getter
public class AssetRequestDTO {
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
