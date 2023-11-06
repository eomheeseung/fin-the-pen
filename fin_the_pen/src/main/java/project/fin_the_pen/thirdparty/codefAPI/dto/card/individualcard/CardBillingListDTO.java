package project.fin_the_pen.thirdparty.codefAPI.dto.card.individualcard;

import lombok.Getter;

@Getter
public class CardBillingListDTO {
    private String organization;
    private String connectedId;
    private String birthDate;
    private String startDate;
    private String memberStoreInfoYN;
    private String cardNo;
    private String cardPassword;
}
