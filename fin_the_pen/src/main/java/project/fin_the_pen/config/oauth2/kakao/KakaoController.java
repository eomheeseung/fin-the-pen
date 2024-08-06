package project.fin_the_pen.config.oauth2.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;

    @GetMapping("/login/oauth2/code/kakao/1107979")
    public String kakaoCallback(@RequestParam String code, @RequestParam String state) {
        kakaoService.getAccessToken(code, state);

        return "redirect:/welcome";
    }

    @GetMapping("/welcome")
    public String welCome() {
        return "welcome";
    }
}
