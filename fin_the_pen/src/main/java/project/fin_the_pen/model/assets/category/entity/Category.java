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
    private String mediumName;
    private String date;
    private String mediumValue;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SmallCategory> categoryList;

    @Builder
    public Category(String userId, String mediumName, String date, String mediumValue) {
        this.userId = userId;
        this.mediumName = mediumName;
        this.date = date;
        this.mediumValue = mediumValue;
    }

    public void addSmallCategory(SmallCategory smallCategory) {
        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        categoryList.add(smallCategory);
    }

    public void update(String userId, String mediumName, String mediumValue) {
        if (userId != null && mediumName != null && mediumValue != null) {
            this.userId = userId;
            this.mediumName = mediumName;
            this.mediumValue = mediumValue;
        }
    }

    public void deleteList() {
        if (!categoryList.isEmpty()) {
            categoryList.clear();
        }
    }
}
