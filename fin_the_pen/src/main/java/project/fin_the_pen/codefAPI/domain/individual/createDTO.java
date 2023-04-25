package project.fin_the_pen.codefAPI.domain.individual;

import lombok.Getter;

@Getter
public class createDTO implements IndividualDTO {
    private String countryCode;
    private String businessType;
    private String clientType;
    private String organization;
    private String loginType;
    private String id;
    private String password;
    private String birthDate;
    private String loginTypeLevel;
    private String clientTypeLevel;
    private String cardNo;
    private String cardPassword;



}
