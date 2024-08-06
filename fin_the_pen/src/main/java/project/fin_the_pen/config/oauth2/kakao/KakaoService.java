package project.fin_the_pen.config.oauth2.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoService {
    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;

    public String getAccessToken(String authorizationCode, String status) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("client_id", kakaoProperties.getClientId());
        params.add("client_secret", kakaoProperties.getClientSecret());
        params.add("code", authorizationCode);
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("grant_type","authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                kakaoProperties.getTokenUri(),
                HttpMethod.POST, // Use POST instead of GET
                httpEntity,
                String.class
        );

        String responseBody = response.getBody();
        JSONObject jsonObject = new JSONObject(responseBody);

        String extractAccessToken = jsonObject.get("access_token").toString();


        return extractAccessToken;

    }
}
