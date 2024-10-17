package project.fin_the_pen.config.jwt;

/*@Order(0)
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String token = parseBearerToken(request);
        User user = parseUserSpecification(token);

        AbstractAuthenticationToken authenticated =
                UsernamePasswordAuthenticationToken.authenticated(user, token, user.getAuthorities());

        authenticated.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        log.info("error pointing accessToken value :{}", authorization);

        return Optional.ofNullable(authorization)
                .filter(token -> token.startsWith("Bearer ")) // Length check
                .map(token -> token.substring(7)) // Extract the token
                .orElse(null);
    }

    private User parseUserSpecification(String token) {
        String[] split = Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 10)
                .map(tokenProvider::validateTokenAndGetSubject)
                .orElse("anonymous:anonymous")
                .split(":");

        return new User(split[0], "", List.of(new SimpleGrantedAuthority(split[1])));
    }
}*/
