package project.fin_the_pen.model.assets.periodic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodicViewRequestDto {
    @JsonProperty(value = "user_id")
    @Schema(description = "현재 로그인된 id", example = "test1234")
    private String userId;

    @JsonProperty(value = "start_date")
    @Schema(description = "일정조회를 시작하는 날짜", example = "2024-02-01")
    private String startDate;

    @JsonProperty(value = "end_date")
    @Schema(description = "일정조회의 마지막 날짜", example = "2024-05-31")
    private String endDate;
}
