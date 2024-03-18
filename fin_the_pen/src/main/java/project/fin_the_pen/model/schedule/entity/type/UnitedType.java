package project.fin_the_pen.model.schedule.entity.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Embeddable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class UnitedType {
    @ColumnDefault("'0'")
    private String term;

    // 특수한 경우
    @ColumnDefault("'none'")
    private String options;
}
