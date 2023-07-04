package project.fin_the_pen.codefAPI.controller.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.codefAPI.dto.card.individualcard.*;
import project.fin_the_pen.codefAPI.dto.individualcard.*;
import project.fin_the_pen.codefAPI.service.card.CodefCardIndividualService;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@RestController
public class IndividualCardController {
    private final CodefCardIndividualService cardIndividualService;
    /**
     * 가맹점 정보
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    @GetMapping("codef/card/member-store/detail")
    public String cardMemberStoreDetail(@RequestBody CardMemberStoreDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardIndividualService.memberStoreDetail(dto);
        return result;
    }

    /**
     * 가맹점정보 수집 대상 등록
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    @GetMapping("codef/card/member_store/bulk")
    public String cardMemberStoreBulk(@RequestBody CardMemberStoreBulkDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardIndividualService.memberStoreBulk(dto);
        return result;
    }

    /**
     * 등록여부
     * 현대카드 : 0302, 우리카드 : 0309, KB카드 : 0301
     */
    @GetMapping("codef/card/user/registration-status")
    public String cardUserRegistrationStatus(@RequestBody CardRegistrationStatusDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardIndividualService.cardRegistration(dto);
        return result;
    }

    /**
     * 보유카드
     */
    @GetMapping("codef/card/account/card-list")
    public String cardAccountList(@RequestBody CardAccountListDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardIndividualService.cardAccountList(dto);
        return result;
    }

    /**
     * 승인내역
     */
    @GetMapping("codef/card/approval-list")
    public String cardApprovalList(@RequestBody CardApprovalListDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardIndividualService.cardApprovalList(dto);
        return result;
    }

    /**
     * 실적조회
     */
    @GetMapping("codef/card/result-check-list")
    public String cardResultCheckList(@RequestBody CardResultCheckListDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardIndividualService.cardResultCheckList(dto);
        return result;
    }

    /**
     * 청구내역
     */
    @GetMapping("codef/card/billing-list")
    public String cardBillingList(@RequestBody CardBillingListDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardIndividualService.cardBillingList(dto);
        return result;
    }

    /**
     * 한도조회
     */
    @GetMapping("codef/card/account/limit")
    public String cardAccountLimit(@RequestBody CardAccountLimitDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardIndividualService.cardAccountLimit(dto);
        return result;
    }

    /**
     * 후불하이패스 이용내역
     */
    @GetMapping("codef/deferred-payment/hi-pass")
    public String cardHiPass(@RequestBody CardHiPassDTO dto) throws IOException, ParseException, InterruptedException {
        String result = cardIndividualService.cardHiPass(dto);
        return result;
    }
}
