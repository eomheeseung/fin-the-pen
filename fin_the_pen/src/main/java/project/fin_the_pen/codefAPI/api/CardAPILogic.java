package project.fin_the_pen.codefAPI.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import project.fin_the_pen.codefAPI.dto.card.individualcard.*;
import project.fin_the_pen.codefAPI.repository.DataAnalysisRepository;
import project.fin_the_pen.codefAPI.util.APIRequest;
import project.fin_the_pen.codefAPI.util.CommonConstant;

import java.io.IOException;
import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class CardAPILogic {
    private final DataAnalysisRepository repository;

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
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/card/p/member-store/detail";

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("reqMemberStoreNoList", dto.getReqMemberStoreNoList().getReqMemberStoreNo());

        String result = APIRequest.request(urlPath, registerMap);

        log.info(result);
        return result;
    }

    public String memberStoreBulk(CardMemberStoreBulkDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/card/p/member-store/bulk/inflow";

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("birthDate", dto.getBirthDate());
        registerMap.put("reqMemberStoreNoList", dto.getReqMemberStoreNoList().getReqMemberStoreNo());

        String result = APIRequest.request(urlPath, registerMap);

        log.info(result);
        return result;
    }

    /**
     * 등록여부
     *
     * @param dto
     * @return
     */
    public String cardRegistration(CardRegistrationStatusDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/card/p/user/registration-status";

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("inquiryType", dto.getInquiryType());
        registerMap.put("identity", dto.getIdentity());
        registerMap.put("birthDate", dto.getBirthDate());
        registerMap.put("userId", dto.getUserId());
        registerMap.put("cardNo", dto.getCardNo());
        registerMap.put("cardPassword", dto.getCardPassword());
        registerMap.put("cardValidPeriod", dto.getCardValidPeriod());


        String result = APIRequest.request(urlPath, registerMap);

        log.info(result);
        return result;
    }

    /**
     * 보유카드
     *
     * @param dto
     * @return
     */
    public String cardAccountList(CardAccountListDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_CD_P_001;

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("cardNo", dto.getCardNo());
        registerMap.put("cardPassword", dto.getCardPassword());
        registerMap.put("birthDate", dto.getBirthDate());
        registerMap.put("inquiryType", dto.getInquiryType());


        String result = APIRequest.request(urlPath, registerMap);

        log.info(result);
        return result;
    }

    /**
     * 승인내역
     *
     * @param dto
     * @return
     */
    public String cardApprovalList(CardApprovalListDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_CD_P_002;

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("birthDate", dto.getBirthDate());
        registerMap.put("startDate", dto.getStartDate());
        registerMap.put("endDate", dto.getEndDate());
        registerMap.put("orderBy", dto.getOrderBy());
        registerMap.put("inquiryType", dto.getInquiryType());
        registerMap.put("cardName", dto.getCardName());
        registerMap.put("duplicateCardIdx", dto.getDuplicateCardIdx());
        registerMap.put("cardNo", dto.getCardNo());
        registerMap.put("cardPassword", dto.getCardPassword());


        String result = APIRequest.request(urlPath, registerMap);

        repository.DataApproval(parseMethod(result));

        log.info(result);
        return result;
    }

    /**
     * 실적조회
     *
     * @param dto
     * @return
     */
    public String cardResultCheckList(CardResultCheckListDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/card/p/account/result-check-list";

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("birthDate", dto.getBirthDate());
        registerMap.put("cardNo", dto.getCardNo());
        registerMap.put("cardPassword", dto.getCardPassword());


        String result = APIRequest.request(urlPath, registerMap);

        repository.resultCheck(parseMethod(result));
        log.info(result);
        return result;
    }

    /**
     * 청구내역
     *
     * @param dto
     * @return
     */
    public String cardBillingList(CardBillingListDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_CD_P_003;

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("birthDate", dto.getBirthDate());
        registerMap.put("startDate", dto.getStartDate());
        registerMap.put("memberStoreInfoYN", dto.getMemberStoreInfoYN());
        registerMap.put("cardNo", dto.getCardNo());
        registerMap.put("cardPassword", dto.getCardPassword());


        String result = APIRequest.request(urlPath, registerMap);
        repository.BillingList(parseMethod(result));

        log.info(result);
        return result;
    }

    /**
     * 한도 조회
     *
     * @param dto
     * @return
     */
    public String cardAccountLimit(CardAccountLimitDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_CD_P_004;

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("birthDate", dto.getBirthDate());
        registerMap.put("cardNo", dto.getCardNo());
        registerMap.put("cardPassword", dto.getCardPassword());

        String result = APIRequest.request(urlPath, registerMap);

        repository.AmountLimit(parseMethod(result));

        log.info(result);
        return result;

    }

    /**
     * 후불하이패스 이용내역
     *
     * @param dto
     * @return
     */
    public String cardHiPass(CardHiPassDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/card/p/deferred-payment/hi-pass";

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("birthDate", dto.getBirthDate());
        registerMap.put("startDate", dto.getStartDate());
        registerMap.put("endDate", dto.getEndDate());
        registerMap.put("orderBy", dto.getOrderBy());
        registerMap.put("inquiryType", dto.getInquiryType());
        registerMap.put("cardNo", dto.getCardNo());
        registerMap.put("cardPassword", dto.getCardPassword());
        registerMap.put("cardName", dto.getCardName());
        registerMap.put("duplicateCardIdx", dto.getDuplicateCardIdx());

        String result = APIRequest.request(urlPath, registerMap);
        repository.HiPass(parseMethod(result));

        log.info(result);
        return result;
    }

    private JSONObject parseMethod(String result) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) parser.parse(result);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

}
