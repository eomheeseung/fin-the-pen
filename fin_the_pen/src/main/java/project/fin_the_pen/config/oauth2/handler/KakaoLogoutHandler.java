package project.fin_the_pen.config.oauth2.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.fin_the_pen.config.oauth2.kakao.KakaoProperties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
@RequiredArgsConstructor
public class KakaoLogoutHandler implements LogoutHandler {
    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String KAKAO_LOGOUT_URL =
            "https://kapi.kakao.com/v1/user/logout?target_id_type=user_id&target_id={userId}";


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("Authorization");

        if (token != null) {
            String userId = ""; // userId를 authentication에서 가져와야 합니다.
            String url = KAKAO_LOGOUT_URL.replace("{userId}", userId);
            restTemplate.postForEntity(url, null, String.class);
        }

        // 세션과 쿠키 삭제
        request.getSession().invalidate();

        for (Cookie cookie : request.getCookies()) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}
