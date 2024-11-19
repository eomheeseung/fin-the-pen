package project.fin_the_pen.finClient.social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.config.jwt.JwtService;
import project.fin_the_pen.finClient.core.util.TokenParser;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoTransferService {
    private final TokenParser tokenParser;
    private final JwtService jwtService;

    public HashMap<String, Object> userInfo(HttpServletRequest request) {
        String parseBearerToken = tokenParser.parseBearerToken(request);

        // email이 id임
        String userId = jwtService.getSubjectFromToken(parseBearerToken);
        String socialTypeFromToken = jwtService.getSocialTypeFromToken(parseBearerToken);

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("user_id", userId);

        log.info("사용자 정보 controller -  userId : {}", userId);
        log.info("사용자 정보 controller -  socialType : {}", socialTypeFromToken);

        String username = jwtService.getUsername(parseBearerToken);

        responseMap.put("username", username);


        responseMap.keySet().forEach(key ->
                log.info(key + ": {}", responseMap.get(key)));

        return responseMap;
    }
}
