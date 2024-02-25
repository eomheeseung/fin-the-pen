package project.fin_the_pen.model.schedule.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DayTypeVO {
    @JsonProperty("repeat_term")
    @Schema(example = "2")
    private String repeatTerm;
}
