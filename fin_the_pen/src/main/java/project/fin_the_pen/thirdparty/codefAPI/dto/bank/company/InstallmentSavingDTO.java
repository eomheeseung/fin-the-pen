package project.fin_the_pen.thirdparty.codefAPI.dto.bank.company;

import lombok.Getter;

@Getter
public class InstallmentSavingDTO implements CompanyDTO {
    private String organization;
    private String account;
    private String startDate;
    private String endDate;
    private String orderBy;
    private String inquiryType;
}
