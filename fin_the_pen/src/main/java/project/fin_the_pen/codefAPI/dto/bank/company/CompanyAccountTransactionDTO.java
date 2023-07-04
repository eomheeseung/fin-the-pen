package project.fin_the_pen.codefAPI.dto.bank.company;

import lombok.Getter;

@Getter
public class CompanyAccountTransactionDTO implements CompanyDTO{
    private String organization;
    private String account;
    private String startDate;
    private String endDate;
    private String orderBy;
    private String inquiryType;
    private String pageCount;

}
