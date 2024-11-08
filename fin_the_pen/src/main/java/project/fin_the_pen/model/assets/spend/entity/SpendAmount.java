package project.fin_the_pen.model.assets.spend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpendAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    // 지출 목표액
    private String spendGoalAmount;

    private String startDate;

    private String endDate;

    // 정기 인지 아닌지 구분 (On이면 정기, off면 일반)
    private SpendAmountRegular regular;


    @Builder
    public SpendAmount(String userId, String spendGoalAmount, String startDate, String endDate, SpendAmountRegular regular) {
        this.userId = userId;
        this.spendGoalAmount = spendGoalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.regular = regular;
    }

    public boolean update(String userId, String spendGoalAmount, String startDate, String endDate, SpendAmountRegular regular) {
        if (userId == null || spendGoalAmount == null || startDate == null || endDate == null || regular == null) {
            return false;
        }
        this.userId = userId;
        this.spendGoalAmount = spendGoalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.regular = regular;
        return true;
    }
}
