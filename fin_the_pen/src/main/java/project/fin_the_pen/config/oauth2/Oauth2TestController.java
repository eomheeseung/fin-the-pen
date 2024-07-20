package project.fin_the_pen.config.oauth2;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Controller
@Slf4j
public class Oauth2TestController {
    /**
     * TODO
     *  login?error로 실패핸들러가 계속 호출됨
     *  리다이렉트로 code / state를 받는데 안됨
     * @return
     */

    // 로그인 페이지를 표시합니다. OAuth2 인증을 시작합니다.
    @GetMapping("/")
    public String login() {
        // 로그인 페이지로 리다이렉트 또는 로그인 시작
        return "redirect:/oauth2/authorization/kakao";
    }

//    @GetMapping("/index")
//    public String index() {
//        return "index";  // 인증 후 사용자에게 보여줄 페이지
//    }


    // Kakao 로그인 후 성공 처리
    @RequestMapping("/login/oauth2/code/kakao")
    public String kakaoCallback(@RequestParam String code, @RequestParam String state) {
        // 액세스 토큰을 요청하는 로직
        String accessToken = getAccessTokenFromCode(code);

        // 사용자 정보 요청 및 처리
        OAuth2User oauth2User = getUserInfo(accessToken);

        log.info(accessToken);
        log.info(oauth2User.toString());

        // 인증 성공 후 리다이렉트할 페이지 (예: index.html)
        return "redirect:/index";
    }

    private final String tokenUri = "https://kauth.kakao.com/oauth/token";
    private final String userInfoUri = "https://kapi.kakao.com/v2/user/me";
    private final String clientId = "58044d766e3a6a856f75bd256d28a073"; // 클라이언트 ID
    private final String clientSecret = "mXZxsQdiZAPbSS6eQTJPvdLmYuHRrqm0"; // 클라이언트 비밀

    private String getAccessTokenFromCode(String code) {
        RestTemplate restTemplate = new RestTemplate();

        String url = tokenUri;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = String.format("grant_type=authorization_code&client_id=%s&client_secret=%s&redirect_uri=%s&code=%s",
                clientId, clientSecret, "http://localhost:8080/login/oauth2/code/kakao", code);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, request, JsonNode.class);
        JsonNode responseBody = response.getBody();

        if (responseBody != null && responseBody.has("access_token")) {
            return responseBody.get("access_token").asText();
        } else {
            throw new RuntimeException("Failed to retrieve access token");
        }
    }

    private OAuth2User getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, JsonNode.class);
        JsonNode responseBody = response.getBody();

        if (responseBody != null) {
            String name = responseBody.path("properties").path("nickname").asText();
            String email = responseBody.path("kakao_account").path("email").asText();

            return new OAuth2User() {
                @Override
                public Map<String, Object> getAttributes() {
                    return Map.of("name", name, "email", email);
                }

                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return Collections.emptyList();
                }

                @Override
                public String getName() {
                    return name;
                }
            };
        } else {
            throw new RuntimeException("Failed to retrieve user info");
        }
    }


    // 로그아웃 핸들러
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.logout();
        return "redirect:/"; // 로그아웃 후 홈 페이지로 리다이렉트
    }
}
