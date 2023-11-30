package project.fin_the_pen.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import project.fin_the_pen.model.usersToken.entity.UsersToken;

import javax.crypto.spec.SecretKeySpec;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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

    // 토큰 갱신
    public UsersToken refreshToken(Optional<UsersToken> findToken) {
        if (findToken.isPresent()) {
            UsersToken usersToken = findToken.get();

            // 토큰 검증 및 정보 추출
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                    .build()
                    .parseClaimsJws(usersToken.getAccessToken());


            // 새로운 만료 시간 설정
            java.util.Date newExpirationTime = Date.from(Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES));

            // 기존 토큰의 만료 시간을 갱신한 후 반환
            usersToken.setExpireTime(newExpirationTime);
            return usersToken;
        } else throw new RuntimeException();
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

