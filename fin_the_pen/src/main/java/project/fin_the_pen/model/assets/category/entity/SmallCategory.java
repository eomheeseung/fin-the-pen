package project.fin_the_pen.model.assets.category.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmallCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String smallName;
    private String smallValue;

    @Builder
    public SmallCategory(String smallName, String smallValue) {
        this.smallName = smallName;
        this.smallValue = smallValue;
    }
}
