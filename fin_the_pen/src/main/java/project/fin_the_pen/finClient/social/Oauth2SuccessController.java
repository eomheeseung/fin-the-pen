package project.fin_the_pen.finClient.social;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.config.jwt.JwtService;
import project.fin_the_pen.finClient.core.util.TokenParser;
import project.fin_the_pen.model.user.repository.UsersRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Tag(name = "사용자 정보 불러오기", description = "사용자 정보 불러오기 oauth2 이후 token을 넣어야 함")
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
     * TODO 여기가 안됌...
     * <p>
     * <p>
     * <p>
     * 기존 uri : /oauth2/success/info
     *
     * @return
     */
    @Operation(description = "사용자 정보 불러오기 post method", summary = "사용자 정보 불러오기")
    @PostMapping("/api/user/info")
    public ResponseEntity<HashMap<String, String>> successTransferUserInfo(HttpServletRequest request) {

        log.info("oauth2 성공시 사용자의 정보 가져오는 controller call");

        String parseBearerToken = tokenParser.parseBearerToken(request);

        // email이 id임
        String userId = jwtService.getSubjectFromToken(parseBearerToken);
        String socialTypeFromToken = jwtService.getSocialTypeFromToken(parseBearerToken);

        HashMap<String, String> responseMap = new HashMap<>();
        responseMap.put("user_id", userId);

        log.info("사용자 정보 controller -  userId : {}", userId);
        log.info("사용자 정보 controller -  socialType : {}", socialTypeFromToken);

        String username = jwtService.getUsername(parseBearerToken);

        responseMap.put("username", username);


        responseMap.keySet().forEach(key ->
                log.info(key + ": {}", responseMap.get(key)));

        return ResponseEntity.ok(responseMap);
    }


}
