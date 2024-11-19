package project.fin_the_pen.config.oauth2.kakao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao")
public class KakaoProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorizationGrantType;
    private String scope;
    private String clientName;
    private String clientAuthenticationMethod;
}
