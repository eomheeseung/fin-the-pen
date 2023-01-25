package project.fin_the_pen.data.schedule;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int eventKey;

    // TODO 나중에 User의 userId와 매핑할 것임 FK로 가져와서
    @Column(name = "user_id")
    private String userId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "date")
    private Date eventDate;

    @Column(name = "start_time")
    private Date startDateTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "period")
    private String period;

    @Column(name = "categories")
    private String categories;

    @Column(name = "type")
    private String type;

    @Column(name = "expected_spending")
    private int expected_spending;

    @Column(name = "exclusion")
    private boolean exclusion;
}
