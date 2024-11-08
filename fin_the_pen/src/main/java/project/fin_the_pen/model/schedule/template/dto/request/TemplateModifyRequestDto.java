package project.fin_the_pen.model.schedule.template.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateModifyRequestDto {

    @JsonProperty(value = "template_id")
    @Schema(description = "수정하려고 하는 templateId", example = "2")
    private String templateId;

    @JsonProperty(value = "user_id")
    @Schema(description = "로그인된 아이디", example = "test1234")
    private String userId;

    @JsonProperty(value = "template_name")
    @Schema(description = "수정할 이름", example = "수정된 이름")
    private String templateName;

    @JsonProperty(value = "category_name")
    @Schema(description = "수정할 카테고리 명", example = "수정된 카테고리명")
    private String categoryName;
}
