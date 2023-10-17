package project.fin_the_pen.codefAPI.service.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import project.fin_the_pen.codefAPI.dto.bank.etc.AuthenticationDTO;
import project.fin_the_pen.codefAPI.dto.bank.etc.HolderAuthDTO;
import project.fin_the_pen.codefAPI.dto.bank.etc.HolderDTO;
import project.fin_the_pen.codefAPI.logic.EtcAPILogic;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EtcAPIService {
    private final EtcAPILogic apiLogic;
    private final ObjectMapper objectMapper;

    /**
     * 계좌 인증 (1원이체)
     * TODO 2. 이거 리팩토링 json으로 리턴하게
     */
    public org.json.simple.JSONObject authentication(AuthenticationDTO dto) throws ParseException, InterruptedException, IOException {
        org.json.simple.JSONObject responseJson = null;
        String jsonResult = apiLogic.authentication(dto);
        JSONParser jsonParser = new JSONParser();
        org.json.simple.JSONObject targetJson = (org.json.simple.JSONObject) jsonParser.parse(jsonResult);
        JSONObject innerJson = (org.json.simple.JSONObject) targetJson.get("data");

        if ("CF-00000".equals(targetJson.get("result"))) {
            responseJson = new org.json.simple.JSONObject();
            responseJson.put("data", innerJson.get("authCode"));
            return responseJson;
        } else {
            responseJson = new org.json.simple.JSONObject();
            responseJson.put("data", "error");
            return responseJson;
        }
    }

    /**
     * 예금주명
     */
    public String holder(HolderDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.holder(dto);

        if (result.equals("")) {
            return "예금주명이 등록되지 않았습니다.";
        } else {
            return result;
        }
    }

    /**
     * 예금주명 인증 (계좌 실명 인증)
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
