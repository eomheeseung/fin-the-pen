package project.fin_the_pen.data.analysis;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DataAnalysis {
    @Id
    @Column(name = "resAccount")
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

    //TODO 2. list에 해당 class들에 포함된 것을 넣어야 함.
    @Column
    @ElementCollection(fetch = FetchType.LAZY)
    private List<ResTrHistory> resTrHistoryList = new ArrayList<>();
    @Column
    private String commStartDate;
    @Column
    private String commEndDate;
}
