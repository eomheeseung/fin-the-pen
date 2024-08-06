package project.fin_the_pen.config.oauth2.kakao;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class KakaoProperties {
    private final String clientId = "58044d766e3a6a856f75bd256d28a073";
    private final String clientSecret = "J7UYWTOODX58Up3ilLUb5U7HxaL7LVoK";
    private final String redirectUri = "http://localhost:8080/login/oauth2/code/kakao/1107979";
    private final String nickName = "profile_nickname";
    private final String image = "profile_image";
    private final String clientName = "Kakao";
    private final String authorizationUri = "https://kauth.kakao.com/oauth/authorize";
    private final String tokenUri = "https://kauth.kakao.com/oauth/token";
    private final String userInfoUri = "https://kapi.kakao.com/v2/user/me";
}
