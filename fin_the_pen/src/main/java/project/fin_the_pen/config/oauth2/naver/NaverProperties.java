package project.fin_the_pen.config.oauth2.naver;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class NaverProperties {
    private final String clientId = "SxhQhNoooSBYAzq1XRno";
    private final String clientSecret = "uk1kGifypE";
    private final String redirectUri = "http://localhost:8080/login/oauth2/code/naver";
    private final String name = "name";
    private final String email = "email";
    private final String clientName = "Naver";
    private final String authorizationUri = "https://nid.naver.com/oauth2.0/authorize";
    private final String tokenUri = "https://nid.naver.com/oauth2.0/token";
    private final String userInfoUri = "https://openapi.naver.com/v1/nid/me";
}
