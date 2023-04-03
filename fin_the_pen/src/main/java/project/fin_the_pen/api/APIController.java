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
    private final StringBuffer URLStringBuffer = new StringBuffer("https://testapi.openbanking.or.kr");
    private final APIService apiService;

    @Autowired
    public APIController(APIService apiService) {
        this.apiService = apiService;
    }

    /**
     * 2.1.2 이용기관 토큰 발급 3-legged
     * 맨 처음 accessToken 받기 위해서
     *
     * @param code
     */
    @GetMapping("auth")
    public void userAuthorization(@RequestParam String code) {
        apiService.userAuthLogic(code, URLStringBuffer.length());
    }

    /**
     * 2.1.2 이용기관 토큰 발급 2-legged
     */
    @GetMapping("noUserAuth")
    public void noUserAuth() {
        apiService.noUserAuth(URLStringBuffer, URLStringBuffer.length());
    }

    /**
     * 2.2.1 사용자 정보 조회
     *
     * @return
     */
    @GetMapping("accountAuth")
    @ResponseBody
    public String accountAuth() {
        URLStringBuffer.append("/v2.0/user/me");
        return apiService.accountAuthLogic(URLStringBuffer);
    }

    /**
     * 2.2.3 등록 계좌 조회
     *
     * @return
     * @throws IOException
     */
    @GetMapping("accountList")
    public String accountList() {
        return apiService.accountListLogic(URLStringBuffer);
    }

    /**
     * 2.2.4 계좌 정보 변경
     * @return
     */
    @GetMapping("accountUpdate")
    public String accountUpdate() {
        return apiService.accountUpdateLogic(URLStringBuffer).toString();
    }

    /**
     * 2.2.6 계좌정보조회
     * @return
     */
    @GetMapping("accountInfo")
    public String accountInfo() {
        return apiService.accountInfoLogic(URLStringBuffer).toString();
    }

    /**
     * 2.3.1 계좌 잔액 조회
     *
     * @return
     * @throws IOException
     */
    @GetMapping("accountInquiry")
    @ResponseBody
    public String accountInquiry() throws IOException {
        return apiService.accountInquiryLogic(URLStringBuffer);
    }

    /**
     * 2.3.2 거래 내역 조회
     *
     * @return
     */
    @GetMapping("accountDetailCheck")
    public String accountCheck() {
        return apiService.accountCheckLogic(URLStringBuffer);
    }

}
