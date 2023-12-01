package project.fin_the_pen.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@PropertySource("classpath:jwt.yml")
public class TokenProvider {
    private final String secretKey;
    private final long expirationHours;
    private final long expirationMinutes;
    private final String issuer;
    private String loginId;

    @Getter
    private java.util.Date expiredTime;

    public TokenProvider(@Value("${secret-key}") String secretKey,
                         @Value("${expiration-hours}") long expirationHours,
                         @Value("${expiration-minutes}") long expirationMinutes,
                         @Value("${issuer}") String issuer) {
        this.secretKey = secretKey;
        this.expirationHours = expirationHours;
        this.expirationMinutes = expirationMinutes;
        this.issuer = issuer;
    }

    public String createToken(String userSpecification) {
        expiredTime = Date.from(Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES));

        return Jwts.builder()
                // HS512 알고리즘을 사용
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .setSubject(userSpecification)  // JWT 토큰 제목
                .setIssuer(issuer)  // JWT 토큰 발급자
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
//                .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))    // JWT 토큰 만료 시간
                .setExpiration(expiredTime)    // JWT 토큰 만료 시간
                .compact();// JWT 토큰 생성
    }









    public String validateTokenAndGetSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

