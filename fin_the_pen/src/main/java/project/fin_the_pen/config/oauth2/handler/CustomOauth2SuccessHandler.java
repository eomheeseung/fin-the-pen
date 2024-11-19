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
import javax.servlet.http.Cookie;
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

        OAuth2AuthorizedClient client =
                oAuth2AuthorizedClientService.loadAuthorizedClient(registrationId, oauthToken.getName());


        if (principal instanceof CustomOAuth2NaverUser) {
            CustomOAuth2NaverUser oAuth2User = (CustomOAuth2NaverUser) principal;
            String email = oAuth2User.getEmail();
            String name = oAuth2User.getFullName();

            String accessToken = client.getAccessToken().getTokenValue();
            log.info("naver accessToken :{}", accessToken);

            log.info("User logged in with Naver: {}", email);
            log.info("User logged in with Naver name: {}", name);
            oauth2UserService.saveUser(email, name, SocialType.NAVER);

            // OAuth2 사용자 정보 처리 후
            String jwtAccessToken = jwtService.createAccessToken(email, SocialType.NAVER, name);
            String jwtRefreshToken = jwtService.createRefreshToken();
            log.info("naver 로그인 후 application access token:{}", jwtAccessToken);
            log.info("naver 로그인 후 application refresh token:{}", jwtRefreshToken);

            Cookie accessTokenCookie = new Cookie("access_token", jwtAccessToken);
            Cookie refreshTokenCookie = new Cookie("refresh_token", jwtRefreshToken);


            // 쿠키 설정
            accessTokenCookie.setPath("/");
            refreshTokenCookie.setPath("/");
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);

            // 이후 리다이렉트 수행

        } else if (principal instanceof CustomOAuth2KakaoUser) {
            CustomOAuth2KakaoUser oAuth2User = (CustomOAuth2KakaoUser) principal;

            String accessToken = client.getAccessToken().getTokenValue();
            log.info("kakao oauth2 accessToken :{}", accessToken);

            String email = oAuth2User.getEmail();
            String name = oAuth2User.getFullName();

            // 나중에 +82 10과 같은 국제번호 처리 어떻게 할 것인지...
            String phoneNumber = oAuth2User.getPhoneNumber();

            log.info("User logged in with Kakao name: {}", name);
            log.info("User logged in with Kakao email: {}", email);
            log.info("User logged in with Kakao phone: {}", phoneNumber);

            oauth2UserService.saveUser(email, name, SocialType.KAKAO);

            // OAuth2 사용자 정보 처리 후
            String jwtAccessToken = jwtService.createAccessToken(email, SocialType.KAKAO, name);
            String jwtRefreshToken = jwtService.createRefreshToken();
            log.info("kakao 로그인 후 application access token:{}", jwtAccessToken);
            log.info("kakao 로그인 후 application refresh token:{}", jwtRefreshToken);

            Cookie accessTokenCookie = new Cookie("access_token", jwtAccessToken);
            Cookie refreshTokenCookie = new Cookie("refresh_token", jwtRefreshToken);

            // 쿠키 설정
            accessTokenCookie.setPath("/");
            refreshTokenCookie.setPath("/");
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);

            // 이후 리다이렉트 수행
        }


        String redirectUrl = "http://localhost:5173/home";
        response.sendRedirect(redirectUrl);
    }
}
