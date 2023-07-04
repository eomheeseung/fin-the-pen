package project.fin_the_pen.codefAPI.controller.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.codefAPI.dto.bank.individual.*;
import project.fin_the_pen.codefAPI.service.bank.CodefIndIndividualService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BankController {
    private final CodefIndIndividualService apiService;

    /**
     * 등록여부 확인
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
     * 보유계좌
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

    // ========= 저축은행 ==========



    //================== 카드 ===============



    //=========== 카드 법인 ===========



    // =========== 가맹점번호 조회 ===========


}
