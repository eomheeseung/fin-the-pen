package project.fin_the_pen.config.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import project.fin_the_pen.config.oauth2.kakao.KakaoClientProperties;
import project.fin_the_pen.config.oauth2.naver.NaverClientProperties;
import project.fin_the_pen.config.oauth2.social.CustomOauth2UserService;

@Order(1)
@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class Oauth2SecurityConfig {
    private final KakaoClientProperties kakaoClientProperties;
    private final CustomOauth2UserService customOauth2UserService;
//    private final NaverClientProperties naverClientProperties;

    private final static String NAVER_REDIRECT_URI = "http://localhost:8080/login/oauth2/code/naver";
    private final static String KAKAO_REDIRECT_URI = "http://localhost:8080/login/oauth2/code/kakao";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.antMatchers("/signup", "/",
                                        "/login",
                                        "**/favicon.ico",
                                        "/oauth2/authorization/**", "/login/oauth2/code/naver").permitAll()
                                .anyRequest().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint()
                        .userService(customOauth2UserService)
                        .and()
                        .successHandler(oauth2AuthenticationSuccessHandler())
//                        .failureHandler(oauth2AuthenticationFailureHandler())
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
        return new CustomOauth2SuccessHandler();
    }

    /*@Bean
    public AuthenticationFailureHandler oauth2AuthenticationFailureHandler() {
        return new CustomOauth2FailureHandler();
    }*/


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration kakaoClientRegistration = ClientRegistration.withRegistrationId("kakao")
                .clientId(kakaoClientProperties.getClientId())
                .clientSecret(kakaoClientProperties.getClientSecret())
                .scope(kakaoClientProperties.getScope())
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")

                // 인증 결과를 애플리케이션으로 제공할 리다이렉트 uri
                .redirectUri(kakaoClientProperties.getRedirectUri())
                .clientName("Kakao")
                .authorizationGrantType(new AuthorizationGrantType(kakaoClientProperties.getAuthorizationGrantType()))
                .build();

        ClientRegistration naverClientRegistration = ClientRegistration.withRegistrationId("naver")
                .clientId(NaverClientProperties.clientId)
                .clientSecret(NaverClientProperties.clientSecret)
                .scope(NaverClientProperties.name, NaverClientProperties.email)
                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://nid.naver.com/oauth2.0/token")
                .redirectUri(NAVER_REDIRECT_URI)
                .clientName("Naver")
                .userInfoUri("https://openapi.naver.com/v1/nid/me")
                .authorizationGrantType(new AuthorizationGrantType(NaverClientProperties.authorization_code))
                .userNameAttributeName("name") // 여기에서 userNameAttributeName을 설정합니다.
                .build();


        return new InMemoryClientRegistrationRepository(kakaoClientRegistration, naverClientRegistration);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
