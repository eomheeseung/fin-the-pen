package project.fin_the_pen.codefAPI.domain.etc;

import lombok.Getter;
import project.fin_the_pen.codefAPI.domain.IntegratedDTO;

@Getter
public class HolderDTO implements IntegratedDTO {
    private String organization;
    private String account;
}
