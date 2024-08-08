package project.fin_the_pen.config.oauth2.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class KakaoLogoutHandler implements LogoutHandler {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String KAKAO_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("Authorization");

        log.info("kakao logout call");
        if (token != null) {
            restTemplate.postForEntity(KAKAO_LOGOUT_URL, null, String.class);
        }
    }
}
