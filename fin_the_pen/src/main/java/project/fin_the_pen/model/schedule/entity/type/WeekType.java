package project.fin_the_pen.model.schedule.entity.type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeekType implements RepeatType{
    private String monthValue;
    private long repeatValue;
}
