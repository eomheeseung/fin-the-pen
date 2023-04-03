package project.fin_the_pen.data.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
