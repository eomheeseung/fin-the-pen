package project.fin_the_pen.config.oauth2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController {
    @GetMapping("/welcome")
    public String welCome() {
        return "welcome";
    }

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/login?logout")
    public String logoutPage() {
        return "logout"; // 로그아웃 후 리다이렉트 페이지를 반환
    }

    @GetMapping("/login/kakao")
    public String kakaoLogin() {
        return "redirect:/oauth2/authorization/kakao";
    }

    @GetMapping("/login/naver")
    public void naverLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:8080/oauth2/authorization/naver");
    }
}
