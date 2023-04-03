package project.fin_the_pen.codefAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import project.fin_the_pen.codefAPI.api.IndividualAPILogic;
import project.fin_the_pen.codefAPI.domain.individual.FastAccountDTO;
import project.fin_the_pen.codefAPI.domain.individual.OccasionalDTO;
import project.fin_the_pen.codefAPI.domain.individual.OccasionalPastDTO;
import project.fin_the_pen.codefAPI.domain.individual.SavingTransactionDTO;
import project.fin_the_pen.codefAPI.repository.TokenRepository;
import project.fin_the_pen.data.token.Token;

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
 *
 * !!!! 프론트에서 필요한 정보를
 * parse => 1. jsonObject로 만들어서 필요한 정보만 던지면 됨.
 *          2. errorCode가 있는 경우 ex 처리함.
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CodefIndividualService {
    private final TokenRepository tokenRepository;
    private Token token;
    private final IndividualAPILogic apiLogic;

    public List<Token> findToken() {
        List<Token> list = tokenRepository.findToken();
        list.forEach(t -> log.info(t.getAccessToken()));
        return list;
    }

    /**
     * 어차피 토큰 1개만 저장된다는 보장이 있음.
     */
    public void accountCreate() throws RuntimeException {
        try {
            apiLogic.accountRegister();
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 InvalidKeySpecException | BadPaddingException | InvalidKeyException | IOException | ParseException |
                 InterruptedException e) {
            throw new RuntimeException(e);
        }
        /*token = tokenRepository.findOneToken();
        apiLogic.accountRegister(token.getAccessToken());*/
    }

    public String registerStatus(String organizationCode, String birth, String withdrawAccountNo, String withdrawAccountPassword) throws RuntimeException {
        JSONParser jsonParser = new JSONParser();
        Object obj = null;

        try {
            obj = jsonParser.parse(apiLogic.registrationStatus(organizationCode, birth, withdrawAccountNo, withdrawAccountPassword));
        } catch (ParseException | IOException | InterruptedException e) {
            return "error";
        }


        JSONObject jsonObj = (JSONObject) obj;
        String status = (String) jsonObj.get("resRegistrationStatus");
        log.info(status);

        if (status.equals("0")) {
            return status;
        } else {
            return "true";
        }
    }

    /**
     * 보유계좌
     * @param organization
     * @param birthDate
     * @param withdrawAccountNo
     * @param withdrawAccountPassword
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String accountList(String organization, String birthDate, String withdrawAccountNo, String withdrawAccountPassword)
            throws IOException, ParseException, InterruptedException {
        JSONParser jsonParser = new JSONParser();
        Object obj = null;

        obj = jsonParser.parse(apiLogic.accountList(organization, birthDate, withdrawAccountNo, withdrawAccountPassword));
        JSONObject jsonObject = (JSONObject) obj;

        return jsonObject.toJSONString();
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
        String result = apiLogic.fastAccountList(dto);
        return result;
    }

    /**
     * 수시입출 거래내역
     * TODO 1. 파싱을 해서 entityManager 넣어야 함.
     * => list에 들어가 있는 것들을 어떻게 파싱할 것인지...
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String occasionalAccount(OccasionalDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.occasionalAccount(dto);
        JSONParser parser = new JSONParser();
        Object obj = null;

        JSONObject jsonObject = (JSONObject) parser.parse(result);
        Object returnObject = jsonObject.get("resTrHistoryList");


        return result;
    }

    /**
     * 수시입출 과거 내역
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
