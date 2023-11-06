package project.fin_the_pen.model.schedule.type;

import lombok.Getter;

@Getter
public enum RepeatType {
    None("none", 0L),

    AllDay("allDay", 1L),

    Week("week",2L),

    Month("month", 3L);

    private final String type;
    private final Long sortNum;

    RepeatType(String type, Long sortNum) {
        this.type = type;
        this.sortNum = sortNum;
    }
}
