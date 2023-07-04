package project.fin_the_pen.codefAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.codefAPI.dto.bank.individual.AccountList;
import project.fin_the_pen.codefAPI.service.bank.CodefIndIndividualService;
import project.fin_the_pen.codefAPI.service.CodefPublishTokenService;
import project.fin_the_pen.data.token.Token;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class InitController {
    private final CodefPublishTokenService tokenService;
    private final CodefIndIndividualService apiService;

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
     *
     * @return
     */
    @GetMapping("codef/findToken")
    public List<Token> codefFindToken() {
        return apiService.findToken();
    }

    /**
     * connectedId 발급(해결!)
     * @param list
     */
    @GetMapping("codef/accountCreate")
    public void codefAccountCreate(@RequestBody AccountList list) {
        apiService.accountCreate(list);
    }
}
