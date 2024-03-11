package project.fin_the_pen.model.assets.category.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String categoryName;
    private String date;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryDetail> categoryDetails;

    @Builder
    public Category(String userId, String categoryName, String date) {
        this.userId = userId;
        this.categoryName = categoryName;
        this.date = date;
    }

    // CategoryDetail을 추가하는 메서드
    public void addCategoryDetail(CategoryDetail categoryDetail) {
        if (this.categoryDetails == null) {
            categoryDetails = new ArrayList<>();
        }
        this.categoryDetails.add(categoryDetail);
        categoryDetail.setCategory(this);
    }
}
