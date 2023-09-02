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
TODO
 1.
 모든 api를 호출할 때 connectedId를 수동으로 입력했는데 이제는 db에 있는 값을 꺼내서 쓰기 때문에
 모든 controller에 대한 dto들을 수정해야 함.
 How) connectedID 상수를 쓰던지 해서....
 2.
 connectedId를 유저별로 어떻게 가져올 것인지
 Ex) 하나의 테이블에 connectedId를 넣어놓는다고 하면 유저가 3명일때 어떻게 구분하여 쿼리로 가져올 것인지
 */
public class Bank2Controller {
    private final Bank2APIService bank2APIService;
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
