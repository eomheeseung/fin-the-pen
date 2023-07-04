package project.fin_the_pen.codefAPI.dto.bank.company;

import lombok.Getter;

@Getter
public class CompanyExchangeDTO implements CompanyDTO{
    private String organization;
    private String account;
    private String startDate;
    private String endDate;
    private String orderBy;
    private String currency;
}
