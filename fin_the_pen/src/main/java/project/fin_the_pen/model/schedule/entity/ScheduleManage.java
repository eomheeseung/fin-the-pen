package project.fin_the_pen.model.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import project.fin_the_pen.model.schedule.entity.Schedule;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class ScheduleManage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_id")
    private Long id;

    @Column(name = "delete_flag")
    private boolean deleteFlag;

    @Column(name = "modify_flag")
    private boolean modifyFlag;

    @OneToOne
    @JoinColumn(name = "session_id")
    private Schedule schedule;
}
