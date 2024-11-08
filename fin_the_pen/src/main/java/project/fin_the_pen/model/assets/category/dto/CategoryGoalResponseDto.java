package project.fin_the_pen.model.assets.category.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
public class CategoryGoalResponseDto {
    private String categoryName;
    private String categoryTotal;
    private List<SmallCategoryResponseDto> list = new ArrayList<>();

    public void addList(SmallCategoryResponseDto smallCategoryResponseDto) {
        list.add(smallCategoryResponseDto);
    }
}
