package project.fin_the_pen.config.oauth2;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomOauth2FailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 실패 후 처리 로직
        // 예: 로그인 실패 시 에러 페이지로 리다이렉트
        response.sendRedirect("/login?error"); // 로그인 실패 후 리다이렉트할 페이지

    }
}
