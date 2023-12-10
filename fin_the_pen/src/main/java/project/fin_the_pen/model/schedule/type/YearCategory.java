package project.fin_the_pen.model.schedule.type;

import lombok.Getter;

@Getter
public enum YearCategory {
    /*
     각각 MM월 DD일, MM월 N번째 D요일, MM월 마지막 D요일
     */
    MonthAndDay(0),
    NthDayOfMonth(1),
    LastDayOfMonth(2);

    private final int value;

    YearCategory(int value) {
        this.value = value;
    }
}
