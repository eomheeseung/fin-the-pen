package project.fin_the_pen.codefAPI.api;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import project.fin_the_pen.codefAPI.dto.card.corporateCard.*;
import project.fin_the_pen.codefAPI.util.APIRequest;
import project.fin_the_pen.codefAPI.util.CommonConstant;

import java.io.IOException;
import java.util.HashMap;

@Component
@Slf4j
public class CardCorporateAPILogic {

    /**
     * 당일 승인내역
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String cardDayApproval(CardDayApprovalListDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/card/b/account/the-day-approval-list";

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("loginTypeLevel", dto.getLoginTypeLevel());
        registerMap.put("clientTypeLevel", dto.getClientTypeLevel());
        registerMap.put("orderBy", dto.getOrderBy());
        registerMap.put("inquiryType", dto.getInquiryType());
        registerMap.put("cardNo", dto.getCardNo());
        registerMap.put("departmentCode", dto.getDepartmentCode());
        registerMap.put("identity", dto.getIdentity());

        String result = APIRequest.request(urlPath, registerMap);

        log.info(result);
        return result;
    }

    /**
     * 매입내역
     *
     * @param dto
     * @return
     */
    public String cardPurchaseDetails(CardPurchaseDetailsDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/card/p/purchase-details";

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("identity", dto.getIdentity());
        registerMap.put("loginTypeLevel", dto.getLoginTypeLevel());
        registerMap.put("clientTypeLevel", dto.getClientTypeLevel());
        registerMap.put("startDate", dto.getStartDate());
        registerMap.put("endDate", dto.getEndDate());
        registerMap.put("orderBy", dto.getOrderBy());
        registerMap.put("inquiryType", dto.getInquiryType());
        registerMap.put("cardNo", dto.getCardNo());
        registerMap.put("memberStoreInfoType", dto.getMemberSToreInfoType());

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
    public String corporateList(CardCorporateListDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_CD_B_001;

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("identity", dto.getIdentity());
        registerMap.put("loginTypeLevel", dto.getLoginTypeLevel());
        registerMap.put("clientTypeLevel", dto.getClientTypeLevel());


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
    public String cardApproval(CorporateApprovalDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_CD_B_002;

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("identity", dto.getIdentity());
        registerMap.put("loginTypeLevel", dto.getLoginTypeLevel());
        registerMap.put("clientTypeLevel", dto.getClientTypeLevel());
        registerMap.put("startDate", dto.getStartDate());
        registerMap.put("endDate", dto.getEndDate());
        registerMap.put("orderBy", dto.getOrderBy());
        registerMap.put("inquiryType", dto.getInquiryType());
        registerMap.put("cardNo", dto.getCardNo());
        registerMap.put("departmentCode", dto.getDepartmentCode());
        registerMap.put("transeType", dto.getTranseType());
        registerMap.put("cardName", dto.getCardName());
        registerMap.put("duplicateCardIdx", dto.getDuplicateCardIdx());
        registerMap.put("applicationType", dto.getApplicationType());
        registerMap.put("memberStoreInfoType", dto.getMemberStoreInfoType());

        String result = APIRequest.request(urlPath, registerMap);

        log.info(result);
        return result;
    }

    /**
     * 청구내역
     *
     * @param dto
     * @return
     */
    public String corporateBilling(CorporateBillingDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_CD_B_003;

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("identity", dto.getIdentity());
        registerMap.put("loginTypeLevel", dto.getLoginTypeLevel());
        registerMap.put("clientTypeLevel", dto.getClientTypeLevel());
        registerMap.put("startDate", dto.getStartDate());
        registerMap.put("inquiryType", dto.getInquiryType());
        registerMap.put("cardNo", dto.getCardNo());
        registerMap.put("memberStoreInfoType", dto.getMemberStoreInfoType());

        String result = APIRequest.request(urlPath, registerMap);

        log.info(result);
        return result;
    }

    /**
     * 한도조회
     */
    public String corporateLimit(CorporateLimitDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_CD_B_004;

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("connectedId", dto.getConnectedId());
        registerMap.put("identity", dto.getIdentity());
        registerMap.put("loginTypeLevel", dto.getLoginTypeLevel());
        registerMap.put("cardNo", dto.getCardNo());
        registerMap.put("departmentCode", dto.getDepartmentCode());
        registerMap.put("cvc", dto.getCvc());
        registerMap.put("clientTypeLevel", dto.getClientTypeLevel());
        registerMap.put("inquiryType", dto.getInquiryType());

        String result = APIRequest.request(urlPath, registerMap);

        log.info(result);
        return result;
    }


    /**
     * 가맹점번호 조회
     */
    public String corporateNumber(CorporateNumberDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/card/a/member-store/corporate-number";

        HashMap<String, Object> registerMap = new HashMap<>();

        registerMap.put("organization", dto.getOrganization());
        registerMap.put("companyIdentityNo", dto.getCompanyIdentityNo());
        registerMap.put("birthDate", dto.getBirthDate());


        String result = APIRequest.request(urlPath, registerMap);

        log.info(result);
        return result;

    }
}
