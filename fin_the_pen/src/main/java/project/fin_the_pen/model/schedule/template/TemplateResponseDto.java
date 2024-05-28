package project.fin_the_pen.model.schedule.template;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Getter
public class TemplateResponseDto {
    private Long id;
    private String userId;
    private String templateName;
    private String categoryName;
    private TemplateBankStatement statement;
    private String amount;

    public TemplateResponseDto(Template template) {
        this.id = template.getId();
        this.userId = template.getUserId();
        this.templateName = template.getTemplateName();
        this.categoryName = template.getCategoryName();
        this.statement = template.getStatement();
    }
}
