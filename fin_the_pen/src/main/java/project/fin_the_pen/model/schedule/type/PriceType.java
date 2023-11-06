package project.fin_the_pen.model.schedule.type;

import lombok.Getter;

@Getter
public enum PriceType {
    // 수입
    Plus("+", 0L),

    // 지출
    Minus("-", 1L);

    private final String type;

    private final Long sortNum;

    PriceType(String type, Long sortNum) {
        this.type = type;
        this.sortNum = sortNum;
    }
}
