package project.fin_the_pen.codefAPI.service.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import project.fin_the_pen.codefAPI.logic.EtcAPILogic;
import project.fin_the_pen.codefAPI.dto.bank.etc.AuthenticationDTO;
import project.fin_the_pen.codefAPI.dto.bank.etc.HolderAuthDTO;
import project.fin_the_pen.codefAPI.dto.bank.etc.HolderDTO;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EtcAPIService {
    private final EtcAPILogic apiLogic;

    /**
     * 계좌 인증 (1원이체)
     */
    public String authentication(AuthenticationDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.authentication(dto);
        return result;
    }
    /**
     * 예금주명
     */
    public String holder(HolderDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.holder(dto);

        if (result.equals("")) {
            return "예금주명이 등록되지 않았습니다.";
        } else {
            return result;
        }
    }

    /**
     * 예금주명 인증 (계좌 실명 인증)
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String holderAuth(HolderAuthDTO dto) throws IOException, ParseException, InterruptedException {
        String result = apiLogic.holderAuth(dto);
        return result;
    }
}
