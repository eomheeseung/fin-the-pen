package project.fin_the_pen.config;

//@Configuration
//@EnableMethodSecurity
/*@RequiredArgsConstructor
public class GeneralSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;    // JwtAuthenticationFilter 주입
    private final String[] allowedUrls =
            {"/", "/swagger-ui/**", "/v3/**", "/sign-up", "/sign-in", "/alive", "/fin-the-pen-web/getMonthSchedules"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(new AntPathRequestMatcher(Arrays.toString(allowedUrls)))
                                .permitAll() // Use AntPathRequestMatcher for specific paths
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}*/
