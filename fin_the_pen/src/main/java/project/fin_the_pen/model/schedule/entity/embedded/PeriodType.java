package project.fin_the_pen.model.schedule.entity.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class PeriodType {
    // 계속 반복
    private boolean isRepeatAgain;

    // 일정 반복 횟수
    private String repeatNumberOfTime;

    // 종료 날짜
    private String repeatEndLine;
}
