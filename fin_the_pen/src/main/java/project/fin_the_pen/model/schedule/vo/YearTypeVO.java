package project.fin_the_pen.model.schedule.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YearTypeVO {
    @JsonProperty("year_repeat")
    @Schema(example = "11월 2번째 수요일")
    private String yearRepeat;

    @JsonProperty("repeat_term")
    @Schema(example = "2")
    private String repeatTerm;

    @JsonProperty("year_category")
    @Schema(example = "NthDayOfMonth")
    private String yearCategory;
}
