package project.fin_the_pen.codefAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.codefAPI.domain.bank2.Bank2AccountDTO;
import project.fin_the_pen.codefAPI.domain.bank2.Bank2AccountTransactionDTO;
import project.fin_the_pen.codefAPI.domain.company.*;
import project.fin_the_pen.codefAPI.domain.etc.AuthenticationDTO;
import project.fin_the_pen.codefAPI.domain.etc.HolderAuthDTO;
import project.fin_the_pen.codefAPI.domain.etc.HolderDTO;
import project.fin_the_pen.codefAPI.domain.individual.*;
import project.fin_the_pen.codefAPI.service.*;
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
 * 프론트에서 이벤트 트리거가 발생하면 여기서 받아서 보낼것임
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class CodefController {
    private final CodefPublishTokenService tokenService;
    private final CodefIndividualService apiService;
    private final CodefCompanyService companyService;
    private final EtcAPIService etcAPIService;
    private final Bank2APIService bank2APIService;

    /**
     * accessTokenPublish
     * 일주일에 1번 발급 받으면 되고, DB에 넣어놓을 것임.
     */
    @GetMapping("codef/accessTokenPublish")
    public void codefAccessToken() {
        String clientId = "4e08356c-1940-4c69-9ec9-1d55f12c3cf2";
        String clientSecret = "9ea5d443-4699-41bf-bbe6-27db50e2dee8";

        tokenService.init(clientId, clientSecret);
    }

    /**
     * 그냥 토큰 DB에 잘 들어갔나 확인하는 코드
     * @return
     */
    @GetMapping("codef/findToken")
    public List<Token> codefFindToken() {
        return apiService.findToken();
    }

    /*@GetMapping("codef/accountCreate")
    public void codefAccountCreate() {
        apiService.accountCreate();
    }*/

    /**
     * connectedId 발급(해결!)
     * @param list
     */
    @GetMapping("codef/accountCreate")
    public void codefAccountCreate(@RequestBody accountList list) {
        apiService.accountCreate(list);
    }

    /**
     * 등록여부 확인(해결!)
     *
     * @param dto
     * @return
     */
    @GetMapping("codef/registerStatus")
    public String registerStatus(@RequestBody AccountDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiService.registerStatus(dto);

        if (result.equals("error")) {
            return "system error";
        }
        if (result.equals("true")) {
            return "true";
        } else {
            return result;
        }
    }

    /**
     * 보유계좌 (해결)
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    @GetMapping("codef/accountList")
    public String accountList(@RequestBody AccountDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiService.accountList(dto);
        return result;
    }

    /**
     * 빠른 조회
     *
     * @param dto
     * @return
     */
    @GetMapping("codef/fastAccount")
    public String fastAccount(@RequestBody FastAccountDTO dto) throws IOException, ParseException, InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String result = apiService.fastAccountList(dto);
        return result;
    }

    /**
     * 수시입출 거래내역
     */
    @GetMapping("codef/occasionalAccount")
    public String occasionalAccount(@RequestBody OccasionalDTO dto) throws IOException, ParseException, InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String result = apiService.occasionalAccount(dto);
        return result;
    }

    /**
     * 수시입출 과거 거래내역
     */
    @GetMapping("codef/occasionalPast")
    public String occasionalPast(@RequestBody OccasionalPastDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiService.occasionalPast(dto);
        return result;
    }

    /**
     * 적금 거래내역
     */
    @GetMapping("codef/saving-transaction")
    public String savingTransaction(@RequestBody SavingTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiService.savingTransaction(dto);
        return result;
    }

    //================ 기업 ================

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

    // =========== 기타 ==========

    /**
     * 계좌인증 (1원 이체)
     */
    @GetMapping("codef/account-authentication")
    public String accountAuthentication(@RequestBody AuthenticationDTO dto) throws IOException, ParseException, InterruptedException {
        String result = etcAPIService.authentication(dto);
        return result;
    }

    /**
     * 예금주명
     */
    @GetMapping("codef/holder")
    public String holder(@RequestBody HolderDTO dto) throws IOException, ParseException, InterruptedException {
        String result = etcAPIService.holder(dto);
        return result;
    }

    /**
     * 예금주명 인증 (계좌 실명 인증)
     */
    @GetMapping("codef/holder-authentication")
    public String holderAuthentication(@RequestBody HolderAuthDTO dto) throws IOException, ParseException, InterruptedException {
        String result = etcAPIService.holderAuth(dto);
        return result;
    }
    // ========= 저축은행 ==========

    /**
     * 보유계좌
     */
    @GetMapping("codef/bank2/account")
    public String bank2Account(@RequestBody Bank2AccountDTO dto) throws IOException, ParseException, InterruptedException {
        String result = bank2APIService.account(dto);
        return result;
    }

    /**
     * 수시입출 거래내역
     *
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    @GetMapping("codef/bank2/account/transaction-list")
    public String bank2AccountTransaction(@RequestBody Bank2AccountTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = bank2APIService.accountTransaction(dto);
        return result;
    }
}
