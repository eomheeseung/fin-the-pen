package project.fin_the_pen.model.schedule.entity.type.month;

import lombok.Getter;
import lombok.Setter;
import project.fin_the_pen.model.schedule.entity.type.RepeatType;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class MonthType extends RepeatType {
    private String monthValue;
}
