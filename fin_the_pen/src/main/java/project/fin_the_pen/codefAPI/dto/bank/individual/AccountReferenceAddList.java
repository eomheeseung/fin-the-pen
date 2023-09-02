package project.fin_the_pen.codefAPI.dto.bank.individual;

import lombok.Getter;

@Getter
public class AccountReferenceAddList extends AccountList {
    private String connectedId;
    private String countryCode;
    private String organization;
    private String clientType;
}
