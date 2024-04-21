package project.fin_the_pen.model.assets.periodic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodicDeleteRequestDto {
    @JsonProperty(value = "user_id")
    @Schema(example = "test1234", description = "현재 로그인 된 사용자 id")
    private String userId;

    @JsonProperty(value = "id")
    @Schema(example = "1", description = "삭제하기 위해 선택된 정기 입출금액 id")
    private Long id;
}
