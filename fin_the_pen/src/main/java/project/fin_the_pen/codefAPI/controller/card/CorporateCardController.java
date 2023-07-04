package project.fin_the_pen.codefAPI.controller.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.codefAPI.dto.card.corporateCard.*;
import project.fin_the_pen.codefAPI.service.card.CodefCardCorporateService;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CorporateCardController {
    private final CodefCardCorporateService cardCorporateService;

    /**
     * 당일 승인내역
     */
    @GetMapping("codef/the-day-approval-list")
    public String cardTheDayApprovalList(@RequestBody CardDayApprovalListDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardCorporateService.cardDayApproval(dto);
        return result;
    }

    /**
     * 매입내역
     */
    @GetMapping("codef/account/purchase-details")
    public String cardPurchaseDetails(@RequestBody CardPurchaseDetailsDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardCorporateService.cardPurchaseDetails(dto);
        return result;
    }

    /**
     * 보유카드
     */
    @GetMapping("codef/account/card-list")
    public String CardCorporateList(@RequestBody CardCorporateListDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardCorporateService.corporateList(dto);
        return result;
    }

    /**
     * 승인내역
     */
    @GetMapping("codef/account/approval-list")
    public String corporateApproval(@RequestBody CorporateApprovalDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardCorporateService.cardApproval(dto);
        return result;
    }

    /**
     * 청구내역
     */
    @GetMapping("codef/account/billing-list")
    public String corporateBilling(@RequestBody CorporateBillingDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardCorporateService.corporateBilling(dto);
        return result;
    }

    /**
     * 한도조회
     */
    @GetMapping("codef/corporate/account/limit")
    public String corporateAccountLimit(@RequestBody CorporateLimitDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardCorporateService.corporateLimit(dto);
        return result;
    }

    /**
     * 가맹점번호 조회
     */
    @GetMapping("codef/corporate-number")
    public String corporateNumber(@RequestBody CorporateNumberDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardCorporateService.corporateNumber(dto);
        return result;
    }
}
