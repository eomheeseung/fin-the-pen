package project.fin_the_pen.config.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class KakaoController {
    @GetMapping("/signup")
    public String signup() {
        // "/oauth2/authorization/kakao"로 리다이렉트하여 카카오 인증을 시작
        return "redirect:/oauth2/authorization/kakao";
    }

}
