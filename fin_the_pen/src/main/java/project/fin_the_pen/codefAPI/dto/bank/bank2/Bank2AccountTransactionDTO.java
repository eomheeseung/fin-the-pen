package project.fin_the_pen.codefAPI.dto.bank.bank2;

import lombok.Getter;
import project.fin_the_pen.codefAPI.dto.IntegratedDTO;

@Getter
public class Bank2AccountTransactionDTO implements IntegratedDTO {
    private String organization;
    private String bankName;
    private String account;
    private String startDate;
    private String endDate;
    private String orderBy;
    private String inquiryType;
}
