package project.fin_the_pen.data.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*TODO 계좌 table
   1. column을 어떻게 설정할 것인지
   2-1 각 사용자의 여러 계좌를 어떻게 끌어올 것인지
   2-2 front에서 값을 넘겨줄 것인지
   2-3 back에서 db 조회한 다음에 category별로 계산할 것인지.

 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String api_tran_id;
    private String rsp_code;
    private String rsp_message;
    private String api_tran_dtm;
    private String bank_tran_id;
    private String bank_tran_date;
    private String bank_code_tran;
    private String bank_rsp_code;
    private String bank_rsp_message;
    private String fintech_use_num;
    private int balance_amt;
    private int available_amt;
    private int account_type;
    private String product_name;
    private String bank_name;
    private String account_issue_date;
    private String maturity_date;
    private String last_tran_date;
}
