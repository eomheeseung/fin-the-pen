package project.fin_the_pen.codefAPI.dto.card.individualcard;

import lombok.Getter;

@Getter
public class CardMemberStoreBulkDTO {
    private String organization;
    private String birthDate;
    private ReqMemberStoreNoList reqMemberStoreNoList;

}
