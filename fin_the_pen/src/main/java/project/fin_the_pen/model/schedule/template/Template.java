package project.fin_the_pen.model.schedule.template;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.fin_the_pen.model.schedule.entity.Schedule;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *  템플릿이름 / 내부에 들어가는 일정명
 *  이 2개가 달라야 함
 */
@Entity
@NoArgsConstructor
@Getter
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long id;

    private String userId;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    // 순환 참조 방지 어노테이션(직렬화 과정에서 발생)
    @JsonManagedReference
    private List<Schedule> scheduleList = new ArrayList<>();

    private String templateName;

    private String categoryName;

    @Enumerated(value = EnumType.STRING)
    private TemplateBankStatement statement;

    // template에 저장된 정기일정들의 모든 자산의 합을 넣어야 함.
    private String amount;

    public void addSchedule(Schedule schedule) {
        schedule.setTemplate(this);
    }

    public void init() {
        if (scheduleList.isEmpty()) {
            scheduleList = new ArrayList<>();
        }
    }

    public void updateStatement(TemplateBankStatement statement) {
        this.statement = statement;
    }

    public void updateAmount(Integer amount) {
        this.amount = String.valueOf(amount);
    }


    @Builder
    public Template(String userId, String templateName, String categoryName, String amount) {
        this.userId = userId;
        this.templateName = templateName;
        this.categoryName = categoryName;
        this.amount = amount;
    }
}
