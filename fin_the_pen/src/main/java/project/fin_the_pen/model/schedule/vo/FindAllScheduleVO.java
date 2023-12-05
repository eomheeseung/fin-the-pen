package project.fin_the_pen.model.schedule.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindAllScheduleVO {
    @JsonProperty(value = "user_id")
    @Schema(example = "test1234", description = "로그인된 userId를 보낸다.")
    private String userId;
}
