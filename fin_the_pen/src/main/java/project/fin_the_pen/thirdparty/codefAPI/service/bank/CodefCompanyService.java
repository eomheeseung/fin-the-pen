package project.fin_the_pen.thirdparty.codefAPI.service.bank;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import project.fin_the_pen.thirdparty.codefAPI.dto.bank.company.*;
import project.fin_the_pen.thirdparty.codefAPI.logic.CompanyAPILogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodefCompanyService {
    private final CompanyAPILogic apiLogic;
    private final ObjectMapper objectMapper;

    /**
     * 계좌 비밀번호 검증, 가공
     *
     * @param dto
     * @return
     */
    public JSONObject accountVerification(AccountVerificationDTO dto) {
        JSONObject responseJson = null;

        try {
            JsonNode jsonNode = objectMapper.readTree(apiLogic.accountVerification(dto));
            String resultCode = jsonNode.get("result").get("code").asText();

            if (!"CF-00000".equals(resultCode)) {
                responseJson = new JSONObject();
                responseJson.put("data", "error");
            } else {
                String resResultCode = jsonNode.get("data").get("resResultCode").asText();

                if (resResultCode.equals("0")) {
                    responseJson = new JSONObject();
                    responseJson.put("data", "false");
                } else {
                    responseJson = new JSONObject();
                    responseJson.put("data", "true");
                }
            }
        } catch (ParseException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return responseJson;
    }

    /**
     * 대출 거래 내역 가공
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public JSONObject loanTransaction(LoanTransactionDTO dto)
            throws IOException, ParseException, InterruptedException {

        JsonNode jsonNode = objectMapper.readTree(apiLogic.loanTransaction(dto));
        String resultCode = jsonNode.get("result").get("code").asText();
        JSONObject responseJson = null;

        if (!"CF-00000".equals(resultCode)) {
            responseJson = new JSONObject();
            responseJson.put("data", "error");
        } else {
            responseJson = new JSONObject();
            JsonNode dataJson = jsonNode.get("data");

            HashMap<String, Object> responseMap = new HashMap<>();

            // 대출 종류
            responseMap.put("resLoanKind", dataJson.get("resLoanKind").asText());
            // 신규일
            responseMap.put("resAccountStartDate", dataJson.get("resAccountStartDate").asText());
            // 만기일
            responseMap.put("resAccountEndDate", dataJson.get("resAccountEndDate").asText());
            // 대출 잔액
            responseMap.put("resLoanBalance", dataJson.get("resLoanBalance").asText());
            // 원금
            responseMap.put("resPrincipal", dataJson.get("resPrincipal").asText());
            // 계좌번호 표시용
            responseMap.put("resAccountDisplay", dataJson.get("resAccountDisplay").asText());
            // 적용 이율
            responseMap.put("resRate", dataJson.get("resRate").asText());
            //  다음 이자 납입일
            responseMap.put("resDatePayment", dataJson.get("resDatePayment").asText());
            // 연체 여부
            responseMap.put("resState", dataJson.get("resState").asText());

            JsonNode innerJson = dataJson.get("resTrHistoryList");
            HashMap<Object, Object> innerMap = new HashMap<>();

            List<Map> list = new ArrayList<>();

            for (JsonNode node : innerJson) {
                innerMap.put("resLoanBalance", node.get("resLoanBalance").asText());
                innerMap.put("resInterestRate", node.get("resInterestRate").asText());
                innerMap.put("resTranAmount", node.get("resTranAmount").asText());
                innerMap.put("resTransTypeNm", node.get("resTransTypeNm").asText());
                innerMap.put("resAccountTrDate", node.get("resAccountTrDate").asText());
                innerMap.put("commEndDate", node.get("commEndDate").asText());
                innerMap.put("commStartDate", node.get("commStartDate").asText());
                list.add(innerMap);
                innerMap.clear();
            }

            responseMap.put("resTrHistoryList", list);
            responseMap.put("commStartDate", dataJson.get("commStartDate").asText());
            responseMap.put("commEndDate", dataJson.get("commEndDate").asText());

            responseJson.put("data", responseMap);
        }

        return responseJson;
    }

    /**
     * 보유 계좌 가공
     *
     * @param organization
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public JSONObject accountList(String organization)
            throws IOException, ParseException, InterruptedException {
        String result = apiLogic.accountList(organization);

        JsonNode targetNode = objectMapper.readTree(result);
        JsonNode codeNode = targetNode.get("result").get("code");
        JSONObject responseJson = null;

        if (!"CF-00000".equals(codeNode.get("code"))) {
            responseJson = new JSONObject();
            responseJson.put("data", "error");
        } else {
            responseJson = new JSONObject();
            responseJson.put("data", targetNode.get("data"));
        }
        return responseJson;
    }

    /**
     * 빠른조회
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String accountTransaction(CompanyTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.accountTransaction(dto);
        return result;
    }

    /**
     * 수시 입출 거래 내외
     *
     * @param dto
     * @return
     */
    public String accountTransactionList(CompanyAccountTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.accountTransactionList(dto);
        return result;
    }

    /**
     * 외화 거래 내역
     *
     * @param dto
     * @return
     */
    public String exchange(CompanyExchangeDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.exchange(dto);
        return result;
    }

    /**
     * 적금 거래 내역
     *
     * @param dto
     * @return
     */
    public String installmentSaving(InstallmentSavingDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.installmentSaving(dto);
        return result;
    }

    /**
     * 펀드 거래내역
     *
     * @param dto
     * @return
     */
    public String fundTransaction(FundDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.fundTransaction(dto);
        return result;
    }

}
