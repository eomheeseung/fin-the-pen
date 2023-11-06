package project.fin_the_pen.model.thirdparty.codef.entity.analysis;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 승인 내역
 */
@Entity
@Table(name = "card_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DataApproval {

    @Builder
    public DataApproval(String resUsedDate, String resUsedTime, String resCardNo, String resCardName, String resUsedAmount, String resPaymentType, String resInstallmentMonth, String resPaymentDueDate, String resHomeForeignType, String resMemberStoreType, String resCancelYN, String resCancelAmount, String resVAT, String resCashBack, String resKRWAmt) {
        this.resUsedDate = resUsedDate;
        this.resUsedTime = resUsedTime;
        this.resCardNo = resCardNo;
        this.resCardName = resCardName;
        this.resUsedAmount = resUsedAmount;
        this.resPaymentType = resPaymentType;
        this.resInstallmentMonth = resInstallmentMonth;
        this.resPaymentDueDate = resPaymentDueDate;
        this.resHomeForeignType = resHomeForeignType;
        this.resMemberStoreType = resMemberStoreType;
        this.resCancelYN = resCancelYN;
        this.resCancelAmount = resCancelAmount;
        this.resVAT = resVAT;
        this.resCashBack = resCashBack;
        this.resKRWAmt = resKRWAmt;
    }

    @Id
    @GeneratedValue
    @Column(name = "cart_table_id")
    private Long id;

    @Column
    private String resUsedDate;

    @Column
    private String resUsedTime;

    @Column
    private String resCardNo;

    /*@Column
    private String resCardNo1;*/

    @Column
    private String resCardName;

    /*@Column
    private String resMemberStoreName;*/

    @Column
    private String resUsedAmount;

    @Column
    private String resPaymentType;

    @Column
    private String resInstallmentMonth;

    /*@Column
    private String resApprovalNo;*/

    @Column
    private String resPaymentDueDate;

    @Column
    private String resHomeForeignType;

    /*@Column
    private String resMemberStoreCorpNo;*/

    @Column
    private String resMemberStoreType;

    /*@Column
    private String resMemberStoreTelNo;

    @Column
    private String resMemberStoreAddr;

    @Column
    private String resMemberStoreNo;*/

    @Column
    private String resCancelYN;

    @Column
    private String resCancelAmount;

    @Column
    private String resVAT;

    @Column
    private String resCashBack;

    @Column
    private String resKRWAmt;

    /*@Column
    private String commStartDate;

    @Column
    private String commEndDate;

    @Column
    private String resAccountCurrency;*/
}
