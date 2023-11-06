package project.fin_the_pen.thirdparty.codefAPI.dto.bank.individual;

import lombok.Getter;

@Getter
public class OccasionalPastDTO implements IndividualDTO {
    private String connectedId;
    private String organization;
    private String account;
    private String startDate;
    private String endDate;
    private String orderBy;
}
