package project.fin_the_pen.config.oauth2.naver;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NaverService {
    private final NaverProperties naverProperties;
    private final RestTemplate restTemplate;

    public String getAccessToken(String authorizationCode, String status) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("client_id", naverProperties.getClientId());
        params.add("client_secret", naverProperties.getClientSecret());
        params.add("code", authorizationCode);
        params.add("redirect_uri", naverProperties.getRedirectUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                naverProperties.getTokenUri(),
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
