package project.fin_the_pen.codefAPI.dto.bank.individual;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class createDTO implements IndividualDTO {
    @Builder
    public createDTO(String countryCode, String businessType, String clientType, String organization, String loginType, String id, String password, String birthDate, String loginTypeLevel, String clientTypeLevel, String cardNo, String cardPassword) {
        this.countryCode = countryCode;
        this.businessType = businessType;
        this.clientType = clientType;
        this.organization = organization;
        this.loginType = loginType;
        this.id = id;
        this.password = password;
        this.birthDate = birthDate;
        this.loginTypeLevel = loginTypeLevel;
        this.clientTypeLevel = clientTypeLevel;
        this.cardNo = cardNo;
        this.cardPassword = cardPassword;
    }

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
