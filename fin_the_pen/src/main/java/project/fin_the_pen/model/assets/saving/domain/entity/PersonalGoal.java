package project.fin_the_pen.model.assets.saving.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.fin_the_pen.model.assets.saving.domain.type.PersonalCriteria;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    private PersonalCriteria criteria;

    private String requiredAmount;

    private Boolean isRemittance;

    private Boolean isPopOn;

    public boolean update(String userId, String goalName, String period, PersonalCriteria criteria,
                          String requiredAmount, Boolean isRemittance, Boolean isPopOn) {

        if (userId != null && goalName != null && period != null && criteria != null
                && requiredAmount != null && isRemittance != null && isPopOn != null) {

            this.userId = userId;
            this.goalName = goalName;
            this.period = period;
            this.criteria = criteria;
            this.requiredAmount = requiredAmount;
            this.isRemittance = isRemittance;
            this.isPopOn = isPopOn;

            return true;
        }
        return false;

    }
}
