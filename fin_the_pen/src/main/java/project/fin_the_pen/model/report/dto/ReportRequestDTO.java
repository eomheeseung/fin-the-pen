package project.fin_the_pen.model.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * TODO
 *  notice!!
 *  실제는 해당 DTO를 사용하는데 사용자의 입력은 month까지지만 front에서 day까지 넘겨줘야 함.
 */
@Data
public class ReportRequestDTO {
    @JsonProperty(value = "user_id")
    @Schema(example = "test1234", description = "현재 로그인된 id")
    private String userId;

    @JsonProperty(value = "date")
    @Schema(description = "리포트 화면에서 조회할 날짜 yyyy-MM", example = "2024-10")
    private String date;
}
