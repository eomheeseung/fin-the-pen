package project.fin_the_pen.model.schedule.entity;

import lombok.Builder;
import lombok.Data;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.embedded.RepeatType;
import project.fin_the_pen.model.schedule.type.PriceType;

import javax.persistence.*;

@Entity
@Data
public class Schedule {
    public Schedule() {
    }
    @Builder
    public Schedule(String id, String userId, String eventName, String category, String startDate, String endDate, String startTime, String endTime, boolean allDay, RepeatType repeat, PeriodType period, PriceType priceType, boolean isExclude, String importance, String amount, boolean isFixAmount) {
        this.id = id;
        this.userId = userId;
        this.eventName = eventName;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allDay = allDay;
        this.repeat = repeat;
        this.period = period;
        this.priceType = priceType;
        this.isExclude = isExclude;
        this.importance = importance;
        this.amount = amount;
        this.isFixAmount = isFixAmount;
    }


    @Id
    @Column(name = "session_id")
    private String id;

    // 일단 schedule와 같은 column을 가져간다고 하자.
    @Column(name = "user_id")
    private String userId;

    // 제목
    @Column(name = "event_name")
    private String eventName;

    // 카테고리
    @Column(name = "category")
    private String category;

    // 시작 일자
    @Column(name = "start_date")
    private String startDate;

    // 종료 일자
    @Column(name = "end_date")
    private String endDate;

    // 시작 시간
    @Column(name = "start_time")
    private String startTime;

    // 종료 시간
    @Column(name = "end_time")
    private String endTime;

    // 하루 종일
    @Column(name = "is_all_day")
    private boolean allDay;

    // 반복
    @Column(name = "repeat")
    @Embedded
    private RepeatType repeat;

    // 반복 기간
    @Column(name = "period")
    @Embedded
    private PeriodType period;

    // 자산 ================================================

    // 입금, 지출
    @Column(name = "price_type")
    private PriceType priceType;

    // 예산에서 제외할 것인지
    @Column(name = "exclusion")
    private boolean isExclude;

    // 중요도
    @Column(name = "importance")
    private String importance;

    // 자산 설정
    @Column(name = "set_amount")
    private String amount;

    // 금액 고정
    @Column(name = "fix_amount")
    private boolean isFixAmount;

    // ScheduleType로 하나 만들어야 함.
//    @Column(name = "regular_type")
//    private RegularType regularType;

    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL)
    private ScheduleManage scheduleManage;
}
