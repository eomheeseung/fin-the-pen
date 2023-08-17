package project.fin_the_pen.codefAPI.dto.bank.individual;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OccasionalDTO implements IndividualDTO {
    private String connectedId;
    private String organization;
    private String account;
    private String startDate;
    private String endDate;
    private String orderBy;
    private String inquiryType;
    private String accountPassword;
    private String birthDate;
    private String withdrawAccountNo;
    private String withdrawAccountPassword;
}
