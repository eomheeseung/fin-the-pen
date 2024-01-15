package project.fin_the_pen.model.schedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeleteScheduleDTO {
    @JsonProperty(value = "schedule_id")
    @Schema(description = "삭제하려고 하는 schedule의 id", example = "2")
    private String scheduleId;

    /**
     * nowFromAfter : 선택된 현재 일정부터 이후까지
     * exceptNowAfter : 현재 일정 제외하고 이후
     * all : 모든 일정
     */
    @JsonProperty(value = "delete_options")
    @Schema(description = "수정하려는 옵션",example = "nowFromAfter")
    private String options;

    @JsonProperty(value = "user_id")
    @Schema(description = "로그인 할 id", example = "test1234")
    private String userId;
}
