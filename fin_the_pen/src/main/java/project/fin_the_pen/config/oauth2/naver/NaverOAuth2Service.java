package project.fin_the_pen.config.oauth2.naver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NaverOAuth2Service {
    private final RestTemplate restTemplate;
    private final String tokenUri = "https://nid.naver.com/oauth2.0/token";

    public String getAccessToken(String authorizationCode) throws JsonProcessingException {
        String redirectUri = "http://localhost:8080/login/oauth2/code/naver";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", NaverClientProperties.clientId);
        params.add("client_secret", NaverClientProperties.clientSecret);
        params.add("code", authorizationCode);
        params.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                request,
                String.class
        );

        // JSON 파싱하여 access_token 추출
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.getBody());
        return rootNode.path("access_token").asText();
    }

    public String getUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}
