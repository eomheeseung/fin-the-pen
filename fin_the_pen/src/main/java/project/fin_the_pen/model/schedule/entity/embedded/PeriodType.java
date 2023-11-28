package project.fin_the_pen.model.schedule.entity.embedded;

import lombok.Setter;

import javax.persistence.Embeddable;

// TODO, 임베디드 타입으로 넣지 않고, 엔티티를 만들어서 조인도 고려
@Embeddable
@Setter
public class PeriodType {
    // 계속 반복
    private String keepRepeat;

    // 일정 반복 횟수
    private String certain;

    // 종료 날짜
    private String repeatEndDate;
}
