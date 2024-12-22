package project.fin_the_pen.model.schedule.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YearTypeVO {
    /**
     * 각각 1. MM월 DD일, 입력 양식 11월 20일 => 11-20
     *     2. MM월 N번째 D요일,
     *     3. MM월 마지막 D요일
     */
    @JsonProperty("year_repeat")
    @Schema(example = "11-20")
    private String yearRepeat;

    @JsonProperty("repeat_term")
    @Schema(example = "2")
    private String repeatTerm;

    /**
     *  1. MonthAndDay
     *  2. NthDayOfMonth
     *  3. LastDayOfMonth
     */
    @JsonProperty("year_category")
    @Schema(example = "MonthAndDay")
    private String yearCategory;
}
