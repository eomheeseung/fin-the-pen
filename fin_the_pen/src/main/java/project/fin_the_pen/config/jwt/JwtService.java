package project.fin_the_pen.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    public String createAccessToken(String userSpecification) {
        Date accessExpiration = Date.from(
                Instant
                        .now()
                        .plus(Long.parseLong(jwtProperties.getAccessExpirationTime()), ChronoUnit.MINUTES));

        return Jwts.builder()
                // HS512 알고리즘을 사용
                .signWith(new SecretKeySpec(jwtProperties.getSecretKey().getBytes(),
                        SignatureAlgorithm.HS512.getJcaName()))
                .setSubject(userSpecification)  // JWT 토큰 제목
                .setIssuer(jwtProperties.getIssuer())  // JWT 토큰 발급자
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
                .setExpiration(accessExpiration)    // JWT 토큰 만료 시간
                .compact();// JWT 토큰 생성
    }

    public String createRefreshToken() {
        Date refreshExpiration = Date.from(
                Instant
                        .now()
                        .plus(Long.parseLong(jwtProperties.getRefreshExpirationTime()), ChronoUnit.MINUTES));

        return Jwts.builder()
                // HS512 알고리즘을 사용
                .signWith(new SecretKeySpec(jwtProperties.getSecretKey().getBytes(),
                        SignatureAlgorithm.HS512.getJcaName()))
                .setIssuer(jwtProperties.getIssuer())  // JWT 토큰 발급자
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
                .setExpiration(refreshExpiration)    // JWT 토큰 만료 시간
                .compact();// JWT 토큰 생성
    }




    public String validateTokenAndGetSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecretKey().getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

