package project.fin_the_pen.model.schedule.entity.type;

import lombok.Getter;
import lombok.Setter;
import project.fin_the_pen.model.schedule.type.YearCategory;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class YearType extends RepeatType {
    private String yearValue;
    private String yearCategory;
}
