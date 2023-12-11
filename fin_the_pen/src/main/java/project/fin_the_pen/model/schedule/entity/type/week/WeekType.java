package project.fin_the_pen.model.schedule.entity.type.week;

import lombok.Getter;
import lombok.Setter;
import project.fin_the_pen.model.schedule.entity.type.RepeatType;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class WeekType extends RepeatType {
    // 반복될 요일 하나가 들어가는 컬럼
    private String DayOfWeek;
}
