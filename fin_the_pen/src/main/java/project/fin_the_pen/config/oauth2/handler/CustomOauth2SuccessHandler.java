package project.fin_the_pen.config.oauth2.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import project.fin_the_pen.config.oauth2.custom.CustomOAuth2KakaoUser;
import project.fin_the_pen.config.oauth2.custom.CustomOAuth2NaverUser;
import project.fin_the_pen.config.oauth2.custom.UserService;
import project.fin_the_pen.config.oauth2.socialDomain.SocialType;

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
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOAuth2NaverUser) {
            CustomOAuth2NaverUser oAuth2User = (CustomOAuth2NaverUser) principal;
            String email = oAuth2User.getEmail();
            String name = oAuth2User.getFullName();
            log.info("User logged in with Naver: {}", email);
            userService.saveUser(email, name, SocialType.NAVER);
        } else if (principal instanceof CustomOAuth2KakaoUser) {
            CustomOAuth2KakaoUser oAuth2User = (CustomOAuth2KakaoUser) principal;
            String name = oAuth2User.getName();
            String email = "test@aaa.com";
            log.info("User logged in with Kakao: {}", name);
            userService.saveUser(email, name, SocialType.KAKAO);
        }

        // 사용자 정보를 DB에 저장

        // 리다이렉트 URL
        // http://localhost:5173/home
        // db에 저장
        // json http body
        // 여기서 token 발행
        String redirectUrl = "http://localhost:5173/home";
        response.sendRedirect(redirectUrl);
    }
}
