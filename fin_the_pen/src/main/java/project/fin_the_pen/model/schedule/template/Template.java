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
 * 템플릿이 정기일정을 관리한다
 *  1(부모테이플): n(자식)
 *
 * 정기일정만 삭제한다 => 자식테이블의 데이터를 먼저삭제
 *
 * 수정이 저장이랑 보여주는것보다 더 힘듬!
 *
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

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
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
