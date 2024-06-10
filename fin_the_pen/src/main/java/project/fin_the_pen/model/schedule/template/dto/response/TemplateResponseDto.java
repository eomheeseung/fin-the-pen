package project.fin_the_pen.model.schedule.template.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.fin_the_pen.model.schedule.template.Template;
import project.fin_the_pen.model.schedule.template.TemplateBankStatement;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Getter
@Setter
public class TemplateResponseDto {
    private Long id;
    private String userId;
    private String templateName;
    private String categoryName;
    private TemplateBankStatement statement;
    private String amount;

    public TemplateResponseDto(Template template) {
        this.id = template.getId();
        this.amount = template.getAmount();
        this.userId = template.getUserId();
        this.templateName = template.getTemplateName();
        this.categoryName = template.getCategoryName();
        this.statement = template.getStatement();
    }
}
