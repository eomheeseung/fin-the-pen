package project.fin_the_pen.codefAPI.service.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import project.fin_the_pen.codefAPI.logic.Bank2APILogic;
import project.fin_the_pen.codefAPI.dto.bank.bank2.Bank2AccountDTO;
import project.fin_the_pen.codefAPI.dto.bank.bank2.Bank2AccountTransactionDTO;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class Bank2APIService {
    private final Bank2APILogic apiLogic;

    /**
     * 보유계좌
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String account(Bank2AccountDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.account(dto);
        return result;
    }

    public String accountTransaction(Bank2AccountTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.accountTransaction(dto);
        return result;
    }
}
