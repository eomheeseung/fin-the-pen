package project.fin_the_pen.codefAPI.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import project.fin_the_pen.codefAPI.domain.*;
import project.fin_the_pen.codefAPI.domain.individual.FastAccountDTO;
import project.fin_the_pen.codefAPI.domain.individual.OccasionalDTO;
import project.fin_the_pen.codefAPI.domain.individual.OccasionalPastDTO;
import project.fin_the_pen.codefAPI.domain.individual.SavingTransactionDTO;
import project.fin_the_pen.codefAPI.util.APIRequest;
import project.fin_the_pen.codefAPI.util.CommonConstant;
import project.fin_the_pen.codefAPI.util.RSAUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 모든 API는 여기서 작성
 */
@Slf4j
@Component
public class IndividualAPILogic implements APILogicInterface{
    private String accessToken;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public HashMap<String, Object> registerMap(IntegratedDTO dto, HashMap<String, Object> registerMap) {
        if (dto instanceof FastAccountDTO) {
            FastAccountDTO transDto = (FastAccountDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("id", transDto.getId());
            registerMap.put("password", transDto.getPassword());
            registerMap.put("fastId", transDto.getFastId());
            registerMap.put("fastPassword", transDto.getFastPassword());
            registerMap.put("account", transDto.getAccount());
            registerMap.put("accountPassword", transDto.getAccountPassword());
            registerMap.put("startDate", transDto.getStartDate());
            registerMap.put("endDate", transDto.getEndDate());
            registerMap.put("orderBy", transDto.getOrderBy());
            registerMap.put("identity", transDto.getIdentity());
            registerMap.put("smsAuthNo", transDto.getSmsAuthNo());
            return registerMap;

        } else if (dto instanceof OccasionalDTO) {
            OccasionalDTO transDto = (OccasionalDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("connectId", CommonConstant.CONNECTED_ID);
            registerMap.put("account", transDto.getAccount());
            registerMap.put("startDate", transDto.getStartDate());
            registerMap.put("endDate", transDto.getEndDate());
            registerMap.put("orderBy", transDto.getOrderBy());
            registerMap.put("inquiryType", transDto.getInquiryType());
            registerMap.put("accountPassword", transDto.getAccountPassword());
            registerMap.put("birthDate", transDto.getBirthDate());
            registerMap.put("withdrawAccountNo", transDto.getWithdrawAccountNo());
            registerMap.put("withdrawAccountPassword", transDto.getWithdrawAccountPassword());
            return registerMap;

        } else if (dto instanceof OccasionalPastDTO) {
            OccasionalPastDTO transDto = (OccasionalPastDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("connectId", CommonConstant.CONNECTED_ID);
            registerMap.put("account", transDto.getAccount());
            registerMap.put("startDate", transDto.getStartDate());
            registerMap.put("endDate", transDto.getEndDate());
            registerMap.put("orderBy", transDto.getOrderBy());
            return registerMap;

        } else if (dto instanceof SavingTransactionDTO) {
            SavingTransactionDTO transDto = (SavingTransactionDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("connectId", CommonConstant.CONNECTED_ID);
            registerMap.put("account", transDto.getAccount());
            registerMap.put("startDate", transDto.getStartDate());
            registerMap.put("endDate", transDto.getEndDate());
            registerMap.put("orderBy", transDto.getOrderBy());
            registerMap.put("inquiryType", transDto.getInquiryType());
            registerMap.put("birthDate", transDto.getBirthDate());
            registerMap.put("withdrawAccountNo", transDto.getWithdrawAccountNo());
            registerMap.put("withdrawAccountPassword", transDto.getWithdrawAccountPassword());
            return registerMap;

        } else {
            return null;
        }
    }

    public void accountRegister()
            throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            InvalidKeySpecException, BadPaddingException, InvalidKeyException,
            IOException, ParseException, InterruptedException {

        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/account/create";

        HashMap<String, Object> bodyMap = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> accountMap1 = new HashMap<String, Object>();
//        accountMap1.put("countryCode", "KR");  // 국가코드
//        accountMap1.put("businessType", "BK");  // 업무구분코드
//        accountMap1.put("clientType", "P");   // 고객구분(P: 개인, B: 기업)
//        accountMap1.put("organization", "0003");// 기관코드
//        accountMap1.put("loginType", "0");   // 로그인타입 (0: 인증서, 1: ID/PW)
//
//        String password1 = "";
//        accountMap1.put("password", RSAUtil.encryptRSA(password1, CommonConstant.PUBLIC_KEY));    /**    password RSA encrypt */
//
//        accountMap1.put("keyFile", "BASE64로 Encoding된 엔드유저의 인증서 key파일 문자열");
//        accountMap1.put("derFile", "BASE64로 Encoding된 엔드유저의 인증서 der파일 문자열");
//        list.add(accountMap1);

        HashMap<String, Object> accountMap2 = new HashMap<String, Object>();
        accountMap2.put("countryCode", "KR");
        accountMap2.put("businessType", "BK");
        accountMap2.put("clientType", "P");
        accountMap2.put("organization", "0004");
        accountMap2.put("loginType", "1");

        //TODO 은행마다 기관코드가 다름, 우리은행 0020, 국민은행 0004
        /*
         로그인 제한 직전에는 "99"를 표시하고, 오류 횟수 체크가 안 되는 경우 빈 값으로 내려옵니다.
         */
        String password2 = "";
        accountMap2.put("password", RSAUtil.encryptRSA(password2, CommonConstant.PUBLIC_KEY));    /**    password RSA encrypt */

        accountMap2.put("id", "");
        accountMap2.put("birthday", "990109");
        list.add(accountMap2);

        bodyMap.put("accountList", list);

        String result = APIRequest.request(urlPath, bodyMap);

        /*JSONParser parser = new JSONParser();
        Object obj = parser.parse(result);
        JSONObject jsonObject = (JSONObject) obj;

        // static final이라 상수인데 어떻게 connected_id에 넣을 것인가?
        CommonConstant.CONNECTED_ID = (String) jsonObject.get("connectedId");*/
        log.info(result);
    }

    /**
     * 등록여부 확인
     *
     * @param organizationCode
     * @param birth
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String registrationStatus(String organizationCode, String birth, String withdrawAccountNo, String withdrawAccountPassword)
            throws IOException, ParseException, InterruptedException {

        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/bank/p/user/registration-status";

        HashMap<String, Object> registerMap = getRegisterMap(new HashMap<>(),
                organizationCode, birth, withdrawAccountNo, withdrawAccountPassword);

        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
        return result;
    }

    /**
     * 보유계좌
     *
     * @param organizationCode
     * @param birth
     * @param withdrawAccountNo
     * @param withdrawAccountPassword
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String accountList(String organizationCode, String birth, String withdrawAccountNo, String withdrawAccountPassword)
            throws IOException, ParseException, InterruptedException {

        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_P_001;

        HashMap<String, Object> registerMap = getRegisterMap(new HashMap<>(),
                organizationCode, birth, withdrawAccountNo, withdrawAccountPassword);

        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);

        return result;
    }

    /**
     * 등록여부와 보유계좌의 내부 logic 똑같아서 extract
     *
     * @param map
     * @param organizationCode
     * @param birth
     * @param withdrawAccountNo
     * @param withdrawAccountPassword
     * @return
     */
    @NotNull
    private HashMap<String, Object> getRegisterMap(HashMap<String, Object> map,
                                                          String organizationCode,
                                                          String birth,
                                                          String withdrawAccountNo,
                                                          String withdrawAccountPassword) {

        map.put("connectedId", CommonConstant.CONNECTED_ID);
        map.put("organizationCode", organizationCode);
        map.put("birthDate", birth);
        map.put("withdrawAccountNo", withdrawAccountNo);
        map.put("withdrawAccountPassword", withdrawAccountPassword);
        return map;
    }

    /**
     * 빠른 조회
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String fastAccountList(FastAccountDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_P_005;

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());

        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
        return result;
    }


    /**
     * 수시 입출 거래내역
     *
     * @param dto
     * @return
     */
    public String occasionalAccount(OccasionalDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_P_002;

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());

        String result = APIRequest.request(urlPath, registerMap);
        return result;
    }


    /**
     * 수시 입출 과거 내역
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String occasionalPast(OccasionalPastDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/bank/p/account/past-transaction-list";

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());

        String result = APIRequest.request(urlPath, registerMap);
        return result;
    }


    /**
     * 적금 거래내역
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String savingTransaction(SavingTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_P_003;

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());

        String result = APIRequest.request(urlPath, registerMap);
        return result;
    }
}
