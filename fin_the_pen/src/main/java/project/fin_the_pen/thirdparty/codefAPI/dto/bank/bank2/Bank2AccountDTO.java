package project.fin_the_pen.thirdparty.codefAPI.dto.bank.bank2;

import lombok.Getter;
import project.fin_the_pen.thirdparty.codefAPI.dto.IntegratedDTO;

@Getter
public class Bank2AccountDTO implements IntegratedDTO {
    private String organization;
    private String bankName;
}
