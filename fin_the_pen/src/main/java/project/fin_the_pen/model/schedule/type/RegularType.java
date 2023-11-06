package project.fin_the_pen.model.schedule.type;

import lombok.Getter;

@Getter
public enum RegularType {
    None("none", 0L),

    Deposit("deposit", 1L),

    Withdrawal("withdrawal", 2L);

    private final String type;
    private final Long sortNum;

    RegularType(String type, Long sortNum) {
        this.type = type;
        this.sortNum = sortNum;
    }
}
