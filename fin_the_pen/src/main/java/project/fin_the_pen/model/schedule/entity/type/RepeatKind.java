package project.fin_the_pen.model.schedule.entity.type;

import lombok.Getter;

@Getter
public enum RepeatKind {
    NONE(0), DAY(1), WEEK(2), MONTH(3), YEAR(4);

    private final int value;

    RepeatKind(int value) {
        this.value = value;
    }
}
