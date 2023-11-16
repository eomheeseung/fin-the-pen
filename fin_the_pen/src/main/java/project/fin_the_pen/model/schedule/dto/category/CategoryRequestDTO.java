package project.fin_the_pen.model.schedule.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoryRequestDTO {
    @JsonProperty("category_name")
    private String categoryName;
}
