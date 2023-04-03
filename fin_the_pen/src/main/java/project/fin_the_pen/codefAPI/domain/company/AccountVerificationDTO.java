package project.fin_the_pen.codefAPI.domain.company;

import lombok.Getter;

@Getter
public class AccountVerificationDTO implements CompanyDTO {
    private String organization;
    private String withdrawAccountNo;
    private String withdrawAccountPassword;
    private String transferPassword;
}
