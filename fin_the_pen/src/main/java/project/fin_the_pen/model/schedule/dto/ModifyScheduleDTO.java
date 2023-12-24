
package project.fin_the_pen.model.schedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ModifyScheduleDTO {
    @JsonProperty(value = "user_id")
    @Schema(description = "현재 로그인되어 있는 user의 id", example = "test1234")
    private String userId;

    @JsonProperty(value = "schedule_id")
    @Schema(description = "수정하려고 하는 schedule의 id", example = "2")
    private String scheduleId;


    /**
     * nowFromAfter : 선택된 현재 일정부터 이후까지
     * exceptNowAfter : 현재 일정 제외하고 이후
     *  all : 모든 일정
     */
    @JsonProperty(value = "modify_options")
    @Schema(description = "수정하려는 옵션",example = "all")
    private String options;

    private ScheduleRequestDTO scheduleRequestDTO;
}
