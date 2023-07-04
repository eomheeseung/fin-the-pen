package project.fin_the_pen.codefAPI.dto.card.individualcard;

import lombok.Getter;

@Getter
public class CardHiPassDTO {
    private String organization;
    private String connectedId;
    private String birthDate;
    private String startDate;
    private String endDate;
    private String orderBy;
    private String inquiryType;
    private String cardNo;
    private String cardPassword;
    private String cardName;
    private String duplicateCardIdx;
}
