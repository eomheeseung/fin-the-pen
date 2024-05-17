package project.fin_the_pen.model.schedule.template;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.fin_the_pen.model.schedule.entity.Schedule;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long id;

    private String userId;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> scheduleList = new ArrayList<>();

    private String templateName;

    private String categoryName;

    public void addSchedule(Schedule schedule) {
        schedule.setTemplate(this);
    }

    public void init() {
        if (scheduleList.isEmpty()) {
            scheduleList = new ArrayList<>();
        }
    }


    @Builder
    public Template(String userId, String templateName, String categoryName) {
        this.userId = userId;
        this.templateName = templateName;
        this.categoryName = categoryName;
    }
}
