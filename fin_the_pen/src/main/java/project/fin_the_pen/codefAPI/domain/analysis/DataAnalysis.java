package project.fin_the_pen.codefAPI.domain.analysis;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class DataAnalysis {
    @Id
    @GeneratedValue
    @Column(name = "identity_id")
    private Long id;
    @Column
    private String resAccount;
    @Column
    private String resAccountDisplay;
    @Column
    private String resAccountName;
    @Column
    private String resAccountNickName;
    @Column
    private String resAccountHolder;
    @Column
    private String resAccountStartDate;
    @Column
    private String resManagementBranch;
    @Column
    private String resAccountStatus;
    @Column
    private String resLastTranDate;
    @Column
    private String resLoanEndDate;
    @Column
    private String resLoanLimitAmt;
    @Column
    private String resInterestRate;
    @Column
    private String resAccountBalance;
    @Column
    private String resWithdrawalAmt;
    @Column
    private String resAccountTrDate;
    @Column
    private String resAccountTrTime;
    @Column
    private String resAccountOut;
    @Column
    private String resAccountIn;
    @Column
    private String resAccountDesc1;
    @Column
    private String resAccountDesc2;
    @Column
    private String resAccountDesc3;
    @Column
    private String resAccountDesc4;
    @Column
    private String resAfterTranBalance;
    @Column
    private String commStartDate;
    @Column
    private String commEndDate;
}
