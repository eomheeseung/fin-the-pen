package project.fin_the_pen.codefAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import project.fin_the_pen.codefAPI.api.CompanyAPILogic;
import project.fin_the_pen.codefAPI.domain.company.*;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodefCompanyService {
    private final CompanyAPILogic apiLogic;

    /**
     * 계좌 비밀번호 검증
     * @param dto
     * @return
     */
    public String accountVerification(AccountVerificationDTO dto) {
        JSONParser parser = new JSONParser();
        Object obj = null;

        try {
            obj = parser.parse(apiLogic.accountVerification(dto));
        } catch (ParseException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        JSONObject jsonObject = (JSONObject) obj;
        String resResultCode = (String) jsonObject.get("resResultCode");

        log.info(resResultCode);

        if (resResultCode.equals("0")) {
            return "error";
        } else {
            return "true";
        }
    }

    /**
     * 대출 거래 내역
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String loanTransaction(LoanTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.loanTransaction(dto);
        return result;
    }

    /**
     * 보유 계좌
     * @param organization
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String accountList(String organization) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.accountList(organization);
        return result;
    }

    /**
     * 빠른조회
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
     * @param dto
     * @return
     */
    public String accountTransactionList(CompanyAccountTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.accountTransactionList(dto);
        return result;
    }

    /**
     * 외화 거래 내역
     * @param dto
     * @return
     */
    public String exchange(CompanyExchangeDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.exchange(dto);
        return result;
    }

    /**
     * 적금 거래 내역
     * @param dto
     * @return
     */
    public String installmentSaving(InstallmentSavingDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.installmentSaving(dto);
        return result;
    }

    /**
     * 펀드 거래내역
     * @param dto
     * @return
     */
    public String fundTransaction(FundDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.fundTransaction(dto);
        return result;
    }

}
