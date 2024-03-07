package project.fin_the_pen.model.assets.dto.personal;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonalResponseDto {
    private String userId;

    private String goalName;

    private String goalAmount;

    private String period;

    private String criteria;

    private String requiredAmount;

    private String isRemittance;

    private String isPopOn;
}
