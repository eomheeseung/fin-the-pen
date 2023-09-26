package project.fin_the_pen.finClient.data.schedule;

import lombok.Builder;
import lombok.Data;
import project.fin_the_pen.finClient.data.schedule.type.PriceType;
import project.fin_the_pen.finClient.data.schedule.type.ScheduleType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * master table 개념
 * why) key를 string으로 했지..?
 */
@Entity
@Data
public class Schedule {
    public Schedule() {
    }

    @Builder
    public Schedule(String id, String userId, String eventName, boolean alarm, String date, String startTime,
                    String endTime, String category, PriceType type, int expectedSpending, String repeatingCycle,
                    String repeatDeadline, String repeatEndDate, boolean exclusion, String importance) {
        this.id = id;
        this.userId = userId;
        this.eventName = eventName;
        this.alarm = alarm;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.type = type;
        this.expectedSpending = expectedSpending;
        this.repeatingCycle = repeatingCycle;
        this.repeatDeadline = repeatDeadline;
        this.repeatEndDate = repeatEndDate;
        this.exclusion = exclusion;
        this.importance = importance;
    }

    @Id
    @Column(name = "id")
    private String id;

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
    private PriceType type;

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

    // ScheduleType로 하나 만들어야 함.
    @Column(name = "schedule_type")
    private ScheduleType scheduleType;
}
