package project.fin_the_pen.config.oauth2.kakao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class KakaoClientProperties {
    public static final String clientId = "58044d766e3a6a856f75bd256d28a073";
    public static final String clientSecret="J7UYWTOODX58Up3ilLUb5U7HxaL7LVoK";
    public static final String authorizationCode = "authorization_code";
    public static final String nickName = "profile_nickname";
    public static final String image = "profile_image";
    public static final String redirectUri = "http://localhost:8080/login/oauth2/code/kakao/1107979";
}
