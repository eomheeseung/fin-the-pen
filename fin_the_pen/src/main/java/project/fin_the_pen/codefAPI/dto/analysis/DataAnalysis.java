package project.fin_the_pen.codefAPI.dto.analysis;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "account_table")
public class DataAnalysis {
    // 고유키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_table_id")
    private Long id;

    // 계좌번호
    @Column
    private String resAccount;

    // 계좌번호 표시용
    @Column
    private String resAccountDisplay;

    // 계좌명
    @Column
    private String resAccountName;

    // 계좌별칭
    @Column
    private String resAccountNickName;

    // 예금주
    @Column
    private String resAccountHolder;

    // 신규일
    @Column
    private String resAccountStartDate;

    //관리지점
    @Column
    private String resManagementBranch;

    // 계좌 상태
    @Column
    private String resAccountStatus;
    // 최종거래일
    @Column
    private String resLastTranDate;
    // 대출만기일
    @Column
    private String resLoanEndDate;
    // 대출한도
    @Column
    private String resLoanLimitAmt;
    // 대출이율
    @Column
    private String resInterestRate;
    // 현재 잔액
    @Column
    private String resAccountBalance;
    // 출금가능 금액
    @Column
    private String resWithdrawalAmt;

    // 시작일자
    @Column
    private String commStartDate;
    // 종료 일자
    @Column
    private String commEndDate;

    // 여기부터는 resTrHistoryList내부의 object안에 있음...

    // 거래일자
    @Column
    private String resAccountTrDate;
    // 거래시각
    @Column
    private String resAccountTrTime;
    // 출금금액
    @Column
    private String resAccountOut;
    // 입금금액
    @Column
    private String resAccountIn;
    // 거래내역 비고 1
    @Column
    private String resAccountDesc1;
    // 거래내역 비고 2
    @Column
    private String resAccountDesc2;
    // 거래내역 비고 3
    @Column
    private String resAccountDesc3;
    // 거래내역 비고 4
    // 거래점
    @Column
    private String resAccountDesc4;
    // 거래 후 잔액
    @Column
    private String resAfterTranBalance;
}
