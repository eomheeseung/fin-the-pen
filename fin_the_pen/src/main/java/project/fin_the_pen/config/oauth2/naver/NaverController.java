package project.fin_the_pen.config.oauth2.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NaverController {
    private final NaverService naverService;

    @GetMapping("/login/oauth2/code/naver")
    public String kakaoCallback(@RequestParam String code, @RequestParam String state) {
        naverService.getAccessToken(code, state);

        return "redirect:/welcome";
    }
}
