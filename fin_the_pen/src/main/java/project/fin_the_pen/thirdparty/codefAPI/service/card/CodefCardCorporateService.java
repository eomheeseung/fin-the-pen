package project.fin_the_pen.thirdparty.codefAPI.service.card;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import project.fin_the_pen.thirdparty.codefAPI.dto.card.corporateCard.*;
import project.fin_the_pen.thirdparty.codefAPI.logic.CardCorporateAPILogic;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CodefCardCorporateService {
    private final CardCorporateAPILogic apiLogic;


    /**
     * 당일 승인내역
     * @param dto
     * @return
     */
    public String cardDayApproval(CardDayApprovalListDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.cardDayApproval(dto);
    }

    /**
     * 매입 내역
     * @param dto
     * @return
     */
    public String cardPurchaseDetails(CardPurchaseDetailsDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.cardPurchaseDetails(dto);
    }

    /**
     * 보유카드
     * @param dto
     * @return
     */
    public String corporateList(CardCorporateListDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.corporateList(dto);
    }

    /**
     * 승인내역
     * @param dto
     * @return
     */
    public String cardApproval(CorporateApprovalDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.cardApproval(dto);
    }

    /**
     * 청구내역
     * @param dto
     * @return
     */
    public String corporateBilling(CorporateBillingDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.corporateBilling(dto);
    }


    /**
     * 한도조회
     */
    public String corporateLimit(CorporateLimitDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.corporateLimit(dto);
    }

    /**
     * 가맹점번호 조회
     * @param dto
     * @return
     */
    public String corporateNumber(CorporateNumberDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.corporateNumber(dto);
    }
}
