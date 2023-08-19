package project.fin_the_pen.codefAPI.connectedId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import project.fin_the_pen.codefAPI.dto.bank.individual.AccountAddList;
import project.fin_the_pen.codefAPI.dto.bank.individual.AccountDeleteDTO;
import project.fin_the_pen.codefAPI.dto.bank.individual.AccountList;
import project.fin_the_pen.codefAPI.dto.bank.individual.createDTO;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class ConnectedLogic {
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
     * TODO connectedId 계정추가 부분인데 나중에 해결해야 함... 동시성 오류
     * @param addList
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public void accountAdd(AccountAddList addList) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.ADD_ACCOUNT;

        List<createDTO> accountList = addList.getAccountList();

        /*for (createDTO obj : accountList) {

            // Create DTO object and populate fields from accountJson
            createDTO accountDTO = createDTO.builder().countryCode(obj.getCountryCode())
                    .businessType(obj.getBusinessType())
                    .clientType(obj.getClientType())
                    .organization(obj.getOrganization())
                    .loginType(obj.getLoginType())
                    .id(obj.getId())
                    .password(obj.getPassword())
                    .birthDate(obj.getBirthDate())
                    .loginTypeLevel(obj.getLoginTypeLevel())
                    .clientTypeLevel(obj.getClientTypeLevel())
                    .cardNo(obj.getCardNo())
                    .cardPassword(obj.getCardPassword())
                    .build();

            addList.getAccountList().add(accountDTO);
        }*/

        HashMap<String, Object> bodyMap = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> accountMap1 = new HashMap<String, Object>();
        accountMap1.put("countryCode", addList.getAccountList().get(0).getCountryCode());  // 국가코드
        accountMap1.put("businessType", addList.getAccountList().get(0).getBusinessType());  // 업무구분코드
        accountMap1.put("clientType", addList.getAccountList().get(0).getClientType());   // 고객구분(P: 개인, B: 기업)
        accountMap1.put("organization", addList.getAccountList().get(0).getOrganization());// 기관코드
        accountMap1.put("loginType", addList.getAccountList().get(0).getLoginTypeLevel());   // 로그인타입 (0: 인증서, 1: ID/PW)

        String password1 = addList.getAccountList().get(0).getPassword();

        try {
            accountMap1.put("password", RSAUtil.encryptRSA(password1, CommonConstant.PUBLIC_KEY));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }    /**    password RSA encrypt */

//        accountMap1.put("keyFile", "BASE64로 Encoding된 엔드유저의 인증서 key파일 문자열");
//        accountMap1.put("derFile", "BASE64로 Encoding된 엔드유저의 인증서 der파일 문자열");
        list.add(accountMap1);

        bodyMap.put("accountList", list);

        String connectedId = addList.getConnectedId();
        bodyMap.put(CommonConstant.CONNECTED_ID, connectedId);

        try {
            String result = APIRequest.request(urlPath, bodyMap);
            log.info(result);
            log.info("success");
        } catch (Exception e) {
            throw new RuntimeException("fail");
        }
    }

    /**
     * 계정 삭제 - 성공
     */
    public void accountDelete(AccountDeleteDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.DELETE_ACCOUNT;

        HashMap<String, Object> bodyMap = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> accountMap1 = new HashMap<String, Object>();
        accountMap1.put("countryCode",	dto.getAccountList().get(0).getCountryCode());  // 국가코드
        accountMap1.put("businessType",	dto.getAccountList().get(0).getBusinessType());  // 업무구분코드
        accountMap1.put("clientType",  	dto.getAccountList().get(0).getClientType());   // 고객구분(P: 개인, B: 기업)
        accountMap1.put("organization",	dto.getAccountList().get(0).getOrganization());// 기관코드
        accountMap1.put("loginType",  	dto.getAccountList().get(0).getLoginType());   // 로그인타입 (0: 인증서, 1: ID/PW)
        list.add(accountMap1);

        bodyMap.put("accountList", list);

        String connectedId = dto.getConnectedId();
        bodyMap.put(CommonConstant.CONNECTED_ID, connectedId);

        String result = APIRequest.request(urlPath, bodyMap);
        log.info(result);
    }
}
