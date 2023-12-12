package project.fin_the_pen.model.schedule.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthTypeVO {
    @JsonProperty("today_repeat")
    @Schema(example = "false")
    private boolean todayRepeat;

    @JsonProperty("select_date")
    @Schema(example = "4, 7, 10, 16, 25, 30")
    private String selectedDate;

    @JsonProperty("repeat_value")
    @Schema(example = "2")
    private String value;
}
