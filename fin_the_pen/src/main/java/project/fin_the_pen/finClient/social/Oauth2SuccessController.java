package project.fin_the_pen.finClient.social;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Tag(name = "사용자 정보 불러오기", description = "사용자 정보 불러오기 oauth2 이후 token을 넣어야 함")
@RestController
@RequiredArgsConstructor
@Slf4j
public class Oauth2SuccessController {
    private final UserInfoTransferService userInfoTransferService;

    @Operation(description = "사용자 정보 불러오기 post method", summary = "사용자 정보 불러오기")
    @PostMapping("/api/user/info")
    public ResponseEntity<Map<String, Object>> successTransferUserInfo(HttpServletRequest request) {

        log.info("oauth2 성공시 사용자의 정보 가져오는 controller call");

        Map<String, Object> responseMap = userInfoTransferService.userInfo(request);


        responseMap.keySet().forEach(key ->
                log.info(key + ": {}", responseMap.get(key)));

        return ResponseEntity.ok(responseMap);
    }


}
