package project.fin_the_pen.thirdparty.codefAPI.service.bank;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import project.fin_the_pen.thirdparty.codefAPI.dto.bank.etc.AuthenticationDTO;
import project.fin_the_pen.thirdparty.codefAPI.dto.bank.etc.HolderAuthDTO;
import project.fin_the_pen.thirdparty.codefAPI.dto.bank.etc.HolderDTO;
import project.fin_the_pen.thirdparty.codefAPI.logic.EtcAPILogic;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EtcAPIService {
    private final EtcAPILogic apiLogic;
    private final ObjectMapper objectMapper;
    private JSONParser parser = new JSONParser();

    /**
     * 계좌 인증 (1원이체)
     * <p>
     * 좀 더 세밀하게 jsonNode라는 것을 사용함.
     */
    public JSONObject authentication(AuthenticationDTO dto) throws ParseException, InterruptedException, IOException {
        String jsonResult = apiLogic.authentication(dto);

        JsonNode targetJson = objectMapper.readTree(jsonResult);
        String responseCode = targetJson.get("result").get("code").asText();
        JsonNode authCodeJson = targetJson.get("data");

        JSONObject responseJson = new JSONObject();

        if ("CF-00000".equals(responseCode)) {
            responseJson.put("data", authCodeJson.get("authCode").asText());
        } else {
            responseJson.put("data", "error");
        }

        return responseJson;
    }

    /**
     * 예금주명
     */
    public JSONObject holder(HolderDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.holder(dto);

        JsonNode targetJson = objectMapper.readTree(result);
        String responseCode = targetJson.get("result").get("code").asText();
        JsonNode dataJson = targetJson.get("data");

        JSONObject responseJson = new JSONObject();

        if ("CF-00000".equals(responseCode)) {
            responseJson.put("data", dataJson.get("name").asText());
        } else {
            responseJson.put("data", "예금주명이 등록되지 않았습니다.");
        }
        return responseJson;
    }

    /**
     * 예금주명 인증 (계좌 실명 인증) X
     * 기능이 동작하지 않음.
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String holderAuth(HolderAuthDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.holderAuth(dto);
        return result;
    }
}
