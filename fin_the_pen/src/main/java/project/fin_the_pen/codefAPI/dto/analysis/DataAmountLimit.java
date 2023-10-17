package project.fin_the_pen.codefAPI.dto.analysis;

import lombok.Builder;

import javax.persistence.*;

/**
 * 한도조회
 */
@Entity
@Table(name = "amount_limit")
public class DataAmountLimit {
    public DataAmountLimit() {
    }
    @Builder
    public DataAmountLimit(String resCardCompany, String resUsedAmount, String resLimitAmount, String resRemainLimit, String resFullUsedAmount, String resFullLimitAmount, String resFullRemainLimit, String resInstallUsedAmount, String resInstallLimitAmount, String resInstallRemainLimit, String resShortUsedAmount, String resShortLimitAmount, String resShortRemainLimit) {
        this.resCardCompany = resCardCompany;
        this.resUsedAmount = resUsedAmount;
        this.resLimitAmount = resLimitAmount;
        this.resRemainLimit = resRemainLimit;
        this.resFullUsedAmount = resFullUsedAmount;
        this.resFullLimitAmount = resFullLimitAmount;
        this.resFullRemainLimit = resFullRemainLimit;
        this.resInstallUsedAmount = resInstallUsedAmount;
        this.resInstallLimitAmount = resInstallLimitAmount;
        this.resInstallRemainLimit = resInstallRemainLimit;
        this.resShortUsedAmount = resShortUsedAmount;
        this.resShortLimitAmount = resShortLimitAmount;
        this.resShortRemainLimit = resShortRemainLimit;
    }

    @Id
    @GeneratedValue
    @Column(name = "account_limit_id")
    private Long id;

    @Column
    private String resCardCompany;

    // resLimitOfTotalList 내부에 있는 값
    @Column
    private String resUsedAmount;
    @Column
    private String resLimitAmount;
    @Column
    private String resRemainLimit;

    // resLimitOfFullTotalList 내부에 있는 값
    @Column
    private String resFullUsedAmount;
    @Column
    private String resFullLimitAmount;
    @Column
    private String resFullRemainLimit;

    // resLimitOfInstallmentList 내부에 있는 값
    @Column
    private String resInstallUsedAmount;
    @Column
    private String resInstallLimitAmount;
    @Column
    private String resInstallRemainLimit;

    // resLimitOfShortLoanList 내부에 있는 값
    @Column
    private String resShortUsedAmount;
    @Column
    private String resShortLimitAmount;
    @Column
    private String resShortRemainLimit;

}
