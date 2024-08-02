package project.fin_the_pen.config.oauth2.naver;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class NaverLoginController {
    private final NaverOAuth2Service naverOAuth2Service;

    @GetMapping("/login/oauth2/code/naver")
    public String naverCallback(@RequestParam String code, @RequestParam String state, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        // 액세스 토큰을 요청합니다.
        String accessToken = naverOAuth2Service.getAccessToken(code);

        // 액세스 토큰을 사용하여 사용자 프로필을 가져옵니다.
        String userProfile = naverOAuth2Service.getUserProfile(accessToken);

        // 사용자 정보를 세션이나 다른 곳에 저장하고 필요한 처리를 합니다.
        redirectAttributes.addFlashAttribute("userProfile", userProfile);

        return "redirect:/welcome";
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome() {
        log.info("welcome");
        return ResponseEntity.ok("Welcome to home page!");
    }


}
