package project.fin_the_pen.model.assets.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CategoryGoalRequestDto {
    @JsonProperty(value = "user_id")
    @Schema(example = "test1234", description = "현재 로그인된 사용자 id")
    private String userId;

    @JsonProperty(value = "date")
    @Schema(example = "2024-02", description = "month")
    private String date;

    @JsonProperty(value = "medium_name")
    @Schema(example = "금융", description = "중분류의 이름")
    private String mediumName;

    @JsonProperty(value = "medium_value")
    @Schema(example = "100000", description = "중분류의 값")
    private String mediumValue;

    @JsonProperty(value = "small_map")
    private Map<String, String> smallMap = new HashMap<>();

}
