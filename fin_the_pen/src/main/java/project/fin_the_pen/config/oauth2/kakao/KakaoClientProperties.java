package project.fin_the_pen.config.oauth2.kakao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao")
@Getter
@Setter
public class KakaoClientProperties {
    private String clientId;
    private String clientSecret;
    private String authorizationGrantType;
    private List<String> scope;
    private String redirectUri;
}
