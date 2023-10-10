package project.fin_the_pen.finClient.data.schedule.type;

import lombok.Getter;

@Getter
public enum PriceType {
    Plus("plus", 0L),

    Minus("minus", 1L);

    private final String type;

    private final Long sortNum;

    PriceType(String type, Long sortNum) {
        this.type = type;
        this.sortNum = sortNum;
    }
}
