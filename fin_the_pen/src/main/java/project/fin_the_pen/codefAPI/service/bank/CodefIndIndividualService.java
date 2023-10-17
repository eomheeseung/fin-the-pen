package project.fin_the_pen.codefAPI.service.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import project.fin_the_pen.codefAPI.dto.bank.individual.*;
import project.fin_the_pen.codefAPI.logic.IndividualAPILogic;
import project.fin_the_pen.codefAPI.repository.TokenRepository;
import project.fin_the_pen.finClient.data.token.Token;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * API를 호출 => 받아온 API 데이터, DB 데이터 를 여기서 처리
 * <p>
 * !!!! 프론트에서 필요한 정보를
 * parse => 1. jsonObject로 만들어서 필요한 정보만 던지면 됨.
 * 2. errorCode가 있는 경우 ex 처리함.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CodefIndIndividualService {
    private final TokenRepository tokenRepository;
    private Token token;
    private final IndividualAPILogic apiLogic;


    public List<Token> findToken() {
        List<Token> list = tokenRepository.findToken();
        list.forEach(t -> log.info(t.getAccessToken()));
        return list;
    }

    /**
     * 등록 여부 확인
     *
     * @param dto
     * @return
     * @throws RuntimeException
     */
    public String registerStatus(AccountDTO dto) throws RuntimeException, IOException, ParseException, InterruptedException {
//        return apiLogic.registrationStatus(dto);
        JSONParser jsonParser = new JSONParser();
        Object obj = null;

        try {
            String logStr = apiLogic.registrationStatus(dto);
            log.info(logStr);
            obj = jsonParser.parse(logStr);
        } catch (ParseException | IOException | InterruptedException | NullPointerException e) {
            throw new RuntimeException("error");
        }

        JSONObject jsonObj = (JSONObject) obj;
        JSONObject innerJson = (JSONObject) jsonObj.get("data");
        String status = innerJson.get("resRegistrationStatus").toString();

        if (status.equals("0")) {
            return status;
        } else {
            return "true";
        }
    }

    /**
     * 보유계좌
     * TODO 1. 이거 objectMapper로 리팩토링하면 오류남.
     * @param dto
     * @return
     * @throws ParseException
     * @throws InterruptedException
     */
    public JSONObject accountList(AccountDTO dto) throws ParseException, InterruptedException, IOException {
        JSONParser jsonParser = new JSONParser();
        Object obj = null;

        obj = jsonParser.parse(apiLogic.accountList(dto));
        JSONObject jsonObject = (JSONObject) obj;

        JSONObject innerJson = (JSONObject) jsonObject.get("result");
        JSONObject responseJson = new JSONObject();

        if (!innerJson.get("code").equals("CF-00000")) {
            return (JSONObject) responseJson.put("data", "error");
        } else {
            JSONObject targetJson = (JSONObject) jsonObject.get("data");
            JSONArray targetArray = (JSONArray) targetJson.get("resDepositTrust");
            JSONArray responseJsonArray = new JSONArray();

            for (Object o : targetArray) {
                JSONObject tmpJson = (JSONObject) o;

                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("resAccountDisplay", tmpJson.get("resAccountDisplay"));
                jsonObject1.put("resAccountName", tmpJson.get("resAccountName"));
                jsonObject1.put("resAccountBalance", tmpJson.get("resAccountBalance"));
                responseJsonArray.add(jsonObject1);
            }

            responseJson.put("data", responseJsonArray);
            return responseJson;
        }
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
        String result = apiLogic.fastAccountList(dto);
        return result;
    }

    /**
     * 수시입출 거래내역
     * => list에 들어가 있는 것들을 어떻게 파싱할 것인지...
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String occasionalAccount(OccasionalDTO dto) throws IOException, ParseException, InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String result = apiLogic.occasionalAccount(dto);
        JSONParser parser = new JSONParser();
        Object obj = null;

        return result;
    }

    /**
     * 수시입출 과거 내역
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String occasionalPast(OccasionalPastDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.occasionalPast(dto);
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
        String result = apiLogic.savingTransaction(dto);
        return result;
    }
}
