package project.fin_the_pen.finClient.data.schedule.type;

import lombok.Getter;

@Getter
public enum ScheduleType {
    Deposit("plus", 0L),

    Withdraw("minus", 1L);

    private final String type;

    private final Long sortNum;

    ScheduleType(String type, Long sortNum) {
        this.type = type;
        this.sortNum = sortNum;
    }
}
