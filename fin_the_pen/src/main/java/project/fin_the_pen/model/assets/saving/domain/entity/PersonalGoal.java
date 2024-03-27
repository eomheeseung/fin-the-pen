package project.fin_the_pen.model.assets.saving.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 개인 목표
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PersonalGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String goalName;

    private String period;

    private String goalAmount;

    private String monthAmount;

    public boolean update(String userId, String goalName, String period, String goalAmount, String monthAmount) {
        if (userId == null || goalName == null || period == null || goalAmount == null || monthAmount == null) {
            return false;
        }

        this.userId = userId;
        this.goalName = goalName;
        this.monthAmount = monthAmount;
        this.period = period;
        this.goalAmount = goalAmount;
        return true;
    }
}
