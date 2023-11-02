package project.fin_the_pen.codefAPI.controller.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.codefAPI.dto.bank.etc.AuthenticationDTO;
import project.fin_the_pen.codefAPI.dto.bank.etc.HolderAuthDTO;
import project.fin_the_pen.codefAPI.dto.bank.etc.HolderDTO;
import project.fin_the_pen.codefAPI.service.bank.EtcAPIService;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/codef/bank/etc")
public class EtcBankController {
    private final EtcAPIService etcAPIService;
    /**
     * 계좌인증 (1원 이체)
     */
    @PostMapping("/account-authentication")
    public JSONObject accountAuthentication(@RequestBody AuthenticationDTO dto) throws IOException, ParseException, InterruptedException {
        JSONObject jsonObject = etcAPIService.authentication(dto);
        return jsonObject;
    }

    /**
     * 예금주명
     */
    @PostMapping("/holder")
    public JSONObject holder(@RequestBody HolderDTO dto) throws IOException, ParseException, InterruptedException {
        JSONObject jsonObject = etcAPIService.holder(dto);
        return jsonObject;
    }

    /**
     * 예금주명 인증 (계좌 실명 인증)
     */
    @GetMapping("/holder-authentication")
    public String holderAuthentication(@RequestBody HolderAuthDTO dto) throws IOException, ParseException, InterruptedException {
        String result = etcAPIService.holderAuth(dto);
        return result;
    }
}
