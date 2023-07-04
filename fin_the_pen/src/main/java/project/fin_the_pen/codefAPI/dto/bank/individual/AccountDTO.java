package project.fin_the_pen.codefAPI.dto.bank.individual;

import lombok.Getter;

@Getter
public class AccountDTO implements IndividualDTO {
    // jsonProperty 써야할 수도...
    private String connectedId;
    private String organization;
    private String birthDate;
    private String withdrawAccountNo;
    private String withdrawAccountPassword;
}
