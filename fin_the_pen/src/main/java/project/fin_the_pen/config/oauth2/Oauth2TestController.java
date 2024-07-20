package project.fin_the_pen.config.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RestController
@Slf4j
public class Oauth2TestController {
    // 로그인 페이지를 표시합니다. OAuth2 인증을 시작합니다.
    @GetMapping("/login")
    public String login() {
        // 로그인 페이지로 리다이렉트 또는 로그인 시작
        return "redirect:/oauth2/authorization/kakao";
    }

    // Kakao 로그인 후 성공 처리
    @RequestMapping("/login/oauth2/code/kakao")
    public ResponseEntity<Object> kakaoCallback(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String name = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");

        // 로그를 통해 사용자 정보를 출력하거나 처리할 수 있습니다.
        log.info("User Info - Name: {}, Email: {}", name, email);

        return ResponseEntity.ok().body(new HashMap<>().put(name, email));
    }

    // 로그아웃 핸들러
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.logout();
        return "redirect:/"; // 로그아웃 후 홈 페이지로 리다이렉트
    }
}
