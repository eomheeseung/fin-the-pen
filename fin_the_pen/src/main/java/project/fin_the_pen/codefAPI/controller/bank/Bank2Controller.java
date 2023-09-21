package project.fin_the_pen.codefAPI.controller.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.codefAPI.dto.bank.bank2.Bank2AccountDTO;
import project.fin_the_pen.codefAPI.dto.bank.bank2.Bank2AccountTransactionDTO;
import project.fin_the_pen.codefAPI.service.bank.Bank2APIService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
/*
 */
public class Bank2Controller {
    private final Bank2APIService bank2APIService;
    /**
     * 보유계좌
     */
    @GetMapping("/codef/bank2/account")
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
    @GetMapping("/codef/bank2/account/transaction-list")
    public String bank2AccountTransaction(@RequestBody Bank2AccountTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = bank2APIService.accountTransaction(dto);
        return result;
    }
}
