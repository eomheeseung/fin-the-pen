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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import project.fin_the_pen.config.oauth2.custom.CustomOAuth2Service;
import project.fin_the_pen.config.oauth2.custom.UserService;
import project.fin_the_pen.config.oauth2.handler.CustomLogoutSuccessHandler;
import project.fin_the_pen.config.oauth2.handler.CustomOauth2SuccessHandler;
import project.fin_the_pen.config.oauth2.handler.KakaoLogoutHandler;
import project.fin_the_pen.config.oauth2.handler.NaverLogoutHandler;
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
    private final KakaoLogoutHandler kakaoLogoutHandler;
    private final NaverLogoutHandler naverLogoutHandler;

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
                                        "/login/oauth2/code/kakao").permitAll()
                                .anyRequest().authenticated())
                .formLogin()
                .loginPage("/login")
                .and()
                .oauth2Login(oauth2Login -> {
                            try {
                                oauth2Login
                                        .loginPage("/loginForm")
                                        .userInfoEndpoint()
                                        .userService(customOAuth2Service)
                                        .and()
                                        .successHandler(oauth2AuthenticationSuccessHandler());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .logout(configurer -> configurer
                        .addLogoutHandler(kakaoLogoutHandler)
                        .addLogoutHandler(naverLogoutHandler)
                        .logoutSuccessHandler(oauth2LogoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID"));
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


//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        ClientRegistration kakaoClientRegistration = ClientRegistration
//                .withRegistrationId(kakaoProperties.getClientId())
//                .clientName(kakaoProperties.getClientName())
//                .clientId(kakaoProperties.getClientId())
//                .clientSecret(kakaoProperties.getClientSecret())
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationUri(kakaoProperties.getAuthorizationUri())
//                .tokenUri(kakaoProperties.getTokenUri())
//                .userInfoUri(kakaoProperties.getUserInfoUri())
//                .scope(kakaoProperties.getNickName(), kakaoProperties.getImage())
////                .scope(kakaoProperties.getKakaoAccount(), kakaoProperties.getProfile())
//                .redirectUri(kakaoProperties.getRedirectUri())
//                .userNameAttributeName("id")
//                .build();
//
//        ClientRegistration naverClientRegistration = ClientRegistration
//                .withRegistrationId(naverProperties.getClientId())
//                .clientName(naverProperties.getClientName())
//                .clientId(naverProperties.getClientId())
//                .clientSecret(naverProperties.getClientSecret())
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationUri(naverProperties.getAuthorizationUri())
//                .tokenUri(naverProperties.getTokenUri())
//                .userInfoUri(naverProperties.getUserInfoUri())
//                .redirectUri(naverProperties.getRedirectUri())
//                .scope(naverProperties.getName(), naverProperties.getEmail())
//                .userNameAttributeName("response")
//                .build();
//
//        return new InMemoryClientRegistrationRepository(kakaoClientRegistration, naverClientRegistration);
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
