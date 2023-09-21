package project.fin_the_pen.codefAPI.controller.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.codefAPI.dto.bank.etc.AuthenticationDTO;
import project.fin_the_pen.codefAPI.dto.bank.etc.HolderAuthDTO;
import project.fin_the_pen.codefAPI.dto.bank.etc.HolderDTO;
import project.fin_the_pen.codefAPI.service.bank.EtcAPIService;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@RestController
public class EtcBankController {
    private final EtcAPIService etcAPIService;
    /**
     * 계좌인증 (1원 이체)
     */
    @GetMapping("/codef/account-authentication")
    public String accountAuthentication(@RequestBody AuthenticationDTO dto) throws IOException, ParseException, InterruptedException {
        String result = etcAPIService.authentication(dto);
        return result;
    }

    /**
     * 예금주명
     */
    @GetMapping("/codef/holder")
    public String holder(@RequestBody HolderDTO dto) throws IOException, ParseException, InterruptedException {
        String result = etcAPIService.holder(dto);
        return result;
    }

    /**
     * 예금주명 인증 (계좌 실명 인증)
     */
    @GetMapping("/codef/holder-authentication")
    public String holderAuthentication(@RequestBody HolderAuthDTO dto) throws IOException, ParseException, InterruptedException {
        String result = etcAPIService.holderAuth(dto);
        return result;
    }
}
