package project.fin_the_pen.codefAPI.domain.bank2;

import lombok.Getter;
import project.fin_the_pen.codefAPI.domain.IntegratedDTO;

@Getter
public class Bank2AccountDTO implements IntegratedDTO {
    private String organization;
    private String bankName;
}
