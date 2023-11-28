package project.fin_the_pen.model.schedule.entity.embedded;

import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Setter
public class RepeatType {
    private String day;

    private String everyWeek;

    private String monthly;


    private String everyYear;
}
