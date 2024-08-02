package project.fin_the_pen.config.oauth2.social;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOauth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final SocialUserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // API 호출하여 사용자 정보 가져오기
        Map<String, Object> userAttributes = fetchUserAttributes(userRequest);

        // 사용자 이름 추출
        String username = (String) userAttributes.get("name");

        return new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority(userAttributes)),
                userAttributes,
                "name" // 사용자 이름 속성
        );
    }

    private Map<String, Object> fetchUserAttributes(OAuth2UserRequest userRequest) {
        String accessToken = userRequest.getAccessToken().getTokenValue();
        String userInfoUri = "https://openapi.naver.com/v1/nid/me";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        // 요청 엔티티 설정
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 사용자 정보 API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                entity,
                String.class
        );

        // JSON 응답 파싱
        Map<String, Object> userAttributes = new HashMap<>();
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode responseNode = rootNode.path("response");

            // JSON 응답에서 필요한 사용자 정보 추출
            userAttributes.put("id", responseNode.path("id").asText());
            userAttributes.put("name", responseNode.path("name").asText());
            userAttributes.put("nickname", responseNode.path("nickname").asText());
            userAttributes.put("email", responseNode.path("email").asText());
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 처리
        }

        return userAttributes;
    }


    private SocialType getSocialType(String registrationId) {
        if (NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        }
        if (KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        }

        return SocialType.GOOGLE;
    }

    private SocialUser getUser(OauthAttributes attributes, SocialType socialType) {
        SocialUser findSocialUser =
                userRepository
                        .findBySocialTypeAndSocialId(socialType,
                                attributes
                                        .getOauth2UserInfo()
                                        .getId())
                        .orElse(null);

        if (findSocialUser == null) {
            return saveUser(attributes, socialType);
        }

        return findSocialUser;
    }

    private SocialUser saveUser(OauthAttributes attributes, SocialType socialType) {
        SocialUser createSocialUser = attributes.toEntity(socialType,
                attributes.getOauth2UserInfo());
        return userRepository.save(createSocialUser);
    }
}
