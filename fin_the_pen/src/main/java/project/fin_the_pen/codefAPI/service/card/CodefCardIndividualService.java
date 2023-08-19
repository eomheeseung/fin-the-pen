package project.fin_the_pen.codefAPI.service.card;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import project.fin_the_pen.codefAPI.logic.CardAPILogic;
import project.fin_the_pen.codefAPI.dto.card.individualcard.*;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CodefCardIndividualService {
    private final CardAPILogic apiLogic;

    /**
     * 가맹점 정보
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String memberStoreDetail(CardMemberStoreDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.memberStoreDetail(dto);
    }

    /**
     * 가맹점정보 수집 대상 등록
     */

    public String memberStoreBulk(CardMemberStoreBulkDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.memberStoreBulk(dto);
    }

    /**
     * 등록여부
     *
     * @param dto
     * @return
     */
    public String cardRegistration(CardRegistrationStatusDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.cardRegistration(dto);
    }

    /**
     * 보유 카드
     *
     * @param dto
     * @return
     */
    public String cardAccountList(CardAccountListDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.cardAccountList(dto);
    }

    /**
     * 승인 내역
     *
     * @param dto
     * @return
     */
    public String cardApprovalList(CardApprovalListDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.cardApprovalList(dto);
    }

    /**
     * 실적조회
     *
     * @param dto
     * @return
     */
    public String cardResultCheckList(CardResultCheckListDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.cardResultCheckList(dto);
    }

    /**
     * 청구내역
     *
     * @param dto
     * @return
     */
    public String cardBillingList(CardBillingListDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.cardBillingList(dto);
    }

    /**
     * 한도 조회
     *
     * @param dto
     * @return
     */
    public String cardAccountLimit(CardAccountLimitDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.cardAccountLimit(dto);
    }

    /**
     * 후불하이패스 이용내역
     *
     * @param dto
     * @return
     */
    public String cardHiPass(CardHiPassDTO dto) throws IOException, ParseException, InterruptedException {
        return apiLogic.cardHiPass(dto);
    }


}
