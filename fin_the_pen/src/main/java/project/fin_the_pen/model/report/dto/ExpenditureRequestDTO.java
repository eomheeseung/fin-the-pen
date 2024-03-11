package project.fin_the_pen.model.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExpenditureRequestDTO {
    @JsonProperty(value = "expenditure_amount")
    @Schema(example = "1000000", description = "지출목표액")
    private String amount;

    @JsonProperty(value = "user_id")
    @Schema(example = "test1234", description = "현재 로그인되어 있는 사용자 id")
    private String userId;

    @JsonProperty(value = "date")
    @Schema(example = "2024-02", description = "지출 목표액이 설정된 달")
    private String date;
}
