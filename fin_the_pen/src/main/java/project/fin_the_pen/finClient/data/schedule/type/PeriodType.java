package project.fin_the_pen.finClient.data.schedule.type;

import lombok.Getter;

@Getter
public enum PeriodType {
    None("none", 0L),
    All("all", 1L),
    numberOf("number of cycle", 2L),
    end("end date", 3L);

    private String type;
    private Long sortType;

    PeriodType(String type, Long sortType) {
        this.type = type;
        this.sortType = sortType;
    }
}
