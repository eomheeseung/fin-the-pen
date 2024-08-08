package project.fin_the_pen.config.oauth2.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.fin_the_pen.config.oauth2.naver.NaverProperties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverLogoutHandler implements LogoutHandler {
    private final NaverProperties naverProperties;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String NAVER_LOGOUT_URL =
            "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id={clientId}&client_secret={clientSecret}&access_token={accessToken}&service_provider=NAVER";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("Authorization");

        if (token != null) {
            String clientId = naverProperties.getClientId();
            String clientSecret = naverProperties.getClientSecret();
            String url = NAVER_LOGOUT_URL
                    .replace("{clientId}", clientId)
                    .replace("{clientSecret}", clientSecret)
                    .replace("{accessToken}", token);
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
