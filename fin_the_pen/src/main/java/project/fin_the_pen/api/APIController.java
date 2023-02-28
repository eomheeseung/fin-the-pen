package project.fin_the_pen.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.api.service.APIService;

import java.io.IOException;

@RestController
@Slf4j
public class APIController {
    //사용자 인증 사이트로 넘어감.
    private final StringBuffer URLstringBuffer = new StringBuffer("https://testapi.openbanking.or.kr");
    private final APIService apiService;

    @Autowired
    public APIController(APIService apiService) {
        this.apiService = apiService;
    }

    /**
     * 맨 처음 accessToken 받기 위해서
     * @param code
     */
    @GetMapping("auth")
    public void userAuthorization(@RequestParam String code) {
        apiService.userAuthLogic(code,URLstringBuffer.length());
    }

    @GetMapping("noUserAuth")
    public void noUserAuth() {
        apiService.noUserAuth(URLstringBuffer,URLstringBuffer.length());
    }

    /**
     * 2.2.1 사용자 계좌 조회
     * @return
     * @throws IOException
     */
    @GetMapping("accountAuth")
    @ResponseBody
    public String accountAuth() throws IOException {
        URLstringBuffer.append("/v2.0/user/me");
        return apiService.accountAuthLogic(URLstringBuffer);
    }

    /**
     * 2.3.1 계좌 잔액 조회
     * @return
     * @throws IOException
     */
    @GetMapping("accountInquiry")
    @ResponseBody
    public String accountInquiry() throws IOException {
        return apiService.accountInquiryLogic(URLstringBuffer);
    }

    /**
     * 2.3.2 거래 내역 조회
     * @return
     */
    @GetMapping("accountDetailCheck")
    public String accountCheck() {
        return apiService.accountCheckLogic(URLstringBuffer);
    }

}
