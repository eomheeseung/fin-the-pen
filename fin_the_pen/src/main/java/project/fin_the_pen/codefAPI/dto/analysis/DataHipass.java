package project.fin_the_pen.codefAPI.dto.analysis;

import lombok.Builder;

import javax.persistence.*;

/**
 * 하이패스
 */
@Entity
@Table(name = "Hi_pass_payment")
public class DataHipass {
    public DataHipass() {
    }

    @Builder
    public DataHipass(String resUsedDate, String resCardNo, String resMemberStoreName, String resUsedAmount, String resDepartureTime, String resArrivalTime, String resDepartureStation, String resArrivalStation, String resPaymentDueDate) {
        this.resUsedDate = resUsedDate;
        this.resCardNo = resCardNo;
        this.resMemberStoreName = resMemberStoreName;
        this.resUsedAmount = resUsedAmount;
        this.resDepartureTime = resDepartureTime;
        this.resArrivalTime = resArrivalTime;
        this.resDepartureStation = resDepartureStation;
        this.resArrivalStation = resArrivalStation;
        this.resPaymentDueDate = resPaymentDueDate;
    }

    @Id
    @GeneratedValue
    @Column(name = "hi_pass_id")
    private Long id;

    @Column
    private String resUsedDate;
    @Column
    private String resCardNo;
    @Column
    private String resMemberStoreName;
    @Column
    private String resUsedAmount;
    @Column
    private String resDepartureTime;
    @Column
    private String resArrivalTime;
    @Column
    private String resDepartureStation;
    @Column
    private String resArrivalStation;
    @Column
    private String resPaymentDueDate;
}
