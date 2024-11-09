package project.fin_the_pen.config.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.config.jwt.JwtService;
import project.fin_the_pen.config.oauth2.custom.CustomOAuth2NaverUser;
import project.fin_the_pen.finClient.core.error.customException.NotFoundDataException;
import project.fin_the_pen.finClient.core.util.TokenParser;
import project.fin_the_pen.model.user.entity.Users;
import project.fin_the_pen.model.user.repository.UsersRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Oauth2SuccessController {
    private final TokenParser tokenParser;
    private final JwtService jwtService;
    private final UsersRepository usersRepository;

    /**
     * 10-18
     * redirect하고,
     * front에서 fetch요청으로 /oauth2/success/info로 요청을 해야지 사용자의 정보와 자체 토큰을 발행해서 넣어줄 것임
     *
     *
     *
     * 기존 uri : /oauth2/success/info
     *
     * @return
     */
    @GetMapping("/api/user/info")
    public ResponseEntity<HashMap<String, String>> successTransferUserInfo(HttpServletRequest request,
                                                                           Authentication authentication) {

        log.info("oauth2 성공시 사용자의 정보 가져오는 controller call");
        String parseBearerToken = tokenParser.parseBearerToken(request);

        // email이 id임
        String userId = jwtService.getEmailFromToken(parseBearerToken);
        String socialTypeFromToken = jwtService.getSocialTypeFromToken(parseBearerToken);

        HashMap<String, String> responseMap = new HashMap<>();
        responseMap.put("user_id", userId);

        if (socialTypeFromToken.equalsIgnoreCase("naver")) {
            CustomOAuth2NaverUser customOAuth2NaverUser = (CustomOAuth2NaverUser) authentication.getPrincipal();

            String name = customOAuth2NaverUser.getFullName();

            responseMap.put("name", name);
        } else if (socialTypeFromToken.equalsIgnoreCase("kakao")) {
            CustomOAuth2NaverUser customOAuth2NaverUser = (CustomOAuth2NaverUser) authentication.getPrincipal();

            String name = customOAuth2NaverUser.getFullName();

            responseMap.put("name", name);
        } else if (socialTypeFromToken.equalsIgnoreCase("none")) {
            Optional<Users> optionalUsers = usersRepository.findByUserId(userId);

            if (optionalUsers.isPresent()) {
                Users users = optionalUsers.get();
                String name = users.getName();
                responseMap.put("name", name);
            } else {
                throw new NotFoundDataException("사용자의 이름을 찾을 수 없습니다.");
            }
        }

        responseMap.keySet().forEach(key ->
                log.info(key + ": {}", responseMap.get(key)));

        return ResponseEntity.ok(responseMap);
    }
}
