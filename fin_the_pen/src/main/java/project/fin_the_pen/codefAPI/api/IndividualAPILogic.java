package project.fin_the_pen.codefAPI.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import project.fin_the_pen.codefAPI.dto.IntegratedDTO;
import project.fin_the_pen.codefAPI.dto.bank.individual.*;
import project.fin_the_pen.codefAPI.repository.DataAnalysisRepository;
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
@RequiredArgsConstructor
public class IndividualAPILogic implements APILogicInterface {
    private String accessToken;
    private final ObjectMapper mapper = new ObjectMapper();
    private final DataAnalysisRepository repository;

    @Override
    public HashMap<String, Object> registerMap(IntegratedDTO dto, HashMap<String, Object> registerMap) {

        if (dto instanceof FastAccountDTO) {
            FastAccountDTO transDto = (FastAccountDTO) dto;
            String password = null;
            String accountPassword = null;

            try {
                password = RSAUtil.encryptRSA(transDto.getPassword(), CommonConstant.PUBLIC_KEY);
                accountPassword = RSAUtil.encryptRSA(transDto.getAccountPassword(), CommonConstant.PUBLIC_KEY);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                     IllegalBlockSizeException | BadPaddingException e) {
                throw new RuntimeException(e);
            }

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("id", transDto.getId());
            registerMap.put("password", password);
            registerMap.put("fastId", transDto.getFastId());
            registerMap.put("fastPassword", transDto.getFastPassword());
            registerMap.put("account", transDto.getAccount());
            registerMap.put("accountPassword", accountPassword);
            registerMap.put("startDate", transDto.getStartDate());
            registerMap.put("endDate", transDto.getEndDate());
            registerMap.put("orderBy", transDto.getOrderBy());
            registerMap.put("identity", transDto.getIdentity());
            registerMap.put("smsAuthNo", transDto.getSmsAuthNo());
            return registerMap;

        } else if (dto instanceof OccasionalDTO) {
            OccasionalDTO transDto = (OccasionalDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("connectedId", transDto.getConnectedId());
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
            registerMap.put("connectedId", transDto.getConnectedId());
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

    // TODO 1. connectedId 기관 아이디
    public void accountRegister(AccountList dto)
            throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            InvalidKeySpecException, BadPaddingException, InvalidKeyException,
            IOException, ParseException, InterruptedException {

        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/account/create";

        HashMap<String, Object> bodyMap = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        log.info(dto.getAccountList().get(0).getLoginTypeLevel());
        HashMap<String, Object> accountMap2 = new HashMap<String, Object>();
        accountMap2.put("countryCode", dto.getAccountList().get(0).getCountryCode());
        accountMap2.put("businessType", dto.getAccountList().get(0).getBusinessType());
        accountMap2.put("clientType", dto.getAccountList().get(0).getClientType());
        accountMap2.put("organization", dto.getAccountList().get(0).getOrganization());
        accountMap2.put("loginType", dto.getAccountList().get(0).getLoginType());

        //은행마다 기관코드가 다름, 우리은행 0020, 국민은행 0004
        /*
         로그인 제한 직전에는 "99"를 표시하고, 오류 횟수 체크가 안 되는 경우 빈 값으로 내려옵니다.
         */
        String password2 = dto.getAccountList().get(0).getPassword();
        accountMap2.put("password", RSAUtil.encryptRSA(password2, CommonConstant.PUBLIC_KEY));    /**    password RSA encrypt */

        accountMap2.put("id", dto.getAccountList().get(0).getId());
        accountMap2.put("birthday", dto.getAccountList().get(0).getBirthDate());
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

    public void accountRegister()
            throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            InvalidKeySpecException, BadPaddingException, InvalidKeyException,
            IOException, ParseException, InterruptedException {

        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/account/create";

        HashMap<String, Object> bodyMap = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> accountMap2 = new HashMap<String, Object>();
        accountMap2.put("countryCode", "KR");
        accountMap2.put("businessType", "BK");
        accountMap2.put("clientType", "P");
        accountMap2.put("organization", "0004");
        accountMap2.put("loginType", "1");

        String password2 = "fomuller@12";
        accountMap2.put("password", RSAUtil.encryptRSA(password2, CommonConstant.PUBLIC_KEY));    /**    password RSA encrypt */

        accountMap2.put("id", "ulass8846");
        accountMap2.put("birthday", "990109");
        list.add(accountMap2);

        bodyMap.put("accountList", list);

        String result = APIRequest.request(urlPath, bodyMap);
        log.info(result);
    }

    /**
     * 등록여부 확인
     *
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String registrationStatus(AccountDTO dto)
            throws IOException, ParseException, InterruptedException {

        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/bank/p/user/registration-status";

        HashMap<String, Object> registerMap = getRegisterMap(new HashMap<>(), dto);

        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
        return result;
    }

    /**
     * 보유계좌
     *
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String accountList(AccountDTO dto)
            throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_P_001;

        HashMap<String, Object> registerMap = getRegisterMap(new HashMap<>(), dto);

        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);

        return result;
    }

    /**
     * 등록여부와 보유계좌의 내부 logic 똑같아서 extract
     *
     * @param map
     * @return
     */
    private HashMap<String, Object> getRegisterMap(HashMap<String, Object> map,
                                                   AccountDTO dto) {

//        String encodingPw = null;
//
//        try {
//            encodingPw = RSAUtil.encryptRSA(dto.getWithdrawAccountPassword(), CommonConstant.PUBLIC_KEY);
//        } catch (NoSuchPaddingException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalBlockSizeException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeySpecException e) {
//            throw new RuntimeException(e);
//        } catch (BadPaddingException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeyException e) {
//            throw new RuntimeException(e);
//        }
        map.put("connectedId", dto.getConnectedId());
        map.put("organization", dto.getOrganization());
        map.put("birthDate", dto.getBirthDate());
        map.put("withdrawAccountNo", dto.getWithdrawAccountNo());
        map.put("withdrawAccountPassword", dto.getWithdrawAccountPassword());
        return map;
    }

    /**
     * 빠른 조회
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String fastAccountList(FastAccountDTO dto) throws IOException, ParseException, InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_P_005;

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());

        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
        return result;
    }


    /**
     * 수시 입출 거래내역 (data를 보내야 하는 부분)
     *
     * @param dto
     * @return
     */
    public String occasionalAccount(OccasionalDTO dto) throws IOException, ParseException, InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_P_002;

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());

        String result = APIRequest.request(urlPath, registerMap);

        //string -> jsonObject
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(result);
        // 여기는 result부터 ~~
        log.info(jsonObject.toJSONString());

        // 여기는 data부터 ~~
        JSONObject dataJson = (JSONObject) jsonObject.get("data");
        log.info(dataJson.toString());

        // 여기는 data내부의 resTrHistoryList를 jsonArray로 바꿔서 0번째 jsonObject를 log에 찍음
        JSONArray historyList = (JSONArray) dataJson.get("resTrHistoryList");
        log.info(historyList.get(0).toString());

        repository.dataAnalysis(jsonObject);
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
