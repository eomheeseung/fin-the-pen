package project.fin_the_pen.thirdparty.codefAPI.dto.bank.company;

import lombok.Getter;

@Getter
public class AccountVerificationDTO implements CompanyDTO {
    private String organization;
    private String withdrawAccountNo;
    private String withdrawAccountPassword;
    private String transferPassword;
}
