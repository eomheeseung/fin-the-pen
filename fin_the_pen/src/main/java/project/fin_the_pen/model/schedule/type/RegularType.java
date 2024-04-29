package project.fin_the_pen.model.schedule.type;

import lombok.Getter;

@Getter
public enum RegularType {
    EACH("each", 0L),
    REGULAR("regular", 1L);

    private final String type;
    private final Long sortNum;

    RegularType(String type, Long sortNum) {
        this.type = type;
        this.sortNum = sortNum;
    }
}
