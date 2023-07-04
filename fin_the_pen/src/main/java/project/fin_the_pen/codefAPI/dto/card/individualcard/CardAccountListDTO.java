package project.fin_the_pen.codefAPI.dto.card.individualcard;

import lombok.Getter;

@Getter
public class CardAccountListDTO {
    private String organization;
    private String connectedId;
    private String cardNo;
    private String cardPassword;
    private String birthDate;
    private String inquiryType;

}
