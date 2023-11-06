package project.fin_the_pen.model.thirdparty.codef.entity.analysis;

import lombok.Builder;

import javax.persistence.*;

/**
 * 청구내역
 */
@Entity
@Table(name = "billing_list")
public class DataBilling {
    public DataBilling() {
    }

    @Builder
    public DataBilling(String resTotalAmount, String resPaymentAccount, String resFullAmt, String resInstallmentAmt, String resMinDepositAmt, String resAmountOutstanding, String resLateFee, String resCashService, String resCardLoan, String resRevolving, String resAnnualFee, String resPreWithdrawal, String resOverseasUse, String resWithdrawalDueDate, String resPaymentDueDate, String resFee, String resMemberStoreName, String resUsedAmount, String resPaymentType, String resInstallmentMonth, String resRoundNo, String resPaymentAmt, String resAfterPaymentBalance, String resEarnPoint, String resUsedCard, String resPaymentPrincipal, String resMemberStoreType, String resUsedDate) {
        this.resTotalAmount = resTotalAmount;
        this.resPaymentAccount = resPaymentAccount;
        this.resFullAmt = resFullAmt;
        this.resInstallmentAmt = resInstallmentAmt;
        this.resMinDepositAmt = resMinDepositAmt;
        this.resAmountOutstanding = resAmountOutstanding;
        this.resLateFee = resLateFee;
        this.resCashService = resCashService;
        this.resCardLoan = resCardLoan;
        this.resRevolving = resRevolving;
        this.resAnnualFee = resAnnualFee;
        this.resPreWithdrawal = resPreWithdrawal;
        this.resOverseasUse = resOverseasUse;
        this.resWithdrawalDueDate = resWithdrawalDueDate;
        this.resPaymentDueDate = resPaymentDueDate;
        this.resFee = resFee;
        this.resMemberStoreName = resMemberStoreName;
        this.resUsedAmount = resUsedAmount;
        this.resPaymentType = resPaymentType;
        this.resInstallmentMonth = resInstallmentMonth;
        this.resRoundNo = resRoundNo;
        this.resPaymentAmt = resPaymentAmt;
        this.resAfterPaymentBalance = resAfterPaymentBalance;
        this.resEarnPoint = resEarnPoint;
        this.resUsedCard = resUsedCard;
        this.resPaymentPrincipal = resPaymentPrincipal;
        this.resMemberStoreType = resMemberStoreType;
        this.resUsedDate = resUsedDate;
    }

    @Id
    @GeneratedValue
    @Column(name = "billing_list_id")
    private Long id;

    @Column
    private String resTotalAmount;
    @Column
    private String resPaymentAccount;
    @Column
    private String resFullAmt;
    @Column
    private String resInstallmentAmt;
    @Column
    private String resMinDepositAmt;
    @Column
    private String resAmountOutstanding;
    @Column
    private String resLateFee;
    @Column
    private String resCashService;
    @Column
    private String resCardLoan;
    @Column
    private String resRevolving;
    @Column
    private String resAnnualFee;
    @Column
    private String resPreWithdrawal;
    @Column
    private String resOverseasUse;
    @Column
    private String resWithdrawalDueDate;
    @Column
    private String resPaymentDueDate;

    // resChargeHistoryList 내부에 있는 값
    @Column
    private String resFee;
    @Column
    private String resMemberStoreName;
    @Column
    private String resUsedAmount;
    @Column
    private String resPaymentType;
    @Column
    private String resInstallmentMonth;
    @Column
    private String resRoundNo;
    @Column
    private String resPaymentAmt;
    @Column
    private String resAfterPaymentBalance;
    @Column
    private String resEarnPoint;
    @Column
    private String resUsedCard;
    @Column
    private String resPaymentPrincipal;
    @Column
    private String resMemberStoreType;
    @Column
    private String resUsedDate;
}
