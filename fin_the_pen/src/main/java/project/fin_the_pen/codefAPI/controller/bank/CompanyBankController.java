package project.fin_the_pen.codefAPI.controller.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.codefAPI.dto.bank.company.*;
import project.fin_the_pen.codefAPI.dto.company.*;
import project.fin_the_pen.codefAPI.service.bank.CodefCompanyService;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CompanyBankController {
    private final CodefCompanyService companyService;
    /**
     * 계좌 비밀번호 검증
     */
    @GetMapping("codef/company-accountVerification")
    public String accountVerification(@RequestBody AccountVerificationDTO dto) {
        String result = companyService.accountVerification(dto);
        return result;
    }

    /**
     * 대출 거래내역
     */
    @GetMapping("codef/company-loan-transaction-list")
    public String loanTransaction(@RequestBody LoanTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = companyService.loanTransaction(dto);
        return result;
    }

    /**
     * 보유 계좌
     */
    @GetMapping("codef/company-account-list")
    public String companyAccount(String organization) throws IOException, ParseException, InterruptedException {
        String result = companyService.accountList(organization);
        return result;
    }

    /**
     * 빠른조회
     */
    @GetMapping("codef/company-transaction-list")
    public String companyTransaction(@RequestBody CompanyTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = companyService.accountTransaction(dto);
        return result;
    }

    /**
     * 수시 입출 거래 내역
     */
    @GetMapping("codef/company-account/transaction-list")
    public String companyAccountTransaction(@RequestBody CompanyAccountTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = companyService.accountTransactionList(dto);
        return result;
    }

    /**
     * 외화 거래 내역
     */
    @GetMapping("codef/company-exchange")
    public String companyExchange(@RequestBody CompanyExchangeDTO dto) throws IOException, ParseException, InterruptedException {
        String result = companyService.exchange(dto);
        return result;
    }

    /**
     * 적금 거래 내역
     */
    @GetMapping("codef/company-installment-saving")
    public String companyInstallmentSaving(@RequestBody InstallmentSavingDTO dto) throws IOException, ParseException, InterruptedException {
        String result = companyService.installmentSaving(dto);
        return result;
    }

    /**
     * 펀드 거래내역
     */
    @GetMapping("codef/compny-fund")
    public String companyFund(@RequestBody FundDTO dto) throws IOException, ParseException, InterruptedException {
        String result = companyService.fundTransaction(dto);
        return result;
    }
}
