package project.fin_the_pen.model.schedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodTypeDTO {
    @JsonProperty("is_repeat_again")
    @Schema(example = "false")
    private boolean isRepeatAgain;

    @JsonProperty("repeat_number_time")
    @Schema(example = "3")
    private String repeatNumberOfTime;

    @JsonProperty(value = "repeat_end_line")
    @Schema(description = "반복의 종료 기간",example = "2030-02-15")
    private String repeatEndLine;
}
