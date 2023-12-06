package project.fin_the_pen.model.schedule.entity.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// dto에서만 사용할 것임
@Getter
@Setter
public class TypeManageDTO {
    @JsonProperty("kind_type")
    @Schema(example = "week")
    private String kindType;

    @Schema(description = "반복 횟수", example = "2")
    private String value;

    @JsonProperty("day_of_XXX")
    @Schema(description = "요일설정", example = "MONDAY, WEDNESDAY, FRIDAY")
    private String dayOfXXX;
}
