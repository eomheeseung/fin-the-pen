package project.fin_the_pen.model.schedule.type;

import lombok.Getter;

@Getter
public enum PeriodType {
    // 기한 설정 x
    None("none", 0L),

    // 매일
    All("all", 1L),

    // 일자
    numberOf("numberOf", 2L),

    // 종료 기한
    end("end date", 3L);

    private String type;
    private Long sortType;

    PeriodType(String type, Long sortType) {
        this.type = type;
        this.sortType = sortType;
    }
}
