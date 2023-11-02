package project.fin_the_pen.finClient.data.schedule;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import project.fin_the_pen.finClient.data.schedule.type.PriceType;
import project.fin_the_pen.finClient.data.schedule.type.RegularType;

import javax.persistence.*;

// 정기 일정을 수정하면 개별일정이 되는데 개별일정에 대한 entity
@Entity
@Data
public class RegularSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public RegularSchedule() {
    }

    @Builder
    public RegularSchedule(String scheduleId, String userId, String eventName, boolean alarm, String date, String startTime, String endTime, String category, PriceType type, int expectedSpending, String repeatingCycle, String repeatDeadline, String repeatEndDate, boolean exclusion, String importance, PriceType priceType, RegularType regularType) {
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.eventName = eventName;
        this.alarm = alarm;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.expectedSpending = expectedSpending;
        this.repeatingCycle = repeatingCycle;
        this.repeatDeadline = repeatDeadline;
        this.repeatEndDate = repeatEndDate;
        this.exclusion = exclusion;
        this.importance = importance;
        this.regularType = regularType;
        this.priceType = priceType;
    }

    @Column(name = "schedule_id")
    private String scheduleId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "alarm")
    private boolean alarm;

    @Column(name = "date")
    private String date;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "category")
    private String category;

    @Column(name = "type")
    private PriceType priceType;

    @Column(name = "expected_spending")
    private int expectedSpending;

    @Column(name = "repeating_cycle")
    private String repeatingCycle;

    @Column(name = "repeat_deadline")
    private String repeatDeadline;

    @Column(name = "repeat_endDate")
    private String repeatEndDate;

    @Column(name = "exclusion")
    private boolean exclusion;

    @Column(name = "importance")
    private String importance;

    @Setter
    @Column(name = "schedule_type")
    private RegularType regularType;
}
