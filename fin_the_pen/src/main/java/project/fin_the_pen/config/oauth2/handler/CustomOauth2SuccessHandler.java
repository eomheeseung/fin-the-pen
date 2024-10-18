package project.fin_the_pen.config.oauth2.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import project.fin_the_pen.config.jwt.JwtService;
import project.fin_the_pen.config.oauth2.custom.CustomOAuth2KakaoUser;
import project.fin_the_pen.config.oauth2.custom.CustomOAuth2NaverUser;
import project.fin_the_pen.config.oauth2.custom.Oauth2UserService;
import project.fin_the_pen.config.oauth2.socialDomain.SocialType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CustomOauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final Oauth2UserService oauth2UserService;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final JwtService jwtService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();


//        accessToken 확인 코드
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();

        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(registrationId, oauthToken.getName());


        if (principal instanceof CustomOAuth2NaverUser) {
            CustomOAuth2NaverUser oAuth2User = (CustomOAuth2NaverUser) principal;
            String email = oAuth2User.getEmail();
            String name = oAuth2User.getFullName();

            String accessToken = client.getAccessToken().getTokenValue();
            log.info("naver accessToken :{}", accessToken);

            log.info("User logged in with Naver: {}", email);
            oauth2UserService.saveUser(email, name, SocialType.NAVER);

            // OAuth2 사용자 정보 처리 후
            String jwtToken = jwtService.createToken(email);
            log.info("application token:{}", jwtToken);
            response.addHeader("Authorization", "Bearer " + jwtToken);

            response.setContentType("application/json"); // 응답의 Content-Type을 JSON으로 설정
            response.setStatus(HttpServletResponse.SC_OK); // 상태 코드 200 설정

            // JSON 형식으로 JWT 토큰을 응답 본문에 추가
            String jsonResponse = "{\"token\": \"" + jwtToken + "\"}";

            // 응답 본문에 쓰기
            response.getWriter().write(jsonResponse);
            response.getWriter().flush(); // 버퍼를 비우고 응답 전송


        } else if (principal instanceof CustomOAuth2KakaoUser) {
            CustomOAuth2KakaoUser oAuth2User = (CustomOAuth2KakaoUser) principal;
            String name = oAuth2User.getName();
            String email = "test@aaa.com";
            log.info("User logged in with Kakao: {}", name);

//            String accessToken = client.getAccessToken().getTokenValue();

//            log.info("kakao accessToken :{}", accessToken);
            oauth2UserService.saveUser(email, name, SocialType.KAKAO);
        }


        // 사용자 정보를 DB에 저장

        // 리다이렉트 URL
        // http://localhost:5173/home
        // db에 저장 (O)
        // json http body
        // 여기서 token 발행


        String redirectUrl = "http://localhost:5173/home";
        response.sendRedirect(redirectUrl);
    }
}
