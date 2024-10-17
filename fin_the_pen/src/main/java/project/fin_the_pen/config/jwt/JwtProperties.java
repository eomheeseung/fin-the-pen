package project.fin_the_pen.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
/*
configurationProperties 사용 시 spring의 value annotation 사용할 필요가 없음.
 */
public class JwtProperties {
    public String secretKey;
    public String accessExpirationTime;
    public String accessDevMinutes;
    public String refreshExpirationTime;
    public String refreshDevMinutes;
    public String issuer;
}
