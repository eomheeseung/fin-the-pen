package project.fin_the_pen.thirdparty.codefAPI.dto.bank.etc;

import lombok.Getter;
import project.fin_the_pen.thirdparty.codefAPI.dto.IntegratedDTO;

@Getter
public class AuthenticationDTO implements IntegratedDTO {
    private String organization;
    private String account;
    private String inPrintType;
    private String inPrintContent;
}
