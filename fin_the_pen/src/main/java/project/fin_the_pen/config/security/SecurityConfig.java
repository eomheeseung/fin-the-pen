package project.fin_the_pen.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    private final static String HEAD = "/fin-the-pen-web";
    private final String[] allowedUrls = {"/", "/swagger-ui/**", "/v3/**", HEAD + "/sign-up", HEAD + "/sign-in"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .headers(headers -> headers.frameOptions().sameOrigin())
                .authorizeRequests(requests ->
                        requests.requestMatchers(PathRequest.toH2Console()).permitAll()
                                .anyRequest().authenticated())
                .antMatcher(Arrays.toString(allowedUrls))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
