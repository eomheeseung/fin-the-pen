package project.fin_the_pen.codefAPI.dto.bank.etc;

import lombok.Getter;
import project.fin_the_pen.codefAPI.dto.IntegratedDTO;

@Getter
public class HolderAuthDTO implements IntegratedDTO {
    private String organization;
    private String account;
    private String identity;
}
