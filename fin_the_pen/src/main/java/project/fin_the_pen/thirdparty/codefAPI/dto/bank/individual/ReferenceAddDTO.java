package project.fin_the_pen.thirdparty.codefAPI.dto.bank.individual;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ReferenceAddDTO {
    private String countryCode;
    private String businessType;
    private String organization;
    private String clientType;
    private String birthDate;
    private String identity;
    private String userName;
    private String loginTypeLevel;
    private String clientTypeLevel;
    private String withdrawAccountNo;
    private String withdrawAccountPassword;
    private String cardNo;
    private String cardPassword;

    @Builder
    public ReferenceAddDTO(String countryCode, String businessType,
                           String organization, String clientType, String birthDate,
                           String identity, String userName, String loginTypeLevel,
                           String clientTypeLevel, String withdrawAccountNo, String withdrawAccountPassword,
                           String cardNo, String cardPassword) {
        this.countryCode = countryCode;
        this.businessType = businessType;
        this.organization = organization;
        this.clientType = clientType;
        this.birthDate = birthDate;
        this.identity = identity;
        this.userName = userName;
        this.loginTypeLevel = loginTypeLevel;
        this.clientTypeLevel = clientTypeLevel;
        this.withdrawAccountNo = withdrawAccountNo;
        this.withdrawAccountPassword = withdrawAccountPassword;
        this.cardNo = cardNo;
        this.cardPassword = cardPassword;
    }
}
