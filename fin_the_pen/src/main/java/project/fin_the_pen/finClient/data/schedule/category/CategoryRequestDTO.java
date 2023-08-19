package project.fin_the_pen.finClient.data.schedule.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryRequestDTO {
    @JsonProperty("category")
    private String categoryName;

    @Builder
    public CategoryRequestDTO(String categoryName) {
        this.categoryName = categoryName;
    }
}
