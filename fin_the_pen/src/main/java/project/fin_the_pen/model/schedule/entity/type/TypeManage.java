package project.fin_the_pen.model.schedule.entity.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.fin_the_pen.model.schedule.entity.type.day.DayType;
import project.fin_the_pen.model.schedule.entity.type.month.MonthType;
import project.fin_the_pen.model.schedule.entity.type.week.WeekType;
import project.fin_the_pen.model.schedule.entity.type.year.YearType;

import javax.persistence.Embeddable;

/**
 * 실제로 DB에 들어갈 entity
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class TypeManage {
    private NoneType noneType;
    private DayType dayType;
    private WeekType weekType;
    private MonthType monthType;
    private YearType yearType;
}
