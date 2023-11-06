package project.fin_the_pen.finClient.data.schedule;

import lombok.Getter;
import lombok.Setter;

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
