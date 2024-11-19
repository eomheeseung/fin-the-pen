package project.fin_the_pen.config.oauth2.naver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/*
oauth2 yml에 registration에 여러 소셜미디어가 묶여있다면,
분리되는 지점까지 명시를 하고
필드를 지정해 주면된다. "-"형식을 카멜케이스로 하면 된다.
그리고 적용된 yml의 프로퍼티의 색은 파란색에서 주황색으로 바뀌게 된다.
 */
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.naver")
@Component
@Getter
@Setter
public class NaverProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorizationGrantType;
    private String scope;
    private String clientName;
}
