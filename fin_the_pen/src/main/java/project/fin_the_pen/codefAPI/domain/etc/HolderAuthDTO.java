package project.fin_the_pen.codefAPI.domain.etc;

import lombok.Getter;
import project.fin_the_pen.codefAPI.domain.IntegratedDTO;

@Getter
public class HolderAuthDTO implements IntegratedDTO {
    private String organization;
    private String account;
    private String identity;
}
