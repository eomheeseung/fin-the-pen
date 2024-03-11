package project.fin_the_pen.model.assets.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.Map;

@Getter
@Setter
public class DetailCategoryMapping {
    @JsonProperty(value = "category_name")
    @Schema(example = "생활", description = "대분류의 카테고리")
    private String categoryName;

    @JsonProperty(value = "category_detail")
    /*
    key : 소분류의 이름
    value : 금액
     */
    @Nullable
    private Map<String, String> details;
}
