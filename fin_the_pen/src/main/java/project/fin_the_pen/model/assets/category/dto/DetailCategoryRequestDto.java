package project.fin_the_pen.model.assets.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailCategoryRequestDto {
    @JsonProperty(value = "user_id")
    @Schema(example = "test1234",description = "현재 로그인된 사용자 id")
    private String userId;

    @JsonProperty(value = "date")
    @Schema(example = "2024-02",description = "month")
    private String date;

    @JsonProperty(value = "category_detail")
    private DetailCategoryMapping details;

}
