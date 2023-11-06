package project.fin_the_pen.thirdparty.codefAPI.controller.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.thirdparty.codefAPI.dto.bank.company.*;
import project.fin_the_pen.thirdparty.codefAPI.service.bank.CodefCompanyService;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/codef/company")
public class CompanyBankController {
    private final CodefCompanyService companyService;

    /**
     * 계좌 비밀번호 검증
     */
    @GetMapping("/accountVerification")
    public String accountVerification(@RequestBody AccountVerificationDTO dto) {
        JSONObject responseJson = companyService.accountVerification(dto);

        if (responseJson.get("data").equals("false")) {
            return "false";
        }else{
            return "true";
        }
    }

    /**
     * 대출 거래내역
     */
    @GetMapping("/loan-transaction-list")
    public JSONObject loanTransaction(@RequestBody LoanTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        return companyService.loanTransaction(dto);
    }

    /**
     * 보유 계좌
     */
    @GetMapping("/account-list")
    public JSONObject companyAccount(String organization) throws IOException, ParseException, InterruptedException {
        return companyService.accountList(organization);
    }

    /**
     * 빠른조회
     */
    @GetMapping("/transaction-list")
    public String companyTransaction(@RequestBody CompanyTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = companyService.accountTransaction(dto);
        return result;
    }

    /**
     * 수시 입출 거래 내역
     */
    @GetMapping("/account/transaction-list")
    public String companyAccountTransaction(@RequestBody CompanyAccountTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = companyService.accountTransactionList(dto);
        return result;
    }

    /**
     * 외화 거래 내역
     */
    @GetMapping("/exchange")
    public String companyExchange(@RequestBody CompanyExchangeDTO dto) throws IOException, ParseException, InterruptedException {
        String result = companyService.exchange(dto);
        return result;
    }

    /**
     * 적금 거래 내역
     */
    @GetMapping("/installment-saving")
    public String companyInstallmentSaving(@RequestBody InstallmentSavingDTO dto) throws IOException, ParseException, InterruptedException {
        String result = companyService.installmentSaving(dto);
        return result;
    }

    /**
     * 펀드 거래내역
     */
    @GetMapping("/fund")
    public String companyFund(@RequestBody FundDTO dto) throws IOException, ParseException, InterruptedException {
        String result = companyService.fundTransaction(dto);
        return result;
    }
}
