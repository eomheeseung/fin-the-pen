package project.fin_the_pen.config.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Order(1)
@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class Oauth2SecurityConfig {
    private final KakaoClientProperties kakaoClientProperties;
    private final CustomOauth2UserService customOauth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/", "/login").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Client(oauth2Client ->
                        oauth2Client.clientRegistrationRepository(clientRegistrationRepository())
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                /*
                                loginPage("/login")**는 사용자에게 로그인 폼을 제공하는 경로를 설정합니다.
                                이 URL은 사용자가 로그인하지 않은 상태에서 보호된 리소스에 접근하려 할 때 표시됩니다.
                                OAuth2 인증 흐름은 사용자 인증 후 OAuth2 로그인 제공자의 로그인 페이지로 리다이렉트되고,
                                로그인 성공 후 커스텀 핸들러에 의해 리다이렉트할 페이지를 설정할 수 있습니다.
                                 */
                                .loginPage("/login")
                                .userInfoEndpoint()
                                .userService(customOauth2UserService))
                .oauth2Login(oauth2Login -> oauth2Login.successHandler(oauth2AuthenticationSuccessHandler()))
                .oauth2Login(oauth2Login -> oauth2Login.failureHandler(oauth2AuthenticationFailureHandler()));
               /* .formLogin(formLogin ->
                        formLogin.successHandler(oauth2AuthenticationSuccessHandler())
                );*/

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
        return new CustomOauth2SuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler oauth2AuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("kakao")
                .clientId(kakaoClientProperties.getClientId())
                .clientSecret(kakaoClientProperties.getClientSecret())
                .scope(kakaoClientProperties.getScope())
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")

                // 인증 결과를 애플리케이션으로 제공할 리다이렉트 uri
                .redirectUri(kakaoClientProperties.getRedirectUri())
                .clientName("kakao")
                .authorizationGrantType(new AuthorizationGrantType(kakaoClientProperties
                        .getAuthorizationGrantType()))
                .build();

        return new InMemoryClientRegistrationRepository(clientRegistration);
    }
}
