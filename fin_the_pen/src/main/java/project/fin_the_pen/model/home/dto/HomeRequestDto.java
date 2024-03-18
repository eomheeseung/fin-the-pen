package project.fin_the_pen.model.home.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HomeRequestDto {
    @JsonProperty(value = "user_id")
    @Schema(description = "현재 접속해 있는 schedule의 id", example = "test1234")
    private String userId;

    @JsonProperty(value = "main_month")
    @Schema(description = "입력받는 월(month)", example = "2024-02")
    private String date;

    @JsonProperty(value = "calendar_date")
    @Schema(description = "캘린더에서 입력받는 일자", example = "2024-02-12")
    private String calenderDate;
}
