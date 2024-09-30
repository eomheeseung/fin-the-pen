package project.fin_the_pen.model.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReportRequestDemoDTO {
    @JsonProperty(value = "user_id")
    @Schema(example = "test1234", description = "현재 로그인된 id")
    private String userId;

    @JsonProperty(value = "date")
    @Schema(description = "리포트 화면에서 조회할 날짜 yyyy-MM-dd", example = "2024-03-27")
    private String date;
}
