package project.fin_the_pen.thirdparty.codefAPI.dto.bank.company;

import lombok.Getter;

@Getter
public class CompanyTransactionDTO implements CompanyDTO {
    private String organization;
    private String fastId;
    private String fastPassword;
    private String id;
    private String password;
    private String account;
    private String accountPassword;
    private String startDate;
    private String endDate;
    private String orderBy;
    private String identity;
}
