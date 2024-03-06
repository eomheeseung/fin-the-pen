package project.fin_the_pen.model.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumeReportDetailRequestDto {
    @JsonProperty(value = "user_id")
    @Schema(example = "test1234", description = "현재 로그인된 사용자 id")
    private String userId;

    @JsonProperty(value = "category")
    @Schema(example = "외식", description = "상세 조회할 카테고리")
    private String category;

    @JsonProperty(value = "date")
    @Schema(example = "2024-02", description = "상세 조회할 달(month)")
    private String date;

}
