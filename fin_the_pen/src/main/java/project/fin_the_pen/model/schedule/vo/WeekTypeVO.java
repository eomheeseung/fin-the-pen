package project.fin_the_pen.model.schedule.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeekTypeVO {
    @JsonProperty("repeat_day_of_week")
    @Schema(example = "MONDAY, TUESDAY, FRIDAY")
    private String repeatDayOfWeek;

    @JsonProperty("repeat_value")
    @Schema(example = "2")
    private String value;
}
