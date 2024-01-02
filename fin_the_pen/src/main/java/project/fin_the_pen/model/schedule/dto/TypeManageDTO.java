package project.fin_the_pen.model.schedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import project.fin_the_pen.model.schedule.vo.DayTypeVO;
import project.fin_the_pen.model.schedule.vo.MonthTypeVO;
import project.fin_the_pen.model.schedule.vo.WeekTypeVO;
import project.fin_the_pen.model.schedule.vo.YearTypeVO;

// dto에서만 사용할 것임
@Getter
@Setter
public class TypeManageDTO {
    @JsonProperty("day_type")
    private DayTypeVO dayTypeVO;

    @JsonProperty("week_type")
    private WeekTypeVO weekTypeVO;

    @JsonProperty("month_type")
    private MonthTypeVO monthTypeVO;

    @JsonProperty("year_type")
    private YearTypeVO yearTypeVO;

    @JsonProperty("kind_type")
    @Schema(example = "week")
    private String kindType;


    /*@Schema(description = "반복 횟수", example = "2")
    private String value;*/
}
