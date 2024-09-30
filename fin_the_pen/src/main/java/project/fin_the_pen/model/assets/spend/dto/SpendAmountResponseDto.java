package project.fin_the_pen.model.assets.spend.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Setter
@Builder
@Getter
public class SpendAmountResponseDto {
    private String userId;
    private String date;
    private String startDate;
    private String endDate;

    // 지출 목표액
    private String spendGoalAmount;

    // 지출액
    private String spendAmount;

}
