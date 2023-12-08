package project.fin_the_pen.model.schedule.entity.type;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class MonthType extends RepeatType {
    private String monthValue;
}
