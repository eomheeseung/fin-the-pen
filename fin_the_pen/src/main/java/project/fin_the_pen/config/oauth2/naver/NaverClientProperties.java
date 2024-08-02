package project.fin_the_pen.config.oauth2.naver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.naver")
@Getter
@Setter
public class NaverClientProperties {
    public final static String clientId = "SxhQhNoooSBYAzq1XRno";
    public final static String clientSecret = "7KEyxFXbyg";
    public final static String authorization_code = "authorization_code";
    public final static String name = "name";
    public final static String email = "email";
    public final static String redirectUri = "http://localhost:8080/login/oauth2/code/naver";
}
