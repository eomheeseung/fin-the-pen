package project.fin_the_pen.model.assets.category.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String detailName;

    /**
     * 관례적으로 참조하는 부모테이블의 기본키를 외래키로 가져감
     * name로 다른 컬럼을 지정해도 괜찮지만 unique하다는 보장이 필요함...
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public CategoryDetail(String detailName) {
        this.detailName = detailName;
    }

    // Category를 설정하는 메서드
    public void setCategory(Category category) {
        if (this.category != null) {
            this.category = category;
        }
    }
}
