package project.fin_the_pen.config.oauth2.social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.fin_the_pen.config.oauth2.kakao.KakaoUserInfoService;
import project.fin_the_pen.config.oauth2.naver.NaverUserInfoService;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOauth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final KakaoUserInfoService kakaoUserInfoService;
    private final NaverUserInfoService naverUserInfoService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String clientId = userRequest.getClientRegistration().getClientId();

        Map<String, Object> userAttributes;
        String userNameAttributeName;
        String socialId;

        if ("kakao".equals(clientId)) {
            userAttributes = kakaoUserInfoService.getUserInfo(userRequest);
            userNameAttributeName = "id";  // Kakao의 경우, 사용자 식별자로 사용될 필드명
            socialId = (String) userAttributes.get("id");
        } else if ("naver".equals(clientId)) {
            userAttributes = naverUserInfoService.getUserInfo(userRequest);
            userNameAttributeName = "id";  // Naver의 경우, 사용자 식별자로 사용될 필드명
            socialId = (String) userAttributes.get("id");
        } else {
            throw new OAuth2AuthenticationException("Unsupported OAuth2 provider");
        }

        // 실제로는 userNameAttributeName은 필요한 경우에 맞게 설정할 수 있음
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                userAttributes,
                userNameAttributeName
        );
    }

}
