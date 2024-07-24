package project.fin_the_pen.config.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomOauth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 성공 후 처리 로직
        // 예: 로그인 성공 시 사용자에게 환영 메시지를 표시
        response.sendRedirect("/welcome"); // 로그인 성공 후 리다이렉트할 페이지
    }
}
