package project.fin_the_pen.config.oauth2.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoUserInfoService {
    private final RestTemplate restTemplate;

    public Map<String, Object> getUserInfo(OAuth2UserRequest userRequest) {
        OAuth2AccessToken accessToken = userRequest.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String userInfoUri = "https://kapi.kakao.com/v2/user/me";
        ResponseEntity<String> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                entity,
                String.class
        );

        // 사용자 정보를 JSON으로 파싱
        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONObject properties = jsonObject.getJSONObject("properties");

        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("id", jsonObject.getString("id"));
        userAttributes.put("name", properties.getString("nickname"));
        userAttributes.put("profile_image", properties.getString("profile_image"));

        return userAttributes;
    }
}
