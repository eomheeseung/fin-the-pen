package project.fin_the_pen.model.schedule.template;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/*
TODO
    1. template을 생성하고 정기일정에 포함시키는 경우
    2. 존재하는 template에 정기일정을 넣는 경우
    - 정기일정은 카테고리와 이름이 같으면 저장이 안되는데...
    => template에 넣는 기준을 어떻게 해야 하는지(template는 (일정명, 카테고리)로 묶임)
 */
public class TemplateRequestDto {
    @JsonProperty(value = "user_id")
    @Schema(name = "test1234")
    private String userId;

    @JsonProperty(value = "template_aname")
    @Schema(name = "가족들과 식사")
    private String templateName;

    @JsonProperty(value = "category")
    @Schema(name = "외식")
    private String category;
}
