package project.fin_the_pen.model.schedule.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import project.fin_the_pen.model.schedule.type.YearCategory;

@Getter
@Setter
public class YearTypeVO {
    @JsonProperty("year_repeat")
    @Schema(example = "11월 2번째 수요일")
    private String yearRepeat;

    @JsonProperty("repeat_value")
    @Schema(example = "2")
    private String value;

    @JsonProperty("year_category")
    @Schema(example = "NthDayOfMonth")
    private String yearCategory;
}
