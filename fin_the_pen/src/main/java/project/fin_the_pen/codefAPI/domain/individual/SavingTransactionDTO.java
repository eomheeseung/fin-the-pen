package project.fin_the_pen.codefAPI.domain.individual;

import lombok.Getter;

@Getter
public class SavingTransactionDTO implements IndividualDTO {
    private String organization;
    private String account;
    private String startDate;
    private String endDate;
    private String orderBy;
    private String inquiryType;
    private String birthDate;
    private String withdrawAccountNo;
    private String withdrawAccountPassword;
}
