package project.fin_the_pen.codefAPI.dto.card.corporateCard;

import lombok.Getter;

@Getter
public class CorporateLimitDTO {
    private String organization;
    private String connectedId;
    private String identity;
    private String loginTypeLevel;
    private String cardNo;
    private String departmentCode;
    private String cvc;
    private String clientTypeLevel;
    private String inquiryType;
}
