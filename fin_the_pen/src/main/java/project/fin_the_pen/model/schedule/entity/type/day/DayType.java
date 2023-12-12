package project.fin_the_pen.model.schedule.entity.type.day;

import lombok.Getter;
import lombok.Setter;
import project.fin_the_pen.model.schedule.entity.type.RepeatType;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class DayType extends RepeatType {
    private String value;
}
