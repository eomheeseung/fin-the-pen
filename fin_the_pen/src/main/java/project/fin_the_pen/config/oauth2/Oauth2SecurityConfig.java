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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import project.fin_the_pen.config.oauth2.custom.CustomOAuth2Service;
import project.fin_the_pen.config.oauth2.custom.UserService;
import project.fin_the_pen.config.oauth2.handler.CustomLogoutSuccessHandler;
import project.fin_the_pen.config.oauth2.handler.CustomOauth2SuccessHandler;
import project.fin_the_pen.config.oauth2.kakao.KakaoProperties;
import project.fin_the_pen.config.oauth2.naver.NaverProperties;

@Order(1)
@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class Oauth2SecurityConfig {
    private final KakaoProperties kakaoProperties;
    private final NaverProperties naverProperties;
    private final CustomOAuth2Service customOAuth2Service;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.antMatchers("/signup", "/",
                                        "/oauth2/authorization/**",
                                        "/login", "/css/**", "/js/**",
                                        "/h2-console/**",
                                        "/login/oauth2/code/naver",
                                        "/login/oauth2/code/kakao/1107979").permitAll()
                                .anyRequest().authenticated())
                .oauth2Login(oauth2Login -> {
                    try {
                        oauth2Login
                                .userInfoEndpoint()
                                .userService(customOAuth2Service)
                                .and()
                                .successHandler(oauth2AuthenticationSuccessHandler())
                                .and()
                                .logout()
                                .logoutUrl("/logout")  // 로그아웃 요청 URL
                                .logoutSuccessHandler(oauth2LogoutSuccessHandler())  // 로그아웃 성공 후 핸들러
                                .permitAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
//                        .failureHandler(oauth2AuthenticationFailureHandler())


        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
        return new CustomOauth2SuccessHandler(userService);
    }

    @Bean
    public LogoutSuccessHandler oauth2LogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }




    /*@Bean
    public AuthenticationFailureHandler oauth2AuthenticationFailureHandler() {
        return new CustomOauth2FailureHandler();
    }*/


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration kakaoClientRegistration = ClientRegistration
                .withRegistrationId(kakaoProperties.getClientId())
                .clientName(kakaoProperties.getClientName())
                .clientId(kakaoProperties.getClientId())
                .clientSecret(kakaoProperties.getClientSecret())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri(kakaoProperties.getAuthorizationUri())
                .tokenUri(kakaoProperties.getTokenUri())
                .userInfoUri(kakaoProperties.getUserInfoUri())
                .scope(kakaoProperties.getNickName(), kakaoProperties.getImage())
                .redirectUri(kakaoProperties.getRedirectUri())
                .userNameAttributeName("id")
                .build();

        ClientRegistration naverClientRegistration = ClientRegistration
                .withRegistrationId(naverProperties.getClientId())
                .clientName(naverProperties.getClientName())
                .clientId(naverProperties.getClientId())
                .clientSecret(naverProperties.getClientSecret())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri(naverProperties.getAuthorizationUri())
                .tokenUri(naverProperties.getTokenUri())
                .userInfoUri(naverProperties.getUserInfoUri())
                .redirectUri(naverProperties.getRedirectUri())
                .scope(naverProperties.getName(), naverProperties.getEmail())
                .userNameAttributeName("response")
                .build();

        return new InMemoryClientRegistrationRepository(kakaoClientRegistration, naverClientRegistration);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
