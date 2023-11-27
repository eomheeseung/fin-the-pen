package project.fin_the_pen.thirdparty.codefAPI.service.connectedId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import project.fin_the_pen.thirdparty.codefAPI.dto.bank.individual.*;
import project.fin_the_pen.model.thirdparty.codef.repository.ConnectedRepository;
import project.fin_the_pen.model.thirdparty.codef.entity.IndividualConnectedID;
import project.fin_the_pen.thirdparty.codefAPI.util.APIRequest;
import project.fin_the_pen.thirdparty.codefAPI.util.CommonConstant;
import project.fin_the_pen.thirdparty.codefAPI.util.RSAUtil;

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
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConnectedLogic {
    private final ConnectedRepository connectedRepository;

    /**
     * connectedId 발급
     *
     * @param dto
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
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

//        log.info(result);

        /*JSONParser parser = new JSONParser();
        Object obj = parser.parse(result);
        JSONObject jsonObject = (JSONObject) obj;

        // static final이라 상수인데 어떻게 connected_id에 넣을 것인가?
        CommonConstant.CONNECTED_ID = (String) jsonObject.get("connectedId");*/

        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) parser.parse(result);

        log.info(jsonObject.get("result").toString());
        log.info("\n");
        log.info(jsonObject.get("data").toString());
        JSONObject innerJson = (JSONObject) jsonObject.get("data");

        IndividualConnectedID individualConnectedID = new IndividualConnectedID();
        individualConnectedID.setConnectedId(innerJson.get("connectedId").toString());


        connectedRepository.save(individualConnectedID);
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
     * 계정 추가
     * 커넥티드 아이디를 생성한 엔드 유저가 새로운 기관의 계정 정보를 추가하고 싶은 경우
     *
     * @param addList
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public void accountAdd(AccountAddList addList) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.ADD_ACCOUNT;

        List<CreateDTO> accountList = addList.getAccountList();

        addList.getAccountList().add(accountList.stream().iterator().next());


        HashMap<String, Object> bodyMap = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> accountMap1 = new HashMap<String, Object>();
        accountMap1.put("countryCode", addList.getAccountList().get(0).getCountryCode());  // 국가코드
        accountMap1.put("businessType", addList.getAccountList().get(0).getBusinessType());  // 업무구분코드
        accountMap1.put("clientType", addList.getAccountList().get(0).getClientType());   // 고객구분(P: 개인, B: 기업)
        accountMap1.put("organization", addList.getAccountList().get(0).getOrganization());// 기관코드
        accountMap1.put("loginType", addList.getAccountList().get(0).getLoginType());   // 로그인타입 (0: 인증서, 1: ID/PW)

        String password1 = addList.getAccountList().get(0).getPassword();

        try {
            accountMap1.put("password", RSAUtil.encryptRSA(password1, CommonConstant.PUBLIC_KEY));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }    /**    password RSA encrypt */

        if (addList.getAccountList().get(0).getLoginType().toString().equals("0")) {
            accountMap1.put("keyFile", "BASE64로 Encoding된 엔드유저의 인증서 key파일 문자열");
            accountMap1.put("derFile", "BASE64로 Encoding된 엔드유저의 인증서 der파일 문자열");
        }

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
        accountMap1.put("countryCode", dto.getAccountList().get(0).getCountryCode());  // 국가코드
        accountMap1.put("businessType", dto.getAccountList().get(0).getBusinessType());  // 업무구분코드
        accountMap1.put("clientType", dto.getAccountList().get(0).getClientType());   // 고객구분(P: 개인, B: 기업)
        accountMap1.put("organization", dto.getAccountList().get(0).getOrganization());// 기관코드
        accountMap1.put("loginType", dto.getAccountList().get(0).getLoginType());   // 로그인타입 (0: 인증서, 1: ID/PW)
        list.add(accountMap1);

        bodyMap.put("accountList", list);

        String connectedId = dto.getConnectedId();
        bodyMap.put(CommonConstant.CONNECTED_ID, connectedId);

        String result = APIRequest.request(urlPath, bodyMap);
        log.info(result);
    }

    /**
     * 계정 수정
     * 등록된 엔드 유저의 계정 정보를 수정
     * 인증서 갱신/재발급, 비밀번호 변경 등 계정등록시 사용된 계정정보가 변경된 경우,
     * 기존 발급된 커넥티드 아이디에 해당하는 계정정보를 변경된 계정정보로 계정수정이 반드시 필요.
     */
    public void accountUpdate(AccountUpdateDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.UPDATE_ACCOUNT;

        HashMap<String, Object> bodyMap = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> accountMap1 = new HashMap<String, Object>();
        accountMap1.put("countryCode", dto.getAccountList().get(0).getCountryCode());  // 국가코드
        accountMap1.put("businessType", dto.getAccountList().get(0).getBusinessType());  // 업무구분코드
        accountMap1.put("clientType", dto.getAccountList().get(0).getClientType());   // 고객구분(P: 개인, B: 기업)
        accountMap1.put("organization", dto.getAccountList().get(0).getOrganization());// 기관코드
        accountMap1.put("loginType", dto.getAccountList().get(0).getLoginType());   // 로그인타입 (0: 인증서, 1: ID/PW)

        String password1 = dto.getAccountList().get(0).getPassword();

        try {
            accountMap1.put("password", RSAUtil.encryptRSA(password1, CommonConstant.PUBLIC_KEY));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }    /**    password RSA encrypt */

        if (dto.getAccountList().get(0).getLoginType().toString().equals("0")) {
            accountMap1.put("keyFile", "BASE64로 Encoding된 엔드유저의 인증서 key파일 문자열");
            accountMap1.put("derFile", "BASE64로 Encoding된 엔드유저의 인증서 der파일 문자열");
        }

        list.add(accountMap1);

        bodyMap.put("accountList", list);

        String connectedId = dto.getConnectedId();
        bodyMap.put(CommonConstant.CONNECTED_ID, connectedId);


        String result = APIRequest.request(urlPath, bodyMap);
        log.info(result);
    }

    /**
     * 계정 출력
     * 등록된 커넥티드 아이디의 계정 목록을 요청.
     *
     * @param
     */
    public void accountOutputList(ConcurrentHashMap<String, String> map) {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.GET_ACCOUNTS;

        HashMap<String, Object> bodyMap = new HashMap<String, Object>();

        String connectedId = map.get("connectedId");
        bodyMap.put(CommonConstant.CONNECTED_ID, connectedId);

        try {
            String result = APIRequest.request(urlPath, bodyMap);
            log.info(result);
        } catch (IOException | InterruptedException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * TODO 계정 추가 - 레퍼런스
     *  계정추가(레퍼런스) -> 한 커넥티드 id로 다른 기관 허용되는 아이디도 통합
     *  1.계정 등록이 완료된 대상기관 정보를 입력합니다. 해당 대상기관의 계정정보 기준으로 추가 기관의 계정이 추가
     *
     *
     * 현재 저장되어 있는 정보(countryCode...)와 추가할 정보(list안에 있는 countryCode..)가 request이름은 같아도 서로 다르다!!
     *
     */
    /*public void accountReferenceAdd(AccountReferenceAddList addList) {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/account/reference-add";

        List<ReferenceAddDTO> accountList = addList.getAccountList();

        addList.getAccountList().add(accountList.stream().iterator().next());


        HashMap<String, Object> bodyMap = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> accountMap1 = new HashMap<String, Object>();
        accountMap1.put("countryCode", addList.getAccountList().get(0).getCountryCode());  // 국가코드
        accountMap1.put("businessType", addList.getAccountList().get(0).getBusinessType());  // 업무구분코드
        accountMap1.put("clientType", addList.getAccountList().get(0).getClientType());   // 고객구분(P: 개인, B: 기업)
        accountMap1.put("organization", addList.getAccountList().get(0).getOrganization());// 기관코드
        accountMap1.put("loginType", addList.getAccountList().get(0).getLoginType());   // 로그인타입 (0: 인증서, 1: ID/PW)

        String password1 = addList.getAccountList().get(0).getPassword();

        try {
            accountMap1.put("password", RSAUtil.encryptRSA(password1, CommonConstant.PUBLIC_KEY));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }    */

    /**
     * password RSA encrypt
     *//*

        if (addList.getAccountList().get(0).getLoginType().toString().equals("0")) {
            accountMap1.put("keyFile", "BASE64로 Encoding된 엔드유저의 인증서 key파일 문자열");
            accountMap1.put("derFile", "BASE64로 Encoding된 엔드유저의 인증서 der파일 문자열");
        }

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
    }*/
    public void accountReferenceAdd(AccountReferenceAddList addList) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/account/reference-add";

        HashMap<String, Object> registerMap = getRegisterMap(new HashMap<>(), addList);

        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
    }

    private HashMap<String, Object> getRegisterMap(HashMap<String, Object> map, AccountReferenceAddList list) {
        map.put("connectedId",list.getConnectedId());
        map.put("countryCode", list.getCountryCode());
        map.put("businessType", list.getBusinessType());
        map.put("organization", list.getOrganization());
        map.put("clientType", list.getClientType());

        map.put("accountList", list.getAccountList());
        return map;
    }
}
