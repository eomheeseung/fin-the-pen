package project.fin_the_pen.model.schedule.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindCertainMonthVO {
    @JsonProperty(value = "user_id")
    @Schema(example = "test1234", description = "현재 로그인된 id")
    private String userId;

    @JsonProperty(value = "date")
    @Schema(example = "2024-02", description = "검색하려는 달 (month)")
    private String date;
}
