package project.fin_the_pen.codefAPI.dto.bank.individual;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AccountReferenceAddList {
    private String connectedId;
    private String countryCode;
    private String businessType;
    private String organization;
    private String clientType;

    private List<ReferenceAddDTO> accountList = new ArrayList<>();
}
