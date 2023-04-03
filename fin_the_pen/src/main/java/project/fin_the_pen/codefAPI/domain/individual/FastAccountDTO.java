package project.fin_the_pen.codefAPI.domain.individual;

import lombok.Getter;

@Getter
public class FastAccountDTO implements IndividualDTO {
    private String organization;
    private String id;
    private String password;
    private String fastId;
    private String fastPassword;
    private String account;
    private String accountPassword;
    private String startDate;
    private String endDate;
    private String orderBy;
    private String identity;
    private String smsAuthNo;
}
