package project.fin_the_pen.model.assets.saving.dto.personal;

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

    private String monthAmount;
}
