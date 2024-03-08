package project.fin_the_pen.model.assets.saving.dto.targetAmount;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TargetAmountResponseDto {
    private String keyId;

    private String userId;

    private String yearsGoalAmount;

    private String monthsGoalAmount;
}
