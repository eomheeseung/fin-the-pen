package project.fin_the_pen.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
/**
 * TODO
 *  applicatoin 자체 access, refresh 발행하고
 *  아래의 클래스 수정
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


//        String token = parseBearerToken(request);
//        User user = parseUserSpecification(token);
//
//        log.info("jwt filter - user spec:{}", user.getUsername());
//
//
//
//        AbstractAuthenticationToken authenticated =
//                UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
//
//        authenticated.setDetails(new WebAuthenticationDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authenticated);
//
//        filterChain.doFilter(request, response);

        String token = parseBearerToken(request);
        User user = parseUserSpecification(token);

        log.info("JWT Filter check - User Spec: {}", user.getUsername());

        // 현재 SecurityContext의 인증 객체를 가져옴
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();

        // 동일한 인증 객체인지 확인
        if (currentAuthentication instanceof UsernamePasswordAuthenticationToken
                && currentAuthentication.isAuthenticated()
                && currentAuthentication.getName().equals(user.getUsername())) {
            log.info("Same authentication object detected, skipping re-authentication.");
            filterChain.doFilter(request, response);
            return;
        }

        // 새로운 인증 객체 생성 및 설정
        AbstractAuthenticationToken authenticated =
                UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());

        authenticated.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        log.info("Authentication object updated for user: {}", user.getUsername());

        // 필터 체인 진행
        filterChain.doFilter(request, response);

    }

    private String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        log.info("filter parsing accessToken value :{}", authorization);

        return Optional.ofNullable(authorization)
                .filter(token -> token.startsWith("Bearer ")) // Length check
                .map(token -> token.substring(7).trim()) // Extract the token
                .orElse(null);
    }

    private User parseUserSpecification(String token) {
        String[] split = Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 10)
                .map(jwtService::validateTokenAndGetSubject)
                .orElse("anonymous:anonymous")
                .split(":");

        // 배열 길이를 체크하여 안전하게 User 객체를 생성
        String username = split.length > 0 ? split[0] : "anonymous"; // 기본값
        String authority = split.length > 1 ? split[1] : "ROLE_ANONYMOUS"; // 기본 역할

        return new User(username, "", List.of(new SimpleGrantedAuthority(authority)));
    }

}
