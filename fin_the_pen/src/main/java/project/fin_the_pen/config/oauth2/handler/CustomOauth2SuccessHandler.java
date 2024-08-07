package project.fin_the_pen.config.oauth2.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import project.fin_the_pen.config.oauth2.custom.CustomOAuth2User;
import project.fin_the_pen.config.oauth2.custom.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CustomOauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        String name = oAuth2User.getFullName();

        // 로그 저장 예시
        if (oAuth2User.isNaver()) {
            log.info("User logged in with Naver: {}", email);
        } else if (oAuth2User.isKakao()) {
            log.info("User logged in with Kakao: {}", email);
        }

        // 사용자 정보를 DB에 저장
        userService.saveUser(email, name);

        // 리다이렉트 URL
        String redirectUrl = "/welcome";
        response.sendRedirect(redirectUrl);
    }
}
