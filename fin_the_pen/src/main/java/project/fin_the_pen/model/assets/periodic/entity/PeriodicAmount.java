package project.fin_the_pen.model.assets.periodic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import project.fin_the_pen.model.schedule.type.PriceType;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PeriodicAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String nickName;

    private String amount;

    private PriceType priceType;

    // 기간
    private String period;

    // 출금일
    private String date;

    @ColumnDefault(value = "false")
    private Boolean fixed;


    public PeriodicAmount update(String userId, String nickName, String amount, PriceType priceType, String period, String date, Boolean fixed) {
        this.userId = userId;
        this.nickName = nickName;
        this.amount = amount;
        this.priceType = priceType;
        this.period = period;
        this.date = date;
        this.fixed = fixed;

        return this;
    }
}
