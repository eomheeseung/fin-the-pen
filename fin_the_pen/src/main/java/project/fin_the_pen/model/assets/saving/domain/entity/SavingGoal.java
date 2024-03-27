package project.fin_the_pen.model.assets.saving.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 저축 목표
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SavingGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "years_goal_amount")
    private String yearsGoalAmount;

    @Column(name = "month_goal_amount")
    private String monthGoalAmount;

    public boolean update(String userId, String yearsGoalAmount, String monthGoalAmount) {
        if (userId != null && yearsGoalAmount != null && monthGoalAmount != null) {
            this.userId = userId;
            this.yearsGoalAmount = yearsGoalAmount;
            this.monthGoalAmount = monthGoalAmount;
            return true;
        } else {
            return false;
        }
    }
}
