package project.fin_the_pen.model.schedule.template.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 일정을 등록할 때 일정명과 카테고리 선택을 누를 때 템플릿을 가져올지 말지
 * case3) 카테고리 선택 후, 동일한 정기템플릿이 존재할 때의 화면
 */
@Getter
@Setter
public class ImportTemplateRequestDto {
    @JsonProperty(value = "user_id")
    @Schema(description = "로그인된 아이디", example = "test1234")
    private String userId;

    @JsonProperty(value = "event_name")
    @Schema(description = "일정 이름", example = "가족과의 식사")
    private String eventName;

    @JsonProperty(value = "category_name")
    @Schema(description = "category 이름", example = "외식")
    private String categoryName;

    @JsonProperty(value = "is_import")
    @Schema(description = "유무", example = "true")
    private String isImport;
}
