package project.fin_the_pen.model.schedule.entity.embedded;

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
public class PeriodType {
    // 계속 반복
//    @ColumnDefault("'false'")

    // mysql version
    @ColumnDefault("0")
    private Boolean isRepeatAgain;

    // 일정 반복 횟수
    @ColumnDefault("'none'")
    private String repeatNumberOfTime;

    // 종료 날짜
    @ColumnDefault("'none'")
    private String repeatEndLine;
}
